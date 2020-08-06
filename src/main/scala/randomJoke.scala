import org.jsoup.Jsoup
import scala.io.Source
import spray.json._

object randomJoke {
  val html = Source.fromURL("https://official-joke-api.appspot.com/jokes/random")
  val joke = html.mkString
  val spray = joke.map(_.toChar).mkString.parseJson
  val v1 = spray.asJsObject().getFields("setup").head
  val v2 = spray.asJsObject().getFields("punchline").head

  (v1, v2)
}
