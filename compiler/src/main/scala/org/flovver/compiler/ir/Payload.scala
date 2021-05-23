package org.flovver.compiler.ir

import org.flovver.compiler.ir.Payload.Node.Definition

object Payload {
  /**
   * Информация о проекте.
   *
   * @param name имя проекта
   */
  case class Project(name: String)

  /**
   * Флаги компилятора.
   *
   * @param useTailCallElimination        флаг устранения хвостовой рекурсии
   * @param useFibonacciElimination       флаг устранения рекурсии Фибоначчи
   * @param useCommonRecursionMemoization флаг мемоизации рекурсии
   */
  case class CompilerFlags(useTailCallElimination: Boolean,
                           useFibonacciElimination: Boolean,
                           useCommonRecursionMemoization: Boolean)

  /**
   * Данные модуля "Представление".
   *
   * @param pane    информация о главной панели окна
   * @param widgets информация о виджетах окна
   */
  case class View(pane: Pane, widgets: List[Widget])

  /**
   * Главная панель представления.
   *
   * @param width  ширина панели
   * @param height высота панели
   */
  case class Pane(width: Int, height: Int)

  /**
   * Элемент интерфейса в окне приложения.
   *
   * @param `type`  тип виджета
   * @param caption текст виджета
   * @param x       позиция виджета по оси OX
   * @param y       позиция виджета по осу OY
   * @param width   ширина виджета (если не установлена, выбирается автоматически)
   * @param height  высота виджета (если не установлена, выбирается автоматически)
   */
  case class Widget(`type`: String, caption: String, x: Int, y: Int, width: Option[Int], height: Option[Int])

  /**
   * Данные модуля "Обновление".
   *
   * @param nodes         узлы программы
   * @param relationships связи между узлами программы
   */
  case class Update(nodes: List[Node], relationships: List[Relationship])

  /**
   * Узел программы.
   */
  sealed trait Node

  object Node {
    /**
     * Узел-вход типа-модели.
     */
    case class ModelInput() extends Node

    /**
     * Узел-вход типа-сообщения.
     */
    case class MessageInput() extends Node

    /**
     * Определение функции.
     *
     * @param arity арность функции
     */
    case class Definition(arity: Int) extends Node

    /**
     * Вызов функции.
     *
     * @param name  имя функции
     * @param arity арность функции
     */
    case class Call(name: String, arity: Int) extends Node

    /**
     * Узел-выход типа-модели.
     */
    case class ModelOutput() extends Node
  }

  /**
   * Связь между узлами программы.
   *
   * @param definition узел-определение
   * @param use        узел-использование
   */
  case class Relationship(definition: Def, use: Use)

  /**
   * Узел-определение (т.е. результат которого передается на вход других узлов)
   */
  sealed trait Def {
    val node: Node
  }

  object Def {
    /**
     * Строгое определение - передается результат вычисления узла.
     *
     * @param node определяемый узел
     */
    case class Strict(node: Node) extends Def

    /**
     * Частичное определение - передается "задумка" (англ. - thunk) вычисления узла.
     *
     * @param node определяемый узел
     */
    case class Partial(node: Node) extends Def

    /**
     * Определение-параметр - передается параметр определения функции.
     *
     * @param node определение функции, параметр которой берется за определение
     * @param port номер параметра функции (начиная с нуля)
     */
    case class Parameter(node: Definition, port: Int) extends Def
  }

  /**
   * Узел-использование (т.е. на один из входов передается результат вычисления)
   *
   * @param node узел, требующий результат другого на вход
   * @param port номер входа (начиная с нуля)
   */
  case class Use(node: Node, port: Int)

  def apply(project: Project, flags: CompilerFlags, view: View, update: Update): Payload = {
    new Payload(project, flags, view, update)
  }
}

/**
 *
 * @param project информация о проекта
 * @param flags   информация о флагах компилятора
 * @param view    данные модуля "Представление"
 * @param update  данные модуля "Обновление"
 */
class Payload(val project: Payload.Project, val flags: Payload.CompilerFlags, val view: Payload.View, val update: Payload.Update) {
  override def toString: String = s"Result($project,$flags,$update)"
}
