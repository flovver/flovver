package org.flovver.server

import org.eclipse.jetty.server.{Server, ServerConnector}
import org.eclipse.jetty.servlet.DefaultServlet
import org.eclipse.jetty.webapp.WebAppContext

import java.awt.Desktop
import java.net.URI

object FlovverServer {
  def run(): Unit = {
    // We request random available port from the system by passing zero value to the server constructor
    val port = 0

    val server = new Server(port)
    val context = new WebAppContext()

    context.setContextPath("/")

    val publicResourcePath = classOf[FlovverServlet].getClassLoader.getResource("public").toExternalForm
    context.setResourceBase(publicResourcePath)

    context.addServlet(classOf[FlovverServlet], "/api/*")
    context.addServlet(classOf[DefaultServlet], "/")

    server.setHandler(context)

    server.start()

    val actualPort = server.getConnectors.head.asInstanceOf[ServerConnector].getLocalPort
    openIdeInBrowser(actualPort)

    server.join()
  }

  private def openIdeInBrowser(actualPort: Int): Unit = {
    println(s"Running Flovver IDE at http://localhost:$actualPort")

    if (Desktop.isDesktopSupported && Desktop.getDesktop.isSupported(Desktop.Action.BROWSE)) {
      Desktop.getDesktop.browse(new URI(s"http://localhost:$actualPort"))
    }
  }
}
