package org.flovver.compiler

import org.flovver.compiler.backend.CodeGenerator
import org.flovver.compiler.frontend.{IRBuilder, PayloadParser}
import org.flovver.compiler.ir.Sandbox
import org.flovver.compiler.middle.Optimizer
import org.flovver.compiler.view.IndexBuilder

import java.io.File

class Compiler(folder: String) extends Env with PayloadParser with Sandbox with IRBuilder with Optimizer with CodeGenerator with IndexBuilder {

  def configure(): Unit = {
    Env.projectFolder = folder

    Env.useTailCallElimination = payload.compiler.flags.`tail-call-elimination`
    Env.useCommonRecursionMemoization = payload.compiler.flags.`common-recursion-memoization`
    Env.useFibonacciRecursionElimination = payload.compiler.flags.`fibonacci-elimination`
  }

  def createOutDirectory(): Unit = {
    new File(Env.outFolder).mkdir()
  }

  configure()

  buildIR()
  optimize()

  createOutDirectory()

  buildIndex()
  generate()

}
