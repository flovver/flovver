package org.flovver.compiler.view

import org.flovver.compiler.Env
import org.flovver.compiler.frontend.PayloadProvider
import org.flovver.std.widgets.Widgets

import java.io.{File, PrintWriter}

trait IndexBuilder { this: Env with PayloadProvider =>

  def buildIndex(): Unit = {
    val view = payload.view

    val index = new PrintWriter(new File(Env.outFolder + File.separator + "index.html"))

    val widgets = view.widgets.map { w =>
      Widgets(w.`type`)(w.caption, w.x, w.y, w.width, w.height)
    }

    val template =
      s"""<!doctype html>
         |<html>
         |<head>
         |  <meta charset="UTF-8" />
         |  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
         |  <link href="https://unpkg.com/tailwindcss@^2/dist/tailwind.min.css" rel="stylesheet">
         |</head>
         |<body>
         |<script src="bundle.js"></script>
         |<div class="flex items-center justify-center h-screen bg-gray-100">
         |<div class="relative bg-white shadow-lg" style="width: ${view.pane.width}px; height: ${view.pane.height}px;">
         |${widgets.foldRight("")(_.toHTML + _)}
         |</div>
         |</div>
         |</body>
         |</html>
         |""".stripMargin

    index.println(template)

    index.close()
  }
}
