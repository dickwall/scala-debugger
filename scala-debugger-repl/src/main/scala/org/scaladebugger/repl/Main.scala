package org.scaladebugger.repl

import org.scaladebugger.repl.commands.SetBreakpointCommand

/**
 * Represents the main entrypoint for the debugger repl.
 */
object Main {
  /**
   * Invoked as the main entrypoint of a new JVM.
   *
   * @param args The commandline arguments
   */
  def main(args: Array[String]): Unit = {
    val repl = new StandardRepl

    repl.loop()
  }
}
