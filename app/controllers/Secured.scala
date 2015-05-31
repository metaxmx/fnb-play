package controllers

import play.api.mvc._
import play.api.mvc.Results._
import models.User
import javax.inject.Inject
import scala.concurrent.Future
import play.Logger
import scala.concurrent.ExecutionContext
import models.FnbSession
import play.modules.reactivemongo.ReactiveMongoPlugin.db
import play.api.Play.current
import scala.concurrent.ExecutionContext.Implicits.global
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

trait Secured {

  def userCollection: JSONCollection = db.collection[JSONCollection]("users")

  def sessionCollection: JSONCollection = db.collection[JSONCollection]("sessions")

  def parseSessionKey(request: RequestHeader) = request.headers.get("x-fnb-session")

  def onMissingSession(request: RequestHeader) = {
    Logger.warn("Returning API request without session header")
    BadRequest("unauthorized")
  }

  def onSessionLoadingError[A](request: Request[A]) = {
    Logger.error("Error reading Session from DB")
    InternalServerError("session not found")
  }

  def withSessionKeyAsync[A](block: => String => Request[A] => Future[Result]): Request[A] => Future[Result] =
    request => parseSessionKey(request) match {
      case None          => Future.successful(onMissingSession(request))
      case Some(session) => block(session)(request)
    }

  def withSessionKey[A](block: => String => Request[A] => Result): Request[A] => Result =
    request => parseSessionKey(request) match {
      case None          => onMissingSession(request)
      case Some(session) => block(session)(request)
    }

  def hasUser(session: FnbSession) = session.user_id match {
    case null => None
    case "" => None
    case s => Some(s)
  }
    
  def withSession[A](block: => Tuple2[FnbSession, Option[User]] => Request[A] => Future[Result]): Request[A] => Future[Result] =
    request =>
      parseSessionKey(request) match {
        case None => Future.successful(onMissingSession(request))
        case Some(sessionKey) =>
          sessionCollection.find(Json.obj("sessionkey" -> sessionKey)).one[FnbSession].flatMap {
            sessionOpt =>
              sessionOpt match {
                case None => Future.successful(None)
                case Some(session) => hasUser(session) match {
                  case None => Future.successful(Some(Tuple2(session, None)))
                  case Some(userId) => userCollection.find(Json.obj("id_" -> session.user_id)).one[User].map {
                    userOpt =>
                      userOpt match {
                        case None       => None
                        case Some(user) => Some(Tuple2(session, Some(user)))
                      }
                  }
                }
              }
          }.flatMap {
            opt =>
              opt match {
                case None        => Future.successful(onSessionLoadingError(request))
                case Some(tuple) => block(tuple)(request)
              }
          }
      }

}