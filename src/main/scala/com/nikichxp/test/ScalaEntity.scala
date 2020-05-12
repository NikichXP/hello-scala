package com.nikichxp.test

import spray.json.DefaultJsonProtocol._
import spray.json.RootJsonFormat

case class ScalaEntity(var id: Int, text: String) {

  implicit val userJsonFormat: RootJsonFormat[ScalaEntity] =
    jsonFormat2(ScalaEntity.apply)

  def foo(): String = "asd"

}
