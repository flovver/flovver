package org.flovver.compiler.view

/**
 * Элемент интерфейса модуля "Представление".
 *
 * @param caption текст виджета
 * @param x позиция виджета по OX
 * @param y позиция виджета по OY
 * @param width ширина виджета (остутствует - будет расчитана автоматически)
 * @param height высота виджета (остутствует - будет расчитана автоматически)
 * @param htmlFormatter функция форматирования виджета в HTML
 */
case class Widget(
  caption: String,
  x: Int,
  y: Int,
  width: Option[Int] = None,
  height: Option[Int] = None,

  htmlFormatter: Widget => String = _ => "") {
  /**
   * Переводит виджет в набор тегов HTML.
   * @return HTML-представление виджета
   */
  def toHTML: String = htmlFormatter(this)
}
