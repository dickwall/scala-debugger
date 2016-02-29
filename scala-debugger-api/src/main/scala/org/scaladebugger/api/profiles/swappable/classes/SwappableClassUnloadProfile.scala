package org.scaladebugger.api.profiles.swappable.classes
import acyclic.file

import org.scaladebugger.api.lowlevel.JDIArgument
import org.scaladebugger.api.lowlevel.classes.ClassUnloadRequestInfo
import org.scaladebugger.api.pipelines.Pipeline.IdentityPipeline
import org.scaladebugger.api.profiles.swappable.SwappableDebugProfileManagement
import org.scaladebugger.api.profiles.traits.classes.ClassUnloadProfile

import scala.util.Try

/**
 * Represents a swappable profile for class unload events that redirects the
 * invocation to another profile.
 */
trait SwappableClassUnloadProfile extends ClassUnloadProfile {
  this: SwappableDebugProfileManagement =>

  override def onClassUnloadWithData(
    extraArguments: JDIArgument*
  ): Try[IdentityPipeline[ClassUnloadEventAndData]] = {
    withCurrentProfile.onClassUnloadWithData(extraArguments: _*)
  }

  override def classUnloadRequests: Seq[ClassUnloadRequestInfo] = {
    withCurrentProfile.classUnloadRequests
  }
}
