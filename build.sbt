name := "opsoBot"

version := "0.1"

scalaVersion := "2.13.3"

resolvers += "scalac repo" at "https://raw.githubusercontent.com/ScalaConsultants/mvn-repo/master/"

libraryDependencies ++= Seq("org.jsoup" % "jsoup" % "1.13.1",
  "io.spray" %% "spray-json" % "1.3.5",
  "com.github.slack-scala-client" %% "slack-scala-client" % "0.2.10",
  "org.slf4j" % "slf4j-simple" % "1.6.4",
  "org.slf4j" % "slf4j-api" % "1.7.5",
  "com.typesafe.akka" %% "akka-stream" % "2.6.8",
  "com.typesafe.akka" %% "akka-http" % "10.2.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.0",
)
libraryDependencies +=  "org.scalaj" %% "scalaj-http" % "2.4.2"
