package org.scaladebugger.api.profiles.pure.info
import acyclic.file

import com.sun.jdi.{ThreadReference, Value}
import com.sun.jdi.event.BreakpointEvent
import org.scaladebugger.api.lowlevel.events.EventType
import org.scaladebugger.api.lowlevel.events.EventType.BreakpointEventType
import org.scaladebugger.api.lowlevel.events.misc.NoResume
import org.scaladebugger.api.profiles.pure.PureDebugProfile
import org.scaladebugger.api.utils.JDITools
import org.scaladebugger.api.virtualmachines.DummyScalaVirtualMachine
import org.scalatest.concurrent.Eventually
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import test.{TestUtilities, VirtualMachineFixtures}

import scala.util.Try

class PureGrabInfoProfileIntegrationSpec extends FunSpec with Matchers
  with ParallelTestExecution with VirtualMachineFixtures
  with TestUtilities with Eventually
{
  implicit override val patienceConfig = PatienceConfig(
    timeout = scaled(test.Constants.EventuallyTimeout),
    interval = scaled(test.Constants.EventuallyInterval)
  )

  describe("PureGrabInfoProfile") {
    it("should be able to find a thread by its unique id") {
      val testClass = "org.scaladebugger.test.misc.LaunchingMain"
      val testFile = JDITools.scalaClassStringToFileString(testClass)

      @volatile var t: Option[ThreadReference] = None
      val s = DummyScalaVirtualMachine.newInstance()
      s.withProfile(PureDebugProfile.Name)
        .onUnsafeBreakpoint(testFile, 7).foreach(e => t = Some(e.thread()))

      withVirtualMachine(testClass, pendingScalaVirtualMachines = Seq(s)) { (s) =>
        logTimeTaken(eventually {
          val id = t.get.uniqueID()

          s.withProfile(PureDebugProfile.Name)
            .forUnsafeThread(id).uniqueId should be (id)

          s.withProfile(PureDebugProfile.Name)
            .forUnsafeThread(t.get).uniqueId should be (id)
        })
      }
    }
  }
}
