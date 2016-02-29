package org.scaladebugger.api.profiles.swappable.steps
import acyclic.file

import com.sun.jdi.ThreadReference
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.profiles.ProfileManager
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile
import org.scaladebugger.api.profiles.traits.DebugProfile

class SwappableStepProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockThreadReference = mock[ThreadReference]
  private val mockDebugProfile = mock[DebugProfile]
  private val mockProfileManager = mock[ProfileManager]

  private val swappableDebugProfile = new Object with SwappableDebugProfile {
    override protected val profileManager: ProfileManager = mockProfileManager
  }

  describe("SwappableStepProfile") {
    describe("#stepRequests") {
      it("should invoke the method on the underlying profile") {
        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.stepRequests _).expects().once()

        swappableDebugProfile.stepRequests
      }

      it("should throw an exception if there is no underlying profile") {
        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.stepRequests
        }
      }
    }

    describe("#stepIntoLineWithData") {
      it("should invoke the method on the underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.stepIntoLineWithData _)
          .expects(mockThreadReference, arguments).once()

        swappableDebugProfile.stepIntoLineWithData(
          mockThreadReference,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.stepIntoLineWithData(
            mockThreadReference,
            arguments: _*
          )
        }
      }
    }

    describe("#stepOverLineWithData") {
      it("should invoke the method on the underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.stepOverLineWithData _)
          .expects(mockThreadReference, arguments).once()


        swappableDebugProfile.stepOverLineWithData(
          mockThreadReference,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.stepOverLineWithData(
            mockThreadReference,
            arguments: _*
          )
        }
      }
    }

    describe("#stepOutLineWithData") {
      it("should invoke the method on the underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.stepOutLineWithData _)
          .expects(mockThreadReference, arguments).once()


        swappableDebugProfile.stepOutLineWithData(
          mockThreadReference,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.stepOutLineWithData(
            mockThreadReference,
            arguments: _*
          )
        }
      }
    }

    describe("#stepIntoMinWithData") {
      it("should invoke the method on the underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.stepIntoMinWithData _)
          .expects(mockThreadReference, arguments).once()


        swappableDebugProfile.stepIntoMinWithData(
          mockThreadReference,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.stepIntoMinWithData(
            mockThreadReference,
            arguments: _*
          )
        }
      }
    }

    describe("#stepOverMinWithData") {
      it("should invoke the method on the underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.stepOverMinWithData _)
          .expects(mockThreadReference, arguments).once()


        swappableDebugProfile.stepOverMinWithData(
          mockThreadReference,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.stepOverMinWithData(
            mockThreadReference,
            arguments: _*
          )
        }
      }
    }

    describe("#stepOutMinWithData") {
      it("should invoke the method on the underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.stepOutMinWithData _)
          .expects(mockThreadReference, arguments).once()


        swappableDebugProfile.stepOutMinWithData(
          mockThreadReference,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.stepOutMinWithData(
            mockThreadReference,
            arguments: _*
          )
        }
      }
    }

    describe("#onStepWithData") {
      it("should invoke the method on the underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.onStepWithData _)
          .expects(mockThreadReference, arguments).once()


        swappableDebugProfile.onStepWithData(
          mockThreadReference,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.onStepWithData(
            mockThreadReference,
            arguments: _*
          )
        }
      }
    }
  }
}
