name := "ameb-image"

version := "0.1.1"

scalaVersion := "2.11.2"

organization := "org.littlewings"

scalacOptions ++= Seq("-Xlint", "-deprecation", "-unchecked", "-feature")

incOptions := incOptions.value.withNameHashing(true)

resolvers += Resolver.sonatypeRepo("public")

libraryDependencies ++= Seq(
  "net.databinder.dispatch" %% "dispatch-jsoup" % "0.11.1",
  "com.jsuereth" %% "scala-arm" % "1.4",
  "org.apache.logging.log4j" % "log4j-api" % "2.0.1",
  "org.apache.logging.log4j" % "log4j-core" % "2.0.1",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.0.1",
  "com.github.scopt" %% "scopt" % "3.2.0"
)
