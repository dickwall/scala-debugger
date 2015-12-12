package org.senkbeil.debugger.api.lowlevel.classes

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, OneInstancePerTest}
import org.senkbeil.debugger.api.lowlevel.requests.JDIRequestArgument
import org.senkbeil.debugger.api.utils.{ActionInfo, PendingActionManager}
import test.{JDIMockHelpers, TestClassUnloadManager}

import scala.util.{Failure, Success}

class PendingClassUnloadSupportSpec extends FunSpec with Matchers
  with OneInstancePerTest with MockFactory with JDIMockHelpers
{
  private val TestRequestId = java.util.UUID.randomUUID().toString
  private val mockClassUnloadManager = mock[ClassUnloadManager]

  private class TestClassUnloadInfoPendingActionManager
    extends PendingActionManager[ClassUnloadRequestInfo]
  private val mockPendingActionManager =
    mock[TestClassUnloadInfoPendingActionManager]

  private val pendingClassUnloadSupport = new TestClassUnloadManager(
    mockClassUnloadManager
  ) with PendingClassUnloadSupport {
    override protected def newRequestId(): String = TestRequestId

    override protected val pendingActionManager: PendingActionManager[ClassUnloadRequestInfo] =
      mockPendingActionManager
  }

  describe("ExtendedClassUnloadManager") {
    describe("#processAllPendingClassUnloadRequests") {
      it("should process all pending class unload requests") {
        val expected = Seq(
          ClassUnloadRequestInfo(),
          ClassUnloadRequestInfo(),
          ClassUnloadRequestInfo()
        )

        // Create class unload requests to use for testing
        (mockClassUnloadManager.createClassUnloadRequestWithId _)
          .expects(*, *)
          .returning(Success(java.util.UUID.randomUUID().toString))
          .repeated(3).times()

        expected.foreach(c => pendingClassUnloadSupport.createClassUnloadRequest(
          c.extraArguments: _*
        ))

        (mockPendingActionManager.processAllActions _).expects()
          .returning(expected.map(c => ActionInfo("id", c, () => {}))).once()

        val actual = pendingClassUnloadSupport.processAllPendingClassUnloadRequests()
        actual should be (expected)
      }
    }

    describe("#pendingClassUnloadRequests") {
      it("should return a collection of pending class unload requests") {
        val expected = Seq(
          ClassUnloadRequestInfo(),
          ClassUnloadRequestInfo(Seq(stub[JDIRequestArgument])),
          ClassUnloadRequestInfo()
        )

        (mockClassUnloadManager.createClassUnloadRequestWithId _)
          .expects(*, *)
          .returning(Success(java.util.UUID.randomUUID().toString))
          .repeated(3).times()

        expected.foreach(c => pendingClassUnloadSupport.createClassUnloadRequest(
          c.extraArguments: _*
        ))

        (mockPendingActionManager.getPendingActionData _).expects(*)
          .returning(expected).once()

        val actual = pendingClassUnloadSupport.pendingClassUnloadRequests

        actual should be (expected)
      }

      it("should be empty if there are no pending class unload requests") {
        val expected = Nil

        // No pending class unload requests
        (mockPendingActionManager.getPendingActionData _).expects(*)
          .returning(Nil).once()

        val actual = pendingClassUnloadSupport.pendingClassUnloadRequests

        actual should be (expected)
      }
    }

    describe("#createClassUnloadRequestWithId") {
      it("should return Success(id) if the class unload was created") {
        val expected = Success(TestRequestId)

        // Create a class unload to use for testing
        (mockClassUnloadManager.createClassUnloadRequestWithId _)
          .expects(TestRequestId, Nil)
          .returning(expected).once()

        val actual = pendingClassUnloadSupport.createClassUnloadRequestWithId(
          TestRequestId
        )

        actual should be (expected)
      }

      it("should add a pending class unload if exception thrown") {
        val expected = Success(TestRequestId)
        val extraArguments = Seq(stub[JDIRequestArgument])

        (mockClassUnloadManager.createClassUnloadRequestWithId _)
          .expects(*, *)
          .returning(Failure(new Throwable)).once()

        // Pending class unload should be set
        (mockPendingActionManager.addPendingActionWithId _).expects(
          TestRequestId,
          ClassUnloadRequestInfo(extraArguments),
          * // Don't care about checking action
        ).returning(TestRequestId).once()

        val actual = pendingClassUnloadSupport.createClassUnloadRequestWithId(
          TestRequestId, extraArguments: _*
        )

        actual should be (expected)
      }

      it("should return a failure if pending disabled and failed to create request") {
        val expected = Failure(new Throwable)
        val extraArguments = Seq(stub[JDIRequestArgument])

        (mockClassUnloadManager.createClassUnloadRequestWithId _)
          .expects(*, *)
          .returning(expected).once()

        pendingClassUnloadSupport.enablePending = false
        val actual = pendingClassUnloadSupport.createClassUnloadRequestWithId(
          TestRequestId, extraArguments: _*
        )

        actual should be (expected)
      }
    }

    describe("#createClassUnloadRequest") {
      it("should return Success(id) if the class unload was created") {
        val expected = Success(TestRequestId)

        // Create a class unload to use for testing
        (mockClassUnloadManager.createClassUnloadRequestWithId _)
          .expects(TestRequestId, Nil)
          .returning(expected).once()

        val actual = pendingClassUnloadSupport.createClassUnloadRequest()

        actual should be (expected)
      }

      it("should add a pending class unload if exception thrown") {
        val expected = Success(TestRequestId)
        val extraArguments = Seq(stub[JDIRequestArgument])

        (mockClassUnloadManager.createClassUnloadRequestWithId _)
          .expects(*, *)
          .returning(Failure(new Throwable)).once()

        // Pending class unload should be set
        (mockPendingActionManager.addPendingActionWithId _).expects(
          TestRequestId,
          ClassUnloadRequestInfo(extraArguments),
          * // Don't care about checking action
        ).returning(TestRequestId).once()

        val actual = pendingClassUnloadSupport.createClassUnloadRequest(
          extraArguments: _*
        )

        actual should be (expected)
      }

      it("should return a failure if pending disabled and failed to create request") {
        val expected = Failure(new Throwable)
        val extraArguments = Seq(stub[JDIRequestArgument])

        (mockClassUnloadManager.createClassUnloadRequestWithId _)
          .expects(*, *)
          .returning(expected).once()

        pendingClassUnloadSupport.enablePending = false
        val actual = pendingClassUnloadSupport.createClassUnloadRequest(
          extraArguments: _*
        )

        actual should be (expected)
      }
    }

    describe("#removeClassUnloadRequest") {
      it("should return true if the class unload was successfully deleted") {
        val expected = true

        (mockClassUnloadManager.createClassUnloadRequestWithId _)
          .expects(*, *)
          .returning(Success(TestRequestId)).once()

        pendingClassUnloadSupport.createClassUnloadRequestWithId(TestRequestId)

        (mockClassUnloadManager.removeClassUnloadRequest _).expects(*)
          .returning(true).once()

        // Return "no removals" for pending class unload requests
        // (performed by standard removeClassUnloadRequest call)
        (mockPendingActionManager.removePendingActionsWithId _).expects(*)
          .returning(None).once()

        val actual = pendingClassUnloadSupport.removeClassUnloadRequest(
          TestRequestId
        )

        actual should be (expected)
      }

      it("should return true if the pending class unload request was successfully deleted") {
        val expected = true
        val extraArguments = Seq(stub[JDIRequestArgument])

        (mockClassUnloadManager.createClassUnloadRequestWithId _)
          .expects(*, *)
          .returning(Failure(new Throwable)).once()

        // Pending class unload request should be set
        (mockPendingActionManager.addPendingActionWithId _).expects(
          TestRequestId,
          ClassUnloadRequestInfo(extraArguments),
          * // Don't care about checking action
        ).returning(TestRequestId).once()

        pendingClassUnloadSupport.createClassUnloadRequestWithId(
          TestRequestId,
          extraArguments: _*
        )

        // Return removals for pending class unload requests
        val pendingRemovalReturn = Seq(
          ActionInfo(
            TestRequestId,
            ClassUnloadRequestInfo(extraArguments),
            () => {}
          )
        )
        (mockClassUnloadManager.removeClassUnloadRequest _)
          .expects(TestRequestId)
          .returning(false).once()
        (mockPendingActionManager.removePendingActionsWithId _).expects(*)
          .returning(Some(pendingRemovalReturn)).once()

        val actual = pendingClassUnloadSupport.removeClassUnloadRequest(
          TestRequestId
        )

        actual should be (expected)
      }

      it("should return false if the class unload request was not found") {
        val expected = false

        (mockClassUnloadManager.removeClassUnloadRequest _)
          .expects(*)
          .returning(false).once()

        // Return "no removals" for pending class unload requests
        (mockPendingActionManager.removePendingActionsWithId _).expects(*)
          .returning(None).once()

        val actual = pendingClassUnloadSupport.removeClassUnloadRequest(
          TestRequestId
        )

        actual should be (expected)
      }
    }
  }
}
