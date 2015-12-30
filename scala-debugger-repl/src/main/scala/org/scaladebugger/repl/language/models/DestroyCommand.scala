package org.scaladebugger.repl.language.models

import com.sun.jdi.ThreadReference

/**
 * Represents a command to destroy something.
 */
class DestroyCommand extends Command

case class DestroyBreakpointRequestCommand(
  fileName: String,
  lineNumber: String
) extends DestroyCommand

case class DestroyClassPrepareRequestCommand() extends DestroyCommand

case class DestroyClassUnloadRequestCommand() extends DestroyCommand

case class DestroyEventHandlerCommand(
  eventHandlerId: String
) extends DestroyCommand

case class DestroyMethodEntryRequestCommand(
  className: String,
  methodName: String
) extends DestroyCommand

case class DestroyMethodExitRequestCommand(
  className: String,
  methodName: String
) extends DestroyCommand

case class DestroyMonitorContendedEnteredRequestCommand() extends DestroyCommand

case class DestroyMonitorContendedEnterRequestCommand() extends DestroyCommand

case class DestroyMonitorWaitedRequestCommand() extends DestroyCommand

case class DestroyMonitorWaitRequestCommand() extends DestroyCommand

case class DestroyStepRequestCommand(
  threadReference: ThreadReference
) extends DestroyCommand

case class DestroyThreadDeathRequestCommand() extends DestroyCommand

case class DestroyThreadStartRequestCommand() extends DestroyCommand

case class DestroyVMDeathRequestCommand() extends DestroyCommand

case class DestroyAccessWatchpointRequestCommand(
  className: String,
  fieldName: String
) extends DestroyCommand

case class DestroyModificationWatchpointRequestCommand(
  className: String,
  fieldName: String
) extends DestroyCommand
