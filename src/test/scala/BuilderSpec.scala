package sing

import org.specs._

object BuilderSpec extends Specification {
  
  type MyAppType = { def apply(a:Map[String, Any]): (Int, Map[String, String], String) }
  
  "Builder" should {
    "build an application with raw types" in {
      val app = new Function1[Map[String, Any], (Int, Map[String, String], String)] {
        def apply(e: Map[String, Any]) = (200, Map("Content-Type"->"text/plain"), "oh hai with env %s" format(e))
      }

      val mw1 = new Function1[MyAppType, MyAppType] {
        def apply(a: MyAppType) = new Function1[Map[String, Any], (Int, Map[String, String], String)]{
           def apply(e: Map[String, Any]) = {
             val (status, headers, body) = a(e)
             (status, headers, "mw1(%s)" format body)
           }
        }
      }

      object mw2 extends (MyAppType => MyAppType) {
        class MappAppending(a: MyAppType) {
          def apply(e: Map[String, Any]) = {
             val (status, headers, body) = a(e + ("baz" -> "boom"))
             (status, headers, "mw2(%s)" format body)
          }
        }
        def apply(a: MyAppType) = new MappAppending(a)
      }

      val (status, headers, body) = (new Builder use(() => mw1) use(() => mw2) run(app))(Map("foo"->"bar"))
      
      status must_==(200)
      headers must havePair("Content-Type" -> "text/plain")
      body must_==("mw1(mw2(oh hai with env Map(foo -> bar, baz -> boom)))")
    }
    
    "builder an application with provided types" in {
      import sing.Types._
      val app = new Function1[Env, Response] {
        def apply(e: Env) = (200, Map("Content-Type"->"text/plain"), "oh hai with env %s" format(e))
      }

      val mw1 = new Function1[App, App] {
        def apply(a: MyAppType) = new Function1[Env, Response]{
           def apply(e: Env) = {
             val (status, headers, body) = a(e)
             (status, headers, "mw1(%s)" format body)
           }
        }
      }

      object mw2 extends (App => App) {
        class MappAppending(a: App) {
          def apply(e: Env) = {
             val (status, headers, body) = a(e + ("baz" -> "boom"))
             (status, headers, "mw2(%s)" format body)
          }
        }
        def apply(a: App) = new MappAppending(a)
      }

      val (status, headers, body) = (new Builder use(() => mw1) use(() => mw2) run(app))(Map("foo"->"bar"))
      
      status must_==(200)
      headers must havePair("Content-Type" -> "text/plain")
      body must_==("mw1(mw2(oh hai with env Map(foo -> bar, baz -> boom)))")
    }
  }
}