package org.scaladebugger.api.virtualmachines
import acyclic.file

import com.sun.jdi._
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.ProfileManager
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile

/**
 * Represents a virtual machine running Scala code.
 */
trait ScalaVirtualMachine extends SwappableDebugProfile with ProfileManager {
  /**
   * Initializes the ScalaVirtualMachine system.
   *
   * @param startProcessingEvents If true, immediately starts processing events
   */
  def initialize(startProcessingEvents: Boolean = true): Unit

  /**
   * Indicates whether or not the virtual machine has started (received the
   * start event).
   *
   * @return True if started, otherwise false
   */
  def isStarted: Boolean

  /**
   * Represents the collection of low-level APIs for the virtual machine.
   *
   * @return The container of low-level managers
   */
  val lowlevel: ManagerContainer

  /**
   * A unique id assigned to the Scala virtual machine on the client (library)
   * side to help distinguish multiple VMs.
   *
   * @return The unique id as a string
   */
  val uniqueId: String

  /**
   * Represents the underlying virtual machine represented by this Scala
   * virtual machine.
   *
   * @return The JDI VirtualMachine instance
   */
  val underlyingVirtualMachine: VirtualMachine

  /**
   * Processes any pending requests contained by the provided Scala virtual
   * machine by applying them using this Scala virtual machine.
   *
   * @note This will not remove the pending requests from the managers
   *       contained in the provided Scala virtual machine!
   *
   * @param scalaVirtualMachine The virtual machine whose pending requests to
   *                            process using this virtual machine
   */
  def processPendingRequests(scalaVirtualMachine: ScalaVirtualMachine): Unit = {
    this.lowlevel.processPendingRequests(scalaVirtualMachine.lowlevel)
  }
}
