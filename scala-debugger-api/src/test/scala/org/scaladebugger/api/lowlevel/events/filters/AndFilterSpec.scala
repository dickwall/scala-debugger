package org.scaladebugger.api.lowlevel.events.filters
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class AndFilterSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val andFilter = AndFilter()

  describe("AndFilter") {
    describe("#toProcessor") {
      it("should return a processor containing the and filter") {
        andFilter.toProcessor.argument should be (andFilter)
      }
    }
  }
}
