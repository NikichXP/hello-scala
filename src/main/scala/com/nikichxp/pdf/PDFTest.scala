package com.nikichxp.pdf

import java.io.File

import com.nikichxp.db.CassandraConnFactory
import com.nikichxp.service.SigningService

object PDFTest {

  val provider = new DBTemplateProvider(CassandraConnFactory.connection)
  val renderer = new MyRenderer
  val signer = new SigningService("D:/keys/keystore.p12", "changeit", "nikichxp")

  val file = renderer.render("db:test", Map("name" -> "hello world", "test" -> "aaaa"))
  val target = new File(f"D:/keys/${System.currentTimeMillis()}.pdf")

  signer.signPdf(file)

  println()
}
