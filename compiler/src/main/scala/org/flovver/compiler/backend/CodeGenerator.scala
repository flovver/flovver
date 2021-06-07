package org.flovver.compiler.backend

import org.flovver.compiler.Env
import org.flovver.compiler.frontend.PayloadProvider
import org.flovver.compiler.ir.{Abstraction, Application, Call, Definition, InternalOutput, InternalParameter, Link, MessageInput, ModelInput, ModelOutput, Node, RecursiveCall, Sandbox}

import java.io.{File, PrintWriter}
import scala.collection.mutable

trait CodeGenerator {
  this: Sandbox with Env with PayloadProvider =>

  private val nextLabel = {
    var i = 1

    () => {
      val label = s"fsa_$i"
      i += 1
      label
    }
  }

  def topSort(): Unit = {
    // CLRS, стр. 641 (dfs), 650 (top-sort)

    val visited = mutable.Set[Node]()

    val sortedNodes = mutable.ListBuffer[Node]()

    def dfs(node: Node): Unit = {
      visited += node

      val adjacentNodes = links.filter(_.`def` == node).filterNot(_.isInstanceOf[InternalOutput]).map(_.use)
      for (adj <- adjacentNodes if !visited(adj)) {
        // TODO: переписать без рекурсии
        dfs(adj)
      }

      sortedNodes.append(node)
    }

    for (node <- nodes if !visited(node)) {
      dfs(node)
    }

    nodes.clear()
    nodes.appendAll(sortedNodes.reverseIterator)
  }

  def generateArg(label: String, substitute: Boolean = false)(v: (Link, Int)): String = {
    val link = v._1
    val i = v._2

    val name = link match {
      case null => s"${label}_arg_$i"
      case InternalParameter(defNode, parameter, _, _) =>
        defNode match {
          case v: Application if substitute && v.incomeLinks(parameter) != null => v.arg(parameter).label.get
          case _ => s"${defNode.label.get}_arg_$parameter"
        }
      case Link(defNode, _, _) => defNode.label.get
    }

    val isNotInputLink = link != null && !(link.`def` == modelInput || link.`def` == messageInput)
    val callPolicy = if (link != null && link.isExternal && link.isValue && isNotInputLink) "()" else ""

    s"$name$callPolicy"
  }

  def generateRecursiveCall(label: String)(abstraction: Node with Abstraction, args: mutable.Seq[Link]): Unit = {
    val self = s"${abstraction.label.get}_r"

    val parameters = args.zipWithIndex.filter(_._1 == null).map(v => s"${label}_arg_${v._2}").mkString(",")
    val arguments = args.zipWithIndex.map(generateArg(label)).mkString(",")

    result.println(s"const $label = ($parameters) => $self($arguments);")
  }

  def generateCall(label: String)(name: String, args: mutable.Seq[Link]): Unit = {
    val parameters = args.zipWithIndex.filter(_._1 == null).map(v => s"${label}_arg_${v._2}").mkString(",")
    val arguments = args.zipWithIndex.map(generateArg(label)).mkString(",")

    result.println(s"const $label = ($parameters) => $name($arguments);")
  }

  def generateDefinitionStart(label: String)(args: mutable.Seq[Link]): Unit = {
    val arguments = args.zipWithIndex.filter(_._1 == null).map(v => s"${label}_arg_${v._2}").mkString(",")
    val parameters = args.zipWithIndex.map(v => s"${label}_arg_${v._2}").mkString(",")

    if (Env.useCommonRecursionMemoization) {
      result.println(
        s"""const $label = ($arguments) => {
           |const ${label}_r = (() => {
           |const ${label}_st = {};
           |const ${label}_w = ($parameters) => {""".stripMargin)
    } else {
      result.println(
        s"""const $label = ($arguments) => {
           |const ${label}_r = ($parameters) => {""".stripMargin)
    }
  }

  def generateUpdateFunctionStart(): Unit = {
    result.println("const update = (model, message) => {")
  }

  def generateUpdateFunctionBody(): Unit = {
    val definitionEnds = mutable.Stack[(String, mutable.Seq[Link], Node, Link)]()

    for (node <- nodes) {

      val label = nextLabel()
      if (node != modelInput && node != messageInput && node != modelOutput) {
        node.label = Some(label)
      }

      node match {
        case RecursiveCall(abstraction, args) => generateRecursiveCall(label)(abstraction, args)
        case Call(name, args) => generateCall(label)(name, args)
        case Definition(args, output) =>
          definitionEnds.push((label, args, output.map(_.`def`).get, output.get))
          generateDefinitionStart(label)(args)
        case ModelInput | MessageInput | ModelOutput =>
        case _ => throw new IllegalStateException()
      }

      if (definitionEnds.headOption.exists(_._3 == node)) {
        val (label, args, _, link) = definitionEnds.pop()

        val memoizationWrapper = if (Env.useCommonRecursionMemoization) {
          val parameters = args.zipWithIndex.map(v => s"${label}_arg_${v._2}").mkString(",")
          s"""
             |return ($parameters) => ${label}_st[[$parameters]] = ${label}_st[[$parameters]] || ${label}_w($parameters);
             |})();""".stripMargin
        } else ""

        result.println(
          s"""return ${node.label.get}${if (link.isValue) "()" else ""};
             |}$memoizationWrapper
             |return ${label}_r(${args.zipWithIndex.map(generateArg(label)).mkString(",")});
             |}""".stripMargin)
      }

    }
  }

  def generateUpdateFunctionEnd(): Unit = {
    result.println(
      s"""return ${modelOutput.outArg.flatMap(_.label).getOrElse("model")}${if (modelOutput.outLink.exists(_.isValue)) "()" else ""};
         |}""".stripMargin)
  }

  def generateStdLibFootprint(): Unit = {
    result.println(
      """const If = (c, l, r) => c ? l() : r();
        |const Eq = (x, y) => x == y;
        |const Mul = (x, y) => x * y;
        |const Num1 = () => 1;
        |const StrToNum = (x) => x;
        |const Minus1 = (x) => x - 1;
        |const DispatchFactorial = (msg, NewInput, ComputeFactorial) => msg.tag == 'NewInput' ? NewInput(msg.value) : ComputeFactorial();
        |""".stripMargin)
  }

  def generateRuntimeFootprint(): Unit = {
    val widgets = payload.view.widgets
      .zipWithIndex
      .map { case (w, i) => s"{ id: 'view_$i', caption_template: '${w.caption}' }" }
      .mkString(",\n")

    result.println(
      s"""let model = 1;
         |
         |const interpolationRegExp = /\\${"$"}{[a-zA-Z]+}/g;
         |const interpolate = (str) => {
         |const matches = str.match(interpolationRegExp);
         |if (matches == null) return str;
         |const substitutions = matches.map((v, i, a) => eval(v.substr(2, v.length - 3)));
         |let result = str;
         |matches.forEach((v, i, a) => (result = result.replace(v, substitutions[i])));
         |return result;
         |}
         |
         |const refreshView = () => view.forEach((v, i, a) => {
         |const element = document.getElementById(v.id);
         |const value = interpolate(v.caption_template)
         |if (element.tagName == 'DIV') {
         |element.innerText = value;
         |} else {
         |element.value = value;
         |}
         |});
         |
         |const signal = (message) => {
         |model = update(model, message);
         |refreshView();
         |}
         |
         |const view = [
         |$widgets
         |];
         |
         |window.onload = () => {
         |refreshView();
         |${
        if (payload.compiler.flags.debug)
          s"""const debugWindow = new WinBox("Debug", {html: `<pre>const update = ${"$"}{update.toString()}</pre>`});
             |debugWindow.minimize();""".stripMargin else ""
      }
         |}""".stripMargin)
  }

  def generate(): Unit = {

    // Упорядочим вершины запуском топологической сортировки
    topSort()

    generateStdLibFootprint()

    generateUpdateFunctionStart()
    generateUpdateFunctionBody()
    generateUpdateFunctionEnd()

    generateRuntimeFootprint()

    result.close()
  }

  private lazy val result = new PrintWriter(new File(Env.outFolder + File.separator + "bundle.js"))

}
