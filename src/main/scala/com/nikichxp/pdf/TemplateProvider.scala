package com.nikichxp.pdf

import com.datastax.oss.driver.api.core.CqlSession

trait TemplateProvider {
  def get(key: String): String
}

class DBTemplateProvider(private val db: CqlSession) extends TemplateProvider {
  override def get(key: String): String = {
    
    ""
  }
}
