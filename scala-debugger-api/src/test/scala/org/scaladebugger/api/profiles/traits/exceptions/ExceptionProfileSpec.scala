package org.scaladebugger.api.profiles.traits.exceptions
import acyclic.file

import com.sun.jdi.event.ExceptionEvent
import org.scaladebugger.api.lowlevel.exceptions.ExceptionRequestInfo
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class ExceptionProfileSpec extends FunSpec with Matchers with ParallelTestExecution
  with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[ExceptionProfile#ExceptionEventAndData]
  )

  private val successExceptionProfile = new Object with ExceptionProfile {
    override def onExceptionWithData(
      exceptionName: String,
      notifyCaught: Boolean,
      notifyUncaught: Boolean,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[ExceptionEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def onAllExceptionsWithData(
      notifyCaught: Boolean,
      notifyUncaught: Boolean,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[ExceptionEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def exceptionRequests: Seq[ExceptionRequestInfo] = ???
  }

  private val failExceptionProfile = new Object with ExceptionProfile {
    override def exceptionRequests: Seq[ExceptionRequestInfo] = ???

    override def onExceptionWithData(
      exceptionName: String,
      notifyCaught: Boolean,
      notifyUncaught: Boolean,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[ExceptionEventAndData]] = {
      Failure(TestThrowable)
    }

    override def onAllExceptionsWithData(
      notifyCaught: Boolean,
      notifyUncaught: Boolean,
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[ExceptionEventAndData]] = {
      Failure(TestThrowable)
    }
  }

  describe("ExceptionProfile") {
    describe("#onException") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[ExceptionEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: ExceptionEvent = null
        successExceptionProfile
          .onException("", true, true)
          .get
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should capture any error as a failure") {
        val expected = TestThrowable

        var actual: Throwable = null
        failExceptionProfile
          .onException("", true, true)
          .failed
          .foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeException") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[ExceptionEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: ExceptionEvent = null
        successExceptionProfile
          .onUnsafeException("", true, true)
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw an error if it occurs") {
        intercept[Throwable] {
          failExceptionProfile.onUnsafeException("", true, true)
        }
      }
    }

    describe("#onUnsafeExceptionWithData") {
      it("should return a pipeline with the event data results") {
        // Data to be run through pipeline
        val expected = (mock[ExceptionEvent], Seq(mock[JDIEventDataResult]))

        var actual: (ExceptionEvent, Seq[JDIEventDataResult]) = null
        successExceptionProfile
          .onUnsafeExceptionWithData("", true, true)
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw an error if it occurs") {
        intercept[Throwable] {
          failExceptionProfile.onUnsafeExceptionWithData("", true, true)
        }
      }
    }

    describe("#onAllExceptions") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[ExceptionEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: ExceptionEvent = null
        successExceptionProfile
          .onAllExceptions(true, true)
          .get
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should capture any error as a failure") {
        val expected = TestThrowable

        var actual: Throwable = null
        failExceptionProfile
          .onAllExceptions(true, true)
          .failed
          .foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeAllExceptions") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[ExceptionEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: ExceptionEvent = null
        successExceptionProfile
          .onUnsafeAllExceptions(true, true)
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw an error if it occurs") {
        intercept[Throwable] {
          failExceptionProfile.onUnsafeAllExceptions(true, true)
        }
      }
    }

    describe("#onUnsafeAllExceptionsWithData") {
      it("should return a pipeline with the event data results") {
        // Data to be run through pipeline
        val expected = (mock[ExceptionEvent], Seq(mock[JDIEventDataResult]))

        var actual: (ExceptionEvent, Seq[JDIEventDataResult]) = null
        successExceptionProfile
          .onUnsafeAllExceptionsWithData(true, true)
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw an error if it occurs") {
        intercept[Throwable] {
          failExceptionProfile.onUnsafeAllExceptionsWithData(true, true)
        }
      }
    }
  }
}
