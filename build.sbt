name := "ameb-image"

version := "0.1.3"

scalaVersion := "2.11.2"

organization := "org.littlewings"

scalacOptions ++= Seq("-Xlint", "-deprecation", "-unchecked", "-feature")

incOptions := incOptions.value.withNameHashing(true)

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-jsoup" % "0.11.2",
  "com.jsuereth" %% "scala-arm" % "1.4",
  "com.github.scopt" %% "scopt" % "3.2.0",
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)
