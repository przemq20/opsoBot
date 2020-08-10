package opsobot

import slack.rtm.SlackRtmClient

import scala.io.{BufferedSource, Source}
import spray.json._

object RandomJoke {

  def getRandomJoke: (JsValue, JsValue) ={
    val html: BufferedSource = Source.fromURL("https://official-joke-api.appspot.com/jokes/random")
    val joke: String = html.mkString
    val spray: JsValue = joke.map(_.toChar).mkString.parseJson
    val v1: JsValue = spray.asJsObject().getFields("setup").head
    val v2: JsValue = spray.asJsObject().getFields("punchline").head

    (v1, v2)
  }


  def randomJoke(): String = {
    val (v1,v2) = getRandomJoke
    val builder = new StringBuilder
    builder.addAll(v1.toString())
    builder.addAll("\n.\n.\n.\n")
    builder.addAll(v2.toString())
    builder.result()
  }

  def sendJoke(channel: String,client: SlackRtmClient): Unit ={
    val (v1,v2) = getRandomJoke
    client.sendMessage(channel, "Here's a joke for you:")
    Thread.sleep(1000)
    client.sendMessage(channel, v1.toString())
    Thread.sleep(2000)
    client.sendMessage(channel, ".")
    Thread.sleep(1000)
    client.sendMessage(channel, ".")
    Thread.sleep(1000)
    client.sendMessage(channel, ".")
    Thread.sleep(1000)
    client.sendMessage(channel, v2.toString())
  }
}
