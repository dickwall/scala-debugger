package org.scaladebugger.repl.language.models

import com.sun.jdi.ThreadReference
import org.scaladebugger.api.lowlevel.events.EventManager.EventHandler
import org.scaladebugger.api.lowlevel.events.EventType.EventType
import org.scaladebugger.api.lowlevel.events.JDIEventArgument
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument

/**
 * Represents a command to create something.
 */
class CreateCommand extends Command

/**
 * Represents a command to create a JDI request.
 *
 * @param extraArguments Extra arguments to provide to the request
 */
class CreateRequestCommand(
  val extraArguments: Seq[JDIRequestArgument]
) extends CreateCommand

case class CreateBreakpointRequestCommand(
  fileName: String,
  lineNumber: Int,
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateClassPrepareRequestCommand(
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateClassUnloadRequestCommand(
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateEventHandlerCommand(
  eventType: EventType,
  eventHandler: EventHandler,
  extraArguments: Seq[JDIEventArgument]
) extends CreateCommand

case class CreateMethodEntryRequestCommand(
  className: String,
  methodName: String,
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateMethodExitRequestCommand(
  className: String,
  methodName: String,
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateMonitorContendedEnteredRequestCommand(
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateMonitorContendedEnterRequestCommand(
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateMonitorWaitedRequestCommand(
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateMonitorWaitRequestCommand(
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateStepRequestCommand(
  threadReference: ThreadReference,
  size: Int,
  depth: Int,
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateThreadDeathRequestCommand(
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateThreadStartRequestCommand(
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateVMDeathRequestCommand(
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateAccessWatchpointRequestCommand(
  className: String,
  fieldName: String,
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

case class CreateModificationWatchpointRequestCommand(
  className: String,
  fieldName: String,
  override val extraArguments: Seq[JDIRequestArgument]
) extends CreateRequestCommand(extraArguments)

