package org.scaladebugger.api.profiles.traits.info

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import test.InfoTestClasses.TestValueInfoProfile

import scala.util.Failure

class ValueInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  describe("ValueInfoProfile") {
    describe("#asObject") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[ObjectInfoProfile]

        val valueInfoProfile = new TestValueInfoProfile {
          override def asUnsafeObject: ObjectInfoProfile = mockUnsafeMethod()
        }

        val r = mock[ObjectInfoProfile]
        mockUnsafeMethod.expects().returning(r).once()
        valueInfoProfile.asObject.get should be (r)
      }
    }

    describe("#asLocalValue") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Any]

        val valueInfoProfile = new TestValueInfoProfile {
          override def asUnsafeLocalValue: Any = mockUnsafeMethod()
        }

        val r = 3
        mockUnsafeMethod.expects().returning(r).once()
        valueInfoProfile.asLocalValue.get should be (r)
      }
    }

    describe("#asArray") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[ArrayInfoProfile]

        val valueInfoProfile = new TestValueInfoProfile {
          override def asUnsafeArray: ArrayInfoProfile = mockUnsafeMethod()
        }

        val r = mock[ArrayInfoProfile]
        mockUnsafeMethod.expects().returning(r).once()
        valueInfoProfile.asArray.get should be (r)
      }
    }
  }
}
