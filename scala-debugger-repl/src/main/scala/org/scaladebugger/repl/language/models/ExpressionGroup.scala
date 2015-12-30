package org.scaladebugger.repl.language.models

/**
 * Represents a collection of expressions to be evaluated in sequential order.
 *
 * @param expressions The collection of expressions
 */
case class ExpressionGroup(expressions: Seq[Expression]) extends Expression
