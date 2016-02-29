package org.scaladebugger.api.profiles.dotty.vm
import acyclic.file

import org.scaladebugger.api.profiles.pure.vm.PureVMStartProfile

/**
 * Represents a profile for vm start events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyVMStartProfile extends PureVMStartProfile

