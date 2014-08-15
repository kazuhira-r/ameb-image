name := "ameb-image"

version := "0.1.2"

scalaVersion := "2.11.2"

organization := "org.littlewings"

scalacOptions ++= Seq("-Xlint", "-deprecation", "-unchecked", "-feature")

incOptions := incOptions.value.withNameHashing(true)

resolvers += Resolver.sonatypeRepo("public")

val log4j2Version = "2.0.1"

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-jsoup" % "0.11.2",
  "com.jsuereth" %% "scala-arm" % "1.4",
  "com.github.scopt" %% "scopt" % "3.2.0",
  "org.apache.logging.log4j" % "log4j-api" % log4j2Version,
  "org.apache.logging.log4j" % "log4j-core" % log4j2Version,
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % log4j2Version
)
