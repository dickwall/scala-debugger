package org.scaladebugger.api.profiles.traits.vm
import acyclic.file

import com.sun.jdi.event.VMDisconnectEvent
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}
import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline

import scala.util.{Failure, Success, Try}

class VMDisconnectProfileSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val TestThrowable = new Throwable

  // Pipeline that is parent to the one that just streams the event
  private val TestPipelineWithData = Pipeline.newPipeline(
    classOf[VMDisconnectProfile#VMDisconnectEventAndData]
  )

  private val successVMDisconnectProfile = new Object with VMDisconnectProfile {
    override def onVMDisconnectWithData(
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[VMDisconnectEventAndData]] = {
      Success(TestPipelineWithData)
    }
  }

  private val failVMDisconnectProfile = new Object with VMDisconnectProfile {
    override def onVMDisconnectWithData(
      extraArguments: JDIArgument*
    ): Try[IdentityPipeline[VMDisconnectEventAndData]] = {
      Failure(TestThrowable)
    }
  }

  describe("VMDisconnectProfile") {
    describe("#onVMDisconnect") {
      it("should return a pipeline with the event data results filtered out") {
        val expected = mock[VMDisconnectEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: VMDisconnectEvent = null
        successVMDisconnectProfile.onVMDisconnect().get.foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should capture any exception as a failure") {
        val expected = TestThrowable

        // Data to be run through pipeline
        val data = (mock[VMDisconnectEvent], Seq(mock[JDIEventDataResult]))

        var actual: Throwable = null
        failVMDisconnectProfile.onVMDisconnect().failed.foreach(actual = _)

        actual should be (expected)
      }
    }

    describe("#onUnsafeVMDisconnect") {
      it("should return a pipeline of events if successful") {
        val expected = mock[VMDisconnectEvent]

        // Data to be run through pipeline
        val data = (expected, Seq(mock[JDIEventDataResult]))

        var actual: VMDisconnectEvent = null
        successVMDisconnectProfile.onUnsafeVMDisconnect().foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(data)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failVMDisconnectProfile.onUnsafeVMDisconnect()
        }
      }
    }

    describe("#onUnsafeVMDisconnectWithData") {
      it("should return a pipeline of events and data if successful") {
        // Data to be run through pipeline
        val expected = (mock[VMDisconnectEvent], Seq(mock[JDIEventDataResult]))

        var actual: (VMDisconnectEvent, Seq[JDIEventDataResult]) = null
        successVMDisconnectProfile
          .onUnsafeVMDisconnectWithData()
          .foreach(actual = _)

        // Funnel the data through the parent pipeline that contains data to
        // demonstrate that the pipeline with just the event is merely a
        // mapping on top of the pipeline containing the data
        TestPipelineWithData.process(expected)

        actual should be (expected)
      }

      it("should throw the exception if unsuccessful") {
        intercept[Throwable] {
          failVMDisconnectProfile.onUnsafeVMDisconnectWithData()
        }
      }
    }
  }
}

