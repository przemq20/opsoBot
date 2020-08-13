package opsobot.bot

import scala.collection.mutable

class Menu() {
  private val data: mutable.Map[String, List[String]] = mutable.Map[String, List[String]]()

  def addCategory(category: String): mutable.Map[String, List[String]] = {
    addCategory(category, List.empty)
    data
  }

  def addCategory(category: String, dishes: List[String]): mutable.Map[String, List[String]] = {
    data.update(category, dishes)
    data
  }

  def addToCategory(category: String, dishes: List[String]): mutable.Map[String, List[String]] = {
    val existingDishes = data.getOrElseUpdate(category, dishes)
    if (existingDishes.isEmpty) data.update(category, dishes)
    data
  }

  def categories(): Iterable[String] = data.keys

  def dishes(category: String): List[String] = {
    data.getOrElse(category, List.empty)
  }

  override def toString: String = {
    val builder = new StringBuilder()
    if (data.isEmpty) {
      "Menu na dzisiaj jest niedostępne"
    }
    else {
      data.foreach(category => {
        val categoryName = category._1
        val dishesList = category._2
        builder.addAll(categoryName)
        builder.addAll(":")
        dishesList.foreach(dish => {
          builder.addAll("\n\t- ")
          builder.addAll(dish)
        })
        builder.addAll("\n")
      })
      builder.result()
    }
  }
}
