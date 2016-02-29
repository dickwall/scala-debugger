package org.scaladebugger.api.profiles.pure.info

import com.sun.jdi.{StackFrame, ThreadReference}
import org.scaladebugger.api.profiles.traits.info.FrameInfoProfile
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class PureThreadInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockNewFrameProfile = mockFunction[StackFrame, FrameInfoProfile]
  private val mockThreadReference = mock[ThreadReference]
  private val pureThreadInfoProfile = new PureThreadInfoProfile(
    mockThreadReference
  ) {
    override protected def newFrameProfile(
      stackFrame: StackFrame
    ): FrameInfoProfile = mockNewFrameProfile(stackFrame)
  }

  describe("PureThreadInfoProfile") {
    describe("#uniqueId") {
      it("should return the unique id of the thread") {
        val expected = 12345L

        (mockThreadReference.uniqueID _).expects().returning(expected).once()

        val actual = pureThreadInfoProfile.uniqueId

        actual should be (expected)
      }
    }

    describe("#name") {
      it("should return the name of the thread") {
        val expected = "some name"

        (mockThreadReference.name _).expects().returning(expected).once()

        val actual = pureThreadInfoProfile.name

        actual should be (expected)
      }
    }

    describe("#unsafeFrames") {
      it("should return a collection of profiles wrapping the frames of the suspended thread") {
        val expected = Seq(mock[FrameInfoProfile])

        val mockStackFrames = Seq(mock[StackFrame])

        // Retrieve raw stack frame
        import scala.collection.JavaConverters._
        (mockThreadReference.frames: Function0[java.util.List[StackFrame]])
          .expects()
          .returning(mockStackFrames.asJava)
          .once()

        // Wrap stack frame in profile instance
        mockStackFrames.zip(expected).foreach { case (sf, e) =>
          mockNewFrameProfile.expects(sf).returning(e).once()
        }

        val actual = pureThreadInfoProfile.unsafeFrames

        actual should be (expected)
      }
    }

    describe("#withUnsafeFrame") {
      it("should return a profile wrapping the frame at the specified position in the stack") {
        val expected = mock[FrameInfoProfile]

        val index = 999
        val mockStackFrame = mock[StackFrame]

        // Retrieve raw stack frame
        (mockThreadReference.frame _).expects(index)
          .returning(mockStackFrame).once()

        // Wrap stack frame in profile instance
        mockNewFrameProfile.expects(mockStackFrame).returning(expected).once()

        val actual = pureThreadInfoProfile.withUnsafeFrame(index)

        actual should be (expected)
      }
    }

    describe("#unsafeTotalFrames") {
      it("should return the total number of frames available in the stack of the suspended thread") {
        val expected = 999

        (mockThreadReference.frameCount _).expects().returning(expected).once()

        val actual = pureThreadInfoProfile.unsafeTotalFrames

        actual should be (expected)
      }
    }
  }
}
