package org.senkbeil.debugger.api.lowlevel.threads

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, OneInstancePerTest}
import org.senkbeil.debugger.api.lowlevel.requests.JDIRequestArgument
import org.senkbeil.debugger.api.utils.{ActionInfo, PendingActionManager}
import test.{JDIMockHelpers, TestThreadDeathManager}

import scala.util.{Failure, Success}

class PendingThreadDeathSupportSpec extends FunSpec with Matchers
  with OneInstancePerTest with MockFactory with JDIMockHelpers
{
  private val TestRequestId = java.util.UUID.randomUUID().toString
  private val mockThreadDeathManager = mock[ThreadDeathManager]

  private class TestThreadDeathInfoPendingActionManager
    extends PendingActionManager[ThreadDeathRequestInfo]
  private val mockPendingActionManager =
    mock[TestThreadDeathInfoPendingActionManager]

  private val pendingThreadDeathSupport = new TestThreadDeathManager(
    mockThreadDeathManager
  ) with PendingThreadDeathSupport {
    override protected def newRequestId(): String = TestRequestId

    override protected val pendingActionManager: PendingActionManager[ThreadDeathRequestInfo] =
      mockPendingActionManager
  }

  describe("PendingThreadDeathSupport") {
    describe("#processAllPendingThreadDeathRequests") {
      it("should process all pending thread death requests") {
        val expected = Seq(
          ThreadDeathRequestInfo(),
          ThreadDeathRequestInfo(),
          ThreadDeathRequestInfo()
        )

        // Create thread death requests to use for testing
        (mockThreadDeathManager.createThreadDeathRequestWithId _)
          .expects(*, *)
          .returning(Success(java.util.UUID.randomUUID().toString))
          .repeated(3).times()

        expected.foreach(c => pendingThreadDeathSupport.createThreadDeathRequest(
          c.extraArguments: _*
        ))

        (mockPendingActionManager.processAllActions _).expects()
          .returning(expected.map(c => ActionInfo("id", c, () => {}))).once()

        val actual = pendingThreadDeathSupport.processAllPendingThreadDeathRequests()
        actual should be (expected)
      }
    }

    describe("#pendingThreadDeathRequests") {
      it("should return a collection of pending thread death requests") {
        val expected = Seq(
          ThreadDeathRequestInfo(),
          ThreadDeathRequestInfo(Seq(stub[JDIRequestArgument])),
          ThreadDeathRequestInfo()
        )

        (mockThreadDeathManager.createThreadDeathRequestWithId _)
          .expects(*, *)
          .returning(Success(java.util.UUID.randomUUID().toString))
          .repeated(3).times()

        expected.foreach(c => pendingThreadDeathSupport.createThreadDeathRequest(
          c.extraArguments: _*
        ))

        (mockPendingActionManager.getPendingActionData _).expects(*)
          .returning(expected).once()

        val actual = pendingThreadDeathSupport.pendingThreadDeathRequests

        actual should be (expected)
      }

      it("should be empty if there are no pending thread death requests") {
        val expected = Nil

        // No pending thread death requests
        (mockPendingActionManager.getPendingActionData _).expects(*)
          .returning(Nil).once()

        val actual = pendingThreadDeathSupport.pendingThreadDeathRequests

        actual should be (expected)
      }
    }

    describe("#createThreadDeathRequestWithId") {
      it("should return Success(id) if the thread death was created") {
        val expected = Success(TestRequestId)

        // Create a thread death to use for testing
        (mockThreadDeathManager.createThreadDeathRequestWithId _)
          .expects(TestRequestId, Nil)
          .returning(expected).once()

        val actual = pendingThreadDeathSupport.createThreadDeathRequestWithId(
          TestRequestId
        )

        actual should be (expected)
      }

      it("should add a pending thread death if exception thrown") {
        val expected = Success(TestRequestId)
        val extraArguments = Seq(stub[JDIRequestArgument])

        (mockThreadDeathManager.createThreadDeathRequestWithId _)
          .expects(*, *)
          .returning(Failure(new Throwable)).once()

        // Pending thread death should be set
        (mockPendingActionManager.addPendingActionWithId _).expects(
          TestRequestId,
          ThreadDeathRequestInfo(extraArguments),
          * // Don't care about checking action
        ).returning(TestRequestId).once()

        val actual = pendingThreadDeathSupport.createThreadDeathRequestWithId(
          TestRequestId, extraArguments: _*
        )

        actual should be (expected)
      }

      it("should return a failure if pending disabled and failed to create request") {
        val expected = Failure(new Throwable)
        val extraArguments = Seq(stub[JDIRequestArgument])

        (mockThreadDeathManager.createThreadDeathRequestWithId _)
          .expects(*, *)
          .returning(expected).once()

        pendingThreadDeathSupport.enablePending = false
        val actual = pendingThreadDeathSupport.createThreadDeathRequestWithId(
          TestRequestId, extraArguments: _*
        )

        actual should be (expected)
      }
    }

    describe("#createThreadDeathRequest") {
      it("should return Success(id) if the thread death was created") {
        val expected = Success(TestRequestId)

        // Create a thread death to use for testing
        (mockThreadDeathManager.createThreadDeathRequestWithId _)
          .expects(TestRequestId, Nil)
          .returning(expected).once()

        val actual = pendingThreadDeathSupport.createThreadDeathRequest()

        actual should be (expected)
      }

      it("should add a pending thread death if exception thrown") {
        val expected = Success(TestRequestId)
        val extraArguments = Seq(stub[JDIRequestArgument])

        (mockThreadDeathManager.createThreadDeathRequestWithId _)
          .expects(*, *)
          .returning(Failure(new Throwable)).once()

        // Pending thread death should be set
        (mockPendingActionManager.addPendingActionWithId _).expects(
          TestRequestId,
          ThreadDeathRequestInfo(extraArguments),
          * // Don't care about checking action
        ).returning(TestRequestId).once()

        val actual = pendingThreadDeathSupport.createThreadDeathRequest(
          extraArguments: _*
        )

        actual should be (expected)
      }

      it("should return a failure if pending disabled and failed to create request") {
        val expected = Failure(new Throwable)
        val extraArguments = Seq(stub[JDIRequestArgument])

        (mockThreadDeathManager.createThreadDeathRequestWithId _)
          .expects(*, *)
          .returning(expected).once()

        pendingThreadDeathSupport.enablePending = false
        val actual = pendingThreadDeathSupport.createThreadDeathRequest(
          extraArguments: _*
        )

        actual should be (expected)
      }
    }

    describe("#removeThreadDeathRequest") {
      it("should return true if the thread death was successfully deleted") {
        val expected = true

        (mockThreadDeathManager.createThreadDeathRequestWithId _)
          .expects(*, *)
          .returning(Success(TestRequestId)).once()

        pendingThreadDeathSupport.createThreadDeathRequestWithId(TestRequestId)

        (mockThreadDeathManager.removeThreadDeathRequest _).expects(*)
          .returning(true).once()

        // Return "no removals" for pending thread death requests
        // (performed by standard removeThreadDeathRequest call)
        (mockPendingActionManager.removePendingActionsWithId _).expects(*)
          .returning(None).once()

        val actual = pendingThreadDeathSupport.removeThreadDeathRequest(
          TestRequestId
        )

        actual should be (expected)
      }

      it("should return true if the pending thread death request was successfully deleted") {
        val expected = true
        val extraArguments = Seq(stub[JDIRequestArgument])

        (mockThreadDeathManager.createThreadDeathRequestWithId _)
          .expects(*, *)
          .returning(Failure(new Throwable)).once()

        // Pending thread death request should be set
        (mockPendingActionManager.addPendingActionWithId _).expects(
          TestRequestId,
          ThreadDeathRequestInfo(extraArguments),
          * // Don't care about checking action
        ).returning(TestRequestId).once()

        pendingThreadDeathSupport.createThreadDeathRequestWithId(
          TestRequestId,
          extraArguments: _*
        )

        // Return removals for pending thread death requests
        val pendingRemovalReturn = Seq(
          ActionInfo(
            TestRequestId,
            ThreadDeathRequestInfo(extraArguments),
            () => {}
          )
        )
        (mockThreadDeathManager.removeThreadDeathRequest _)
          .expects(TestRequestId)
          .returning(false).once()
        (mockPendingActionManager.removePendingActionsWithId _).expects(*)
          .returning(Some(pendingRemovalReturn)).once()

        val actual = pendingThreadDeathSupport.removeThreadDeathRequest(
          TestRequestId
        )

        actual should be (expected)
      }

      it("should return false if the thread death request was not found") {
        val expected = false

        (mockThreadDeathManager.removeThreadDeathRequest _)
          .expects(*)
          .returning(false).once()

        // Return "no removals" for pending thread death requests
        (mockPendingActionManager.removePendingActionsWithId _).expects(*)
          .returning(None).once()

        val actual = pendingThreadDeathSupport.removeThreadDeathRequest(
          TestRequestId
        )

        actual should be (expected)
      }
    }
  }
}
