package org.scaladebugger.api.profiles.swappable.info
import acyclic.file

import com.sun.jdi.ThreadReference
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.info.{ThreadInfoProfile, GrabInfoProfile, MiscInfoProfile}

/**
 * Represents a swappable profile for grabbing various info that redirects the
 * invocation to another profile.
 */
trait SwappableGrabInfoProfile extends GrabInfoProfile {
  this: SwappableDebugProfileManagement =>
  override def forUnsafeThread(
    threadReference: ThreadReference
  ): ThreadInfoProfile = withCurrentProfile.forUnsafeThread(threadReference)

  override def forUnsafeThread(
    threadId: Long
  ): ThreadInfoProfile = withCurrentProfile.forUnsafeThread(threadId)
}
