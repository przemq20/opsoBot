package opsobot

import scala.collection.mutable

class Menu() {
  val data: mutable.Map[String, List[String]] = mutable.Map[String, List[String]]()

  def add(category: String, elems: List[String]): mutable.Map[String, List[String]] = {
    data.addOne(category, elems)
  }

  def dishes(category: String): List[String] = {
    data.getOrElse(category, List.empty)
  }
}
