package com.nikichxp.api

import java.io.File

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.FileIO
import com.nikichxp.db.CassandraConnFactory

import scala.concurrent.Future

class SignatureApi(implicit override val system: ActorSystem[_]) extends RouterParent {

  private val connection = CassandraConnFactory.connection

  /** /sign API. methods:
   * POST /key
   * GET  /key
   * GET  /key/{name}
   * POST /
   */

  def getRoutes: Route =
    concat(
      path("key") {
        concat(
          concat(
            get {
              complete("TODO list keys")
            },
            post {
              complete("Upload key")
            }
          ),
          path(Segment) { id =>
            complete(f"Get key with id $id")
          }
        )
      },
      path("/file") {
        pathEnd {
          get {
            complete("TODO upload file")
          }
        }
      }
    )


  def uploadFile(): Route = post {
    entity(as[Multipart.FormData]) { formData =>
      formData.parts
      val allPartsF: Future[Map[String, Any]] = formData.parts.mapAsync[(String, Any)](1) {
        case b: BodyPart =>
          b.entity.contentType.toString() match {
            case "text/plain" =>
              println(s"text ${b.getName()} ${b.entity.toString}")
              Future("", "")
            case s: String if (s.startsWith("application")) =>
              println(s"application ${b.getName()} ${b.entity.toString}")
              Future("", "")
            case _ =>
              println(b.getName())
              val file = File.createTempFile("upload", "tmp")
              b.entity.dataBytes.runWith(FileIO.toPath(file.toPath)).map(_ =>
                (b.name -> file))
          }
        case other =>
          Future("title", other)
      }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)
      complete {
        "TODO"
      }
    }
  }
}
