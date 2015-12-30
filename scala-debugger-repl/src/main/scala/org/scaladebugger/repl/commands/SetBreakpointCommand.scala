package org.scaladebugger.repl.commands

import org.scaladebugger.api.lowlevel.breakpoints.BreakpointRequestInfo
import org.scaladebugger.api.virtualmachines.ScalaVirtualMachine

import scala.util.{Failure => TryFailure, Success => TrySuccess}

class SetBreakpointCommand(
  scalaVirtualMachine: ScalaVirtualMachine,
  breakpointRequestInfo: BreakpointRequestInfo
) extends Command[BreakpointRequestInfo] {
  /**
   * Evaluates the command.
   *
   * @return The result of the evaluation
   */
  override def evaluate(): CommandResult[BreakpointRequestInfo] = {
    val fileName = breakpointRequestInfo.fileName
    val lineNumber = breakpointRequestInfo.lineNumber
    val arguments = breakpointRequestInfo.extraArguments

    scalaVirtualMachine.onBreakpoint(fileName, lineNumber, arguments: _*) match {
      case TrySuccess(_)  => Success(
        breakpointRequestInfo,
        s"Successfully created breakpoint $fileName:$lineNumber"
      )
      case TryFailure(ex) => Failure(
        s"Failed to create breakpoint $fileName:$lineNumber:\n$ex"
      )
    }
  }

  /**
   * Returns a string of help information about the command.
   *
   * @return The string providing helpful information
   */
  override def toHelpString: String = ???
}
