package com.nikichxp.pdf

import java.io.File
import java.util.UUID

import com.nikichxp.db.CassandraConnFactory
import com.nikichxp.service.FileSignService

object PDFTest {

  val provider = new DBTemplateProvider(CassandraConnFactory.connection)
  val renderer = new MyRenderer
  val signer = new FileSignService

  val file = renderer.render("db:test", Map("name" -> "hello world", "test" -> "aaaa"))
  val target = new File(f"D:/keys/${System.currentTimeMillis()}.pdf")

  signer.signFileAlt(file, target)

}
