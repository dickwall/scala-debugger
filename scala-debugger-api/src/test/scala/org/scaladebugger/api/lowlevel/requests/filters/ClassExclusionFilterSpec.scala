package org.scaladebugger.api.lowlevel.requests.filters
import acyclic.file

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class ClassExclusionFilterSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  private val testPattern = "some pattern"
  private val classExclusionFilter = ClassExclusionFilter(
    classPattern = testPattern
  )

  describe("ClassExclusionFilter") {
    describe("#toProcessor") {
      it("should return a processor containing the class exclusion filter") {
        classExclusionFilter.toProcessor.argument should be (classExclusionFilter)
      }
    }
  }
}
