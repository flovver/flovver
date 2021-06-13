package org.flovver.compiler.middle.optimizations

import org.flovver.compiler.ir.{Call, Definition, RecursiveCall, Sandbox}

import scala.collection.mutable

trait TailCallElimination { this: Sandbox =>


  def markTailCalls(): Unit = {
    val tailContexts = new mutable.Queue[Call]()
    var isTailRec = false

    for (definition <- collect[Definition]) {
      definition.outLink.filter(_.isValue).map(_.`def`) match {
        case Some(rCall: RecursiveCall) =>
          isTailRec = true
          markAsTail(rCall)
        case Some(call: Call) if call.name == "If" && !call.incomeLinks.contains(null) =>
          tailContexts.enqueue(call)
        case None =>
      }

      while (tailContexts.nonEmpty) {

        val context = tailContexts.dequeue()

        for (node <- context.args) {
          node match {
            case rCall: RecursiveCall =>
              isTailRec = true
              markAsTail(rCall)
            case call: Call if call.name == "If" && !call.incomeLinks.contains(null) =>
              tailContexts.enqueue(call)
            case _ =>
          }
        }

      }

      definition.isTailRecursive = isTailRec
    }
  }

  private def markAsTail(rCall: RecursiveCall): Unit = {
    rCall.isTail = true
  }

}
