
import java.util.Calendar

import akka.actor.ActorSystem
import org.slf4j.LoggerFactory
import opsobot.{OpsoParser, randomJoke}
import slack.SlackUtil
import slack.rtm.SlackRtmClient

import scala.concurrent.Future

object Bot extends App {
  val token = "xoxb-1268418401399-1278662473782-DwtPVEHTmptqnTj86Bfdx2Rw"
  implicit val system: ActorSystem = ActorSystem("slack")
  val logger = LoggerFactory.getLogger(Bot.getClass)
  import system.dispatcher

  val client = SlackRtmClient(token)

  val future2 = Future {
    client.onMessage { message =>
      val mentionedIds = SlackUtil.extractMentionedIds(message.text)
      logger.info(s"Client ID: ${client.getState().self.id}")
      if (mentionedIds.contains(client.getState().self.id)) {
        if (message.text.contains("-pizza") || message.text.contains("-p")) {
          client.sendMessage(message.channel, "Tutaj kiedyś będzie menu z pizzą")
          logger.info("Sent pizza menu")
        }
        else if(message.text.contains("-joke")){
          client.sendMessage(message.channel, randomJoke.randomJoke())
          logger.info("Sent joke")
        }
        else {
          client.sendMessage(message.channel, s"<@${message.user}>: Hey!")
        }
      }
    }
  }

  val future1 = Future {
    while (true) {
      if (Calendar.getInstance().get(Calendar.SECOND) % 60 == 1) {
        client.sendMessage("C017WCACXD5", OpsoParser.parse().toString) //tutaj channel będzie do zmiany
        logger.info(s"Sent menu, date: ${Calendar.getInstance().getTime}")
      }
      Thread.sleep(1000)
    }
  }
}
