package org.scaladebugger.api.profiles.scala212

import com.sun.jdi.VirtualMachine
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.scala212.breakpoints.Scala212BreakpointProfile
import org.scaladebugger.api.profiles.scala212.classes.{Scala212ClassUnloadProfile, Scala212ClassPrepareProfile}
import org.scaladebugger.api.profiles.scala212.events.Scala212EventProfile
import org.scaladebugger.api.profiles.scala212.exceptions.Scala212ExceptionProfile
import org.scaladebugger.api.profiles.scala212.info.Scala212MiscInfoProfile
import org.scaladebugger.api.profiles.scala212.methods.{Scala212MethodExitProfile, Scala212MethodEntryProfile}
import org.scaladebugger.api.profiles.scala212.monitors.{Scala212MonitorWaitProfile, Scala212MonitorWaitedProfile, Scala212MonitorContendedEnterProfile, Scala212MonitorContendedEnteredProfile}
import org.scaladebugger.api.profiles.scala212.steps.Scala212StepProfile
import org.scaladebugger.api.profiles.scala212.threads.{Scala212ThreadStartProfile, Scala212ThreadDeathProfile}
import org.scaladebugger.api.profiles.scala212.vm.{Scala212VMDisconnectProfile, Scala212VMDeathProfile, Scala212VMStartProfile}
import org.scaladebugger.api.profiles.scala212.watchpoints.{Scala212ModificationWatchpointProfile, Scala212AccessWatchpointProfile}
import org.scaladebugger.api.profiles.traits.DebugProfile

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
  private val managerContainer: ManagerContainer
)
  extends DebugProfile
  with Scala212AccessWatchpointProfile
  with Scala212BreakpointProfile
  with Scala212ClassPrepareProfile
  with Scala212ClassUnloadProfile
  with Scala212EventProfile
  with Scala212ExceptionProfile
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
