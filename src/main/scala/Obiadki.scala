import org.jsoup._
import org.jsoup.select.Elements

object Obiadki extends App {
  val doc = Jsoup.connect("https://opso.pl/menu/").get()
  val obiadki: Elements = doc.select(".zestawy-obiadowe")
  println(obiadki)
}
