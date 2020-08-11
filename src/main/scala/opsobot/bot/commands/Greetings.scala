package opsobot.bot.commands

import scala.math.abs
import scala.util.Random

object Greetings {

  val greetings: Seq[String] = Vector[String]("Hej", "Cześć", "Dzień dobry", "Czołem", "Hola", "Ciao", "Zdrastwujcie")

  def randomGreeting: String = {
    val rand = new Random()
    val n = greetings.length
    greetings(abs(rand.nextInt() % n))
  }

  def toString(name: String = ""): String = {

    s"$randomGreeting <@$name>!"
  }
}
