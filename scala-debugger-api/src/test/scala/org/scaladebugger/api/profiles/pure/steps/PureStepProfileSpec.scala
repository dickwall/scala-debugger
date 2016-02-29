package org.scaladebugger.api.profiles.pure.steps
import acyclic.file

import com.sun.jdi.ThreadReference
import com.sun.jdi.event.{StepEvent, Event, EventQueue}
import com.sun.jdi.request.EventRequestManager
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.{ScalaFutures, Futures}
import org.scalatest.time.{Span, Milliseconds}
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.requests.filters.ThreadFilter
import org.scaladebugger.api.lowlevel.requests.properties.UniqueIdProperty
import org.scaladebugger.api.lowlevel.steps.{PendingStepSupportLike, StepRequestInfo, StepManager, StandardStepManager}
import org.scaladebugger.api.lowlevel.events.{JDIEventArgument, EventManager}
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument
import org.scaladebugger.api.pipelines.{Operation, Pipeline}
import org.scaladebugger.api.utils.LoopingTaskRunner
import org.scaladebugger.api.lowlevel.events.EventType.StepEventType
import test.JDIMockHelpers

import scala.util.{Failure, Success}

class PureStepProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory with JDIMockHelpers with Futures
  with ScalaFutures
{
  implicit override val patienceConfig = PatienceConfig(
    timeout = scaled(Span(3000, Milliseconds)),
    interval = scaled(Span(5, Milliseconds))
  )

  private val TestRequestId = java.util.UUID.randomUUID().toString
  private val mockThreadReference = mock[ThreadReference]
  private val mockStepManager = mock[StepManager]
  private val mockEventManager = mock[EventManager]

  private val pureStepProfile = new Object with PureStepProfile {
    override protected val stepManager = mockStepManager
    override protected val eventManager = mockEventManager
  }

  describe("PureStepProfile") {
    describe("#stepRequests") {
      it("should include all active requests") {
        val expected = Seq(
          StepRequestInfo(
            TestRequestId,
            false,
            mock[ThreadReference],
            0,
            1
          )
        )

        val mockStepManager = mock[PendingStepSupportLike]
        val pureStepProfile = new Object with PureStepProfile {
          override protected val stepManager = mockStepManager
          override protected val eventManager: EventManager = mockEventManager
        }

        (mockStepManager.stepRequestList _).expects()
          .returning(expected).once()

        (mockStepManager.pendingStepRequests _).expects()
          .returning(Nil).once()

        val actual = pureStepProfile.stepRequests

        actual should be (expected)
      }

      it("should include pending requests if supported") {
        val expected = Seq(
          StepRequestInfo(
            TestRequestId,
            false,
            mock[ThreadReference],
            0,
            1
          )
        )

        val mockStepManager = mock[PendingStepSupportLike]
        val pureStepProfile = new Object with PureStepProfile {
          override protected val stepManager = mockStepManager
          override protected val eventManager: EventManager = mockEventManager
        }

        (mockStepManager.stepRequestList _).expects()
          .returning(Nil).once()

        (mockStepManager.pendingStepRequests _).expects()
          .returning(expected).once()

        val actual = pureStepProfile.stepRequests

        actual should be (expected)
      }

      it("should only include active requests if pending unsupported") {
        val expected = Seq(
          StepRequestInfo(
            TestRequestId,
            false,
            mock[ThreadReference],
            0,
            1
          )
        )

        (mockStepManager.stepRequestList _).expects()
          .returning(expected).once()

        val actual = pureStepProfile.stepRequests

        actual should be (expected)
      }
    }

    describe("#stepIntoLineWithData") {
      it("should create a new step request and pipeline whose future is returned") {
        val expected = (mock[StepEvent], Nil)

        val stepPipeline = Pipeline.newPipeline(
          classOf[(Event, Seq[JDIEventDataResult])]
        )
        val rArgs = Seq(mock[JDIRequestArgument])
        val eArgs = Seq(mock[JDIEventArgument])

        // These filters should be injected by our profile
        val threadFilter = ThreadFilter(threadReference = mockThreadReference)

        (mockStepManager.createStepIntoLineRequest _)
          .expects(mockThreadReference, rArgs :+ threadFilter)
          .returning(Success(TestRequestId)).once()

        (mockEventManager.addEventDataStream _)
          .expects(StepEventType, eArgs)
          .returning(stepPipeline).once()

        val stepFuture = pureStepProfile.stepIntoLineWithData(
          mockThreadReference,
          rArgs ++ eArgs: _*
        )

        // Process the pipeline to trigger the future
        stepPipeline.process(expected)

        whenReady(stepFuture) { actual => actual should be (expected) }
      }

      it("should capture exceptions thrown when creating the request") {
        val expected = new Throwable

        (mockStepManager.createStepIntoLineRequest _).expects(*, *)
          .returning(Failure(expected)).once()

        val stepFuture = pureStepProfile.stepIntoLineWithData(
          mockThreadReference
        )

        whenReady(stepFuture.failed) { actual => actual should be (expected) }
      }
    }

    describe("#stepOutLineWithData") {
      it("should create a new step request and pipeline whose future is returned") {
        val expected = (mock[StepEvent], Nil)

        val stepPipeline = Pipeline.newPipeline(
          classOf[(Event, Seq[JDIEventDataResult])]
        )
        val rArgs = Seq(mock[JDIRequestArgument])
        val eArgs = Seq(mock[JDIEventArgument])

        // These filters should be injected by our profile
        val threadFilter = ThreadFilter(threadReference = mockThreadReference)

        (mockStepManager.createStepOutLineRequest _)
          .expects(mockThreadReference, rArgs :+ threadFilter)
          .returning(Success(TestRequestId)).once()

        (mockEventManager.addEventDataStream _)
          .expects(StepEventType, eArgs)
          .returning(stepPipeline).once()

        val stepFuture = pureStepProfile.stepOutLineWithData(
          mockThreadReference,
          rArgs ++ eArgs: _*
        )

        // Process the pipeline to trigger the future
        stepPipeline.process(expected)

        whenReady(stepFuture) { actual => actual should be (expected) }
      }

      it("should capture exceptions thrown when creating the request") {
        val expected = new Throwable

        (mockStepManager.createStepOutLineRequest _).expects(*, *)
          .returning(Failure(expected)).once()

        val stepFuture = pureStepProfile.stepOutLineWithData(
          mockThreadReference
        )

        whenReady(stepFuture.failed) { actual => actual should be (expected) }
      }
    }

    describe("#stepOverLineWithData") {
      it("should create a new step request and pipeline whose future is returned") {
        val expected = (mock[StepEvent], Nil)

        val stepPipeline = Pipeline.newPipeline(
          classOf[(Event, Seq[JDIEventDataResult])]
        )
        val rArgs = Seq(mock[JDIRequestArgument])
        val eArgs = Seq(mock[JDIEventArgument])

        // These filters should be injected by our profile
        val threadFilter = ThreadFilter(threadReference = mockThreadReference)

        (mockStepManager.createStepOverLineRequest _)
          .expects(mockThreadReference, rArgs :+ threadFilter)
          .returning(Success(TestRequestId)).once()

        (mockEventManager.addEventDataStream _)
          .expects(StepEventType, eArgs)
          .returning(stepPipeline).once()

        val stepFuture = pureStepProfile.stepOverLineWithData(
          mockThreadReference,
          rArgs ++ eArgs: _*
        )

        // Process the pipeline to trigger the future
        stepPipeline.process(expected)

        whenReady(stepFuture) { actual => actual should be (expected) }
      }

      it("should capture exceptions thrown when creating the request") {
        val expected = new Throwable

        (mockStepManager.createStepOverLineRequest _).expects(*, *)
          .returning(Failure(expected)).once()

        val stepFuture = pureStepProfile.stepOverLineWithData(
          mockThreadReference
        )

        whenReady(stepFuture.failed) { actual => actual should be (expected) }
      }
    }

    describe("#stepIntoMinWithData") {
      it("should create a new step request and pipeline whose future is returned") {
        val expected = (mock[StepEvent], Nil)

        val stepPipeline = Pipeline.newPipeline(
          classOf[(Event, Seq[JDIEventDataResult])]
        )
        val rArgs = Seq(mock[JDIRequestArgument])
        val eArgs = Seq(mock[JDIEventArgument])

        // These filters should be injected by our profile
        val threadFilter = ThreadFilter(threadReference = mockThreadReference)

        (mockStepManager.createStepIntoMinRequest _)
          .expects(mockThreadReference, rArgs :+ threadFilter)
          .returning(Success(TestRequestId)).once()

        (mockEventManager.addEventDataStream _)
          .expects(StepEventType, eArgs)
          .returning(stepPipeline).once()

        val stepFuture = pureStepProfile.stepIntoMinWithData(
          mockThreadReference,
          rArgs ++ eArgs: _*
        )

        // Process the pipeline to trigger the future
        stepPipeline.process(expected)

        whenReady(stepFuture) { actual => actual should be (expected) }
      }

      it("should capture exceptions thrown when creating the request") {
        val expected = new Throwable

        (mockStepManager.createStepIntoMinRequest _).expects(*, *)
          .returning(Failure(expected)).once()

        val stepFuture = pureStepProfile.stepIntoMinWithData(
          mockThreadReference
        )

        whenReady(stepFuture.failed) { actual => actual should be (expected) }
      }
    }

    describe("#stepOutMinWithData") {
      it("should create a new step request and pipeline whose future is returned") {
        val expected = (mock[StepEvent], Nil)

        val stepPipeline = Pipeline.newPipeline(
          classOf[(Event, Seq[JDIEventDataResult])]
        )
        val rArgs = Seq(mock[JDIRequestArgument])
        val eArgs = Seq(mock[JDIEventArgument])

        // These filters should be injected by our profile
        val threadFilter = ThreadFilter(threadReference = mockThreadReference)

        (mockStepManager.createStepOutMinRequest _)
          .expects(mockThreadReference, rArgs :+ threadFilter)
          .returning(Success(TestRequestId)).once()

        (mockEventManager.addEventDataStream _)
          .expects(StepEventType, eArgs)
          .returning(stepPipeline).once()

        val stepFuture = pureStepProfile.stepOutMinWithData(
          mockThreadReference,
          rArgs ++ eArgs: _*
        )

        // Process the pipeline to trigger the future
        stepPipeline.process(expected)

        whenReady(stepFuture) { actual => actual should be (expected) }
      }

      it("should capture exceptions thrown when creating the request") {
        val expected = new Throwable

        (mockStepManager.createStepOutMinRequest _).expects(*, *)
          .returning(Failure(expected)).once()

        val stepFuture = pureStepProfile.stepOutMinWithData(
          mockThreadReference
        )

        whenReady(stepFuture.failed) { actual => actual should be (expected) }
      }
    }

    describe("#stepOverMinWithData") {
      it("should create a new step request and pipeline whose future is returned") {
        val expected = (mock[StepEvent], Nil)

        val stepPipeline = Pipeline.newPipeline(
          classOf[(Event, Seq[JDIEventDataResult])]
        )
        val rArgs = Seq(mock[JDIRequestArgument])
        val eArgs = Seq(mock[JDIEventArgument])

        // These filters should be injected by our profile
        val threadFilter = ThreadFilter(threadReference = mockThreadReference)

        (mockStepManager.createStepOverMinRequest _)
          .expects(mockThreadReference, rArgs :+ threadFilter)
          .returning(Success(TestRequestId)).once()

        (mockEventManager.addEventDataStream _)
          .expects(StepEventType, eArgs)
          .returning(stepPipeline).once()

        val stepFuture = pureStepProfile.stepOverMinWithData(
          mockThreadReference,
          rArgs ++ eArgs: _*
        )

        // Process the pipeline to trigger the future
        stepPipeline.process(expected)

        whenReady(stepFuture) { actual => actual should be (expected) }
      }

      it("should capture exceptions thrown when creating the request") {
        val expected = new Throwable

        (mockStepManager.createStepOverMinRequest _).expects(*, *)
          .returning(Failure(expected)).once()

        val stepFuture = pureStepProfile.stepOverMinWithData(
          mockThreadReference
        )

        whenReady(stepFuture.failed) { actual => actual should be (expected) }
      }
    }

    describe("#onStepWithData") {
      it("should create a stream of events with data for steps") {
        val expected = (mock[StepEvent], Seq(mock[JDIEventDataResult]))
        val arguments = Seq(mock[JDIEventArgument])

        (mockEventManager.addEventDataStream _).expects(
          StepEventType, arguments
        ).returning(
            Pipeline.newPipeline(classOf[(Event, Seq[JDIEventDataResult])])
              .map(t => (expected._1, expected._2))
          ).once()

        var actual: (StepEvent, Seq[JDIEventDataResult]) = null
        val pipeline =
          pureStepProfile.onStepWithData(mockThreadReference, arguments: _*)
        pipeline.get.foreach(actual = _)

        pipeline.get.process(expected)

        actual should be (expected)
      }
    }
  }
}
