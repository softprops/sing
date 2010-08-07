package sing.handler

import sing.Types._

case class Jetty(port: Int) {
  
  import org.eclipse.jetty.server.{Server => JettyServer, Connector, Handler}
  import org.eclipse.jetty.server.handler.{ContextHandlerCollection, ResourceHandler}
  import org.eclipse.jetty.servlet.{FilterHolder, FilterMapping, ServletContextHandler}
  import org.eclipse.jetty.server.bio.SocketConnector
  import org.eclipse.jetty.util.resource.Resource
   
  val conn = new SocketConnector()
  conn.setPort(port)
  
  val server = new JettyServer()
  server.addConnector(conn)
  val handlers = new ContextHandlerCollection
  server.setHandler(handlers)
  
  val ctx = new ServletContextHandler(handlers, "/", false, false)
  ctx.addServlet(classOf[org.eclipse.jetty.servlet.DefaultServlet], "/")
  handlers.addHandler(ctx)
  
  def run(app: App) = {
    ctx.addFilter(new FilterHolder(FilterAdapter(app)), "/*", FilterMapping.DEFAULT)
    this
  }
  
  def run() {
    Thread.currentThread.getName match {
      case "main" => 
        server.setStopAtShutdown(true)
        server.start()
        server.join()
      case _ => 
        server.start()
        println("Embedded server running. Press any key to stop.")
        def doWait() {
          try { Thread.sleep(1000) } catch { case _: InterruptedException => () }
          if(System.in.available() <= 0)
            doWait()
        }
        doWait()
        stop()
    }
  }
 
  def start() {
    server.start()
  }

  def stop() {
    server.stop() 
  }
}