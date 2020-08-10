package opsobot.bot.commands

import opsobot.parsers.PizzaParser

object Pizza {
  override def toString: String = {
    val builder = new StringBuilder
    builder.addAll("=" * 40)
    builder.addAll("\n")
    builder.addAll(PizzaParser.parse().toString)
    builder.addAll("=" * 40)
    builder.result()
  }
}
