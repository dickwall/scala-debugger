package org.scaladebugger.api.lowlevel.watchpoints
import acyclic.file

import org.scaladebugger.api.utils.PendingActionManager

/**
 * Provides pending access watchpoint capabilities to an existing access
 * watchpoint manager.
 *
 * Contains an internal pending action manager.
 */
trait StandardPendingAccessWatchpointSupport extends PendingAccessWatchpointSupport {
  override protected val pendingActionManager: PendingActionManager[AccessWatchpointRequestInfo] =
    new PendingActionManager[AccessWatchpointRequestInfo]
}
