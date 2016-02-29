package org.scaladebugger.api.profiles.dotty.vm
import acyclic.file

import org.scaladebugger.api.profiles.pure.vm.PureVMDisconnectProfile

/**
 * Represents a profile for vm disconnect events that adds logic
 * specifically for Scala's dotty compiler.
 */
trait DottyVMDisconnectProfile extends PureVMDisconnectProfile

