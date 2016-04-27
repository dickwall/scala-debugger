package org.scaladebugger.api.lowlevel.requests.misc

import org.scalamock.scalatest.MockFactory
import org.scalatest.{FunSpec, Matchers, ParallelTestExecution}

class IsShortNameSpec extends FunSpec with Matchers
  with ParallelTestExecution with MockFactory
{
  describe("IsShortName") {
    describe("#isShortName") {
      it("should return true") {
        val expected = true
        val actual = IsShortName.isShortName
        actual should be (expected)
      }
    }

    describe("#toProcessor") {
      it("should return a processor containing IsShortName") {
        IsShortName.toProcessor.argument should be (IsShortName)
      }
    }
  }
}

