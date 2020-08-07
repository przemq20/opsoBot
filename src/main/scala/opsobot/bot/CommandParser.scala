package opsobot.bot

import opsobot.randomJoke
import org.slf4j.{Logger, LoggerFactory}
import slack.models.Message
import slack.rtm.SlackRtmClient

object CommandParser {
  val logger: Logger = LoggerFactory.getLogger(CommandParser.getClass)

  def greetings(message: Message, client: SlackRtmClient): Unit = {
    client.sendMessage(message.channel, s"<@${message.user}>: Hey!")
  }

  def parse(command: String, message: Message, client: SlackRtmClient): Unit = {

    if (command.equals(s"<@${client.getState().self.id}>")) {
      //do nothing
    }
    else if (command.equals("-pizza") || command.equals("-p")) {
      client.sendMessage(message.channel, "Tutaj kiedyś będzie menu z pizzą")
      logger.info("Sent pizza menu")
    }
    else if (command.equals("-joke")) {
      client.sendMessage(message.channel, randomJoke.randomJoke())
      logger.info("Sent joke")
    }
    else if (command.equals("-help")) {
      client.sendMessage(message.channel, commands.Help.help())
    }
    else if (command.equals("-addDailyReminder")) {
      if (Bot.channels.contains(message.channel)) {
        client.sendMessage(message.channel, "This channel already is subscribing to daily menu")
      }
      else {
        client.sendMessage(message.channel, "ok, from now on, i will send daily menu to this channel")
        Bot.channels += message.channel
      }
    }
    else if (command.equals("-rmDailyReminder")) {
      if (Bot.channels.contains(message.channel)) {
        client.sendMessage(message.channel, "Ok, you will not received my daily updates")
        Bot.channels.subtractOne(message.channel)
      }
      else {
        client.sendMessage(message.channel, "I can't delete this channel, because it's not on the list")
      }
    }
    else {
      //sory, nie rozumiem twojej komendy
      //      client.sendMessage(message.channel, s"<@${message.user}>: Hey!")
    }
  }
}
