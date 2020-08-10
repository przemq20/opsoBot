package opsobot.bot

import java.time.temporal.ChronoUnit
import java.time.{DayOfWeek, LocalDate, LocalTime}
import java.util.Calendar

import akka.actor.ActorSystem
import opsobot.{OlimpParser, OpsoParser}
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
      val currentDayOfWeek = LocalDate.now.getDayOfWeek
      val currentTime = LocalTime.now.truncatedTo(ChronoUnit.SECONDS)
      val localizedDay = Locale.dayOfWeek(currentDayOfWeek)

      if (currentDayOfWeek == DayOfWeek.TUESDAY
        || currentDayOfWeek == DayOfWeek.THURSDAY
        || currentDayOfWeek == DayOfWeek.FRIDAY) {

        val tenOClock = LocalTime.of(10, 0, 0)
        if (currentTime == tenOClock) {
          for (channel <- channels) {
            client.sendMessage(channel, s"Witaj w $localizedDay! Dzisiaj możesz zamówić PIZZUNIĘ w OPSO. Ponadto, menu na dzisiaj to:")
            // client.sendMessage(channel, OpsoParser.parse().toString) //tutaj channel będzie do zmiany tylko jeszcze nie wiem na jaki, możliwe, że trzeba będzie to rozwiązać przez jakieś zapytanie do bota, albo wywołanie go na jakimś konkretnym kanale, na razie do testów pozostaje tak jak jest
            sendMenu(channel, "OPSO", OpsoParser.parse().toString)
            sendMenu(channel, "OLIMP", OlimpParser.parse().toString)
          }
        }
      } else if (currentDayOfWeek == DayOfWeek.MONDAY
        || currentDayOfWeek == DayOfWeek.WEDNESDAY) {
        val elevenOClock = LocalTime.of(11, 0, 0)
        if (currentTime == elevenOClock) {
          for (channel <- channels) {
            client.sendMessage(channel, s"Witaj w $localizedDay! Menu na dzisiaj to:")
            sendMenu(channel, "OPSO", OpsoParser.parse().toString)
            sendMenu(channel, "OLIMP", OlimpParser.parse().toString)
          }
        }
      }
      Thread.sleep(999)
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
