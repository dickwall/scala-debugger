package org.scaladebugger.api.profiles.pure.info

import com.sun.jdi.ThreadReference
import org.scaladebugger.api.lowlevel.events.misc.NoResume
import org.scaladebugger.api.profiles.pure.PureDebugProfile
import org.scaladebugger.api.utils.JDITools
import org.scaladebugger.api.virtualmachines.DummyScalaVirtualMachine
import org.scalatest.concurrent.Eventually
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import test.{TestUtilities, VirtualMachineFixtures}

class PureMethodInfoProfileIntegrationSpec extends FunSpec with Matchers
  with ParallelTestExecution with VirtualMachineFixtures
  with TestUtilities with Eventually
{
  implicit override val patienceConfig = PatienceConfig(
    timeout = scaled(test.Constants.EventuallyTimeout),
    interval = scaled(test.Constants.EventuallyInterval)
  )

  describe("PureMethodInfoProfile") {
    it("should be able to get the name of the method") {
      val testClass = "org.scaladebugger.test.info.Methods"
      val testFile = JDITools.scalaClassStringToFileString(testClass)

      @volatile var t: Option[ThreadReference] = None
      val s = DummyScalaVirtualMachine.newInstance()

      // NOTE: Do not resume so we can check the variables at the stack frame
      s.withProfile(PureDebugProfile.Name)
        .onUnsafeBreakpoint(testFile, 22, NoResume)
        .foreach(e => t = Some(e.thread()))

      withVirtualMachine(testClass, pendingScalaVirtualMachines = Seq(s)) { (s) =>
        logTimeTaken(eventually {
          val method = s.withProfile(PureDebugProfile.Name)
            .forUnsafeThread(t.get).withUnsafeTopFrame.withUnsafeThisObject
            .unsafeMethod("publicMethod")

          method.name should be ("publicMethod")
        })
      }
    }

    it("should be able to get the parameter types of the method") {
      val testClass = "org.scaladebugger.test.info.Methods"
      val testFile = JDITools.scalaClassStringToFileString(testClass)

      @volatile var t: Option[ThreadReference] = None
      val s = DummyScalaVirtualMachine.newInstance()

      // NOTE: Do not resume so we can check the variables at the stack frame
      s.withProfile(PureDebugProfile.Name)
        .onUnsafeBreakpoint(testFile, 22, NoResume)
        .foreach(e => t = Some(e.thread()))

      withVirtualMachine(testClass, pendingScalaVirtualMachines = Seq(s)) { (s) =>
        logTimeTaken(eventually {
          val method = s.withProfile(PureDebugProfile.Name)
            .forUnsafeThread(t.get).withUnsafeTopFrame.withUnsafeThisObject
            .unsafeMethod("publicMethod")

          method.unsafeParameterTypeNames should be (Seq("int", "java.lang.String"))
        })
      }
    }

    it("should be able to get the return value of the method") {
      val testClass = "org.scaladebugger.test.info.Methods"
      val testFile = JDITools.scalaClassStringToFileString(testClass)

      @volatile var t: Option[ThreadReference] = None
      val s = DummyScalaVirtualMachine.newInstance()

      // NOTE: Do not resume so we can check the variables at the stack frame
      s.withProfile(PureDebugProfile.Name)
        .onUnsafeBreakpoint(testFile, 22, NoResume)
        .foreach(e => t = Some(e.thread()))

      withVirtualMachine(testClass, pendingScalaVirtualMachines = Seq(s)) { (s) =>
        logTimeTaken(eventually {
          val method = s.withProfile(PureDebugProfile.Name)
            .forUnsafeThread(t.get).withUnsafeTopFrame.withUnsafeThisObject
            .unsafeMethod("publicMethod")

          method.unsafeReturnTypeName should be ("java.lang.String")
        })
      }
    }
  }
}
