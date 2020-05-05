package com.nikichxp.pdf

import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}
import java.util.UUID

import com.nikichxp.db.CassandraConnFactory
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import org.xhtmlrenderer.pdf.ITextRenderer

class MyRenderer {

  private val testText = "<html lang=\"ru\">\n<body>\n<h1>Hello: generated template msg is [[${test}]]!</h1>\n</body>\n</html>"

  val id = UUID.randomUUID().toString
  val testStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f"src/main/resources/templates/$id.pdf")))
  testStream.write(testText)
  testStream.close()

  val templateResolver = new ClassLoaderTemplateResolver
  templateResolver.setSuffix(".html")
  templateResolver.setTemplateMode("HTML")


  val templateEngine = new TemplateEngine
  templateEngine.addTemplateResolver(new ThymeleafDatabaseResolver(new DBTemplateProvider(CassandraConnFactory.connection)))
  //    templateEngine.setTemplateResolver(templateResolver)

  val context = new Context()
  context.setVariable("test", "hello world")

  // Get the plain HTML with the resolved ${name} variable!
  val html = templateEngine.process(f"db:$id", context)

  val outputStream = new FileOutputStream(f"temp/$id.pdf")
  val renderer = new ITextRenderer
  renderer.setDocumentFromString(html)
  renderer.layout()
  renderer.createPDF(outputStream)


}
