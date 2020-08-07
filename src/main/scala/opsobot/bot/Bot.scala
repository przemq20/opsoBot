package opsobot.bot

import java.util.Calendar

import akka.actor.ActorSystem
import opsobot.{OpsoParser, randomJoke}
import org.slf4j.LoggerFactory
import slack.SlackUtil
import slack.rtm.SlackRtmClient

import scala.concurrent.Future

object Bot extends App {
  private val token = resources.Token.token
  var channel = "C017WCACXD5"
  implicit val system: ActorSystem = ActorSystem("slack")
  val logger = LoggerFactory.getLogger(Bot.getClass)

  import system.dispatcher

  val client = SlackRtmClient(token)

  val future2 = Future {
    client.onMessage { message =>
      val mentionedIds = SlackUtil.extractMentionedIds(message.text)
      logger.info(s"Client ID: ${client.getState().self.id}")
      if (mentionedIds.contains(client.getState().self.id)) {
        CommandParser.greetings(message, client)
        val commands = message.text.split(" ").distinct

        logger.info(s"I received commands: ${commands.mkString("Array(", ", ", ")")}")

        for (command <- commands){
          CommandParser.parse(command, message, client)
        }
      }
    }
  }

  val future1 = Future {
    while (true) {
      if (Calendar.getInstance().get(Calendar.SECOND) % 60 == 1) {
        client.sendMessage(channel, OpsoParser.parse().toString) //tutaj channel będzie do zmiany tylko jeszcze nie wiem na jaki, możliwe, że trzeba będzie to rozwiązać przez jakieś zapytanie do bota, albo wywołanie go na jakimś konkretnym kanale, na razie do testów pozostaje tak jak jest
        logger.info(s"Sent menu, date: ${Calendar.getInstance().getTime}")
      }
      Thread.sleep(1000)
    }
  }
}
