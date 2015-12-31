import UnidocKeys._

//
// DEBUGGER API PROJECT CONFIGURATION
//
lazy val scalaDebuggerApi = project
  .in(file("scala-debugger-api"))
  .configs(IntegrationTest)
  .settings(Common.settings: _*)
  .settings(Acyclic.settings: _*)
  .settings(Defaults.itSettings: _*)
  .settings(Api.settings: _*)
  .settings(name := "scala-debugger-api")
  .dependsOn(scalaDebuggerMacros % "compile->compile;test->compile;it->compile")
  .dependsOn(scalaDebuggerTest % "test->compile;it->compile")

//
// DEBUGGER TEST CODE PROJECT CONFIGURATION
//
lazy val scalaDebuggerTest = project
  .in(file("scala-debugger-test"))
  .settings(Common.settings: _*)
  .settings(Acyclic.settings: _*)
  .settings(
    // Do not publish the test project
    publishArtifact := false,
    publishLocal := {}
  )

//
// DEBUGGER MACRO PROJECT CONFIGURATION
//
lazy val scalaDebuggerMacros = project
  .in(file("scala-debugger-macros"))
  .settings(Common.settings: _*)
  .settings(Acyclic.settings: _*)
  .settings(Macros.settings: _*)
  .settings(name := "scala-debugger-macros")

//
// DEBUGGER REPL PROJECT CONFIGURATION
//
lazy val scalaDebuggerRepl = project
  .in(file("scala-debugger-repl"))
  .configs(IntegrationTest)
  .settings(Common.settings: _*)
  .settings(Repl.settings: _*)
  .settings(Seq(
    name := "scala-debugger-repl",
    libraryDependencies ++= {
      // If Scala 2.11 or greater, include scala parser combinators that were
      // removed after Scala 2.10
      CrossVersion.partialVersion(scalaVersion.value) match {
        case Some((2, scalaMajor)) if scalaMajor >= 11 => Seq(
          "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4"
        )
        case _ => Nil
      }
    }
  ))
  .dependsOn(scalaDebuggerApi % "compile->compile;test->compile;it->compile")

//
// MAIN PROJECT CONFIGURATION
//
lazy val root = project
  .in(file("."))
  .settings(Common.settings: _*)
  .settings(Acyclic.settings: _*)
  .settings(unidocSettings: _*)
  .settings(
    name := "scala-debugger",
    // Do not publish the aggregation project
    publishArtifact := false,
    publishLocal := {},
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inAnyProject -- inProjects(
      scalaDebuggerTest
    )
  ).aggregate(
    scalaDebuggerApi,
    scalaDebuggerTest,
    scalaDebuggerMacros,
    scalaDebuggerRepl
  )

