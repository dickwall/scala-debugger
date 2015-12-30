package org.scaladebugger.repl.language.parser

/**
 * Contains the collection of delimiters for the small debugger language.
 */
object Delimiters {
  /**
   * Returns the delimiters as a collection.
   *
   * @return The collection of delimiters
   */
  def toSeq: Seq[String] = Seq(
    // Standard operators
    "*",
    "/",
    "%",
    "+",
    "-",

    // Expression separation
    "(",
    ")",

    // Assignment-related
    "=",

    // Conditional-related
    "<",
    ">",
    "==",
    "!=",
    "<=",
    ">=",

    // List-related
    ",",

    // ???
    ":"
  )
}
