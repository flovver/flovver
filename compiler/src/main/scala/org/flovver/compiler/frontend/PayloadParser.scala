package org.flovver.compiler.frontend

import org.flovver.compiler.Env
import org.json4s._
import org.json4s.jackson.JsonMethods

import java.io.File

trait PayloadProvider {
  val payload: Payload
}

/**
 * Парсер данных, посылаемых клиентом серверу.
 */
trait PayloadParser extends PayloadProvider { this: Env =>
  lazy val payload: Payload = {
    val f = new File(Env.projectFolder + File.separator + "project.json")
    parse(f)
  }

  implicit val formats: Formats = DefaultFormats

  /**
   * Разбирает данные, хранимые в `source`,
   * переводит в программное представление, хранимое в памяти.
   *
   * @return данные в программном представлении
   */
  def parse[A: AsJsonInput](source: A): Payload = {
    val ast = JsonMethods.parse(source)
    val result = ast.extract[Payload]
    result
  }
}