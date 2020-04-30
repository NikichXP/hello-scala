package com.nikichxp.api

import java.util.UUID

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives.{as, complete, entity, onSuccess, post}
import akka.http.scaladsl.server.Route
import com.nikichxp.db.CassandraConnFactory
import com.nikichxp.service.FileSignService
import spray.json.DefaultJsonProtocol

class DBFooRouter(implicit override val system: ActorSystem[_]) extends RouterParent {

  private val connection = CassandraConnFactory.connection
  private val fileSignService = new FileSignService()

  def getRoutes(): Route = post {
    entity(as[String]) { person =>
      person
      complete(connection.getName)
    }
  }

}

case class Person(id: String = UUID.randomUUID().toString, name: String, age: Int)
object PersonJsonSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val PortofolioFormats = jsonFormat3(Person)
}
