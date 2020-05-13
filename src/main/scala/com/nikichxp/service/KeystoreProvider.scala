package com.nikichxp.service

import java.io.{File, FileInputStream}

import com.mongodb.client.MongoClients
import com.nikichxp.model.SigningKey
import org.bson.Document
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._

class KeystoreProvider {

  private val mongo = MongoClients.create("mongodb://localhost:27017").getDatabase("otp")
  private val keyCollection = mongo.getCollection("key")

  /**
   * TODO Store passwords in some external storage
   */
  def insertKey(file: File, alias: String, password: String): Unit = {
    val data = new Array[Byte](file.length().toInt)
    new FileInputStream(file).read(data)
    val key = SigningKey(data, alias, password)

    val json = key.asJson.noSpaces
    val document = Document.parse(json)

    keyCollection.insertOne(document)
  }

  def getByAlias(alias: String): SigningKey = {
    val search = keyCollection.find(new Document().append("alias", alias))
    val document = search.cursor().tryNext()
    if (document == null) {
      return null
    }
    decode[SigningKey](document.toJson()) match {
      case Right(key) => key
      case Left(err) =>
        err.fillInStackTrace().printStackTrace()
        null
    }
  }

}
