package com.nikichxp.pdf

import java.io.File

import com.nikichxp.db.CassandraConnFactory
import com.nikichxp.service.{KeystoreProvider, SigningService}

object PDFTest {

  val keystoreProvider = new KeystoreProvider

  keystoreProvider.insertKey(new File("D:/keys/keystore.p12"), "changeit")

  val provider = new DBTemplateProvider(CassandraConnFactory.connection)
  val renderer = new MyRenderer
  val signer = new SigningService("D:/keys/keystore.p12", "changeit", "nikichxp")

  val file = renderer.render("db:test", Map("name" -> "hello world", "test" -> "aaaa"))

  signer.signPdf(file)

  println()
}
