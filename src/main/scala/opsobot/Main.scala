package opsobot

import opsobot.parsers.OlimpParser

object Main {
  def main(args: Array[String]): Unit = {
    val menu = OlimpParser.parse()

    println("Olimp Menu:")
    println(menu.toString)
  }
}
