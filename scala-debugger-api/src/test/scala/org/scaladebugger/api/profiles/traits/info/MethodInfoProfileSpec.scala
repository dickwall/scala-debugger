package org.scaladebugger.api.profiles.traits.info

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import test.InfoTestClasses.TestMethodInfoProfile

class MethodInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  describe("MethodInfoProfile") {
    describe("#returnTypeName") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[String]

        val methodInfoProfile = new TestMethodInfoProfile {
          override def unsafeReturnTypeName: String = mockUnsafeMethod()
        }

        val r = "some.return.type"
        mockUnsafeMethod.expects().returning(r).once()
        methodInfoProfile.returnTypeName.get should be (r)
      }
    }

    describe("#parameterTypeNames") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Seq[String]]

        val methodInfoProfile = new TestMethodInfoProfile {
          override def unsafeParameterTypeNames: Seq[String] = mockUnsafeMethod()
        }

        val r = Seq("some.param.type")
        mockUnsafeMethod.expects().returning(r).once()
        methodInfoProfile.parameterTypeNames.get should be (r)
      }
    }
  }
}
