package opsobot.parsers

import opsobot.Menu
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object OlimpParser {
  final val MENU_URL = "https://www.olimprest.pl/restauracje/olimp-krakow-avia-software-park"

  def parse(): Menu = {
    val document: Document = Jsoup.connect(MENU_URL).get()
    val menu = new Menu()

    val dishTypeBlocks: Elements = document.select(".menu-category-block")
    if (dishTypeBlocks.isEmpty) {
      //      throw NoUpdatedMenuException("Olimp menu is unavailable")
      return menu
    }
    dishTypeBlocks.forEach(block => {
      val dishType = block.select("h3").text

      val spanTagRegex = "(.*)<span>(.*)</span>".r
      val parsedDishType = dishType match {
        case spanTagRegex(a, b) => a + b
        case _ => dishType
      }

      menu.addCategory(parsedDishType)

      val dishes = block
        .select(".menu-dishes")
        .text()
        .split(',')
        .map(_.trim)
        .toList

      menu.addToCategory(parsedDishType, dishes)
    })
    menu
  }
}
