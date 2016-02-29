package org.scaladebugger.api.profiles.pure.info
import acyclic.file

import java.util.concurrent.atomic.{AtomicBoolean, AtomicInteger}

import org.scalatest.concurrent.Eventually
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.profiles.pure.PureDebugProfile
import test.{TestUtilities, VirtualMachineFixtures}

class PureMiscInfoProfileIntegrationSpec extends FunSpec with Matchers
  with ParallelTestExecution with VirtualMachineFixtures
  with TestUtilities with Eventually
{
  implicit override val patienceConfig = PatienceConfig(
    timeout = scaled(test.Constants.EventuallyTimeout),
    interval = scaled(test.Constants.EventuallyInterval)
  )

  describe("PureMiscInfoProfile") {
    it("should return the class name of a Scala main method entrypoint") {
      val testClass = "org.scaladebugger.test.misc.MainUsingMethod"

      withVirtualMachine(testClass) { (s) =>
        val expected = testClass

        // NOTE: This is not available until AFTER we have resumed from the
        //       start event (as the main method is not yet loaded)
        eventually {
          val actual = s.withProfile(PureDebugProfile.Name).mainClassName
          actual should be(expected)
        }
      }
    }

    it("should return the class name of a Scala App entrypoint") {
      val testClass = "org.scaladebugger.test.misc.MainUsingApp"

      withVirtualMachine(testClass) { (s) =>
        val expected = testClass

        // NOTE: This is not available until AFTER we have resumed from the
        //       start event (as the main method is not yet loaded)
        eventually {
          val actual = s.withProfile(PureDebugProfile.Name).mainClassName
          actual should be(expected)
        }
      }
    }

    it("should return the arguments provided to the virtual machine") {
      val testClass = "org.scaladebugger.test.misc.MainUsingApp"
      val testArguments = Seq("a", "b", "c")

      withVirtualMachine(testClass, testArguments) { (s) =>
        val expected = testArguments

        // NOTE: This is not available until AFTER we have resumed from the
        //       start event (as the main method is not yet loaded)
        eventually {
          val actual = s.withProfile(PureDebugProfile.Name).commandLineArguments
          actual should contain theSameElementsInOrderAs expected
        }
      }
    }
  }
}
