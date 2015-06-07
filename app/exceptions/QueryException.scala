package exceptions

class QueryException(message: String, cause: Throwable) extends Exception(message, cause) {

  def this(cause: Throwable) = this(null, cause)

  def this(message: String) = this(message, null)

  def this() = this(null, null)

}