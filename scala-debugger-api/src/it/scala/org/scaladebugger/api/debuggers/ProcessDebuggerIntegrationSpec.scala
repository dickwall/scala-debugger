package org.scaladebugger.api.debuggers
import acyclic.file

import java.net.ServerSocket
import java.util.concurrent.atomic.AtomicBoolean

import org.scaladebugger.api.utils.JDITools
import org.scalatest.concurrent.Eventually
import org.scalatest.time.{Milliseconds, Seconds, Span}
import org.scalatest.{BeforeAndAfter, FunSpec, Matchers}
import test.{TestUtilities, VirtualMachineFixtures}

class ProcessDebuggerIntegrationSpec extends FunSpec with Matchers
  with BeforeAndAfter with VirtualMachineFixtures
  with TestUtilities with Eventually
{
  implicit override val patienceConfig = PatienceConfig(
    timeout = scaled(Span(5, Seconds)),
    interval = scaled(Span(5, Milliseconds))
  )

  @volatile private var jvmProcess: (Int, Process) = _

  before {
    jvmProcess = createProcess()
  }

  after {
    destroyProcess(jvmProcess._2)
  }

  describe("ProcessDebugger") {
    it("should be able to attach to a running JVM process") {
      val processDebugger = ProcessDebugger(jvmProcess._1)

      val attachedToVirtualMachine = new AtomicBoolean(false)

      // Need to keep retrying until process is ready to be attached to
      // NOTE: If unable to connect, ensure that hostname is "localhost"
      eventually {
        processDebugger.start(_ => attachedToVirtualMachine.set(true))
      }

      // Keep checking back until we have successfully attached
      eventually {
        attachedToVirtualMachine.get() should be (true)
      }
    }
  }

  private def createProcess(): (Int, Process) = {
    val port = {
      val socket = new ServerSocket(0)
      val _port = socket.getLocalPort
      socket.close()
      _port
    }

    val quote = '"'
    val uniqueId = java.util.UUID.randomUUID().toString
    val process = JDITools.spawn(
      className = "org.scaladebugger.test.misc.AttachingMain",
      server = true,
      suspend = false,
      port = port,
      options = Seq(s"-Dscala.debugger.id=$quote$uniqueId$quote")
    )

    val pid = JDITools.javaProcesses()
      .find(_.jvmOptions.properties.get("scala.debugger.id").exists(_ == uniqueId))
      .map(_.pid.toInt).getOrElse(0)

    (pid, process)
  }

  private def destroyProcess(process: Process): Unit = process.destroy()
}
