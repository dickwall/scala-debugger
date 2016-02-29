package org.scaladebugger.api.profiles.swappable.info
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.profiles.ProfileManager
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile
import org.scaladebugger.api.profiles.traits.DebugProfile

class SwappableMiscInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockDebugProfile = mock[DebugProfile]
  private val mockProfileManager = mock[ProfileManager]

  private val swappableDebugProfile = new Object with SwappableDebugProfile {
    override protected val profileManager: ProfileManager = mockProfileManager
  }

  describe("SwappableMiscInfoProfile") {
    describe("#availableLinesForFile") {
      it("should invoke the method on the underlying profile") {
        val expected = Some(Seq(1, 2, 3))
        val fileName = "some file"

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.availableLinesForFile _).expects(fileName)
          .returning(expected).once()

        val actual = swappableDebugProfile.availableLinesForFile(fileName)

        actual should be (expected)
      }

      it("should throw an exception if there is no underlying profile") {
        val fileName = "some file"

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.availableLinesForFile(fileName)
        }
      }
    }

    describe("#mainClassName") {
      it("should invoke the method on the underlying profile") {
        val expected = "some class name"

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.mainClassName _).expects().returning(expected).once()

        val actual = swappableDebugProfile.mainClassName

        actual should be (expected)
      }

      it("should throw an exception if there is no underlying profile") {
        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.mainClassName
        }
      }
    }

    describe("#commandLineArguments") {
      it("should invoke the method on the underlying profile") {
        val expected = Seq("some", "arguments")

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.commandLineArguments _).expects()
          .returning(expected).once()

        val actual = swappableDebugProfile.commandLineArguments

        actual should be (expected)
      }

      it("should throw an exception if there is no underlying profile") {
        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.mainClassName
        }
      }
    }
  }
}
