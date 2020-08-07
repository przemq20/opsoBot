
import java.util.Calendar

import akka.actor.ActorSystem
import opsobot.{OpsoParser, randomJoke}
import slack.SlackUtil
import slack.rtm.SlackRtmClient

import scala.concurrent.Future

object Bot extends App {
  val token = "xoxb-1268418401399-1278662473782-DwtPVEHTmptqnTj86Bfdx2Rw"
  implicit val system: ActorSystem = ActorSystem("slack")

  import system.dispatcher

  val client = SlackRtmClient(token)

  val future2 = Future {
    client.onMessage { message =>
      val mentionedIds = SlackUtil.extractMentionedIds(message.text)
      println(client.getState().self.id)
      if (mentionedIds.contains(client.getState().self.id)) {
        if (message.text.contains("-pizza") || message.text.contains("-p")) {
          client.sendMessage(message.channel, "Tutaj kiedyś będzie menu z pizzą")
        }
        else if(message.text.contains("-joke")){
          client.sendMessage(message.channel, randomJoke.randomJoke())
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
      }
      Thread.sleep(1000)
    }
  }
}
