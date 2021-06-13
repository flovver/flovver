package org.flovver.compiler.middle

import org.flovver.compiler.Env
import org.flovver.compiler.ir.Sandbox
import org.flovver.compiler.middle.optimizations._

trait Optimizer extends TailCallElimination with FibonacciRecursionElimination { this: Env with Sandbox =>

  def optimize(): Unit = {
    if (Env.useFibonacciRecursionElimination) {
      markFibonacciFunctions()
      if (!Env.useTailCallElimination) {
        markTailCalls()
      }
    }

    if (Env.useTailCallElimination) {
      markTailCalls()
    }
  }

}
