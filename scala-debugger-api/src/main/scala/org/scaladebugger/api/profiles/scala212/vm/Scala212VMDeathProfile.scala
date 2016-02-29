package org.scaladebugger.api.profiles.scala212.vm
import acyclic.file

import org.scaladebugger.api.profiles.pure.vm.PureVMDeathProfile

/**
 * Represents a profile for vm death events that adds logic
 * specifically for Scala 2.12.
 */
trait Scala212VMDeathProfile extends PureVMDeathProfile

