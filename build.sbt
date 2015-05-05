name := "ScalaDebugger"

//
// DEBUGGER API PROJECT CONFIGURATION
//
lazy val debuggerApi = project
  .in(file("debugger-api"))
  .configs(IntegrationTest)
  .settings(Common.settings: _*)
  .settings(Defaults.itSettings: _*)
  /*.settings(scalariformSettings: _*)
  .settings(ScalariformKeys.preferences := ScalariformKeys.preferences.value
    .setPreference(PreserveDanglingCloseParenthesis, true)
    .setPreference(CompactControlReadability, true)
  )*/.settings(Seq(
    libraryDependencies ++= Seq(
      "org.slf4j" % "slf4j-api" % "1.7.5",
      "org.slf4j" % "slf4j-log4j12" % "1.7.5" % "test,it",
      "log4j" % "log4j" % "1.2.17" % "test,it",
      "org.scalatest" %% "scalatest" % "2.2.1" % "test,it",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test,it"
    ),
    // JDK Dependency (just for sbt, must exist on classpath for execution,
    // cannot be redistributed)
    internalDependencyClasspath in Compile +=
      { Attributed.blank(Build.JavaTools) },
    internalDependencyClasspath in Runtime +=
      { Attributed.blank(Build.JavaTools) },
    internalDependencyClasspath in Test +=
      { Attributed.blank(Build.JavaTools) }
  ): _*)
  .dependsOn(debuggerTest % "test->compile;test->test")

//
// DEBUGGER TEST CODE PROJECT CONFIGURATION
//
lazy val debuggerTest = project
  .in(file("debugger-test"))
  .settings(Common.settings: _*)

//
// MAIN PROJECT CONFIGURATION
//
lazy val root = project
  .in(file("."))
  .settings(Common.settings: _*)
  .aggregate(debuggerApi, debuggerTest)

