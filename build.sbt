name := "ameb-image"

version := "0.1.4"

scalaVersion := "2.11.5"

organization := "org.littlewings"

scalacOptions ++= Seq("-Xlint", "-deprecation", "-unchecked", "-feature")

incOptions := incOptions.value.withNameHashing(true)

updateOptions := updateOptions.value.withCachedResolution(true)

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-jsoup" % "0.11.2",
  "com.jsuereth" %% "scala-arm" % "1.4",
  "com.github.scopt" %% "scopt" % "3.3.0",
  "org.slf4j" % "slf4j-api" % "1.7.10",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)
