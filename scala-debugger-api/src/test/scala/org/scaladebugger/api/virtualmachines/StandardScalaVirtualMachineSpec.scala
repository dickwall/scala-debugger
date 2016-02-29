package org.scaladebugger.api.virtualmachines
import acyclic.file

import com.sun.jdi.VirtualMachine
import org.scaladebugger.api.lowlevel.ManagerContainer
import org.scaladebugger.api.profiles.ProfileManager
import org.scaladebugger.api.profiles.traits.DebugProfile
import org.scaladebugger.api.utils.LoopingTaskRunner
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class StandardScalaVirtualMachineSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockVirtualMachine = mock[VirtualMachine]
  private val mockProfileManager = mock[ProfileManager]
  private val mockLoopingTaskRunner = mock[LoopingTaskRunner]
  private val scalaVirtualMachine = new StandardScalaVirtualMachine(
    mockVirtualMachine,
    mockProfileManager,
    mockLoopingTaskRunner
  )

  describe("ScalaVirtualMachine") {
    describe("#register") {
      it("should invoke the underlying profile manager") {
        val testName = "some name"
        val testProfile = mock[DebugProfile]
        val expected = Some(testProfile)

        (mockProfileManager.register _).expects(testName, testProfile)
          .returning(expected).once()

        val actual = scalaVirtualMachine.register(testName, testProfile)
        actual should be (expected)
      }
    }

    describe("#unregister") {
      it("should invoke the underlying profile manager") {
        val testName = "some name"
        val testProfile = mock[DebugProfile]
        val expected = Some(testProfile)

        (mockProfileManager.unregister _).expects(testName)
          .returning(expected).once()

        val actual = scalaVirtualMachine.unregister(testName)
        actual should be (expected)
      }
    }

    describe("#retrieve") {
      it("should invoke the underlying profile manager") {
        val testName = "some name"
        val testProfile = mock[DebugProfile]
        val expected = Some(testProfile)

        (mockProfileManager.retrieve _).expects(testName)
          .returning(expected).once()

        val actual = scalaVirtualMachine.retrieve(testName)
        actual should be (expected)
      }
    }
  }
}
