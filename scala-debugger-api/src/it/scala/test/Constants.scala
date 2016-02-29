package test
import acyclic.file

import org.scalatest.time.{Span, Milliseconds, Seconds}

/**
 * Constants for our tests.
 */
object Constants {
  val EventuallyTimeout = Span(10, Seconds)
  val EventuallyInterval = Span(5, Milliseconds)
}
