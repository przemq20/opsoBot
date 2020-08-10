package opsobot

final case class NoUpdatedMenuException(private val message: String = "Menu is unavailable",
                                        private val cause: Throwable = None.orNull)
  extends Exception(message, cause)