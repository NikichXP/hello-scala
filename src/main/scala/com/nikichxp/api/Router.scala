package com.nikichxp.api

import java.util.concurrent.Executors

import akka.actor.typed.scaladsl.AskPattern._
import akka.actor.typed.{ActorRef, ActorSystem}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.nikichxp.JsonFormats._
import com.nikichxp.UserRegistry._
import com.nikichxp.db.CassandraConnFactory
import com.nikichxp.pdf.{DBTemplateProvider, MyRenderer, PDFTest}
import com.nikichxp.{User, UserRegistry, Users}

import scala.concurrent.{ExecutionContext, Future}

class Router(userRegistry: ActorRef[UserRegistry.Command])(implicit val system: ActorSystem[_]) {

  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  implicit val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  private val signatureApi = new SignatureApi
  private val dbFooRouter = new DBFooRouter
  private val myRenderer = new MyRenderer
  private val testBean = PDFTest

  private val templateProvider = new DBTemplateProvider(CassandraConnFactory.connection)

  def getUsers: Future[Users] =
    userRegistry.ask(GetUsers)

  def getUser(name: String): Future[GetUserResponse] =
    userRegistry.ask(GetUser(name, _))

  def createUser(user: User): Future[ActionPerformed] =
    userRegistry.ask(CreateUser(user, _))

  def deleteUser(name: String): Future[ActionPerformed] =
    userRegistry.ask(DeleteUser(name, _))

  def test() = "Hello!"

  val routes: Route =
    concat(
      pathPrefix("sign") {
        signatureApi.getRoutes
      },
      pathPrefix("templates") {
        signatureApi.getRoutes
      },
      pathPrefix("key") {
        signatureApi.getRoutes
      },


      // test below
      pathPrefix("db") {
        dbFooRouter.getRoutes
      },
      pathPrefix("users") {
        concat(
          pathEnd {
            concat(
              get {
                complete(getUsers)
              },
              post {
                entity(as[User]) { user =>
                  onSuccess(createUser(user)) { performed =>
                    complete((StatusCodes.Created, performed))
                  }
                }
              })
          },
          path(Segment) { name =>
            concat(
              get {
                rejectEmptyResponse {
                  onSuccess(getUser(name)) { response =>
                    complete(response.maybeUser)
                  }
                }
              },
              delete {
                onSuccess(deleteUser(name)) { performed =>
                  complete((StatusCodes.OK, performed))
                }
              })
          })
      }
    )
}
