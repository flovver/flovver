package org.flovver.compiler.ir

trait Link {

  def `def`: Node
  def use: Node
  val useArg: Int

  def isValue: Boolean
  def isThunk: Boolean = !isValue

  def isExternal: Boolean
  def isInternal: Boolean = !isExternal
}

object Link {

  class Impl(val defNode: Node, val useNode: Node, override val useArg: Int)(value: Boolean)(external: Boolean) extends Link {
    override def `def`: Node = defNode
    override def use: Node = useNode
    override def isValue: Boolean = value
    override def isExternal: Boolean = external
  }

  def apply(defNode: Node, useNode: Node, useArg: Int, value: Boolean, external: Boolean): Link = new Impl(defNode, useNode, useArg)(value)(external)

  def unapply(arg: Link): Option[(Node, Node, Int)] = Some(arg.`def`, arg.use, arg.useArg)
}

trait InternalParameter { this: Link =>

  val parameter: Int

  override def isExternal: Boolean = false
}

object InternalParameter {

  class Impl(override val `def`: Node with Abstraction,
             override val parameter: Int,
             override val use: Node with Application,
             override val useArg: Int)(value: Boolean) extends Link with InternalParameter {
    override def isValue: Boolean = value
  }

  def apply(defNode: Node with Abstraction, parameter: Int, useNode: Node with Application, useArg: Int, value: Boolean): Link with InternalParameter = {
    new Impl(defNode, parameter, useNode, useArg)(value)
  }

  def unapply(arg: Link): Option[(Node with Abstraction, Int, Node with Application, Int)] = arg match {
    case link: Impl => Some(link.`def`, link.parameter, link.use, link.useArg)
    case _ => None
  }
}

trait InternalOutput { this: Link =>

  override val useArg: Int = 0
  override def isValue: Boolean = true
  override def isExternal: Boolean = false
}

object InternalOutput {

  class Impl(override val `def`: Node, override val use: Node with Abstraction) extends Link with InternalOutput

  def apply(defNode: Node, useNode: Node with Abstraction): Link with InternalOutput = new Impl(defNode, useNode)

  def unapply(arg: Link): Option[(Node, Node with Abstraction)] = arg match {
    case link: Impl => Some(link.`def`, link.use)
    case _ => None
  }
}