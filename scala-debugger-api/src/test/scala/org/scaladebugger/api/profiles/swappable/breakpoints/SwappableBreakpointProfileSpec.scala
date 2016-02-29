package org.scaladebugger.api.profiles.swappable.breakpoints
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{ParallelTestExecution, Matchers, FunSpec}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.profiles.ProfileManager
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile
import org.scaladebugger.api.profiles.traits.DebugProfile

class SwappableBreakpointProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockDebugProfile = mock[DebugProfile]
  private val mockProfileManager = mock[ProfileManager]

  private val swappableDebugProfile = new Object with SwappableDebugProfile {
    override protected val profileManager: ProfileManager = mockProfileManager
  }

  describe("SwappableBreakpointProfile") {
    describe("#breakpointRequests") {
      it("should invoke the method on the underlying profile") {
        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.breakpointRequests _).expects().once()

        swappableDebugProfile.breakpointRequests
      }

      it("should throw an exception if there is no underlying profile") {
        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.breakpointRequests
        }
      }
    }

    describe("#onBreakpointWithData") {
      it("should invoke the method on the underlying profile") {
        val fileName = "some file"
        val lineNumber = 999
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.onBreakpointWithData _).expects(
          fileName,
          lineNumber,
          arguments
        ).once()

        swappableDebugProfile.onBreakpointWithData(
          fileName,
          lineNumber,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val fileName = "some file"
        val lineNumber = 999
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.onBreakpointWithData(
            fileName,
            lineNumber,
            arguments: _*
          )
        }
      }
    }
  }
}
