package org.scaladebugger.api.profiles.pure.info
import acyclic.file

import com.sun.jdi.{Location, VirtualMachine}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.classes.ClassManager
import test.JDIMockHelpers

class PureMiscInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory with JDIMockHelpers
{
  private val mockVirtualMachine = mock[VirtualMachine]
  private val mockClassManager = mock[ClassManager]

  private val mockRetrieveCommandLineArguments = mockFunction[Seq[String]]
  private val mockRetrieveMainClassName = mockFunction[String]

  private val pureMiscInfoProfile = new Object with PureMiscInfoProfile {
    override protected def retrieveCommandLineArguments(): Seq[String] =
      mockRetrieveCommandLineArguments()
    override protected def retrieveMainClassName(): String =
      mockRetrieveMainClassName()

    override protected val classManager: ClassManager = mockClassManager
    override protected val _virtualMachine: VirtualMachine = mockVirtualMachine
  }

  describe("PureMiscInfoProfile") {
    describe("#availableLinesForFile") {
      it("should return the lines (sorted) that can have breakpoints") {
        val expected = Seq(1, 8, 999)

        // Setup the return from class manager to be reverse order
        val linesAndLocations = expected.reverseMap(i =>
          (i, Seq(stub[Location]))
        ).toMap
        (mockClassManager.linesAndLocationsForFile _).expects(*)
          .returning(Some(linesAndLocations))

        val actual = pureMiscInfoProfile.availableLinesForFile("").get

        actual should contain theSameElementsInOrderAs expected
      }

      it("should return None if the file does not exist") {
        val expected = None

        // Set the return from class manager to be "not found"
        (mockClassManager.linesAndLocationsForFile _).expects(*).returning(None)

        val actual = pureMiscInfoProfile.availableLinesForFile("")

        actual should be (expected)
      }
    }

    describe("#mainClassName") {
      it("should invoke the JDI Helper method equivalent") {
        val expected = "some main class name"

        mockRetrieveMainClassName.expects().returning(expected).once()
        val actual = pureMiscInfoProfile.mainClassName

        actual should be (expected)
      }
    }

    describe("#commandLineArguments") {
      it("should invoke the JDI Helper method equivalent") {
        val expected = Seq("some", "arguments")

        mockRetrieveCommandLineArguments.expects().returning(expected).once()
        val actual = pureMiscInfoProfile.commandLineArguments

        actual should be (expected)
      }
    }
  }
}
