package org.scaladebugger.api.profiles.dotty
import acyclic.file

import com.sun.jdi.VirtualMachine
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.dotty.breakpoints.DottyBreakpointProfile
import org.scaladebugger.api.profiles.dotty.classes.{DottyClassPrepareProfile, DottyClassUnloadProfile}
import org.scaladebugger.api.profiles.dotty.events.DottyEventProfile
import org.scaladebugger.api.profiles.dotty.exceptions.DottyExceptionProfile
import org.scaladebugger.api.profiles.dotty.info.{DottyGrabInfoProfile, DottyMiscInfoProfile}
import org.scaladebugger.api.profiles.dotty.methods.{DottyMethodEntryProfile, DottyMethodExitProfile}
import org.scaladebugger.api.profiles.dotty.monitors.{DottyMonitorContendedEnterProfile, DottyMonitorContendedEnteredProfile, DottyMonitorWaitProfile, DottyMonitorWaitedProfile}
import org.scaladebugger.api.profiles.dotty.steps.DottyStepProfile
import org.scaladebugger.api.profiles.dotty.threads.{DottyThreadDeathProfile, DottyThreadStartProfile}
import org.scaladebugger.api.profiles.dotty.vm.{DottyVMDeathProfile, DottyVMDisconnectProfile, DottyVMStartProfile}
import org.scaladebugger.api.profiles.dotty.watchpoints.{DottyAccessWatchpointProfile, DottyModificationWatchpointProfile}
import org.scaladebugger.api.profiles.traits.ManagerContainerDebugProfile

/**
 * Contains information about the Scala's dotty compiler debug profile.
 */
object DottyDebugProfile {
  val Name: String = "scala-dotty"
}

/**
 * Represents a debug profile that adds specific logic for Scala's dotty
 * compiler code.
 *
 * @param _virtualMachine The underlying virtual machine to use for various
 *                        retrieval methods
 * @param managerContainer The container of low-level managers to use as the
 *                         underlying implementation
 */
class DottyDebugProfile(
  protected val _virtualMachine: VirtualMachine,
  protected val managerContainer: ManagerContainer
) extends ManagerContainerDebugProfile
  with DottyAccessWatchpointProfile
  with DottyBreakpointProfile
  with DottyClassPrepareProfile
  with DottyClassUnloadProfile
  with DottyEventProfile
  with DottyExceptionProfile
  with DottyGrabInfoProfile
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
