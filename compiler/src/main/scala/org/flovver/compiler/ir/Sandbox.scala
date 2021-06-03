package org.flovver.compiler.ir

import scala.collection.mutable

trait Sandbox {
  val links: mutable.Buffer[Link] = mutable.Buffer()
  val nodes: mutable.Buffer[Node] = mutable.Buffer()

  // external
  // a <- Link(defNode, useNode, useArg, isValue=byValue, external)
  // useNode.links(useArg) = a
  def use(defNode: Node, useNode: Node with Application, useArg: Int)(byValue: Boolean): (Node, Link, Node) = {
    val link = Link(defNode, useNode, useArg, byValue, external = true)

    useNode.setLink(useArg, link)
    links.append(link)

    (defNode, link, useNode)
  }

  // internal by value
  // a <- Link(defNode, useNode, isValue, internal)
  // useNode.outLink = Some(a)
  def output(defNode: Node, useNode: Node with Abstraction): (Node, Link, Node) = {
    val link = InternalOutput(defNode, useNode)

    useNode.outLink = Some(link)
    links.append(link)

    (defNode, link, useNode)
  }

  // internal by value
  // a <- InternalParameter(defNode, parameter, useNode, useArg)
  // useNode.links(useArg) = a
  def input(defNode: Node with Abstraction, parameter: Int, useNode: Node with Application, useArg: Int): (Node, Link, Node) = {
    val link = InternalParameter(defNode, parameter, useNode, useArg, value = true)

    useNode.setLink(useArg, link)
    links.append(link)

    (defNode, link, useNode)
  }
}
