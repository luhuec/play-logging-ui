name := "play-logging-ui-sample-app"
version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "com.github.luhuec" % "play-logging-ui" % "0.0.7",
  guice
)
