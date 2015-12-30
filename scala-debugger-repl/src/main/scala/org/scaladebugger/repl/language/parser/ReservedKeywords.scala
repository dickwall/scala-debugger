package org.scaladebugger.repl.language.parser

/**
 * Contains the collection of reserved keywords for the small debugger language.
 */
object ReservedKeywords {
  /**
   * Returns the keywords as a collection.
   *
   * @return The collection of keywords
   */
  def toSeq: Seq[String] = Seq(
    "var",      // Variable creation
    "create",   // Request creation
    "destroy",  // Request deletion
    "list",     // Request listing
    "if",       // Start of condition
    "else",     // Alternative branch of condition
    "end",      // End of condition block
    "help",     // Display help information

    // Debugger-related keywords
    "attach",
    "launch",
    "connect",

    // Request-related keywords
    "access", "watchpoint", "aw",
    "modification", "watchpoint", "mw",
    "breakpoint", "bp",
    "class", "prepare", "cp",
    "class", "unload", "cu",
    "exception", "ex",
    "event", "ev",
    "method", "entry", "me",
    "method", "exit", "mx",
    "monitor", "contended", "entered", "mcd",
    "monitor", "contended", "enter", "mce",
    "monitor", "waited", "md",
    "monitor", "wait", "mt",
    "step", "sp",
    "thread", "death", "td",
    "thread", "start", "ts",
    "vm", "death", "vd"
  ).distinct
}
