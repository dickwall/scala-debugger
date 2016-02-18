package org.scaladebugger.api.profiles.scala210

import com.sun.jdi.VirtualMachine
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.scala210.breakpoints.Scala210BreakpointProfile
import org.scaladebugger.api.profiles.scala210.classes.{Scala210ClassUnloadProfile, Scala210ClassPrepareProfile}
import org.scaladebugger.api.profiles.scala210.events.Scala210EventProfile
import org.scaladebugger.api.profiles.scala210.exceptions.Scala210ExceptionProfile
import org.scaladebugger.api.profiles.scala210.info.Scala210MiscInfoProfile
import org.scaladebugger.api.profiles.scala210.methods.{Scala210MethodExitProfile, Scala210MethodEntryProfile}
import org.scaladebugger.api.profiles.scala210.monitors.{Scala210MonitorWaitProfile, Scala210MonitorWaitedProfile, Scala210MonitorContendedEnterProfile, Scala210MonitorContendedEnteredProfile}
import org.scaladebugger.api.profiles.scala210.steps.Scala210StepProfile
import org.scaladebugger.api.profiles.scala210.threads.{Scala210ThreadStartProfile, Scala210ThreadDeathProfile}
import org.scaladebugger.api.profiles.scala210.vm.{Scala210VMDisconnectProfile, Scala210VMDeathProfile, Scala210VMStartProfile}
import org.scaladebugger.api.profiles.scala210.watchpoints.{Scala210ModificationWatchpointProfile, Scala210AccessWatchpointProfile}
import org.scaladebugger.api.profiles.traits.DebugProfile

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
  private val managerContainer: ManagerContainer
)
  extends DebugProfile
  with Scala210AccessWatchpointProfile
  with Scala210BreakpointProfile
  with Scala210ClassPrepareProfile
  with Scala210ClassUnloadProfile
  with Scala210EventProfile
  with Scala210ExceptionProfile
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
