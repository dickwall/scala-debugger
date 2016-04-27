package org.scaladebugger.api.lowlevel.requests.misc.processors

import com.sun.jdi.request.EventRequest
import org.scaladebugger.api.lowlevel.requests.misc.IsShortName
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class ShortNameProcessorSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val shortNameProcessor = new ShortNameProcessor(IsShortName)

  describe("ShortNameProcessor") {
    describe("#process") {
      it("should do nothing to the event request") {
        val mockEventRequest = mock[EventRequest]

        shortNameProcessor.process(mockEventRequest) should be (mockEventRequest)
      }
    }
  }
}
