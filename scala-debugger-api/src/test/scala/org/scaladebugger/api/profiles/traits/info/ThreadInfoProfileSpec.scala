package org.scaladebugger.api.profiles.traits.info

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import test.InfoTestClasses
import test.InfoTestClasses.TestThreadInfoProfile

import scala.util.{Success, Failure, Try}

class ThreadInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  describe("ThreadInfoProfile") {
    describe("#frames") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Seq[FrameInfoProfile]]

        val threadInfoProfile = new TestThreadInfoProfile {
          override def unsafeFrames: Seq[FrameInfoProfile] = mockUnsafeMethod()
        }

        val r = Seq(mock[FrameInfoProfile])
        mockUnsafeMethod.expects().returning(r).once()
        threadInfoProfile.frames.get should be (r)
      }
    }

    describe("#withFrame") {
      it("should wrap the unsafe call in a Try") {
        val mockUnsafeMethod = mockFunction[Int, FrameInfoProfile]

        val threadInfoProfile = new TestThreadInfoProfile {
          override def withUnsafeFrame(index: Int): FrameInfoProfile =
            mockUnsafeMethod(index)
        }

        val a1 = 999
        val r = mock[FrameInfoProfile]

        mockUnsafeMethod.expects(a1).returning(r).once()
        threadInfoProfile.withFrame(a1).get should be (r)
      }
    }

    describe("#withTopFrame") {
      it("should invoke withFrame(0) underneath") {
        val mockUnsafeMethod = mockFunction[Int, Try[FrameInfoProfile]]

        val threadInfoProfile = new TestThreadInfoProfile {
          override def withFrame(index: Int): Try[FrameInfoProfile] =
            mockUnsafeMethod(index)
        }

        val r = Success(mock[FrameInfoProfile])

        mockUnsafeMethod.expects(0).returning(r).once()
        threadInfoProfile.withTopFrame should be (r)
      }
    }

    describe("#withUnsafeTopFrame") {
      it("should invoke withUnsafeFrame(0) underneath") {
        val mockUnsafeMethod = mockFunction[Int, FrameInfoProfile]

        val threadInfoProfile = new TestThreadInfoProfile {
          override def withUnsafeFrame(index: Int): FrameInfoProfile =
            mockUnsafeMethod(index)
        }

        val r = mock[FrameInfoProfile]

        mockUnsafeMethod.expects(0).returning(r).once()
        threadInfoProfile.withUnsafeTopFrame should be (r)
      }
    }
  }
}
