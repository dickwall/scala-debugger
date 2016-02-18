package org.scaladebugger.api.profiles.dotty

import com.sun.jdi.VirtualMachine
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.dotty.breakpoints.DottyBreakpointProfile
import org.scaladebugger.api.profiles.dotty.classes.{DottyClassPrepareProfile, DottyClassUnloadProfile}
import org.scaladebugger.api.profiles.dotty.events.DottyEventProfile
import org.scaladebugger.api.profiles.dotty.exceptions.DottyExceptionProfile
import org.scaladebugger.api.profiles.dotty.info.DottyMiscInfoProfile
import org.scaladebugger.api.profiles.dotty.methods.{DottyMethodEntryProfile, DottyMethodExitProfile}
import org.scaladebugger.api.profiles.dotty.monitors.{DottyMonitorContendedEnteredProfile, DottyMonitorContendedEnterProfile, DottyMonitorWaitedProfile, DottyMonitorWaitProfile}
import org.scaladebugger.api.profiles.dotty.steps.DottyStepProfile
import org.scaladebugger.api.profiles.dotty.threads.{DottyThreadDeathProfile, DottyThreadStartProfile}
import org.scaladebugger.api.profiles.dotty.vm.{DottyVMStartProfile, DottyVMDisconnectProfile, DottyVMDeathProfile}
import org.scaladebugger.api.profiles.dotty.watchpoints.{DottyAccessWatchpointProfile, DottyModificationWatchpointProfile}
import org.scaladebugger.api.profiles.traits.DebugProfile

/**
 * Contains information about the Scala's dotty compiler debug profile.
 */
object DottyDebugProfile {
  val Name: String = "scala-dotty"
}

/**
 * Represents a debug profile that adds specific logic for Scala's dotty compiler code.
 *
 * @param _virtualMachine The underlying virtual machine to use for various
 *                        retrieval methods
 * @param managerContainer The container of low-level managers to use as the
 *                         underlying implementation
 */
class DottyDebugProfile(
  protected val _virtualMachine: VirtualMachine,
  private val managerContainer: ManagerContainer
)
  extends DebugProfile
  with DottyAccessWatchpointProfile
  with DottyBreakpointProfile
  with DottyClassPrepareProfile
  with DottyClassUnloadProfile
  with DottyEventProfile
  with DottyExceptionProfile
  with DottyMethodEntryProfile
  with DottyMethodExitProfile
  with DottyMiscInfoProfile
  with DottyModificationWatchpointProfile
  with DottyMonitorContendedEnteredProfile
  with DottyMonitorContendedEnterProfile
  with DottyMonitorWaitedProfile
  with DottyMonitorWaitProfile
  with DottyStepProfile
  with DottyThreadDeathProfile
  with DottyThreadStartProfile
  with DottyVMStartProfile
  with DottyVMDeathProfile
  with DottyVMDisconnectProfile
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
