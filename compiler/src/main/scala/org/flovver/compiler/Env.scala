package org.flovver.compiler

trait Env {
  val useTailCallElimination: Boolean
  val useCommonRecursionMemoization: Boolean
  val useFibonacciRecursionElimination: Boolean
}
