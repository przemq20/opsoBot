package opsobot.bot

import java.util.Calendar

import akka.actor.ActorSystem
import opsobot.{OlimpParser, OpsoParser, randomJoke}
import org.slf4j.LoggerFactory
import slack.SlackUtil
import slack.rtm.SlackRtmClient

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future

object Bot extends App {
  private val token = resources.Token.token
  //  var channel = "C017WCACXD5"
  var channels = ListBuffer[String]("C017WCACXD5")
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
      if (//Calendar.getInstance().get(Calendar.MINUTE) % 3 == 1 &&
        Calendar.getInstance().get(Calendar.SECOND) % 60 == 1) {
        for (channel <- channels) {
          client.sendMessage(channel, "OPSO MENU:")
          client.sendMessage(channel, OpsoParser.parse().toString) //tutaj channel będzie do zmiany tylko jeszcze nie wiem na jaki, możliwe, że trzeba będzie to rozwiązać przez jakieś zapytanie do bota, albo wywołanie go na jakimś konkretnym kanale, na razie do testów pozostaje tak jak jest
          client.sendMessage(channel, "OLIMP MENU:")
          client.sendMessage(channel, OlimpParser.parse().toString)
          logger.info(s"Sent menu, date: ${Calendar.getInstance().getTime}")
        }
      }
      Thread.sleep(1000)
    }
  }

  Future {
    while (true) {
      val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
      if (dayOfWeek == Calendar.TUESDAY || dayOfWeek == Calendar.THURSDAY || dayOfWeek == Calendar.FRIDAY) {
        if (Calendar.getInstance().get(Calendar.HOUR) == 10 && Calendar.getInstance().get(Calendar.MINUTE) == 0 && Calendar.getInstance().get(Calendar.SECOND) == 0) {
          for (channel <- channels) {

            client.sendMessage(channel, s"Jest ${intToDayOfWeek(dayOfWeek)}, dzień PIZZY. Dzisiejsze menu to:")
            client.sendMessage(channel, "OPSO MENU:")
            client.sendMessage(channel, OpsoParser.parse().toString) //tutaj channel będzie do zmiany tylko jeszcze nie wiem na jaki, możliwe, że trzeba będzie to rozwiązać przez jakieś zapytanie do bota, albo wywołanie go na jakimś konkretnym kanale, na razie do testów pozostaje tak jak jest
            client.sendMessage(channel, "OLIMP MENU:")
            client.sendMessage(channel, OlimpParser.parse().toString)
          }
        }
      }
      else if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.WEDNESDAY) {
        if (Calendar.getInstance().get(Calendar.HOUR) == 11 && Calendar.getInstance().get(Calendar.MINUTE) == 0 && Calendar.getInstance().get(Calendar.SECOND) == 0) {
          for (channel <- channels) {
            client.sendMessage(channel, s"Jest ${intToDayOfWeek(dayOfWeek)}. Dzisiejsze menu to:")
            client.sendMessage(channel, "OPSO MENU:")
            client.sendMessage(channel, OpsoParser.parse().toString) //tutaj channel będzie do zmiany tylko jeszcze nie wiem na jaki, możliwe, że trzeba będzie to rozwiązać przez jakieś zapytanie do bota, albo wywołanie go na jakimś konkretnym kanale, na razie do testów pozostaje tak jak jest
            client.sendMessage(channel, "OLIMP MENU:")
            client.sendMessage(channel, OlimpParser.parse().toString)
          }
        }
      }
      Thread.sleep(1000)
    }
  }

  def intToDayOfWeek(day: Int): String = {
    day match {
      case Calendar.MONDAY => "poniedziałek"
      case Calendar.TUESDAY => "wtorek"
      case Calendar.WEDNESDAY => "środa"
      case Calendar.TUESDAY => "czwartek"
      case Calendar.FRIDAY => "piątek"
      case _ => "INTERNAL ERROR"
    }
  }
}
