package org.flovver.std.widgets

import org.flovver.compiler.view.Widget

/**
 * Предоставляемый набор виджетов.
 */
object Widgets {
  /**
   * Виджет типа "Кнопка".
   */
  def button(id: String, caption: String, x: Int, y: Int, width: Option[Int] = None, height: Option[Int] = None, onclick: String): Widget = {
    def formatter(widget: Widget): String =
      s"""<input
         |   id="$id"
         |   value="${widget.caption}"
         |   type="button"
         |   class="absolute border px-2 py-1 bg-gray-50 hover:bg-gray-50"
         |   style="left: ${widget.x}px; top: ${widget.y}px;"
         |   onclick="$onclick"
         |   />
         |""".stripMargin

    Widget(id, caption, x, y, width, height, htmlFormatter = formatter)
  }

  /**
   * Виджет типа "Текстовая форма".
   */
  def textBox(id: String, caption: String, x: Int, y: Int, width: Option[Int] = None, height: Option[Int] = None, onclick: String, oninput: String): Widget = {
    def formatter(widget: Widget): String =
      s"""<input
         |   id="$id"
         |   value="${widget.caption}"
         |   type="text"
         |   class="absolute border px-2 py-1"
         |   style="left: ${widget.x}px; top: ${widget.y}px;"
         |   onclick="$onclick"
         |   oninput="$oninput"
         |   />
         |""".stripMargin

    Widget(id, caption, x, y, width, height, htmlFormatter = formatter)
  }

  /**
   * Виджет типа "Метка".
   */
  def label(id: String, caption: String, x: Int, y: Int, width: Option[Int] = None, height: Option[Int] = None, onclick: String): Widget = {
    def formatter(widget: Widget): String =
      s"""<div
         |   id="$id"
         |   class="absolute px-2 py-1"
         |   onclick="$onclick"
         |   style="left: ${widget.x}px; top: ${widget.y}px;">${widget.caption}</div>
         |""".stripMargin

    Widget(id, caption, x, y, width, height, htmlFormatter = formatter)
  }

  def apply(`type`: String)(id: String, caption: String, x: Int, y: Int, width: Option[Int] = None, height: Option[Int] = None, onclick: String, oninput: String): Widget = {
    `type` match {
      case "Button" => button(id, caption, x, y, width, height, onclick = onclick)
      case "TextBox" => textBox(id, caption, x, y, width, height, onclick = onclick, oninput = oninput)
      case "Label" => label(id, caption, x, y, width, height, onclick)
      case _ => throw new IllegalArgumentException(s"""There's no such widget "${`type`}" in standard library""")
    }
  }
}
