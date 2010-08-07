package sing.handler

import javax.servlet.{Filter, FilterConfig, FilterChain, ServletRequest, ServletResponse}
import javax.servlet.http.{HttpServletRequest, HttpServletResponse}

import sing.Types._

case class FilterAdapter(app: App) extends Filter {
  def init(config: FilterConfig) { }
   
   def destroy { }
   
   def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) = {
     (request, response) match {
       case (request: HttpServletRequest, response: HttpServletResponse) => {
         val env = Map(
            "foo" -> "bar"
         )
         val (status, headers, body) = app(env)
         response.setStatus(status)
         headers.foreach(h => response.setHeader(h._1, h._2))
         response.getWriter.write(body)
       }
     } 
   }
}