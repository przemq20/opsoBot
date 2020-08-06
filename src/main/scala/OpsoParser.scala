import scala.io.Source
import org.jsoup._
object OpsoParser extends App {
  val html = Source.fromURL("https://opso.pl/menu/")
  val s = html.mkString
//  print(s)

  val doc = Jsoup.connect("https://opso.pl/menu/").get()
  print(doc)

}
