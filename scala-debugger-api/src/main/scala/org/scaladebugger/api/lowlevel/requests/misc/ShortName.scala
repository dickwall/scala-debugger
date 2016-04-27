package org.scaladebugger.api.lowlevel.requests.misc

import org.scaladebugger.api.lowlevel.requests.JDIRequestArgument
import org.scaladebugger.api.lowlevel.requests.misc.processors.ShortNameProcessor

/**
 * Represents a flag that the provided name is a short name, not a full
 * source path.
 */
sealed trait ShortName extends JDIRequestArgument {
  /** True if just the short name, otherwise false if full source path. */
  val isShortName: Boolean

  override def toProcessor: ShortNameProcessor = new ShortNameProcessor(this)
}

/**
 * Represents a flag indicating that the file name is a short name.
 */
case object IsShortName extends ShortName { val isShortName: Boolean = true }

/**
 * Represents a flag indicating that the file name is a source path.
 */
case object IsSourcePath extends ShortName { val isShortName: Boolean = false }

