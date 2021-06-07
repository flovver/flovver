package org.flovver.server

import org.flovver.compiler
import org.scalatra.ScalatraServlet

import java.io.{File, PrintWriter}
import scala.io.Source

class FlovverServlet(val folder: String) extends ScalatraServlet {
  get("/compile") {
    try {
      new compiler.Compiler(folder)
    } catch {
      case e: Throwable =>
        new PrintWriter(new File(folder + File.separator + "error.log")) {
          write(e.getStackTrace.mkString("\n")); close()
        }
    }
    status = 200
  }

  get("/load") {
    contentType = "application/json"

    val source = Source.fromFile(new File(folder + File.separator + "project.json"))
    val content = source.mkString
    source.close()

    content
  }

  post("/save") {
    new PrintWriter(new File(folder + File.separator + "project.json")) {
      write(request.body); close()
    }
    status = 200
  }

  get("/stop") {
    println("Shutting down Flovver IDE...")
    System.exit(1)
  }
}
