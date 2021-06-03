package org.flovver.compiler.ir

import scala.collection.mutable

trait Node {
  val arity: Int
  var label: Option[String] = None
}

trait Application { this: Node =>
  // TODO: придумать схему пустых ссылок без null. Null Object!
  val incomeLinks: mutable.Seq[Link] = mutable.Seq.from(Range(0, arity).map(_ => null))
  def setLink(i: Int, link: Link): Unit = {
    incomeLinks(i) = link
  }

  def args: Seq[Node] = incomeLinks.map(_.`def`).toSeq
  def arg(i: Int): Node = incomeLinks(i).`def`
}

trait Abstraction { this: Node =>
  def outLink: Option[Link with InternalOutput]
  def outLink_=(l: Option[Link with InternalOutput]): Unit
  def outArg: Option[Node] = outLink.map(_.`def`)
}

trait Recursion extends Application { this: Node =>
  val abstraction: Node with Abstraction
}