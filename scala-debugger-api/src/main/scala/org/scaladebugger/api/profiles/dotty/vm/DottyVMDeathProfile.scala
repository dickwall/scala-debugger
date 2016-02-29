package org.scaladebugger.api.profiles.dotty.vm
import acyclic.file

import org.scaladebugger.api.profiles.pure.vm.PureVMDeathProfile

/**
 * Represents a profile for vm death events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyVMDeathProfile extends PureVMDeathProfile

