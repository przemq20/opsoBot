import org.jsoup._
import org.jsoup.select.Elements
object OpsoParser extends App {
  val doc = Jsoup.connect("https://opso.pl/menu/").get()
  val obiadki: Elements = doc.select(".zestawy-obiadowe")
  val data = obiadki.select("h4").first().html
  println(s"\nJestem opsoBOT. Menu na ${data.toLowerCase()} to:\n")

  obiadki.select("h4").forEach(category => {
    if (category.html() != data) {
      println(category.html())
      val obiady = category.nextElementSibling().select("p").select(":not(.priceelement)")
      obiady.forEach(obiad => println(s"\t- ${obiad.html()}"))
    }

  })
}
