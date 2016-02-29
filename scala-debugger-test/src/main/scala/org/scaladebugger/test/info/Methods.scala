package org.scaladebugger.test.info

/**
 * Provides test of examining method information.
 *
 * @note Should have a class name of org.scaladebugger.test.info.Methods
 */
object Methods {
  def main(args: Array[String]) = {
    def innerMethod(x: Int, y: String): String = x + y

    val x = 111
    val y = "test"

    val inm = innerMethod(x, y)
    val pum = publicMethod(x, y)
    val pom = protectedMethod(x)
    val pim = privateMethod(y)
    val zam = zeroArgMethod()
    val fum = functionMethod()

    println("Done!")
  }

  def publicMethod(x: Int, y: String): String = x + y

  protected def protectedMethod(x: Int): Int = x

  private def privateMethod(y: String): String = y

  def zeroArgMethod(): Int = 999

  val functionMethod = () => 999
}
