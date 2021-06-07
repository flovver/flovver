package org.flovver.compiler.view

/**
 * Элемент интерфейса модуля "Представление".
 *
 * @param id DOM-идентификатор виджета
 * @param caption текст виджета
 * @param x позиция виджета по OX
 * @param y позиция виджета по OY
 * @param width ширина виджета (остутствует - будет расчитана автоматически)
 * @param height высота виджета (остутствует - будет расчитана автоматически)
 * @param oninput событие при вводе текста в виджете
 * @param onclick событие при клике на виджете
 * @param htmlFormatter функция форматирования виджета в HTML
 */
case class Widget(
  id: String = "",
  caption: String,
  x: Int,
  y: Int,
  width: Option[Int] = None,
  height: Option[Int] = None,
  oninput: String = "",
  onclick: String = "",

  htmlFormatter: Widget => String = _ => "") {
  /**
   * Переводит виджет в набор тегов HTML.
   * @return HTML-представление виджета
   */
  def toHTML: String = htmlFormatter(this)
}
