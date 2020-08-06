package opsobot

import scala.io.{BufferedSource, Source}
import spray.json._

object randomJoke {
  val html: BufferedSource = Source.fromURL("https://official-joke-api.appspot.com/jokes/random")
  val joke: String = html.mkString
  val spray: JsValue = joke.map(_.toChar).mkString.parseJson
  val v1: JsValue = spray.asJsObject().getFields("setup").head
  val v2: JsValue = spray.asJsObject().getFields("punchline").head

  (v1, v2)
}
