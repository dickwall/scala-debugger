package org.scaladebugger.api.profiles.swappable.exceptions
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.profiles.ProfileManager
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile
import org.scaladebugger.api.profiles.traits.DebugProfile

class SwappableExceptionProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockDebugProfile = mock[DebugProfile]
  private val mockProfileManager = mock[ProfileManager]

  private val swappableDebugProfile = new Object with SwappableDebugProfile {
    override protected val profileManager: ProfileManager = mockProfileManager
  }

  describe("SwappableExceptionProfile") {
    describe("#exceptionRequests") {
      it("should invoke the method on the underlying profile") {
        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.exceptionRequests _).expects().once()

        swappableDebugProfile.exceptionRequests
      }

      it("should throw an exception if there is no underlying profile") {
        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.exceptionRequests
        }
      }
    }
    describe("#onExceptionWithData") {
      it("should invoke the method on the underlying profile") {
        val exceptionName = "some exception"
        val notifyCaught = true
        val notifyUncaught = true
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.onExceptionWithData _).expects(
          exceptionName,
          notifyCaught,
          notifyUncaught,
          arguments
        ).once()

        swappableDebugProfile.onExceptionWithData(
          exceptionName,
          notifyCaught,
          notifyUncaught,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val exceptionName = "some exception"
        val notifyCaught = true
        val notifyUncaught = true
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.onExceptionWithData(
            exceptionName,
            notifyCaught,
            notifyUncaught,
            arguments: _*
          )
        }
      }
    }

    describe("#onAllExceptionsWithData") {
      it("should invoke the method on the underlying profile") {
        val notifyCaught = true
        val notifyUncaught = true
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.onAllExceptionsWithData _).expects(
          notifyCaught,
          notifyUncaught,
          arguments
        ).once()

        swappableDebugProfile.onAllExceptionsWithData(
          notifyCaught,
          notifyUncaught,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val notifyCaught = true
        val notifyUncaught = true
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.onAllExceptionsWithData(
            notifyCaught,
            notifyUncaught,
            arguments: _*
          )
        }
      }
    }
  }
}
