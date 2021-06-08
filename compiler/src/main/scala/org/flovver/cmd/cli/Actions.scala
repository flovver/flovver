package org.flovver.cmd.cli

import org.flovver.server.FlovverServer
import org.flovver.compiler

import java.io.{File, PrintWriter}

object Actions {
  def scaffoldProject(project: String): Unit = {
    val projectDirectory = new File(project)

    if (projectDirectory.exists()) {
      println(s"directory ${'"'}$project${'"'} is already exist")
    }

    val projectDirectoryIsCreated = projectDirectory.mkdir()

    if (projectDirectoryIsCreated) {
      new PrintWriter(new File(project + File.separator + "project.json")) {
        write(
          s"""{
             |  "project": {
             |    "name": "$project"
             |  },
             |  "compiler": {
             |    "flags": {
             |      "tail-call-elimination": false,
             |      "fibonacci-elimination": false,
             |      "common-recursion-memoization": false,
             |      "debug": true
             |    }
             |  },
             |  "model": {
             |    "model-type": "Model",
             |    "message-type": "Msg",
             |    "types": [
             |      {
             |        "base": "Int",
             |        "alias": "Model",
             |        "x": 880,
             |        "y": 300
             |      },
             |      {
             |        "base": "Variant",
             |        "alias": "Msg",
             |        "x": 835,
             |        "y": 350,
             |        "variants": [
             |          {
             |            "name": "NewInput",
             |            "type": "String"
             |          },
             |          {
             |            "name": "ComputeFactorial",
             |            "type": "Unit"
             |          }
             |        ]
             |      }
             |    ]
             |  },
             |  "view": {
             |    "pane": {
             |      "width": 420,
             |      "height": 65
             |    },
             |    "widgets": [
             |      {
             |        "type": "Label",
             |        "caption": "Factorial",
             |        "x": 5,
             |        "y": 14,
             |        "onclick": "",
             |        "oninput": ""
             |      },
             |      {
             |        "type": "TextBox",
             |        "caption": "${"$"}{model}",
             |        "x": 83,
             |        "y": 14,
             |        "onclick": "",
             |        "oninput": "signal({ tag: 'NewInput', value: this.value })"
             |      },
             |      {
             |        "type": "Button",
             |        "caption": "Compute",
             |        "x": 330,
             |        "y": 14,
             |        "onclick": "signal({ tag: 'ComputeFactorial' })",
             |        "oninput": ""
             |      }
             |    ]
             |  },
             |  "update": {
             |    "nodes": [
             |      {
             |        "type": "model-input",
             |        "inputs": [],
             |        "output": "Model",
             |        "x": 277,
             |        "y": 360,
             |        "name": "Model"
             |      },
             |      {
             |        "type": "message-input",
             |        "inputs": [],
             |        "output": "Msg",
             |        "x": 1260,
             |        "y": 213,
             |        "name": "Message"
             |      },
             |      {
             |        "type": "model-output",
             |        "inputs": [
             |          "Model"
             |        ],
             |        "x": 1702,
             |        "y": 385,
             |        "name": "View"
             |      }
             |    ],
             |    "relationships": []
             |  }
             |}""".stripMargin)
        close()
      }
    }
  }

  def launchIde(folder: String): Unit = {
    FlovverServer.run(folder)
  }

  def buildProject(folder: String): Unit = {
    new compiler.Compiler(folder)
  }
}
