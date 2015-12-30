package org.scaladebugger.repl.language.models

import java.util.concurrent.ConcurrentHashMap
import scala.annotation.tailrec
import scala.collection.JavaConverters._

/**
 * Represents a scope of variables available in the program.
 *
 * @param name The name of the scope
 * @param parent If available, Some parent scope, otherwise None (root)
 */
class Scope(val name: String, val parent: Option[Scope]) {
  /** Represents the available expressions (by name) in the scope. */
  var variables: collection.mutable.Map[String, Expression] =
    new ConcurrentHashMap[String, Expression].asScala

  /**
   * Indicates whether or not this scope is a root scope (no parent).
   *
   * @return True if the scope has no parent, otherwise false
   */
  def isRoot: Boolean = parent.isEmpty

  /**
   * Finds a variable with the specified identifier. Recursively looks through
   * all ancestor scopes until the variable is found.
   *
   * @param identifier The identifier of the variable to retrieve
   *
   * @return Some expression representing the variable if found, otherwise None
   */
  def findVariable(identifier: Identifier): Option[Expression] =
    findVariable(identifier, recursive = true)

  /**
   * Finds a variable with the specified identifier.
   *
   * @param identifier The identifier of the variable to retrieve
   * @param recursive If true, will check scope parents until the variable is
   *                  found or all ancestors are exhausted
   *
   * @return Some expression representing the variable if found, otherwise None
   */
  def findVariable(
    identifier: Identifier,
    recursive: Boolean
  ): Option[Expression] = findVariable(identifier, this, recursive)

  /**
   * Finds a variable with the specified identifier in the specified scope,
   * recursively checking parent scopes until the variable is found.
   *
   * @param identifier The identifier of the variable to retrieve
   * @param scope The scope whose variables to search
   * @param recursive If true, will check scope parents until the variable is
   *                  found or all ancestors are exhausted
   *
   * @return Some expression representing the variable if found, otherwise None
   */
  @tailrec
  private def findVariable(
    identifier: Identifier,
    scope: Scope,
    recursive: Boolean
  ): Option[Expression] = {
    val hasVariable = scope.variables.contains(identifier.name)

    if (hasVariable)
      Some(scope.variables(identifier.name))
    else if (!scope.isRoot && recursive)
      findVariable(identifier, scope.parent.get, recursive)
    else
      None
  }
}

object Scope {
  /** Represents the default name for the root scope. */
  val DefaultRootName = "root"

  /** Creates a new root scope with the default root scope name. */
  def newRootScope(): Scope = new Scope(DefaultRootName, None)
}
