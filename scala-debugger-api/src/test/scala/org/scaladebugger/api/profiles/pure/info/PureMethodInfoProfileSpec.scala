package org.scaladebugger.api.profiles.pure.info

import com.sun.jdi.Method
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class PureMethodInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockMethod = mock[Method]
  private val pureMethodInfoProfile = new PureMethodInfoProfile(mockMethod)

  describe("PureMethodInfoProfile") {
    describe("#name") {
      it("should return the name of the method") {
        val expected = "someName"

        (mockMethod.name _).expects().returning(expected).once()

        val actual = pureMethodInfoProfile.name

        actual should be (expected)
      }
    }

    describe("#unsafeReturnTypeName") {
      it("should return the name of the method's return type") {
        val expected = "some.return.type"

        (mockMethod.returnTypeName _).expects().returning(expected).once()

        val actual = pureMethodInfoProfile.unsafeReturnTypeName

        actual should be (expected)
      }
    }

    describe("#unsafeParameterTypeNames") {
      it("should return an ordered collection of parameter names for the method") {
        val expected = Seq("some.parameter.type", "some.other.parameter.type")

        import scala.collection.JavaConverters._
        (mockMethod.argumentTypeNames _).expects()
          .returning(expected.asJava).once()

        val actual = pureMethodInfoProfile.unsafeParameterTypeNames

        actual should be (expected)
      }
    }
  }
}
