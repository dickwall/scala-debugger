package org.scaladebugger.api.profiles.traits.info

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import test.InfoTestClasses.{TestVariableInfoProfile, TestValueInfoProfile}

import scala.util.Failure

class VariableInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  describe("VariableInfoProfile") {
    describe("#toValue") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[ValueInfoProfile]

        val variableInfoProfile = new TestVariableInfoProfile {
          override def toUnsafeValue: ValueInfoProfile = mockUnsafeMethod()
        }

        val r = mock[ValueInfoProfile]
        mockUnsafeMethod.expects().returning(r).once()
        variableInfoProfile.toValue.get should be (r)
      }
    }
  }
}
