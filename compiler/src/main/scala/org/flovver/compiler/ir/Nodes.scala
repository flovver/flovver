package org.flovver.compiler.ir

import scala.collection.mutable

trait Call extends Node with Application {
  val name: String
}

object Call {
  class Impl(override val arity: Int)(val name: String) extends Call

  def apply(arity: Int, name: String): Call = new Impl(arity)(name)

  def unapply(arg: Node): Option[(String, mutable.Seq[Link])] = arg match {
    case call: Call => Some(call.name, call.incomeLinks)
    case _ => None
  }
}

trait Definition extends Node with Application with Abstraction

object Definition {
  class Impl(override val arity: Int) extends Definition {
    override var outLink: Option[Link with InternalOutput] = None
  }

  def apply(arity: Int): Definition = new Impl(arity)

  def unapply(arg: Node): Option[(mutable.Seq[Link], Option[Link with InternalOutput])] = arg match {
    case definition: Definition => Some(definition.incomeLinks, definition.outLink)
    case _ => None
  }
}

trait RecursiveCall extends Call with Recursion

// TODO: задавать arity неявно?
object RecursiveCall {
  class Impl(override val arity: Int)(override val abstraction: Node with Abstraction) extends RecursiveCall {
    override val name: String = "self"
  }

  def apply(arity: Int, abstraction: Node with Abstraction): RecursiveCall = new Impl(arity)(abstraction)

  def unapply(arg: Node): Option[(Node with Abstraction, mutable.Seq[Link])] = arg match {
    case rCall: RecursiveCall => Some(rCall.abstraction, rCall.incomeLinks)
    case _ => None
  }
}