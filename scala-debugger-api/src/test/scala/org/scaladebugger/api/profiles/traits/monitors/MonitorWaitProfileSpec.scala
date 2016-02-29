package org.scaladebugger.api.profiles.traits.monitors
import acyclic.file

import com.sun.jdi.event.MonitorWaitEvent
import org.scaladebugger.api.lowlevel.monitors.MonitorWaitRequestInfo
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class MonitorWaitProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[MonitorWaitProfile#MonitorWaitEventAndData]
  )

  private val successMonitorWaitProfile = new Object with MonitorWaitProfile {
    override def onMonitorWaitWithData(
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[MonitorWaitEventAndData]] = {
      Success(TestPipelineWithData)
    }

    override def monitorWaitRequests: Seq[MonitorWaitRequestInfo] = ???
  }

  private val failMonitorWaitProfile = new Object with MonitorWaitProfile {
    override def onMonitorWaitWithData(
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[MonitorWaitEventAndData]] = {
      Failure(TestThrowable)
    }

    override def monitorWaitRequests: Seq[MonitorWaitRequestInfo] = ???
  }

  describe("MonitorWaitProfile") {
    describe("#onMonitorWait") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[MonitorWaitEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: MonitorWaitEvent = null
        successMonitorWaitProfile
          .onMonitorWait()
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
        failMonitorWaitProfile
          .onMonitorWait()
          .failed
          .foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeMonitorWait") {
      it("should return a pipeline of events if successful") {
        val expected = mock[MonitorWaitEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: MonitorWaitEvent = null
        successMonitorWaitProfile
          .onUnsafeMonitorWait()
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failMonitorWaitProfile.onUnsafeMonitorWait()
        }
      }
    }

    describe("#onUnsafeMonitorWaitWithData") {
      it("should return a pipeline of events and data if successful") {
        // Data to be run through pipeline
        val expected = (mock[MonitorWaitEvent], Seq(mock[JDIEventDataResult]))

        var actual: (MonitorWaitEvent, Seq[JDIEventDataResult]) = null
        successMonitorWaitProfile
          .onUnsafeMonitorWaitWithData()
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failMonitorWaitProfile.onUnsafeMonitorWaitWithData()
        }
      }
    }
  }
}

