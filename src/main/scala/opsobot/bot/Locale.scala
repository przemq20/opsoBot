package opsobot.bot

import java.time.DayOfWeek

object Locale {
  def dayOfWeek(dow: DayOfWeek): String = {
    dow match {
      case DayOfWeek.MONDAY => "poniedziałek"
      case DayOfWeek.TUESDAY => "wtorek"
      case DayOfWeek.WEDNESDAY => "środa"
      case DayOfWeek.THURSDAY => "czwartek"
      case DayOfWeek.FRIDAY => "piątek"
      case DayOfWeek.SATURDAY => "sobota"
      case DayOfWeek.SUNDAY => "niedziela"
    }
  }
}
