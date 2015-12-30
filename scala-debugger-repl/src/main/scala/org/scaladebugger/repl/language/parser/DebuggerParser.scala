package org.scaladebugger.repl.language.parser

import org.scaladebugger.repl.language.models._

import scala.util.parsing.combinator.syntactical.StandardTokenParsers

/**
 * Represents a parser for the small debugger language.
 */
class DebuggerParser extends StandardTokenParsers {
  lexical.reserved ++= ReservedKeywords.toSeq
  lexical.delimiters ++= Delimiters.toSeq

  def program: Parser[Program] = rep(expression) ^^ {
    case e => new Program(e)
  }

  def expression: Parser[Expression] =
    numericLit                    ^^ { n => Number(n.toInt) }         |
    ident                         ^^ { i => new Identifier(i) }       |
    "{" ~> rep(expression) <~ "}" ^^ { e => new ExpressionGroup(e) }  |
    "(" ~> expression <~ ")"      ^^ { e => e }                       |
    createCommand                 ^^ { c => c }                       |
    listCommand                   ^^ { l => l }                       |
    destroyCommand                ^^ { d => d }                       |
    help                          ^^ { h => h }

  def createCommand: Parser[CreateCommand] =
    "create" ~ (("access" ~ "watchpoint") | "aw") ~> ident ~ ident ^^ {
      case c ~ f => new CreateAccessWatchpointRequestCommand(c, f, Nil)
    } |
    "create" ~ ("modification watchpoint" | "mw") ~> ident ~ ident ^^ {
      case c ~ f => new CreateModificationWatchpointRequestCommand(c, f, Nil)
    } |
    "create" ~ ("breakpoint" | "bp") ~> ident ~ numericLit ^^ {
      case f ~ l => new CreateBreakpointRequestCommand(f, l.toInt, Nil)
    } |
    "create" ~ ("class prepare" | "cp") ^^ {
      _ => new CreateClassPrepareRequestCommand(Nil)
    } |
    "create" ~ ("class unload" | "cu") ^^ {
      _ => new CreateClassPrepareRequestCommand(Nil)
    }

  def listCommand: Parser[ListCommand] =
    "list" ~ ("access watchpoint" | "aw") ^^ {
      _ => new ListAccessWatchpointRequestsCommand
    }

  def destroyCommand: Parser[DestroyCommand] =
    "create" ~ ("access watchpoint" | "aw") ~> ident ~ ident ^^ {
      case c ~ f => new DestroyAccessWatchpointRequestCommand(c, f)
    }

  def help: Parser[Help] =
    "help" ~> ("access watchpoint" | "aw") ^^ {
      _ => Help(Some("aw"))
    } |
    "help" ^^ {
      _ => Help(None)
    }

  def parseAll[T](p: Parser[T], in: String): ParseResult[T] = {
    phrase(p)(new lexical.Scanner(in))
  }
}
