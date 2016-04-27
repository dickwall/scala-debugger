package org.scaladebugger.api.lowlevel.requests.misc

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class IsSourcePathSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  describe("IsSourcePath") {
    describe("#isShortName") {
      it("should return false") {
        val expected = false
        val actual = IsSourcePath.isShortName
        actual should be (expected)
      }
    }

    describe("#toProcessor") {
      it("should return a processor containing IsSourcePath") {
        IsSourcePath.toProcessor.argument should be (IsSourcePath)
      }
    }
  }
}

