package org.scaladebugger.api.profiles.swappable.events
import acyclic.file

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.events.EventHandlerInfo
import org.scaladebugger.api.lowlevel.events.EventType.EventType
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.events.EventProfile

import scala.util.Try

/**
 * Represents a swappable profile for events that redirects the
 * invocation to another profile.
 */
trait SwappableEventProfile extends EventProfile {
  this: SwappableDebugProfileManagement =>

  override def onEventWithData(
    eventType: EventType,
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[EventAndData]] = {
    withCurrentProfile.onEventWithData(eventType, extraArguments: _*)
  }

  override def eventHandlers: Seq[EventHandlerInfo] = {
    withCurrentProfile.eventHandlers
  }
}
