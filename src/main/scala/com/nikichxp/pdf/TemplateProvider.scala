package com.nikichxp.pdf

trait TemplateProvider {
  def get(key: String): String
  def add(key: String, template: String): Boolean
}
