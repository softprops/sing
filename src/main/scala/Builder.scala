package sing

import Types._

/** A simple dsl for building up applications with middleware */
class Builder {
  import scala.collection.mutable.{ListBuffer => Buffer}
  
  private val middleware = new Buffer[MBinder[Middleware]]
  
  /** a generic function that, when executed should 
   * return a unit of middleware 
   */
  type MBinder[T <: Middleware] = () => T
  
  /** wrap application in a unit of middleware */
  def use[T <: Middleware](mb: MBinder[T]) = {
    middleware += mb
    this
  }
  
  /** builds the wrapped application */
  def run(a: App) = (a /: middleware.reverse)((a, m)=> m()(a))
}