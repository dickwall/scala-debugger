package org.scaladebugger.api.profiles.swappable.watchpoints

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.profiles.ProfileManager
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile
import org.scaladebugger.api.profiles.traits.DebugProfile

class SwappableModificationWatchpointProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockDebugProfile = mock[DebugProfile]
  private val mockProfileManager = mock[ProfileManager]

  private val swappableDebugProfile = new Object with SwappableDebugProfile {
    override protected val profileManager: ProfileManager = mockProfileManager
  }

  describe("SwappableModificationWatchpointProfile") {
    describe("#modificationWatchpointRequests") {
      it("should invoke the method on the underlying profile") {
        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        (mockDebugProfile.modificationWatchpointRequests _).expects().once()

        swappableDebugProfile.modificationWatchpointRequests
      }

      it("should throw an exception if there is no underlying profile") {
        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.modificationWatchpointRequests
        }
      }
    }

    describe("#onModificationWatchpointWithData") {
      it("should invoke the method on the underlying profile") {
        val className = "some class"
        val fieldName = "some field"
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        // NOTE: Forced to use onCall with product due to issues with ScalaMock
        //       casting and inability to work with varargs directly
        (mockDebugProfile.onModificationWatchpointWithData(
          _: String,
          _: String,
          _: JDIArgument)
        ).expects(className, fieldName, *).onCall(t => {
          val args = t.productElement(2).asInstanceOf[Seq[JDIArgument]]
          args should be (arguments)
          null
        })

        swappableDebugProfile.onModificationWatchpointWithData(
          className,
          fieldName,
          arguments: _*
        )
      }

      it("should throw an exception if there is no underlying profile") {
        val className = "some class"
        val fieldName = "some field"
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.onModificationWatchpointWithData(
            className,
            fieldName,
            arguments: _*
          )
        }
      }
    }
  }
}
