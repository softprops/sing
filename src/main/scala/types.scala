package sing

/** Basic types that should be defined by the ? spec */
object Types {
  /** tuple of (statusCode, headers, body)
   *  @note the body type needs thought out more
   *  other like specs use some kind of iterable to support
   *  output stream and files as response body
   */
  type Response = (Int, Map[String, String], String)

  /** An Env represents the current context of the request. 
   *  It should contain information relavant to the http request
   *  and also can contain application specific information.
   */
  type Env = Map[String, Any]

  /** Apps are the handlers of requests. They are given an Env containing
   *  requestion information and return some response.
   */
  type App = {
    def apply(e: Env): Response
  }
  
  /** Middleware function augment applicaton behavoir and acts as extending points
   *  for injecting new and reusable behavior
   */
  type Middleware = {
    def apply(a: App): App
  }
}