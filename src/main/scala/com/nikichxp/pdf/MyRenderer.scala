package com.nikichxp.pdf

import java.io.{File, FileOutputStream}
import java.util.UUID

import com.nikichxp.db.CassandraConnFactory
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.xhtmlrenderer.pdf.ITextRenderer

class MyRenderer {

  private val templateResolver = new ClassLoaderTemplateResolver
  templateResolver.setSuffix(".html")
  templateResolver.setTemplateMode("HTML")

  private val templateEngine = new TemplateEngine
  templateEngine.addTemplateResolver(new ThymeleafDatabaseResolver(new DBTemplateProvider(CassandraConnFactory.connection)))
  templateEngine.addTemplateResolver(templateResolver)

  /**
   * @param key test is "db:test"
   */
  def render(key: String, params: Map[String, String]): File = {
    val context = new Context
    for ((k, v) <- params) context.setVariable(k, v)
    val html = templateEngine.process(key, context)
    renderInternal(html)
  }

  private def renderInternal(html: String): File = {
    val id = UUID.randomUUID().toString
    val file = new File(f"temp/${System.currentTimeMillis()}-${id.substring(0, 8)}.pdf")
    val outputStream = new FileOutputStream(file)
    val renderer = new ITextRenderer
    renderer.setDocumentFromString(html)
    renderer.layout()
    renderer.createPDF(outputStream)

    file
  }

}
