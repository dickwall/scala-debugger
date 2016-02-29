package org.scaladebugger.api.lowlevel.events.filters
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class NotFilterSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val notFilter = NotFilter(mock[JDIEventFilter])

  describe("NotFilter") {
    describe("#toProcessor") {
      it("should return a processor containing the or filter") {
        notFilter.toProcessor.argument should be (notFilter)
      }
    }
  }
}
