package org.scaladebugger.repl.language.runtime

import java.io.File

import org.scaladebugger.api.debuggers.Debugger
import org.scaladebugger.api.virtualmachines.ScalaVirtualMachine
import org.scaladebugger.repl.language.models._
import org.scaladebugger.repl.language.parser.DebuggerParser

import scala.annotation.tailrec
import scala.reflect.ClassTag
import scala.util.Try

/**
 * Represents the interpreter for the small debugger language.
 *
 * @param parser The parser to use on all provided code
 * @param rootScope The root (global) scope for the interpreter
 */
class DebuggerInterpreter(
  val parser: DebuggerParser,
  val rootScope: Scope
) {
  /**
   * Creates a new interpreter with a default root scope.
   *
   * @param parser The parser to use on all provided code
   *
   * @return The new interpreter instance
   */
  def this(parser: DebuggerParser) = this(parser, Scope.newRootScope())

  def interpret(file: File): String = {
    ""
  }

  def interpret(code: String): String = {
    val parseResults = parser.parseAll(parser.program, code)

    parseResults match {
      case parser.Success(p, n) =>
        // TODO: Determine why program is cast as any in success
        val program = p.asInstanceOf[Program]
        Try(walk(program.expressions)).map(_.mkString("\n")).recover {
          case t: Throwable => t.getMessage
        }.get
      case parser.Error(msg, n) => s"Error: $msg"
      case parser.Failure(msg, n) => s"Error: $msg"
      case _ => s"Unknown parser result: $parseResults"
    }
  }

  private def walk(tree: Seq[Expression]): Seq[String] = walk(tree, Nil)


  // TODO: Include debuggers/JVMs somewhere
  private val currentDebugger: Debugger = null
  private val currentJvm: ScalaVirtualMachine = null // NOTE: This can be more than one!

  @tailrec
  private def walk(tree: Seq[Expression], result: Seq[String]): Seq[String] = tree match {
    case Nil                => result
    case node :: remainder  =>
      val newResult = Try(node match {
        case ExpressionGroup(expressions) => walk(expressions).mkString("\n")
        case i: Identifier                => processExpression(i, rootScope)
        case c: CreateCommand             => processCreate(c)
        case l: ListCommand               => processList(l)
        case d: DestroyCommand            => processDestroy(d)
        case h: Help                      => processHelp(h)
        case x                            => processUnknown(x)
      }).recover { case t: Throwable => t.getMessage }.get
      walk(remainder, result :+ newResult)
  }

  private def processCreate(createCommand: CreateCommand): String = {
    createCommand match {
      case CreateAccessWatchpointRequestCommand(c, f, _) =>
        val result = currentJvm.onAccessWatchpoint(c, f)
        if (result.isSuccess) s"Created access watchpoint for $c:$f"
        else s"Failed to create access watchpoint for $c:$f"
      case CreateBreakpointRequestCommand(f, l, _) =>
        val result = currentJvm.onBreakpoint(f, l)
        if (result.isSuccess) s"Created breakpoint for $f:$l"
        else s"Failed to create breakpoint for $f:$l"
      case x => processUnknown(x)
    }
  }

  private def processList(listCommand: ListCommand): String = {
    listCommand match {
      case x => processUnknown(x)
    }
  }

  private def processDestroy(destroyCommand: DestroyCommand): String = {
    destroyCommand match {
      case x => processUnknown(x)
    }
  }

  private def processHelp(help: Help): String = help.topic match {
    case Some(t)  => s"No help available for $t"
    case None     => "TODO: Implement help"
  }

  private def processUnknown(unknownExpression: Expression): String = {
    s"Unknown expression: $unknownExpression"
  }

  /**
   * Retrieves the variable with the associated identifier from the specified
   * scope.
   *
   * @param identifier The identifier of the variable to retrieve
   * @param scope The scope whose variables to search
   *
   * @throws RuntimeException When the identifier is not found in any scope
   * @return The variable if found, otherwise throws an error
   */
  @throws[RuntimeException]
  private def getVariable(identifier: Identifier, scope: Scope): Expression = {
    val variable = scope.findVariable(identifier)

    if (variable.nonEmpty) variable.get
    else {
      val name = identifier.name
      val column = identifier.pos.column
      val line = identifier.pos.line

      sys.error(s"Error: Undefined variable $name at $line:$column")
    }
  }

  /**
   *
   * @param expression
   * @param scope
   * @throws RuntimeException When an identifier is not found in any scope
   * @return
   */
  @throws[RuntimeException]
  @tailrec
  private def processExpression(expression: Expression, scope: Scope): String = {
    expression match {
      case Number(value) => value.toString
      case Identifier(name) =>
        processExpression(getVariable(
          expression.asInstanceOf[Identifier],
          scope
        ), scope)
      case x => processUnknown(x)
    }
  }
}
