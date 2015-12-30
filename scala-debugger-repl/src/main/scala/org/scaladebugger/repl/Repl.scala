package org.scaladebugger.repl

/**
 * Represents the interface for the READ-EVAL-PRINT-LOOP construct.
 */
trait Repl {
  /**
   * Reads the next chunk of code.
   *
   * @return Some string of code if available, otherwise None
   */
  def read(): Option[String]

  /**
   * Evaluates the given code, returning the result.
   *
   * @param code The code to evaluate as a string
   *
   * @return The result of the evaluation as a string
   */
  def eval(code: String): String

  /**
   * Prints out the result of an operation.
   *
   * @param result The result to print out as a string
   */
  def print(result: String): Unit

  /**
   * Performs a standard loop using the READ-EVAL-PRINT operations.
   */
  def loop(): Unit = {
    @volatile var input: Option[String] = None

    do {
      input = read()

      input.map(eval).foreach(print)

      Thread.sleep(1)
    } while (input.nonEmpty)
  }
}
