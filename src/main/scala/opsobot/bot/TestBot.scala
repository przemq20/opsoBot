package opsobot.bot

import java.time.LocalTime
import java.util.Calendar

import akka.actor.ActorSystem
import opsobot.bot.Bot.channels
import opsobot.parsers.{OlimpParser, OpsoParser}
import org.slf4j.LoggerFactory
import slack.SlackUtil
import slack.rtm.SlackRtmClient

import scala.concurrent.Future

object TestBot extends App {
  //class used in testing bot. It sends message every 10 seconds and have support to all commands.

  private val token = resources.Token.token
  var channel = "C017WCACXD5"
  implicit val system: ActorSystem = ActorSystem("slack")
  val logger = LoggerFactory.getLogger(Bot.getClass)

  import system.dispatcher

  val client = SlackRtmClient(token)

  Future {
    client.onMessage { message =>
      val mentionedIds = SlackUtil.extractMentionedIds(message.text)
      logger.info(s"Client ID: ${client.getState().self.id}")
      if (mentionedIds.contains(client.getState().self.id)) {
        CommandParser.greetings(message, client)
        val commands = message.text.split(" ").distinct

        logger.info(s"I received commands: ${commands.mkString("Array(", ", ", ")")}")
        if (commands.length == 1) {
          client.sendMessage(message.channel, "Jeśli potrzebujesz pomocy wpisz \"@opsoolimpbot -help\"")
        }
        else {
          for (command <- commands) {
            CommandParser.parse(command, message, client)
          }
        }
      }
    }
  }

  Future {
    while (true) {
      val secondOfMinute = LocalTime.now.getSecond
      println(secondOfMinute)
      if (secondOfMinute % 10 == 0) {
        for (channel <- channels) {
          sendMenu(channel, "OPSO", OpsoParser.parse().toString)
          sendMenu(channel, "OLIMP", OlimpParser.parse().toString)
        }
      }
      Thread.sleep(1000)
    }
  }

  def sendMenu(channel: String, restaurant: String, menu: String): Unit = {
    val sb = new StringBuilder()
    sb.addAll(restaurant.toUpperCase)
    sb.addAll(" Menu:\n")
    sb.addAll("----" * restaurant.length)
    sb.addAll("\n")
    sb.addAll(menu)
    sb.addAll("\n")
    sb.addAll("-" * 40)
    sb.addAll("\n")
    client.sendMessage(channel, sb.result())
    logger.info(s"Sent menu, date: ${Calendar.getInstance().getTime}")
  }
}
