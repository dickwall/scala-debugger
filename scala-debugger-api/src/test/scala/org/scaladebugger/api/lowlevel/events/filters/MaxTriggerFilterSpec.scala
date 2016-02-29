package org.scaladebugger.api.lowlevel.events.filters
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{ParallelTestExecution, Matchers, FunSpec}

class MaxTriggerFilterSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val testCount = 3
  private val maxTriggerFilter = MaxTriggerFilter(count = testCount)

  describe("MaxTriggerFilter") {
    describe("#toProcessor") {
      it("should return a processor containing the max trigger filter") {
        maxTriggerFilter.toProcessor.argument should be (maxTriggerFilter)
      }
    }
  }
}
