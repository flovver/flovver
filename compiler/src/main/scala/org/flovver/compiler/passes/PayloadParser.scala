package org.flovver.compiler.passes

import org.flovver.compiler.ir.Payload
import org.flovver.compiler.ir.Payload.Node.Definition
import org.flovver.compiler.ir.Payload._
import org.flovver.compiler.passes.PayloadParser.InnerFormat
import org.json4s._
import org.json4s.jackson.JsonMethods

/**
 * Парсер данных, посылаемых клиентом серверу.
 */
trait PayloadParser {
  implicit val formats: Formats = DefaultFormats

  /**
   * Разбирает данных, хранимых в строке `source`,
   * переводит в программное представление, хранимое в памяти.
   *
   * @return данные в программном представлении
   */
  def parse(source: String): Payload = {
    val ast = JsonMethods.parse(source)
    val innerResult = ast.extract[InnerFormat.Result]
    val result = fromInner(innerResult)
    result
  }

  /**
   * Преобразует данные во внутреннем представлении парсера
   * в общее для разных проходов компилятора представление.
   *
   * @return данные в программном представлении
   */
  private def fromInner(innerResult: InnerFormat.Result): Payload = {
    val project = Project(innerResult.project.name)
    val flags = CompilerFlags(
      useTailCallElimination = innerResult.compiler.flags.`tail-call-elimination`,
      useFibonacciElimination = innerResult.compiler.flags.`fibonacci-elimination`,
      useCommonRecursionMemoization = innerResult.compiler.flags.`common-recursion-memoization`
    )

    val pane = Pane(innerResult.view.pane.width, innerResult.view.pane.height)
    val widgets = innerResult.view.widgets.map(w => Widget(w.`type`, w.caption, w.x, w.y, w.width, w.height))
    val view = View(pane, widgets)

    def convertNode(node: InnerFormat.Node): Node = {
      import org.flovver.compiler.ir.Payload.Node._

      node.`type` match {
        case "model-input" => ModelInput()
        case "message-input" => MessageInput()
        case "definition" => Definition(node.inputs.size)
        case "call" if node.name.isDefined => Call(node.name.get, node.inputs.size)
        case "model-output" => ModelOutput()
        case _ => throw new IllegalArgumentException()
      }
    }

    val nodes = innerResult.update.nodes.map(convertNode)

    def convertRelationship(relationship: InnerFormat.Relationship): Relationship = {
      val definitionNode = nodes(relationship.`def`.node)
      val definition = if (relationship.`def`.inner.getOrElse(false)) {
        assert(definitionNode.isInstanceOf[Definition])
        Def.Parameter(definitionNode.asInstanceOf[Definition], relationship.`def`.port.get)
      } else {
        relationship.`def`.evaluation match {
          case "strict" => Def.Strict(definitionNode)
          case "partial" => Def.Partial(definitionNode)
          case _ => throw new IllegalArgumentException()
        }
      }
      val use = Use(nodes(relationship.use.node), relationship.use.port)
      Relationship(definition, use)
    }

    val relationships = innerResult.update.relationships.map(convertRelationship)

    val update = Update(nodes, relationships)

    Payload(project, flags, view, update)
  }
}

object PayloadParser {
  /**
   * Внутренний формат хранения данных в парсере.
   *
   * Можно генерировать по JSON-схеме клиентского запроса / формата хранения проекта.
   */
  private object InnerFormat {
    case class Project(name: String)
    case class Compiler(flags: Flags)
    case class Flags(`tail-call-elimination`: Boolean, `fibonacci-elimination`: Boolean, `common-recursion-memoization`: Boolean)
    case class Model(`model-type`: String, `message-type`: String, types: List[Type])
    case class Type(base: String, alias: String, variants: Option[List[Variant]])
    case class Variant(name: String, `type`: String)
    case class View(pane: Pane, widgets: List[Widget])
    case class Pane(width: Int, height: Int)
    case class Widget(`type`: String, caption: String, x: Int, y: Int, width: Option[Int], height: Option[Int])
    case class Update(nodes: List[Node], relationships: List[Relationship])
    case class Node(`type`: String, name: Option[String], inputs: List[String], output: Option[String], parent: Option[Int])
    case class Relationship(`def`: Def, use: Use)
    case class Def(node: Int, evaluation: String, inner: Option[Boolean], port: Option[Int])
    case class Use(node: Int, port: Int)
    case class Result(project: Project, compiler: Compiler, model: Model, view: View, update: Update)
  }
}