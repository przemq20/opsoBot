name := "opsoBot"

version := "0.1"

scalaVersion := "2.13.3"

resolvers += "scalac repo" at "https://raw.githubusercontent.com/ScalaConsultants/mvn-repo/master/"

libraryDependencies += "org.jsoup" % "jsoup" % "1.13.1"
libraryDependencies += "io.spray" %% "spray-json" % "1.3.5"
libraryDependencies += "com.github.slack-scala-client" %% "slack-scala-client" % "0.2.10"
