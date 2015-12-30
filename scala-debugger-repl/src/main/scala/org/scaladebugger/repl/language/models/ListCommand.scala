package org.scaladebugger.repl.language.models

/**
 * Represents a command to list requests.
 */
class ListCommand() extends Command

case class ListBreakpointRequestsCommand() extends ListCommand
case class ListClassPrepareRequestsCommand() extends ListCommand
case class ListClassUnloadRequestsCommand() extends ListCommand
case class ListEventHandlersCommand() extends ListCommand
case class ListMethodEntryRequestsCommand() extends ListCommand
case class ListMethodExitRequestsCommand() extends ListCommand
case class ListMonitorContendedEnteredRequestsCommand() extends ListCommand
case class ListMonitorContendedEnterRequestsCommand() extends ListCommand
case class ListMonitorWaitedRequestsCommand() extends ListCommand
case class ListMonitorWaitRequestsCommand() extends ListCommand
case class ListStepRequestsCommand() extends ListCommand
case class ListThreadDeathRequestsCommand() extends ListCommand
case class ListThreadStartRequestsCommand() extends ListCommand
case class ListVMDeathRequestsCommand() extends ListCommand
case class ListAccessWatchpointRequestsCommand() extends ListCommand
case class ListModificationWatchpointRequestsCommand() extends ListCommand
