
import java.util.Calendar

import akka.actor.ActorSystem
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
        client.sendMessage(message.channel, s"<@${message.user}>: Hey!")
      }
    }
  }

  val future1 = Future {
    while (true) {
      if (Calendar.getInstance().get(Calendar.SECOND) % 10 == 1) {
        client.sendMessage("C017WCACXD5", "") //tutaj channel będzie do zmiany
      }
      Thread.sleep(1000)
    }
  }
}
