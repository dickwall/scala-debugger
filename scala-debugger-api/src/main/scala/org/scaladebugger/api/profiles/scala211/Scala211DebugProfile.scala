package org.scaladebugger.api.profiles.scala211

import com.sun.jdi.VirtualMachine
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.scala211.breakpoints.Scala211BreakpointProfile
import org.scaladebugger.api.profiles.scala211.classes.{Scala211ClassUnloadProfile, Scala211ClassPrepareProfile}
import org.scaladebugger.api.profiles.scala211.events.Scala211EventProfile
import org.scaladebugger.api.profiles.scala211.exceptions.Scala211ExceptionProfile
import org.scaladebugger.api.profiles.scala211.info.Scala211MiscInfoProfile
import org.scaladebugger.api.profiles.scala211.methods.{Scala211MethodExitProfile, Scala211MethodEntryProfile}
import org.scaladebugger.api.profiles.scala211.monitors.{Scala211MonitorWaitProfile, Scala211MonitorWaitedProfile, Scala211MonitorContendedEnterProfile, Scala211MonitorContendedEnteredProfile}
import org.scaladebugger.api.profiles.scala211.steps.Scala211StepProfile
import org.scaladebugger.api.profiles.scala211.threads.{Scala211ThreadStartProfile, Scala211ThreadDeathProfile}
import org.scaladebugger.api.profiles.scala211.vm.{Scala211VMDisconnectProfile, Scala211VMDeathProfile, Scala211VMStartProfile}
import org.scaladebugger.api.profiles.scala211.watchpoints.{Scala211ModificationWatchpointProfile, Scala211AccessWatchpointProfile}
import org.scaladebugger.api.profiles.traits.DebugProfile

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
  private val managerContainer: ManagerContainer
)
  extends DebugProfile
  with Scala211AccessWatchpointProfile
  with Scala211BreakpointProfile
  with Scala211ClassPrepareProfile
  with Scala211ClassUnloadProfile
  with Scala211EventProfile
  with Scala211ExceptionProfile
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
