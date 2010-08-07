package sing

import sing.Types._
import sing.handler.Jetty

object Main {
  def main(args: Array[String]) {
    Jetty(8080).run(new Function[Env, Response] {
      def apply(e:Env) = (200, Map("Content-Type"->"text/plain"), "hi world")
    }).run()
  }
}