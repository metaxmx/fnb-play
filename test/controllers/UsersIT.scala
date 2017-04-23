package controllers

import scala.concurrent._
import duration._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._
import java.util.concurrent.TimeUnit

import org.scalatestplus.play.PlaySpec


/**
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class UsersIT extends PlaySpec {

  val timeout: FiniteDuration = FiniteDuration(5, TimeUnit.SECONDS)

//  "Users" should {
//
//    "insert a valid json" in {
//      val app = FakeApplication()
//      running(app) {
//        val request = FakeRequest.apply(POST, "/user").withJsonBody(Json.obj(
//          "firstName" -> "Jack",
//          "lastName" -> "London",
//          "age" -> 27,
//          "active" -> true))
//        val response = route(app, request)
//        response.isDefined mustEqual true
//        val result = Await.result(response.get, timeout)
//        result.header.status must equalTo(CREATED)
//      }
//    }
//
//    "fail inserting a non valid json" in {
//      val app = FakeApplication()
//      running(app) {
//        val request = FakeRequest.apply(POST, "/user").withJsonBody(Json.obj(
//          "firstName" -> 98,
//          "lastName" -> "London",
//          "age" -> 27))
//        val response = route(app, request)
//        response.isDefined mustEqual true
//        val result = Await.result(response.get, timeout)
//        contentAsString(response.get) mustEqual "invalid json"
//        result.header.status mustEqual BAD_REQUEST
//      }
//    }
//
//    "update a valid json" in {
//      val app = FakeApplication()
//      running(app) {
//        val request = FakeRequest.apply(PUT, "/user/Jack/London").withJsonBody(Json.obj(
//          "firstName" -> "Jack",
//          "lastName" -> "London",
//          "age" -> 27,
//          "active" -> true))
//        val response = route(app, request)
//        response.isDefined mustEqual true
//        val result = Await.result(response.get, timeout)
//        result.header.status must equalTo(CREATED)
//      }
//    }
//
//    "fail updating a non valid json" in {
//      val app = FakeApplication()
//      running(app) {
//        val request = FakeRequest.apply(PUT, "/user/Jack/London").withJsonBody(Json.obj(
//          "firstName" -> "Jack",
//          "lastName" -> "London",
//          "age" -> 27))
//        val response = route(app, request)
//        response.isDefined mustEqual true
//        val result = Await.result(response.get, timeout)
//        contentAsString(response.get) mustEqual "invalid json"
//        result.header.status mustEqual BAD_REQUEST
//      }
//    }
//
//  }
}
