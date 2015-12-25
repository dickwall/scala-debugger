import UnidocKeys._

//
// DEBUGGER API PROJECT CONFIGURATION
//
lazy val scalaDebuggerApi = project
  .in(file("scala-debugger-api"))
  .configs(IntegrationTest)
  .settings(Common.settings: _*)
  .settings(Defaults.itSettings: _*)
  .settings(Seq(
    name := "scala-debugger-api",

    // NOTE: Fork needed to avoid mixing in sbt classloader, which is causing
    //       LinkageError to be thrown for JDI-based classes
    fork in Test := true,
    fork in IntegrationTest := true,

    // Run tests in parallel
    // NOTE: Needed to avoid ScalaTest serialization issues
    parallelExecution in Test := true,
    testForkedParallel in Test := true,

    // TODO: Determine why this causes multiple tests to fail (maybe processes
    //       spawned are clashing?)
    // Run integration tests in parallel
//    parallelExecution in IntegrationTest := true,
//    testForkedParallel in IntegrationTest := true,

    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-api" % "1.7.5",
      "org.slf4j" % "slf4j-log4j12" % "1.7.5" % "test,it",
      "log4j" % "log4j" % "1.2.17" % "test,it",
      "org.scalatest" %% "scalatest" % "3.0.0-M14" % "test,it",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.2.1" % "test,it"
    ),
    // JDK Dependency (just for sbt, must exist on classpath for execution,
    // cannot be redistributed)
    internalDependencyClasspath in Compile +=
      { Attributed.blank(Build.JavaTools) },
    internalDependencyClasspath in Runtime +=
      { Attributed.blank(Build.JavaTools) },
    internalDependencyClasspath in Test +=
      { Attributed.blank(Build.JavaTools) },
    internalDependencyClasspath in IntegrationTest +=
      { Attributed.blank(Build.JavaTools) }
  ): _*)
  .dependsOn(scalaDebuggerMacros % "compile->compile;test->compile;it->compile")
  .dependsOn(scalaDebuggerTest % "test->compile;it->compile")

//
// DEBUGGER TEST CODE PROJECT CONFIGURATION
//
lazy val scalaDebuggerTest = project
  .in(file("scala-debugger-test"))
  .settings(Common.settings: _*)
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
  .settings(Macros.settings: _*)
  .settings(Seq(
    name := "scala-debugger-macros"
  ))

lazy val scalaDebuggerRepl = project
  .in(file("scala-debugger-repl"))
  .configs(IntegrationTest)
  .settings(Common.settings: _*)
  .settings(Repl.settings: _*)
  .settings(Seq(
    name := "scala-debugger-repl"
  ))
  .dependsOn(scalaDebuggerApi % "compile->compile;test->compile;it->compile")

//
// MAIN PROJECT CONFIGURATION
//
lazy val root = project
  .in(file("."))
  .settings(Common.settings: _*)
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

