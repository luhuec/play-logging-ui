name := "play-logging-ui-sample-app"
version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

resolvers += Resolver.sonatypeRepo("snapshots")

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(
  "foo.luhuec" %% "play-logging-ui" % "1.0-SNAPSHOT",
  guice
)
