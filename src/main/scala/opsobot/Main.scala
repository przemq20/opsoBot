package opsobot

object Main {
  def main(args: Array[String]): Unit = {
    val menu = OpsoParser.parse()

    println("Olimp Menu:")
    println(menu.toString)
  }
}
