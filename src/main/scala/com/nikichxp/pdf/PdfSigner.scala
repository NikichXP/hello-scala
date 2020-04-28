package com.nikichxp.pdf

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.ActorMaterializer

class PdfSigner {

  def test(): Unit = {
    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher
    Http().singleRequest(HttpRequest(uri = "https://heroku.com"))
  }

  def signPdf(source: File, signature: String): Unit = {

  }

}

object Runner {
  def main(args: Array[String]): Unit = {
    new PdfSigner().signPdf(new File(""), signature = "qwerty")
  }
}