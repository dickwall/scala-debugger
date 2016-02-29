package org.scaladebugger.api.lowlevel.events.filters
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class OrFilterSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val orFilter = OrFilter()

  describe("OrFilter") {
    describe("#toProcessor") {
      it("should return a processor containing the or filter") {
        orFilter.toProcessor.argument should be (orFilter)
      }
    }
  }
}
