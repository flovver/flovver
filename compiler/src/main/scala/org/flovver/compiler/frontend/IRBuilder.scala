package org.flovver.compiler.frontend

import org.flovver.compiler.Env
import org.flovver.compiler.ir.{Abstraction, Application, Call, Definition, Node, RecursiveCall, Sandbox}

trait IRBuilder { this: PayloadParser with Sandbox with Env =>

  def buildIR(): Unit = {
    for (node <- payload.update.nodes) {
      node.`type` match {
        case "model-input" => addNode(modelInput)
        case "message-input" => addNode(messageInput)
        case "definition" => addNode(Definition(node.inputs.size))
        case "call" => addNode(Call(node.inputs.size, node.name.get))
        case "recursive-call" => addNode(RecursiveCall(node.inputs.size, nodes(node.parent.get).asInstanceOf[Definition]))
        case "model-output" => addNode(modelOutput)
        case _ => throw new IllegalArgumentException()
      }
    }

    for (rel <- payload.update.relationships) {
      rel.location match {
        case "external" => use(nodes(rel.`def`), nodes(rel.use).asInstanceOf[Node with Application], rel.`use-arg`)(rel.`pass-by` == "value")
        case "internal" =>
          // TODO: классифицировать связи в Payload более надежным способом
          rel.parameter match {
            case Some(parameter) => input(nodes(rel.`def`).asInstanceOf[Node with Abstraction], parameter, nodes(rel.use).asInstanceOf[Node with Application], rel.`use-arg`)
            case None => output(nodes(rel.`def`), nodes(rel.use).asInstanceOf[Node with Abstraction])
          }
        case _ => throw new IllegalArgumentException()
      }
    }
  }
}
