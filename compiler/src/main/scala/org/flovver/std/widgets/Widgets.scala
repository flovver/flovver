package org.flovver.std.widgets

import org.flovver.compiler.view.Widget

/**
 * Предоставляемый набор виджетов.
 */
object Widgets {
  /**
   * Виджет типа "Кнопка".
   */
  def button(caption: String, x: Int, y: Int, width: Option[Int] = None, height: Option[Int] = None): Widget = {
    def formatter(widget: Widget): String =
      s"""<input
         |   value="${widget.caption}"
         |   type="button"
         |   class="absolute border px-2 py-1 bg-gray-50 hover:bg-gray-50"
         |   style="left: ${widget.x}px; top: ${widget.y}px;"
         |   />
         |""".stripMargin

    Widget(caption, x, y, width, height, formatter)
  }

  /**
   * Виджет типа "Текстовая форма".
   */
  def textBox(caption: String, x: Int, y: Int, width: Option[Int] = None, height: Option[Int] = None): Widget = {
    def formatter(widget: Widget): String =
      s"""<input
         |   value="${widget.caption}"
         |   type="text"
         |   class="absolute border px-2 py-1"
         |   style="left: ${widget.x}px; top: ${widget.y}px;"
         |   />
         |""".stripMargin

    Widget(caption, x, y, width, height, formatter)
  }

  /**
   * Виджет типа "Метка".
   */
  def label(caption: String, x: Int, y: Int, width: Option[Int] = None, height: Option[Int] = None): Widget = {
    def formatter(widget: Widget): String =
      s"""<div
         |   class="absolute px-2 py-1"
         |   style="left: ${widget.x}px; top: ${widget.y}px;">${widget.caption}</div>
         |""".stripMargin

    Widget(caption, x, y, width, height, formatter)
  }

  def apply(`type`: String)(caption: String, x: Int, y: Int, width: Option[Int] = None, height: Option[Int] = None): Widget = {
    `type` match {
      case "Button" => button(caption, x, y, width, height)
      case "TextBox" => textBox(caption, x, y, width, height)
      case "Label" => label(caption, x, y, width, height)
      case _ => throw new IllegalArgumentException(s"""There's no such widget "${`type`}" in standard library""")
    }
  }
}
