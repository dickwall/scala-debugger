package org.scaladebugger.api.dsl.monitors

import com.sun.jdi.event.MonitorContendedEnterEvent
import org.scaladebugger.api.lowlevel.events.data.JDIEventDataResult
import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument
import org.scaladebugger.api.pipelines.Pipeline
import org.scaladebugger.api.profiles.traits.monitors.MonitorContendedEnterProfile
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

import scala.util.Success

class MonitorContendedEnterDSLWrapperSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val mockMonitorContendedEnterProfile = mock[MonitorContendedEnterProfile]

  describe("MonitorContendedEnterDSLWrapper") {
    describe("#onMonitorContendedEnter") {
      it("should invoke the underlying profile method") {
        import org.scaladebugger.api.dsl.Implicits.MonitorContendedEnterDSL

        val extraArguments = Seq(mock[JDIRequestArgument])
        val returnValue = Success(Pipeline.newPipeline(classOf[MonitorContendedEnterEvent]))

        (mockMonitorContendedEnterProfile.tryGetOrCreateMonitorContendedEnterRequest _).expects(
          extraArguments
        ).returning(returnValue).once()

        mockMonitorContendedEnterProfile.onMonitorContendedEnter(
          extraArguments: _*
        ) should be (returnValue)
      }
    }

    describe("#onUnsafeMonitorContendedEnter") {
      it("should invoke the underlying profile method") {
        import org.scaladebugger.api.dsl.Implicits.MonitorContendedEnterDSL

        val extraArguments = Seq(mock[JDIRequestArgument])
        val returnValue = Pipeline.newPipeline(classOf[MonitorContendedEnterEvent])

        (mockMonitorContendedEnterProfile.getOrCreateMonitorContendedEnterRequest _).expects(
          extraArguments
        ).returning(returnValue).once()

        mockMonitorContendedEnterProfile.onUnsafeMonitorContendedEnter(
          extraArguments: _*
        ) should be (returnValue)
      }
    }

    describe("#onMonitorContendedEnterWithData") {
      it("should invoke the underlying profile method") {
        import org.scaladebugger.api.dsl.Implicits.MonitorContendedEnterDSL

        val extraArguments = Seq(mock[JDIRequestArgument])
        val returnValue = Success(Pipeline.newPipeline(
          classOf[(MonitorContendedEnterEvent, Seq[JDIEventDataResult])]
        ))

        (mockMonitorContendedEnterProfile.tryGetOrCreateMonitorContendedEnterRequestWithData _).expects(
          extraArguments
        ).returning(returnValue).once()

        mockMonitorContendedEnterProfile.onMonitorContendedEnterWithData(
          extraArguments: _*
        ) should be (returnValue)
      }
    }

    describe("#onUnsafeMonitorContendedEnterWithData") {
      it("should invoke the underlying profile method") {
        import org.scaladebugger.api.dsl.Implicits.MonitorContendedEnterDSL

        val extraArguments = Seq(mock[JDIRequestArgument])
        val returnValue = Pipeline.newPipeline(
          classOf[(MonitorContendedEnterEvent, Seq[JDIEventDataResult])]
        )

        (mockMonitorContendedEnterProfile.getOrCreateMonitorContendedEnterRequestWithData _).expects(
          extraArguments
        ).returning(returnValue).once()

        mockMonitorContendedEnterProfile.onUnsafeMonitorContendedEnterWithData(
          extraArguments: _*
        ) should be (returnValue)
      }
    }
  }
}
