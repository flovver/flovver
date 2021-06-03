package org.flovver.cmd

import org.flovver.compiler

object Launcher extends App {
  // Run command line interface parser
  //cli.App.run(args.toList)
  new compiler.Compiler
}
