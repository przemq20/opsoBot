package opsobot

import org.jsoup.Jsoup
import org.jsoup.select.Elements

object OlimpParser extends App {
  val doc = Jsoup.connect("https://www.olimprest.pl/restauracje/olimp-krakow-avia-software-park").get()

  val menu = new Menu()

  val categoryBlocks: Elements = doc.select(".menu-category-block")
  categoryBlocks.forEach(category => {
    val categoryName = category.select("h3").html()
    val dishes = category
      .select(".menu-dishes")
      .html()
      .split(',')
      .map(_.trim)

    val re = "(.*)<span>(.*)</span>".r
    val cleanName = categoryName match {
      case re(a, b) => a + b
      case _ => categoryName
    }

    menu.add(categoryName, dishes.toList)
  })

}
