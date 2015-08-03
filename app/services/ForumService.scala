package services

import javax.inject.{ Inject, Singleton }

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

import dao.ForumDAO
import exceptions.ApiExceptions.{ dbException, notFoundException }
import exceptions.QueryException
import models.{ Forum, User }

@Singleton
class ForumService @Inject() (forumDAO: ForumDAO) {

  def getForum(id: Int): Future[Option[Forum]] = forumDAO ?? id

  def getForumsByCategory: Future[Map[Int, Seq[Forum]]] = forumDAO >> { _.groupBy { _.category } }

  def getForumForApi(id: Int): Future[Forum] = getForum(id) map {
    case None        => notFoundException
    case Some(forum) => forum
  } recover {
    case e: QueryException => dbException(e)
  }

  def getForumsByCategoryForApi = getForumsByCategory recover {
    case e: QueryException => dbException(e)
  }

}

