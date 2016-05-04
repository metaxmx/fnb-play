package rest

import org.json4s.Extraction._
import org.json4s.MappingException
import play.api.Logger
import play.api.http.Status._
import play.api.mvc.Results.Status
import play.api.mvc.{Request, RequestHeader, Result}
import rest.Implicits._

import scala.concurrent.Future
import scala.util.control.NonFatal

/**
  * REST Exceptions.
  * Created by Christian on 30.04.2016.
  */
object Exceptions {

  case class ApiErrorMessage(error: String)

  /**
    * Default Rest Exception, indicating any error when calling the requested
    *
    * @param msg   error message
    * @param cause cause
    */
  abstract class RestException(msg: String,
                               cause: Throwable = null,
                               statusCode: Option[Int] = None,
                               clientMessage: Option[String] = None,
                               reportError: Boolean = false
                              )(implicit req: RequestHeader) extends Exception(msg, cause) {

    def toResult: Result = {
      val logFun: (=> String, => Throwable) => Unit = if (reportError) Logger.error else Logger.debug
      val logMsg = s"Request Error ${req.method} to ${req.path}: $msg"
      logFun(logMsg, cause)
      req match {
        case request: Request[_] =>
          Logger.info(s"Request Body: \n${request.body.toString take 100}")
        case _ =>
      }

      val responseEntity = decompose(ApiErrorMessage(clientMessage getOrElse msg))
      Status(statusCode getOrElse OK).apply(responseEntity)
    }

  }

  object RestException {

    def apply(exc: Throwable)(implicit req: RequestHeader): RestException = exc match {
      case re: RestException => re
      case other => InternalServerException(other.getMessage, other)
    }

    def errorHandler()(implicit req: RequestHeader): PartialFunction[Throwable, Result] = {
      case NonFatal(exc) => RestException(exc).toResult
    }

    def errorHandlerAsync()(implicit req: RequestHeader): PartialFunction[Throwable, Future[Result]] = {
      case NonFatal(exc) => Future.successful(RestException(exc).toResult)
    }

  }

  case class InternalServerException(msg: String, cause: Throwable)(implicit req: RequestHeader) extends RestException(msg, cause = cause,
    statusCode = Some(INTERNAL_SERVER_ERROR), clientMessage = Some("An unexpected error occurred during this request."), reportError = true)

  case class BadRequestException(cause: Throwable)(implicit req: RequestHeader) extends RestException("Error parsing request", cause = cause,
    statusCode = Some(BAD_REQUEST), clientMessage = Some("Request could not be parsed"))

  case class JsonParseException()(implicit req: RequestHeader) extends RestException("Error parsing JSON from request",
    statusCode = Some(BAD_REQUEST), clientMessage = Some("JSON from Request could not be parsed"))

  case class JsonExtractException(cause: MappingException)(implicit req: RequestHeader) extends RestException(
    "Error extracting JSON from request", cause = cause,
    statusCode = Some(BAD_REQUEST), clientMessage = Some("JSON from Request could not be extracted: " + cause.msg))

  case class InvalidSessionException(sessionId: String)(implicit req: RequestHeader) extends RestException("Invalid Session",
    statusCode = Some(BAD_REQUEST), clientMessage = Some(s"Invalid session id: $sessionId"))

  case class InvalidSessionUserException(sessionId: String)(implicit req: RequestHeader) extends RestException(s"User for session $sessionId not found",
    statusCode = Some(INTERNAL_SERVER_ERROR), clientMessage = Some(s"Invalid user for session id: $sessionId"), reportError = true)

  case class ForbiddenException()(implicit req: RequestHeader) extends RestException("Permission not granted",
    statusCode = Some(FORBIDDEN), clientMessage = Some("You do not have the required permissions for this action"))

  case class NotFoundException(msg: String)(implicit req: RequestHeader) extends RestException(msg,
    statusCode = Some(NOT_FOUND))

  case class UnsupportedMediaTypeException(requiredType: String)(implicit req: RequestHeader) extends RestException(
    "Unsupported Media Type " + req.contentType.getOrElse("(undefined)") + s": type $requiredType required",
    statusCode = Some(UNSUPPORTED_MEDIA_TYPE), clientMessage = Some(s"Media Type $requiredType required"))

}