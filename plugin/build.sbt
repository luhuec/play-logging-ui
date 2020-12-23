name := "play-logging-ui"
organization := "com.github.luhuec"
homepage := Some(url("https://github.com/luhuec/play-logging-ui"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/luhuec/play-logging-ui"),
    "https://github.com/luhuec/play-logging-ui.git"
  )
)
developers := List(Developer("luhuec", "luhuec", "lh@luhuec.de", url("https://gitlab.com/luhuec")))
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true
version := "0.0.4-SNAPSHOT"

crossPaths := false

lazy val root =
  project.in(file(".")).enablePlugins(PlayScala).enablePlugins(SbtTwirl)

libraryDependencies ++= Seq(
  guice % Provided,
  "org.mockito"            %% "mockito-scala"      % "1.14.8" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3"  % Test
)

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)
