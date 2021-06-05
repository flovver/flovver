package org.flovver.compiler.backend

import org.flovver.compiler.Env
import org.flovver.compiler.frontend.PayloadProvider
import org.flovver.compiler.ir.{Abstraction, Application, Call, Definition, InternalOutput, InternalParameter, Link, MessageInput, ModelInput, ModelOutput, Node, RecursiveCall, Sandbox}

import java.io.{File, PrintWriter}
import scala.collection.mutable

trait CodeGenerator { this: Sandbox with Env with PayloadProvider =>

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

  def generateArg(label: String)(v: (Link, Int)): String = {
    val (link: Link, i: Int) = v

    val name = link match {
      case null => s"${label}_arg_$i"
      case InternalParameter(defNode, parameter, _, _) =>
        defNode match {
          case v: Application if v.incomeLinks(parameter) != null => v.arg(parameter).label
          case _ => s"${defNode.label.get}_arg_$parameter"
        }
      case Link(defNode, _, _) => defNode.label.get
    }

    val isInputLink = link.`def` == modelInput || link.`def` == messageInput
    val callPolicy = if (link != null && link.isExternal && link.isValue && !isInputLink) "()" else ""

    s"$name$callPolicy"
  }

  def generateRecursiveCall(label: String)(abstraction: Node with Abstraction, args: mutable.Seq[Link]): Unit = {
    val self = abstraction.label.get

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
    val parameters = args.zipWithIndex.filter(_._1 == null).map(v => s"${label}_arg_${v._2}").mkString(",")

    if (Env.useCommonRecursionMemoization) {
      result.println(
        s"""const $label = (() => {
           |const ${label}_st = {};
           |
           |const ${label}_w = ($parameters) => {""".stripMargin)
    } else {
      result.println(s"const $label = ($parameters) => {")
    }
  }

  def generateUpdateFunctionStart(): Unit = {
    result.println("const update = (model, message) => {")
  }

  def generateUpdateFunctionBody(): Unit = {
    val definitionEnds = mutable.Stack[(String, mutable.Seq[Link], Node)]()

    for (node <- nodes) {

      val label = nextLabel()
      if (node != modelInput && node != messageInput && node != modelOutput) {
        node.label = Some(label)
      }

      node match {
        case RecursiveCall(abstraction, args) => generateRecursiveCall(label)(abstraction, args)
        case Call(name, args) => generateCall(label)(name, args)
        case Definition(args, output) =>
          definitionEnds.push((label, args, output.map(_.`def`).get))
          generateDefinitionStart(label)(args)
        case ModelInput | MessageInput | ModelOutput =>
        case _ => throw new IllegalStateException()
      }

      if (definitionEnds.headOption.exists(_._3 == node)) {
        result.println(s"return ${node.label.get}();\n}")

        val (label, args, _) = definitionEnds.pop()

        if (Env.useCommonRecursionMemoization) {
          val parameters = args.zipWithIndex.filter(_._1 == null).map(v => s"${label}_arg_${v._2}").mkString(",")
          result.println(s"return ($parameters) => ${label}_st[[$parameters]] = ${label}_st[[$parameters]] || ${label}_w($parameters);\n})();")
        }
      }

    }
  }

  def generateUpdateFunctionEnd(): Unit = {
    result.println(s"return ${modelOutput.outArg.get.label.get};\n}")
  }

  def generateStdLibFootprint(): Unit = {}

  def generateRuntimeFootprint(): Unit = {
    result.println(
      """let model = {};
        |
        |const signal = (message) => {
        |model = update(model, message);
        |}
        |""".stripMargin)
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
