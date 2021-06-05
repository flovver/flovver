package org.flovver.compiler

import java.io.File

trait Env {
  object Env {
    var projectFolder: String = "."

    var useTailCallElimination: Boolean = false
    var useCommonRecursionMemoization: Boolean = false
    var useFibonacciRecursionElimination: Boolean = false

    def outFolder: String = projectFolder + File.separator + "out"
  }
}
