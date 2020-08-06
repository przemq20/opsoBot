import org.jsoup._
object OpsoParser extends App {
  val doc = Jsoup.connect("https://opso.pl/menu/").get()
  print(doc.body())
}
