package org.flovver.cmd.cli

import org.flovver.server.FlovverServer

import java.io.File

object Actions {
  def scaffoldProject(project: String): Unit = {
    val projectDirectory = new File(project)

    if (projectDirectory.exists()) {
      println(s"directory ${'"'}$project${'"'} is already exist")
    }

    val projectDirectoryIsCreated = projectDirectory.mkdir()

    if (projectDirectoryIsCreated) {
      Seq("model", "view", "update", "project")
        .map(_ + ".json")
        .foreach { fileName =>
          new File(project + File.separator + fileName).createNewFile()
        }
    }
  }

  def launchIde(folder: String): Unit = {
    FlovverServer.run()
  }
}
