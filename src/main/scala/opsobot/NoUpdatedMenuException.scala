package opsobot

final case class NoUpdatedMenuException(private val message: String = "",
                                        private val cause: Throwable = None.orNull)
  extends Exception(message, cause)