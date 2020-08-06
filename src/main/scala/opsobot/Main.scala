package opsobot

object Main {
  def main(args: Array[String]): Unit = {
    val menu = OpsoParser.parse()

    println("Olimp Menu:")
    menu.categories().foreach(category => {
      println(category)
      menu.dishes(category).foreach(dish => {
        println(s"\t- $dish")
      })
    })
  }
}
