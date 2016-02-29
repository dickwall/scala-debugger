package org.scaladebugger.api.profiles.scala212
import acyclic.file

import com.sun.jdi.VirtualMachine
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.scala212.breakpoints.Scala212BreakpointProfile
import org.scaladebugger.api.profiles.scala212.classes.{Scala212ClassPrepareProfile, Scala212ClassUnloadProfile}
import org.scaladebugger.api.profiles.scala212.events.Scala212EventProfile
import org.scaladebugger.api.profiles.scala212.exceptions.Scala212ExceptionProfile
import org.scaladebugger.api.profiles.scala212.info.{Scala212GrabInfoProfile, Scala212MiscInfoProfile}
import org.scaladebugger.api.profiles.scala212.methods.{Scala212MethodEntryProfile, Scala212MethodExitProfile}
import org.scaladebugger.api.profiles.scala212.monitors.{Scala212MonitorContendedEnterProfile, Scala212MonitorContendedEnteredProfile, Scala212MonitorWaitProfile, Scala212MonitorWaitedProfile}
import org.scaladebugger.api.profiles.scala212.steps.Scala212StepProfile
import org.scaladebugger.api.profiles.scala212.threads.{Scala212ThreadDeathProfile, Scala212ThreadStartProfile}
import org.scaladebugger.api.profiles.scala212.vm.{Scala212VMDeathProfile, Scala212VMDisconnectProfile, Scala212VMStartProfile}
import org.scaladebugger.api.profiles.scala212.watchpoints.{Scala212AccessWatchpointProfile, Scala212ModificationWatchpointProfile}
import org.scaladebugger.api.profiles.traits.ManagerContainerDebugProfile

/**
 * Contains information about the Scala 2.12 debug profile.
 */
object Scala212DebugProfile {
  val Name: String = "scala-2.12"
}

/**
 * Represents a debug profile that adds specific logic for Scala 2.12 code.
 *
 * @param _virtualMachine The underlying virtual machine to use for various
 *                        retrieval methods
 * @param managerContainer The container of low-level managers to use as the
 *                         underlying implementation
 */
class Scala212DebugProfile(
  protected val _virtualMachine: VirtualMachine,
  protected val managerContainer: ManagerContainer
) extends ManagerContainerDebugProfile
  with Scala212AccessWatchpointProfile
  with Scala212BreakpointProfile
  with Scala212ClassPrepareProfile
  with Scala212ClassUnloadProfile
  with Scala212EventProfile
  with Scala212ExceptionProfile
  with Scala212GrabInfoProfile
  with Scala212MethodEntryProfile
  with Scala212MethodExitProfile
  with Scala212MiscInfoProfile
  with Scala212ModificationWatchpointProfile
  with Scala212MonitorContendedEnteredProfile
  with Scala212MonitorContendedEnterProfile
  with Scala212MonitorWaitedProfile
  with Scala212MonitorWaitProfile
  with Scala212StepProfile
  with Scala212ThreadDeathProfile
  with Scala212ThreadStartProfile
  with Scala212VMStartProfile
  with Scala212VMDeathProfile
  with Scala212VMDisconnectProfile
