package org.scaladebugger.api.profiles.swappable.info
import acyclic.file

import com.sun.jdi.ThreadReference
import org.scaladebugger.api.profiles.ProfileManager
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfile
import org.scaladebugger.api.profiles.traits.DebugProfile
import org.scaladebugger.api.profiles.traits.info.{ThreadInfoProfile, ValueInfoProfile}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class SwappableGrabInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockDebugProfile = mock[DebugProfile]
  private val mockProfileManager = mock[ProfileManager]

  private val swappableDebugProfile = new Object with SwappableDebugProfile {
    override protected val profileManager: ProfileManager = mockProfileManager
  }

  describe("SwappableGrabInfoProfile") {
    describe("#forUnsafeThread") {
      it("should invoke the method on the underlying profile") {
        val expected = mock[ThreadInfoProfile]

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).twice()

        (mockDebugProfile.forUnsafeThread(_: Long)).expects(*)
          .returning(expected).once()

        (mockDebugProfile.forUnsafeThread(_: ThreadReference)).expects(*)
          .returning(expected).once()

        swappableDebugProfile.forUnsafeThread(0L) should be(expected)
        swappableDebugProfile.forUnsafeThread(mock[ThreadReference]) should be(expected)
      }

      it("should throw an exception of there is no underlying profile") {
        intercept[AssertionError] {
          (mockProfileManager.retrieve _).expects(*).returning(None).once()
          swappableDebugProfile.forUnsafeThread(0L)
        }

        intercept[AssertionError] {
          (mockProfileManager.retrieve _).expects(*).returning(None).once()
          swappableDebugProfile.forUnsafeThread(mock[ThreadReference])
        }
      }
    }
  }
}
