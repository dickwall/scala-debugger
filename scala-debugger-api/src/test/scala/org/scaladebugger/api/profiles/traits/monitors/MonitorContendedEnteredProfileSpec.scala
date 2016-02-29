package org.scaladebugger.api.profiles.traits.monitors
import acyclic.file

import com.sun.jdi.event.MonitorContendedEnteredEvent
import org.scaladebugger.api.lowlevel.monitors.MonitorContendedEnteredRequestInfo
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class MonitorContendedEnteredProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[MonitorContendedEnteredProfile#MonitorContendedEnteredEventAndData]
  )

  private val successMonitorContendedEnteredProfile = new Object with MonitorContendedEnteredProfile {
    override def onMonitorContendedEnteredWithData(
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[MonitorContendedEnteredEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def monitorContendedEnteredRequests: Seq[MonitorContendedEnteredRequestInfo] = ???
  }

  private val failMonitorContendedEnteredProfile = new Object with MonitorContendedEnteredProfile {
    override def onMonitorContendedEnteredWithData(
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[MonitorContendedEnteredEventAndData]] = {
      Failure(TestThrowable)
    }

    override def monitorContendedEnteredRequests: Seq[MonitorContendedEnteredRequestInfo] = ???
  }

  describe("MonitorContendedEnteredProfile") {
    describe("#onMonitorContendedEntered") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[MonitorContendedEnteredEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: MonitorContendedEnteredEvent = null
        successMonitorContendedEnteredProfile
          .onMonitorContendedEntered()
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
        failMonitorContendedEnteredProfile
          .onMonitorContendedEntered()
          .failed
          .foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeMonitorContendedEntered") {
      it("should return a pipeline of events if successful") {
        val expected = mock[MonitorContendedEnteredEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: MonitorContendedEnteredEvent = null
        successMonitorContendedEnteredProfile
          .onUnsafeMonitorContendedEntered()
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failMonitorContendedEnteredProfile.onUnsafeMonitorContendedEntered()
        }
      }
    }

    describe("#onUnsafeMonitorContendedEnteredWithData") {
      it("should return a pipeline of events and data if successful") {
        // Data to be run through pipeline
        val expected = (mock[MonitorContendedEnteredEvent], Seq(mock[JDIEventDataResult]))

        var actual: (MonitorContendedEnteredEvent, Seq[JDIEventDataResult]) = null
        successMonitorContendedEnteredProfile
          .onUnsafeMonitorContendedEnteredWithData()
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failMonitorContendedEnteredProfile
            .onUnsafeMonitorContendedEnteredWithData()
        }
      }
    }
  }
}

