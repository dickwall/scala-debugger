package org.scaladebugger.api.profiles.traits.steps
import acyclic.file

import com.sun.jdi.ThreadReference
import com.sun.jdi.event.StepEvent
import org.scaladebugger.api.lowlevel.steps.StepRequestInfo
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.concurrent._
import scala.util.{Failure, Success, Try}

class StepProfileSpec extends FunSpec with Matchers with ParallelTestExecution
  with MockFactory with ScalaFutures
{
  private val mockThreadReference = mock[ThreadReference]

  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[StepProfile#StepEventAndData]
  )

  private val successStepProfile = new Object with StepProfile {
    override def stepIntoLineWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[StepEventAndData] = ???

    override def stepOverLineWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[StepEventAndData] = ???

    override def stepOutLineWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[StepEventAndData] = ???

    override def stepIntoMinWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[(StepEventAndData)] = ???

    override def stepOutMinWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[(StepEventAndData)] = ???

    override def stepOverMinWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[(StepEventAndData)] = ???

    override def onStepWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[StepEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def stepRequests: Seq[StepRequestInfo] = ???
  }

  private val failStepProfile = new Object with StepProfile {
    override def stepIntoLineWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[StepEventAndData] = ???

    override def stepOverLineWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[StepEventAndData] = ???

    override def stepOutLineWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[StepEventAndData] = ???

    override def stepIntoMinWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[(StepEventAndData)] = ???

    override def stepOutMinWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[(StepEventAndData)] = ???

    override def stepOverMinWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Future[(StepEventAndData)] = ???

    override def onStepWithData(
      threadReference: ThreadReference,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[StepEventAndData]] = {
      Failure(TestThrowable)
    }

    override def stepRequests: Seq[StepRequestInfo] = ???
  }

  describe("StepProfile") {
    describe("#stepIntoLine") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[StepEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        // Pipeline that is parent to the one that just streams the event
        val promiseWithData = Promise[StepProfile#StepEventAndData]()
        val futureWithData = promiseWithData.future

        val stepProfile = new Object with StepProfile {
          override def stepIntoLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = {
            futureWithData
          }

          override def stepOverLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepOutLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepIntoMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def stepOutMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def stepOverMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def onStepWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Try[IdentityPipeline[(StepEvent, Seq[JDIEventDataResult])]] = ???

          override def stepRequests: Seq[StepRequestInfo] = ???
        }

        val actual = stepProfile.stepIntoLine(mockThreadReference)

        // Funnel the data through the future that is mapped by the wrapper
        // method
        promiseWithData.success(data)

        whenReady(actual) { a =>
          a should be(expected)
        }
      }
    }

    describe("#stepOverLine") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[StepEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        // Pipeline that is parent to the one that just streams the event
        val promiseWithData = Promise[StepProfile#StepEventAndData]()
        val futureWithData = promiseWithData.future

        val stepProfile = new Object with StepProfile {
          override def stepIntoLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepOverLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = {
            futureWithData
          }

          override def stepOutLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepIntoMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def stepOutMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def stepOverMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def onStepWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Try[IdentityPipeline[(StepEvent, Seq[JDIEventDataResult])]] = ???

          override def stepRequests: Seq[StepRequestInfo] = ???
        }

        val actual = stepProfile.stepOverLine(mockThreadReference)

        // Funnel the data through the future that is mapped by the wrapper
        // method
        promiseWithData.success(data)

        whenReady(actual) { a =>
          a should be(expected)
        }
      }
    }

    describe("#stepOutLine") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[StepEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        // Pipeline that is parent to the one that just streams the event
        val promiseWithData = Promise[StepProfile#StepEventAndData]()
        val futureWithData = promiseWithData.future

        val stepProfile = new Object with StepProfile {
          override def stepIntoLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepOverLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepOutLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = {
            futureWithData
          }

          override def stepIntoMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def stepOutMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def stepOverMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def onStepWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Try[IdentityPipeline[(StepEvent, Seq[JDIEventDataResult])]] = ???

          override def stepRequests: Seq[StepRequestInfo] = ???
        }

        val actual = stepProfile.stepOutLine(mockThreadReference)

        // Funnel the data through the future that is mapped by the wrapper
        // method
        promiseWithData.success(data)

        whenReady(actual) { a =>
          a should be (expected)
        }
      }
    }

    describe("#stepIntoMin") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[StepEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        // Pipeline that is parent to the one that just streams the event
        val promiseWithData = Promise[StepProfile#StepEventAndData]()
        val futureWithData = promiseWithData.future

        val stepProfile = new Object with StepProfile {
          override def stepIntoLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepOverLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepOutLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepIntoMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = {
            futureWithData
          }

          override def stepOutMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def stepOverMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def onStepWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Try[IdentityPipeline[(StepEvent, Seq[JDIEventDataResult])]] = ???

          override def stepRequests: Seq[StepRequestInfo] = ???
        }

        val actual = stepProfile.stepIntoMin(mockThreadReference)

        // Funnel the data through the future that is mapped by the wrapper
        // method
        promiseWithData.success(data)

        whenReady(actual) { a =>
          a should be(expected)
        }
      }
    }

    describe("#stepOverMin") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[StepEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        // Pipeline that is parent to the one that just streams the event
        val promiseWithData = Promise[StepProfile#StepEventAndData]()
        val futureWithData = promiseWithData.future

        val stepProfile = new Object with StepProfile {
          override def stepIntoLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepOverLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepOutLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepIntoMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def stepOutMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def stepOverMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = {
            futureWithData
          }

          override def onStepWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Try[IdentityPipeline[(StepEvent, Seq[JDIEventDataResult])]] = ???

          override def stepRequests: Seq[StepRequestInfo] = ???
        }

        val actual = stepProfile.stepOverMin(mockThreadReference)

        // Funnel the data through the future that is mapped by the wrapper
        // method
        promiseWithData.success(data)

        whenReady(actual) { a =>
          a should be(expected)
        }
      }
    }

    describe("#stepOutMin") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[StepEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        // Pipeline that is parent to the one that just streams the event
        val promiseWithData = Promise[StepProfile#StepEventAndData]()
        val futureWithData = promiseWithData.future

        val stepProfile = new Object with StepProfile {
          override def stepIntoLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepOverLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepOutLineWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[StepEventAndData] = ???

          override def stepIntoMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def stepOutMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = {
            futureWithData
          }

          override def stepOverMinWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Future[(StepEventAndData)] = ???

          override def onStepWithData(
            threadReference: ThreadReference,
            extraArguments: JDIArgument*
          ): Try[IdentityPipeline[(StepEvent, Seq[JDIEventDataResult])]] = ???

          override def stepRequests: Seq[StepRequestInfo] = ???
        }

        val actual = stepProfile.stepOutMin(mockThreadReference)

        // Funnel the data through the future that is mapped by the wrapper
        // method
        promiseWithData.success(data)

        whenReady(actual) { a =>
          a should be (expected)
        }
      }
    }

    describe("#onStep") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[StepEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: StepEvent = null
        successStepProfile
          .onStep(mockThreadReference)
          .get
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should capture any exception as a failure") {
        val expected = TestThrowable

        var actual: Throwable = null
        failStepProfile
          .onStep(mockThreadReference)
          .failed
          .foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeStep") {
      it("should return a pipeline of events if successful") {
        val expected = mock[StepEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: StepEvent = null
        successStepProfile
          .onUnsafeStep(mockThreadReference)
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failStepProfile.onUnsafeStep(mockThreadReference)
        }
      }
    }

    describe("#onUnsafeStepWithData") {
      it("should return a pipeline of events and data if successful") {
        // Data to be run through pipeline
        val expected = (mock[StepEvent], Seq(mock[JDIEventDataResult]))

        var actual: (StepEvent, Seq[JDIEventDataResult]) = null
        successStepProfile
          .onUnsafeStepWithData(mockThreadReference)
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failStepProfile
            .onUnsafeStepWithData(mockThreadReference)
        }
      }
    }
  }
}
