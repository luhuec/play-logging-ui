name := "play-logging-ui"
organization := "foo.luhuec"
version := "1.0-SNAPSHOT"

lazy val root =
  project.in(file(".")).enablePlugins(PlayScala).enablePlugins(SbtTwirl)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.7.5",
  guice,
  "org.scalatest" %% "scalatest" % "3.2.1" % Test,
  "org.mockito" %% "mockito-scala" % "1.14.8" % Test
)
