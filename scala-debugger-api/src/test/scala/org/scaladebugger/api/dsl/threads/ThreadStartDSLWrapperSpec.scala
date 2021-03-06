package org.scaladebugger.api.dsl.threads

import com.sun.jdi.event.ThreadStartEvent
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.profiles.traits.threads.ThreadStartProfile
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

import scala.util.Success

class ThreadStartDSLWrapperSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockThreadStartProfile = mock[ThreadStartProfile]

  describe("ThreadStartDSLWrapper") {
    describe("#onThreadStart") {
      it("should invoke the underlying profile method") {
        import org.scaladebugger.api.dsl.Implicits.ThreadStartDSL

        val extraArguments = Seq(mock[JDIRequestArgument])
        val returnValue = Success(Pipeline.newPipeline(classOf[ThreadStartEvent]))

        (mockThreadStartProfile.tryGetOrCreateThreadStartRequest _).expects(
          extraArguments
        ).returning(returnValue).once()

        mockThreadStartProfile.onThreadStart(
          extraArguments: _*
        ) should be (returnValue)
      }
    }

    describe("#onUnsafeThreadStart") {
      it("should invoke the underlying profile method") {
        import org.scaladebugger.api.dsl.Implicits.ThreadStartDSL

        val extraArguments = Seq(mock[JDIRequestArgument])
        val returnValue = Pipeline.newPipeline(classOf[ThreadStartEvent])

        (mockThreadStartProfile.getOrCreateThreadStartRequest _).expects(
          extraArguments
        ).returning(returnValue).once()

        mockThreadStartProfile.onUnsafeThreadStart(
          extraArguments: _*
        ) should be (returnValue)
      }
    }

    describe("#onThreadStartWithData") {
      it("should invoke the underlying profile method") {
        import org.scaladebugger.api.dsl.Implicits.ThreadStartDSL

        val extraArguments = Seq(mock[JDIRequestArgument])
        val returnValue = Success(Pipeline.newPipeline(
          classOf[(ThreadStartEvent, Seq[JDIEventDataResult])]
        ))

        (mockThreadStartProfile.tryGetOrCreateThreadStartRequestWithData _).expects(
          extraArguments
        ).returning(returnValue).once()

        mockThreadStartProfile.onThreadStartWithData(
          extraArguments: _*
        ) should be (returnValue)
      }
    }

    describe("#onUnsafeThreadStartWithData") {
      it("should invoke the underlying profile method") {
        import org.scaladebugger.api.dsl.Implicits.ThreadStartDSL

        val extraArguments = Seq(mock[JDIRequestArgument])
        val returnValue = Pipeline.newPipeline(
          classOf[(ThreadStartEvent, Seq[JDIEventDataResult])]
        )

        (mockThreadStartProfile.getOrCreateThreadStartRequestWithData _).expects(
          extraArguments
        ).returning(returnValue).once()

        mockThreadStartProfile.onUnsafeThreadStartWithData(
          extraArguments: _*
        ) should be (returnValue)
      }
    }
  }
}
