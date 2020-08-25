package opsobot.bot

import opsobot.bot.Bot.sendMenu
import opsobot.parsers.{OlimpParser, OpsoParser}
import org.slf4j.{Logger, LoggerFactory}
import slack.models.Message
import slack.rtm.SlackRtmClient

object CommandParser {
  val logger: Logger = LoggerFactory.getLogger(CommandParser.getClass)

  def greetings(message: Message, client: SlackRtmClient): Unit = {
    val mess = commands.Greetings.toString(message.user)
    client.sendMessage(message.channel, mess)
  }

  def parse(command: String, message: Message, client: SlackRtmClient): Unit = {
    command match {
      case "<@${client.getState().self.id}>" => //do nothing
      case "-pizza" =>
        client.sendMessage(message.channel, commands.Pizza.toString)
        logger.info("Sent pizza menu")
      case "-joke" =>
        RandomJoke.sendJoke(message.channel, client)
        logger.info("Sent joke")
      case "-help" =>
        client.sendMessage(message.channel, commands.Help.toString)
      case "-addDailyReminder" =>
        if (Bot.channels.contains(message.channel)) {
          client.sendMessage(message.channel, "This channel already is subscribing to daily menu")
        }
        else {
          client.sendMessage(message.channel, "ok, from now on, i will send daily menu to this channel")
          Bot.channels += message.channel
        }
      case "-rmDailyReminder" =>
        if (Bot.channels.contains(message.channel)) {
          client.sendMessage(message.channel, "Ok, you will not received my daily updates")
          Bot.channels.subtractOne(message.channel)
        }
        else {
          client.sendMessage(message.channel, "I can't delete this channel, because it's not on the list")
        }
      case "-opsoMenu" => sendMenu(message.channel, "OPSO", makePretty(OpsoParser.parse().sort()))
      case "-olimpMenu" => sendMenu(message.channel, "Olimp", makePretty(OlimpParser.parse().sort()))
      case _ =>
        val text = s"Sorry, I don't understand \'$command\' :c "
        client.sendMessage(message.channel, text)
    }
  }

  def makePretty(menu: Seq[(String, List[String])]): String = {
    val builder = new StringBuilder()
    if (menu.isEmpty) {
      "Menu na dzisiaj jest niedostępne"
    }
    else {
      menu.foreach(category => {
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
