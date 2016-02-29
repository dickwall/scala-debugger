package org.scaladebugger.api.profiles.pure.info

import com.sun.jdi.{ThreadReference, VirtualMachine}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class PureGrabInfoProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockVirtualMachine = mock[VirtualMachine]
  private val pureGrabInfoProfile = new PureGrabInfoProfile {
    override protected val _virtualMachine: VirtualMachine = mockVirtualMachine
  }

  describe("PureGrabInfoProfile") {
    describe("#forUnsafeThread(threadReference)") {
      it("should return a pure thread info profile wrapping the thread") {
        val expected = mock[ThreadReference]

        val actual = pureGrabInfoProfile.forUnsafeThread(expected)

        (expected.uniqueID _).expects().returning(999L).twice()
        actual.uniqueId should be (expected.uniqueID())
      }
    }

    describe("#forUnsafeThread(threadId)") {
      it("should return a new profile if a thread with matching unique id is found") {
        val expected = mock[ThreadReference]

        import scala.collection.JavaConverters._
        (mockVirtualMachine.allThreads _).expects()
          .returning(Seq(expected).asJava).once()

        (expected.uniqueID _).expects().returning(999L).repeated(3).times()
        val actual = pureGrabInfoProfile.forUnsafeThread(999L)

        actual.uniqueId should be (expected.uniqueID())
      }

      it("should throw an exception if no thread with a matching unique id is found") {
        val mockThreadReference = mock[ThreadReference]

        import scala.collection.JavaConverters._
        (mockVirtualMachine.allThreads _).expects()
          .returning(Seq(mockThreadReference).asJava).once()

        intercept[NoSuchElementException] {
          (mockThreadReference.uniqueID _).expects().returning(998L).once()
          pureGrabInfoProfile.forUnsafeThread(999L)
        }
      }
    }
  }
}
