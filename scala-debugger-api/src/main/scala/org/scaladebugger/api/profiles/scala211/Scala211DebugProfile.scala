package org.scaladebugger.api.profiles.scala211
import acyclic.file

import com.sun.jdi.VirtualMachine
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.scala211.breakpoints.Scala211BreakpointProfile
import org.scaladebugger.api.profiles.scala211.classes.{Scala211ClassPrepareProfile, Scala211ClassUnloadProfile}
import org.scaladebugger.api.profiles.scala211.events.Scala211EventProfile
import org.scaladebugger.api.profiles.scala211.exceptions.Scala211ExceptionProfile
import org.scaladebugger.api.profiles.scala211.info.{Scala211GrabInfoProfile, Scala211MiscInfoProfile}
import org.scaladebugger.api.profiles.scala211.methods.{Scala211MethodEntryProfile, Scala211MethodExitProfile}
import org.scaladebugger.api.profiles.scala211.monitors.{Scala211MonitorContendedEnterProfile, Scala211MonitorContendedEnteredProfile, Scala211MonitorWaitProfile, Scala211MonitorWaitedProfile}
import org.scaladebugger.api.profiles.scala211.steps.Scala211StepProfile
import org.scaladebugger.api.profiles.scala211.threads.{Scala211ThreadDeathProfile, Scala211ThreadStartProfile}
import org.scaladebugger.api.profiles.scala211.vm.{Scala211VMDeathProfile, Scala211VMDisconnectProfile, Scala211VMStartProfile}
import org.scaladebugger.api.profiles.scala211.watchpoints.{Scala211AccessWatchpointProfile, Scala211ModificationWatchpointProfile}
import org.scaladebugger.api.profiles.traits.ManagerContainerDebugProfile

/**
 * Contains information about the Scala 2.11 debug profile.
 */
object Scala211DebugProfile {
  val Name: String = "scala-2.11"
}

/**
 * Represents a debug profile that adds specific logic for Scala 2.11 code.
 *
 * @param _virtualMachine The underlying virtual machine to use for various
 *                        retrieval methods
 * @param managerContainer The container of low-level managers to use as the
 *                         underlying implementation
 */
class Scala211DebugProfile(
  protected val _virtualMachine: VirtualMachine,
  protected val managerContainer: ManagerContainer
) extends ManagerContainerDebugProfile
  with Scala211AccessWatchpointProfile
  with Scala211BreakpointProfile
  with Scala211ClassPrepareProfile
  with Scala211ClassUnloadProfile
  with Scala211EventProfile
  with Scala211ExceptionProfile
  with Scala211GrabInfoProfile
  with Scala211MethodEntryProfile
  with Scala211MethodExitProfile
  with Scala211MiscInfoProfile
  with Scala211ModificationWatchpointProfile
  with Scala211MonitorContendedEnteredProfile
  with Scala211MonitorContendedEnterProfile
  with Scala211MonitorWaitedProfile
  with Scala211MonitorWaitProfile
  with Scala211StepProfile
  with Scala211ThreadDeathProfile
  with Scala211ThreadStartProfile
  with Scala211VMStartProfile
  with Scala211VMDeathProfile
  with Scala211VMDisconnectProfile
