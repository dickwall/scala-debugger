package org.senkbeil.debugger.api.profiles.swappable.vm

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, OneInstancePerTest}
import org.senkbeil.debugger.api.lowlevel.JDIArgument
import org.senkbeil.debugger.api.profiles.ProfileManager
import org.senkbeil.debugger.api.profiles.swappable.SwappableDebugProfile
import org.senkbeil.debugger.api.profiles.traits.DebugProfile

class SwappableVMStartProfileSpec extends FunSpec with Matchers
  with OneInstancePerTest with MockFactory
{
  private val mockDebugProfile = mock[DebugProfile]
  private val mockProfileManager = mock[ProfileManager]

  private val swappableDebugProfile = new Object with SwappableDebugProfile {
    override protected val profileManager: ProfileManager = mockProfileManager
  }

  describe("SwappableVMStartProfile") {
    describe("#onVMStartWithData") {
      it("should invoke the method on the underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*)
          .returning(Some(mockDebugProfile)).once()

        // NOTE: Forced to use onCall with product due to issues with ScalaMock
        //       casting and inability to work with varargs directly
        (mockDebugProfile.onVMStartWithData _).expects(*)
          .onCall((t: Product) => {
            val args = t.productElement(0).asInstanceOf[Seq[JDIArgument]]
            args should be(arguments)
            null
          })

        swappableDebugProfile.onVMStartWithData(arguments: _*)
      }

      it("should throw an exception if there is no underlying profile") {
        val arguments = Seq(mock[JDIArgument])

        (mockProfileManager.retrieve _).expects(*).returning(None).once()

        intercept[AssertionError] {
          swappableDebugProfile.onVMStartWithData(arguments: _*)
        }
      }
    }
  }
}
