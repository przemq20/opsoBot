package opsobot.bot

import opsobot.bot.Bot.{client, logger}
import opsobot.randomJoke
import org.slf4j.{Logger, LoggerFactory}
import slack.models.Message
import slack.rtm.SlackRtmClient

object CommandParser {
  val logger: Logger = LoggerFactory.getLogger(CommandParser.getClass)

  def greetings(message:Message, client:SlackRtmClient): Unit ={
    client.sendMessage(message.channel, s"<@${message.user}>: Hey!")
  }

  def parse(command : String, message:Message, client:SlackRtmClient): Unit = {

    if(command.equals(s"<@${client.getState().self.id}>")){}
    else if (command.equals("-pizza") || command.equals("-p")) {
      client.sendMessage(message.channel, "Tutaj kiedyś będzie menu z pizzą")
      logger.info("Sent pizza menu")
    }
    else if (command.equals("-joke")) {
      client.sendMessage(message.channel, randomJoke.randomJoke())
      logger.info("Sent joke")
    }
    else if(command.equals("-help")){
      client.sendMessage(message.channel, commands.Help.help())
    }
    else {
//      client.sendMessage(message.channel, s"<@${message.user}>: Hey!")
    }
  }
}
