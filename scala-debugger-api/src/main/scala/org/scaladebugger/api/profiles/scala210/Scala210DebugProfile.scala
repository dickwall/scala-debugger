package org.scaladebugger.api.profiles.scala210
import acyclic.file

import com.sun.jdi.VirtualMachine
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.scala210.breakpoints.Scala210BreakpointProfile
import org.scaladebugger.api.profiles.scala210.classes.{Scala210ClassPrepareProfile, Scala210ClassUnloadProfile}
import org.scaladebugger.api.profiles.scala210.events.Scala210EventProfile
import org.scaladebugger.api.profiles.scala210.exceptions.Scala210ExceptionProfile
import org.scaladebugger.api.profiles.scala210.info.{Scala210GrabInfoProfile, Scala210MiscInfoProfile}
import org.scaladebugger.api.profiles.scala210.methods.{Scala210MethodEntryProfile, Scala210MethodExitProfile}
import org.scaladebugger.api.profiles.scala210.monitors.{Scala210MonitorContendedEnterProfile, Scala210MonitorContendedEnteredProfile, Scala210MonitorWaitProfile, Scala210MonitorWaitedProfile}
import org.scaladebugger.api.profiles.scala210.steps.Scala210StepProfile
import org.scaladebugger.api.profiles.scala210.threads.{Scala210ThreadDeathProfile, Scala210ThreadStartProfile}
import org.scaladebugger.api.profiles.scala210.vm.{Scala210VMDeathProfile, Scala210VMDisconnectProfile, Scala210VMStartProfile}
import org.scaladebugger.api.profiles.scala210.watchpoints.{Scala210AccessWatchpointProfile, Scala210ModificationWatchpointProfile}
import org.scaladebugger.api.profiles.traits.ManagerContainerDebugProfile

/**
 * Contains information about the Scala 2.10 debug profile.
 */
object Scala210DebugProfile {
  val Name: String = "scala-2.10"
}

/**
 * Represents a debug profile that adds specific logic for Scala 2.10 code.
 *
 * @param _virtualMachine The underlying virtual machine to use for various
 *                        retrieval methods
 * @param managerContainer The container of low-level managers to use as the
 *                         underlying implementation
 */
class Scala210DebugProfile(
  protected val _virtualMachine: VirtualMachine,
  protected val managerContainer: ManagerContainer
) extends ManagerContainerDebugProfile
  with Scala210AccessWatchpointProfile
  with Scala210BreakpointProfile
  with Scala210ClassPrepareProfile
  with Scala210ClassUnloadProfile
  with Scala210EventProfile
  with Scala210ExceptionProfile
  with Scala210GrabInfoProfile
  with Scala210MethodEntryProfile
  with Scala210MethodExitProfile
  with Scala210MiscInfoProfile
  with Scala210ModificationWatchpointProfile
  with Scala210MonitorContendedEnteredProfile
  with Scala210MonitorContendedEnterProfile
  with Scala210MonitorWaitedProfile
  with Scala210MonitorWaitProfile
  with Scala210StepProfile
  with Scala210ThreadDeathProfile
  with Scala210ThreadStartProfile
  with Scala210VMStartProfile
  with Scala210VMDeathProfile
  with Scala210VMDisconnectProfile
