package org.flovver.compiler

import org.flovver.compiler.backend.CodeGenerator
import org.flovver.compiler.frontend.PayloadParser
import org.flovver.compiler.ir.{Call, Definition, RecursiveCall, Sandbox}
import org.flovver.compiler.middle.Optimizer
import org.flovver.compiler.view.IndexBuilder

class Compiler extends PayloadParser with Env with Sandbox with Optimizer with CodeGenerator with IndexBuilder {
  //private val p = parse("{\n  \"project\": {\n    \"name\": \"factorial\"\n  },\n  \"compiler\": {\n    \"flags\": {\n      \"tail-call-elimination\": false,\n      \"fibonacci-elimination\": false,\n      \"common-recursion-memoization\": false\n    }\n  },\n  \"model\": {\n    \"model-type\": \"Model\",\n    \"message-type\": \"Msg\",\n    \"types\": [\n      {\n        \"base\": \"Int\",\n        \"alias\": \"Model\",\n        \"x\": 100,\n        \"y\": 100\n      },\n      {\n        \"base\": \"Variant\",\n        \"alias\": \"Msg\",\n        \"variants\": [\n          { \"name\": \"NewInput\", \"type\": \"String\" },\n          { \"name\": \"ComputeFactorial\", \"type\": \"Unit\" }\n        ],\n        \"x\": 200,\n        \"y\": 100\n      }\n    ]\n  },\n  \"view\": {\n    \"pane\": {\n      \"width\": 420,\n      \"height\": 65\n    },\n    \"widgets\": [\n      {\n        \"type\": \"Label\",\n        \"caption\": \"Factorial\",\n        \"x\": 5,\n        \"y\": 14\n      },\n      {\n        \"type\": \"TextBox\",\n        \"caption\": \"${model}\",\n        \"x\": 83,\n        \"y\": 14\n      },\n      {\n        \"type\": \"Button\",\n        \"caption\": \"Compute\",\n        \"x\": 330,\n        \"y\": 14\n      }\n    ]\n  },\n  \"update\": {\n    \"nodes\": [\n      {\n        \"type\": \"model-input\",\n        \"output\": \"Model\",\n        \"x\": 100,\n        \"y\": 100\n      },\n      {\n        \"type\": \"message-input\",\n        \"output\": \"Msg\",\n        \"x\": 100,\n        \"y\": 300\n      },\n      {\n        \"type\": \"definition\",\n        \"name\": \"Test Function\",\n        \"inputs\": [\"Int\"],\n        \"output\": \"Int\",\n        \"x\": 300,\n        \"y\": 200,\n        \"width\": 300,\n        \"height\": 200,\n        \"minimized\": false\n      },\n      {\n        \"type\": \"call\",\n        \"name\": \"+\",\n        \"inputs\": [\"Int\", \"Int\"],\n        \"output\": \"Int\",\n        \"x\": 200,\n        \"y\": 200\n      },\n      {\n        \"type\": \"call\",\n        \"name\": \"+1\",\n        \"inputs\": [\"Int\"],\n        \"output\": \"Int\",\n        \"x\": 300,\n        \"y\": 200,\n        \"parent\": 2\n      },\n      {\n        \"type\": \"model-output\",\n        \"inputs\": [\"Model\"],\n        \"x\": 100,\n        \"y\": 500\n      }\n    ],\n    \"relationships\": [\n      {\n        \"def\": { \"node\": 0, \"evaluation\": \"strict\" },\n        \"use\": { \"node\": 2, \"port\": 0 }\n      },\n      {\n        \"def\": { \"node\": 2, \"evaluation\": \"strict\" },\n        \"use\": { \"node\": 4, \"port\": 0 }\n      },\n      {\n        \"def\": {\n          \"node\": 2,\n          \"evaluation\": \"strict\",\n          \"inner\": true,\n          \"port\": 0\n        },\n        \"use\": { \"node\": 4, \"port\": 0 }\n      },\n      {\n        \"def\": { \"node\": 2, \"evaluation\": \"strict\" },\n        \"use\": { \"node\": 5, \"port\": 0 }\n      }\n    ]\n  }\n}")
  //buildIndex(p.view)
  override val useTailCallElimination: Boolean = false
  override val useCommonRecursionMemoization: Boolean = true
  override val useFibonacciRecursionElimination: Boolean = false

  // private val program = Definition(2)
  private val factorial = Definition(1)

  private val ifNode = Call(3, "If")
  private val one = Call(0, "Int1")
  private val mul = Call(2, "Mul")
  private val eq = Call(2, "Eq")
  private val minusOne = Call(1, "MinusOne")
  private val self = RecursiveCall(1, factorial)

  nodes.appendAll(Seq(/*program,*/ factorial, ifNode, one, mul, eq, minusOne, self))

  output(ifNode, factorial)

  use(eq, ifNode, 0)(byValue = true)
  use(one, ifNode, 1)(byValue = false)
  use(mul, ifNode, 2)(byValue = false)

  use(one, eq, 0)(byValue = true)
  input(factorial, 0, eq, 1)

  input(factorial, 0, mul, 0)
  use(self, mul, 1)(byValue = true)

  input(factorial, 0, minusOne, 0)

  use(minusOne, self, 0)(byValue = true)

  // input(program, 0, factorial, 0)
  // output(factorial, program)

  println(nodes)

  topSort()

  println(nodes)

  generate()
}
