package com.nikichxp.service

import java.io.{File, FileInputStream}
import java.util.UUID

import com.mongodb.client.MongoClients
import com.nikichxp.util.DocUtils
import org.bson.Document
//import org.mongodb.scala.{Completed, Observer}

class KeystoreProvider {

  private val mongo = MongoClients.create("mongodb://localhost:27017").getDatabase("otp")

  /**
   * TODO Store passwords in some external storage
   */
  def insertKey(file: File, password: String): Unit = {
    val data = new Array[Byte](file.length().toInt)
    new FileInputStream(file).read(data)

    val obj = Map("data" -> data, "password" -> password)
    val document = new Document()

    for ((key, value) <- obj) document.append(key, value)

    DocUtils.toDocument(Key(data, password))

    mongo.getCollection("key")
      .insertOne(document)
    //      .insertOne(Key(_id = UUID.randomUUID().toString, data, password))
    //      .subscribe(new Observer[Completed] {
    //        override def onNext(result: Completed): Unit = {
    //
    //        }
    //
    //        override def onError(e: Throwable): Unit = {
    //
    //        }
    //
    //        override def onComplete(): Unit = {
    //
    //        }
    //      })
  }

}

case class Key(var data: Array[Byte], var pass: String)
