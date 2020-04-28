package com.nikichxp.api

import java.io.File
import java.util.concurrent.Executors

import akka.actor.typed.ActorSystem
import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.model.Multipart.BodyPart
import akka.http.scaladsl.server.Directives.{as, complete, entity, onSuccess, post}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.FileIO
import akka.util.Timeout

import scala.concurrent.{ExecutionContext, Future}

class FileRouter(implicit val system: ActorSystem[_]) {

  private implicit val timeout = Timeout.create(system.settings.config.getDuration("my-app.routes.ask-timeout"))
  private implicit val ec = ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  def getRoutes(): Route = post {
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

      val done = allPartsF.map { allParts =>
        println(allParts)
        //                  db.create(Video(
        //                    file = allParts("file").asInstanceOf[File],
        //                    title = allParts("title").asInstanceOf[String],
        //                    author = allParts("author").asInstanceOf[String]))
      }

      // when processing have finished create a response for the user
      onSuccess(allPartsF) { allParts =>
        complete {
          "ok!"
        }
      }
    }
  }

}
