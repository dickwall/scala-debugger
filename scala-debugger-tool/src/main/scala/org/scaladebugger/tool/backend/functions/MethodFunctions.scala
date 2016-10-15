package org.scaladebugger.tool.backend.functions
import acyclic.file
import org.scaladebugger.api.lowlevel.events.misc.NoResume
import org.scaladebugger.tool.backend.StateManager

/**
 * Represents a collection of functions for managing methods.
 *
 * @param stateManager The manager whose state to share among functions
 * @param writeLine Used to write output to the terminal
 */
class MethodFunctions(
  private val stateManager: StateManager,
  private val writeLine: String => Unit
) {
  /** Entrypoint for creating a method entry break. */
  def createEntry(m: Map[String, Any]) = {
    val jvms = stateManager.state.scalaVirtualMachines

    if (jvms.isEmpty) writeLine("No VM connected!")

    val className = m.get("class").map(_.toString).getOrElse(
      throw new RuntimeException("Missing class argument!")
    )

    val methodName = m.get("method").map(_.toString).getOrElse(
      throw new RuntimeException("Missing method argument!")
    )

    jvms.foreach(s => {
      val m = s.getOrCreateMethodEntryRequest(className, methodName, NoResume)
      m.foreach(_ => writeLine(s"Method entry hit for $className.$methodName"))
    })
  }

  /** Entrypoint for creating a method exit break. */
  def createExit(m: Map[String, Any]) = {
    val jvms = stateManager.state.scalaVirtualMachines

    if (jvms.isEmpty) writeLine("No VM connected!")

    val className = m.get("class").map(_.toString).getOrElse(
      throw new RuntimeException("Missing class argument!")
    )

    val methodName = m.get("method").map(_.toString).getOrElse(
      throw new RuntimeException("Missing method argument!")
    )

    jvms.foreach(s => {
      val m = s.getOrCreateMethodExitRequest(className, methodName, NoResume)
      m.foreach(_ => writeLine(s"Method exit hit for $className.$methodName"))
    })
  }

  /** Entrypoint for listing all method entry requests. */
  def listEntries(m: Map[String, Any]) = {
    val jvms = stateManager.state.scalaVirtualMachines

    if (jvms.isEmpty) writeLine("No VM connected!")

    jvms.foreach(s => {
      writeLine(s"<= JVM ${s.uniqueId} =>")
      s.methodEntryRequests
        .map(m => s"${m.className}.${m.methodName}")
        .foreach(writeLine)
    })
  }

  /** Entrypoint for listing all method exit requests. */
  def listExits(m: Map[String, Any]) = {
    val jvms = stateManager.state.scalaVirtualMachines

    if (jvms.isEmpty) writeLine("No VM connected!")

    jvms.foreach(s => {
      writeLine(s"<= JVM ${s.uniqueId} =>")
      s.methodExitRequests
        .map(m => s"${m.className}.${m.methodName}")
        .foreach(writeLine)
    })
  }

  /** Entrypoint for clearing a method entry request. */
  def clearEntry(m: Map[String, Any]) = {
    val jvms = stateManager.state.scalaVirtualMachines

    if (jvms.isEmpty) writeLine("No VM connected!")

    val className = m.get("class").map(_.toString).getOrElse(
      throw new RuntimeException("Missing class argument!")
    )

    val methodName = m.get("method").map(_.toString).getOrElse(
      throw new RuntimeException("Missing method argument!")
    )

    jvms.foreach(_.removeMethodEntryRequests(className, methodName))
  }

  /** Entrypoint for clearing a method exit request. */
  def clearExit(m: Map[String, Any]) = {
    val jvms = stateManager.state.scalaVirtualMachines

    if (jvms.isEmpty) writeLine("No VM connected!")

    val className = m.get("class").map(_.toString).getOrElse(
      throw new RuntimeException("Missing class argument!")
    )

    val methodName = m.get("method").map(_.toString).getOrElse(
      throw new RuntimeException("Missing method argument!")
    )

    jvms.foreach(_.removeMethodExitRequests(className, methodName))
  }
}