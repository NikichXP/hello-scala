package com.nikichxp.util

import org.bson.Document

object DocUtils {

  val registeredEntities = "com.nikichxp" // TODO Make something like `this::class.getCurrentPackage.substringBeforeSecond('.')`

  def toDocument(obj: Any): Document = {
    val fields = obj.getClass.getDeclaredFields
    val methods = obj.getClass.getDeclaredMethods

    val document = new Document()

    fields
      .foreach(field => methods.find(method => method.getName == field.getName) match {
        case Some(value) =>
          val result = value.invoke(obj)
          if (value.getReturnType.getCanonicalName.startsWith(registeredEntities)) {
            document.put(field.getName, toDocument(result))
          } else {
            document.put(field.getName, result)
          }
      })
    document
  }

}
