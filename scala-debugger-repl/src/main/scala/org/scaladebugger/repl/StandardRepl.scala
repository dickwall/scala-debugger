package org.scaladebugger.repl

import java.io.PrintWriter

import jline.console.ConsoleReader
import org.scaladebugger.repl.language.parser.DebuggerParser
import org.scaladebugger.repl.language.runtime.DebuggerInterpreter

/**
 * Represents the interface for the READ-EVAL-PRINT-LOOP construct.
 */
class StandardRepl extends Repl {
  private lazy val consoleReader = new ConsoleReader(System.in, System.out)
  private lazy val out = new PrintWriter(consoleReader.getOutput)
  private lazy val interpreter = new DebuggerInterpreter(new DebuggerParser)

  /**
   * Reads the next chunk of code.
   *
   * @return Some string of code if available, otherwise None
   */
  override def read(): Option[String] = {
    Option(consoleReader.readLine("debug> "))
  }

  /**
   * Evaluates the given code, returning the result.
   *
   * @param code The code to evaluate as a string
   *
   * @return The result of the evaluation as a string
   */
  override def eval(code: String): String = {
    interpreter.interpret(code)
  }

  /**
   * Prints out the result of an operation.
   *
   * @param result The result to print out as a string
   */
  override def print(result: String): Unit = {
    out.println(result)
    out.flush()
  }
}
