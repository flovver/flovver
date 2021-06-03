package org.flovver.compiler.backend

import org.flovver.compiler.Env
import org.flovver.compiler.ir.{Abstraction, Application, Call, Definition, InternalOutput, InternalParameter, Link, Node, RecursiveCall, Sandbox}

import scala.collection.mutable

trait CodeGenerator { this: Sandbox with Env =>

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

  def generate(): Unit = {
    val nextLabel = {
      var i = 1

      () => {
        val label = s"fsa_$i"
        i += 1
        label
      }
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

      val callPolicy = if (link != null && link.isExternal && link.isValue) "()" else ""

      s"$name$callPolicy"
    }

    def generateRecursiveCall(label: String)(abstraction: Node with Abstraction, args: mutable.Seq[Link]): Unit = {
      val self = abstraction.label.get

      val parameters = args.zipWithIndex.filter(_._1 == null).map(v => s"${label}_arg_${v._2}").mkString(",")
      val arguments = args.zipWithIndex.map(generateArg(label)).mkString(",")

      println(s"const $label = ($parameters) => $self($arguments);")
    }

    def generateCall(label: String)(name: String, args: mutable.Seq[Link]): Unit = {
      val parameters = args.zipWithIndex.filter(_._1 == null).map(v => s"${label}_arg_${v._2}").mkString(",")
      val arguments = args.zipWithIndex.map(generateArg(label)).mkString(",")

      println(s"const $label = ($parameters) => $name($arguments);")
    }

    def generateDefinitionStart(label: String)(args: mutable.Seq[Link]): Unit = {
      val parameters = args.zipWithIndex.filter(_._1 == null).map(v => s"${label}_arg_${v._2}").mkString(",")

      if (useCommonRecursionMemoization) {
        println(
          s"""const $label = (() => {
             |const ${label}_st = {};
             |
             |const ${label}_w = ($parameters) => {""".stripMargin)
      } else {
        println(s"const $label = ($parameters) => {")
      }
    }

    val definitionEnds = mutable.Stack[(String, mutable.Seq[Link], Node)]()

    for (node <- nodes) {

      val label = nextLabel()
      node.label = Some(label)

      node match {
        case RecursiveCall(abstraction, args) => generateRecursiveCall(label)(abstraction, args)
        case Call(name, args) => generateCall(label)(name, args)
        case Definition(args, output) =>
          definitionEnds.push((label, args, output.map(_.`def`).get))
          generateDefinitionStart(label)(args)
        case _ => throw new IllegalStateException()
      }

      if (definitionEnds.headOption.exists(_._3 == node)) {
        println(s"return ${node.label.get}();\n}")

        val (label, args, _) = definitionEnds.pop()

        if (useCommonRecursionMemoization) {
          val parameters = args.zipWithIndex.filter(_._1 == null).map(v => s"${label}_arg_${v._2}").mkString(",")
          println(s"return ($parameters) => ${label}_st[[$parameters]] = ${label}_st[[$parameters]] || ${label}_w($parameters);\n})();")
        }
      }

    }
  }

}
