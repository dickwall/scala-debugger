package org.scaladebugger.api.profiles.pure

import com.sun.jdi.VirtualMachine
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.pure.breakpoints.PureBreakpointProfile
import org.scaladebugger.api.profiles.pure.classes.{PureClassUnloadProfile, PureClassPrepareProfile}
import org.scaladebugger.api.profiles.pure.events.PureEventProfile
import org.scaladebugger.api.profiles.pure.exceptions.PureExceptionProfile
import org.scaladebugger.api.profiles.pure.info.PureMiscInfoProfile
import org.scaladebugger.api.profiles.pure.methods.{PureMethodExitProfile, PureMethodEntryProfile}
import org.scaladebugger.api.profiles.pure.monitors.{PureMonitorWaitProfile, PureMonitorWaitedProfile, PureMonitorContendedEnterProfile, PureMonitorContendedEnteredProfile}
import org.scaladebugger.api.profiles.pure.steps.PureStepProfile
import org.scaladebugger.api.profiles.pure.threads.{PureThreadStartProfile, PureThreadDeathProfile}
import org.scaladebugger.api.profiles.pure.vm.{PureVMDisconnectProfile, PureVMStartProfile, PureVMDeathProfile}
import org.scaladebugger.api.profiles.pure.watchpoints.{PureAccessWatchpointProfile, PureModificationWatchpointProfile}
import org.scaladebugger.api.profiles.traits.DebugProfile

/**
 * Contains information about the pure debug profile.
 */
object PureDebugProfile {
  val Name: String = "pure"
}

/**
 * Represents a debug profile that adds no extra logic on top of the standard
 * JDI.
 *
 * @param _virtualMachine The underlying virtual machine to use for various
 *                        retrieval methods
 * @param managerContainer The container of low-level managers to use as the
 *                         underlying implementation
 */
class PureDebugProfile(
  protected val _virtualMachine: VirtualMachine,
  private val managerContainer: ManagerContainer
)
  extends DebugProfile
  with PureAccessWatchpointProfile
  with PureBreakpointProfile
  with PureClassPrepareProfile
  with PureClassUnloadProfile
  with PureEventProfile
  with PureExceptionProfile
  with PureMethodEntryProfile
  with PureMethodExitProfile
  with PureMiscInfoProfile
  with PureModificationWatchpointProfile
  with PureMonitorContendedEnteredProfile
  with PureMonitorContendedEnterProfile
  with PureMonitorWaitedProfile
  with PureMonitorWaitProfile
  with PureStepProfile
  with PureThreadDeathProfile
  with PureThreadStartProfile
  with PureVMStartProfile
  with PureVMDeathProfile
  with PureVMDisconnectProfile
{
  protected lazy val accessWatchpointManager =
    managerContainer.accessWatchpointManager

  protected lazy val breakpointManager = managerContainer.breakpointManager

  protected lazy val classManager = managerContainer.classManager

  protected lazy val classPrepareManager = managerContainer.classPrepareManager

  protected lazy val classUnloadManager = managerContainer.classUnloadManager

  protected lazy val eventManager = managerContainer.eventManager

  protected lazy val exceptionManager = managerContainer.exceptionManager

  protected lazy val modificationWatchpointManager =
    managerContainer.modificationWatchpointManager

  protected lazy val monitorContendedEnteredManager =
    managerContainer.monitorContendedEnteredManager

  protected lazy val monitorContendedEnterManager =
    managerContainer.monitorContendedEnterManager

  protected lazy val monitorWaitedManager =
    managerContainer.monitorWaitedManager

  protected lazy val monitorWaitManager =
    managerContainer.monitorWaitManager

  protected lazy val methodEntryManager = managerContainer.methodEntryManager

  protected lazy val methodExitManager = managerContainer.methodExitManager

  protected lazy val stepManager = managerContainer.stepManager

  protected lazy val threadDeathManager = managerContainer.threadDeathManager

  protected lazy val threadStartManager = managerContainer.threadStartManager

  protected lazy val vmDeathManager = managerContainer.vmDeathManager
}