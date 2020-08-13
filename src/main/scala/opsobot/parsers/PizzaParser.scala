package opsobot.parsers

import opsobot.bot.Menu
import org.jsoup.Jsoup
import org.jsoup.select.Elements

import scala.jdk.CollectionConverters._

object PizzaParser extends App {
  final val MENU_URL = "https://opso.pl/menu/"
  println(parse())

  def parse(): Menu = {
    val document = Jsoup.connect(MENU_URL).get()
    val menu = new Menu()
    val menuSection: Elements = document.select(".pizza")
    val headers = menuSection.select("h4")

    val dishType = "PIZZUNIA"
    val dishes = headers.first().nextElementSibling()
      .select("p")
      .select(":not(.priceelement)")
      .eachText()
      .asScala.toList
      .map(x => x.toLowerCase()) // A w sumie to nie wiem czy to jest potrzebne

    menu.addCategory(dishType, dishes)

    menu
  }
}
