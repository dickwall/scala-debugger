package org.scaladebugger.repl.commands

import org.scaladebugger.api.lowlevel.breakpoints.BreakpointRequestInfo
import org.scaladebugger.api.virtualmachines.ScalaVirtualMachine

import scala.util.{Failure => TryFailure, Success => TrySuccess}

class ListBreakpointsCommand(
  scalaVirtualMachine: ScalaVirtualMachine
) extends Command[Seq[BreakpointRequestInfo]] {
  /**
   * Evaluates the command.
   *
   * @return The result of the evaluation
   */
  override def evaluate(): CommandResult[Seq[BreakpointRequestInfo]] = {
    Success(scalaVirtualMachine.breakpointRequests, "")
  }

  /**
   * Returns a string of help information about the command.
   *
   * @return The string providing helpful information
   */
  override def toHelpString: String = "list breakpoints"
}
