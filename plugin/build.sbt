name := "play-logging-ui"
organization := "de.luhuec"
version := "1.0-SNAPSHOT"

lazy val root =
  project.in(file(".")).enablePlugins(PlayScala).enablePlugins(SbtTwirl)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play" % "2.7.5",
  guice,
  "org.mockito"            %% "mockito-scala"      % "1.14.8" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0"  % Test
)
