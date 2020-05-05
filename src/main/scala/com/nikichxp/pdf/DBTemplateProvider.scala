package com.nikichxp.pdf

import com.datastax.oss.driver.api.core.CqlSession

class DBTemplateProvider(private val db: CqlSession) extends TemplateProvider {

  override def get(key: String): String = {
    val statement = db.prepare("select template from test.templates where id = :id")
    val bind = statement.bind()
      .setString("id", key)
    val result = db.execute(bind).one()
    if (result == null) {
      null
    } else {
      result.getString("template")
    }
  }

  override def add(key: String, template: String): Boolean = {
    val statement = db.prepare("insert into test.templates (id, template) values (:id, :template)")
    val bind = statement.bind()
      .setString("id", key)
      .setString("template", template)
    db.execute(bind).wasApplied()
  }
}
