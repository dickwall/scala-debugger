package org.scaladebugger.api.profiles.traits.info

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import test.InfoTestClasses.TestFrameInfoProfile

class FrameInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  describe("FrameInfoProfile") {
    describe("#withThisObject") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[ObjectInfoProfile]

        val frameInfoProfile = new TestFrameInfoProfile {
          override def withUnsafeThisObject: ObjectInfoProfile = mockUnsafeMethod()
        }

        val r = mock[ObjectInfoProfile]
        mockUnsafeMethod.expects().returning(r).once()
        frameInfoProfile.withThisObject.get should be (r)
      }
    }

    describe("#withCurrentThread") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[ThreadInfoProfile]

        val frameInfoProfile = new TestFrameInfoProfile {
          override def withUnsafeCurrentThread: ThreadInfoProfile =
            mockUnsafeMethod()
        }

        val r = mock[ThreadInfoProfile]
        mockUnsafeMethod.expects().returning(r).once()
        frameInfoProfile.withCurrentThread.get should be (r)
      }
    }

    describe("#forVariable") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[String, VariableInfoProfile]

        val frameInfoProfile = new TestFrameInfoProfile {
          override def forUnsafeVariable(name: String): VariableInfoProfile =
            mockUnsafeMethod(name)
        }

        val a1 = "someName"
        val r = mock[VariableInfoProfile]
        mockUnsafeMethod.expects(a1).returning(r).once()
        frameInfoProfile.forVariable(a1).get should be (r)
      }
    }

    describe("#forFieldVariables") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Seq[VariableInfoProfile]]

        val frameInfoProfile = new TestFrameInfoProfile {
          override def forUnsafeFieldVariables: Seq[VariableInfoProfile] =
            mockUnsafeMethod()
        }

        val r = Seq(mock[VariableInfoProfile])
        mockUnsafeMethod.expects().returning(r).once()
        frameInfoProfile.forFieldVariables.get should be (r)
      }
    }

    describe("#forLocalVariables") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Seq[VariableInfoProfile]]

        val frameInfoProfile = new TestFrameInfoProfile {
          override def forUnsafeLocalVariables: Seq[VariableInfoProfile] =
            mockUnsafeMethod()
        }

        val r = Seq(mock[VariableInfoProfile])
        mockUnsafeMethod.expects().returning(r).once()
        frameInfoProfile.forLocalVariables.get should be (r)
      }
    }

    describe("#forAllVariables") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Seq[VariableInfoProfile]]

        val frameInfoProfile = new TestFrameInfoProfile {
          override def forUnsafeAllVariables: Seq[VariableInfoProfile] =
            mockUnsafeMethod()
        }

        val r = Seq(mock[VariableInfoProfile])
        mockUnsafeMethod.expects().returning(r).once()
        frameInfoProfile.forAllVariables.get should be (r)
      }
    }

    describe("#forArguments") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Seq[VariableInfoProfile]]

        val frameInfoProfile = new TestFrameInfoProfile {
          override def forUnsafeArguments: Seq[VariableInfoProfile] =
            mockUnsafeMethod()
        }

        val r = Seq(mock[VariableInfoProfile])
        mockUnsafeMethod.expects().returning(r).once()
        frameInfoProfile.forArguments.get should be (r)
      }
    }

    describe("#forNonArguments") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Seq[VariableInfoProfile]]

        val frameInfoProfile = new TestFrameInfoProfile {
          override def forUnsafeNonArguments: Seq[VariableInfoProfile] =
            mockUnsafeMethod()
        }

        val r = Seq(mock[VariableInfoProfile])
        mockUnsafeMethod.expects().returning(r).once()
        frameInfoProfile.forNonArguments.get should be (r)
      }
    }
  }
}
