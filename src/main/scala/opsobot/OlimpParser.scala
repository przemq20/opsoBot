package opsobot

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

object OlimpParser {
  final val MENU_URL = "https://www.olimprest.pl/restauracje/olimp-krakow-avia-software-park"

  def parse(): Menu = {
    val document: Document = Jsoup.connect(MENU_URL).get()
    val menu = new Menu()

    val categoryBlocks: Elements = document.select(".menu-category-block")
    categoryBlocks.forEach(category => {
      val categoryName = category.select("h3").html

      val re = "(.*)<span>(.*)</span>".r
      val parsedCategoryName = categoryName match {
        case re(a, b) => a + b
        case _ => categoryName
      }

      menu.addCategory(parsedCategoryName)

      val dishes = category
        .select(".menu-dishes")
        .html()
        .split(',')
        .map(_.trim)
        .toList

      menu.addToCategory(parsedCategoryName, dishes)
    })
    menu
  }
}
