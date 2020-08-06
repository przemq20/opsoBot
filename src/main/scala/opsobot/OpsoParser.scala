package opsobot

import org.jsoup.Jsoup
import org.jsoup.select.Elements
import scala.jdk.CollectionConverters._

object OpsoParser extends App {
  final val MENU_URL = "https://opso.pl/menu/"

  def parse(): Menu = {
    val document = Jsoup.connect(MENU_URL).get()
    val menu = new Menu()

    val dinners: Elements = document.select(".zestawy-obiadowe")
    val headers = dinners.select("h4")

    // TODO: Wyrzucać wyjątek jeśli nie ma menu na dziś - obsługa w Main
    val data = headers.first().html
    headers.first().remove()

    headers.forEach(category => {
      val categoryName = category.html
      if (categoryName != data) {
        val dishes = category
          .nextElementSibling()
          .select("p")
          .select(":not(.priceelement)")
          .eachText()
          .asScala.toList

        menu.addCategory(categoryName, dishes)
      }
    })
    menu
  }
}
