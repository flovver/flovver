package org.flovver.server

import org.flovver.compiler
import org.scalatra.ScalatraServlet

import java.io.File
import scala.io.Source

class FlovverServlet(val folder: String) extends ScalatraServlet {
  get("/compile") {
    new compiler.Compiler(folder)
    redirect("/preview")
  }

  get("/load") {
    contentType = "application/json"

    val source = Source.fromFile(new File(folder + File.separator + "project.json"))
    val content = source.mkString
    source.close()

    content
  }

  post("/save") {
    println(request.body)
    "save"
  }

  get("/stop") {
    println("Shutting down Flovver IDE...")
    System.exit(1)
  }
}
