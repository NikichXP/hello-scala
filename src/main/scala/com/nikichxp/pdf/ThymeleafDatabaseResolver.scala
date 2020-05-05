package com.nikichxp.pdf

import com.datastax.oss.driver.shaded.guava.common.collect.Sets
import org.thymeleaf.IEngineConfiguration
import org.thymeleaf.templateresolver.StringTemplateResolver
import org.thymeleaf.templateresource.ITemplateResource
import java.util

class ThymeleafDatabaseResolver(private val provider: TemplateProvider) extends StringTemplateResolver {

  setResolvablePatterns(Sets.newHashSet("db:*"))

  override protected def computeTemplateResource(configuration: IEngineConfiguration, ownerTemplate: String, template: String, templateResolutionAttributes: util.Map[String, AnyRef]): ITemplateResource = {
    val thymeleafTemplate = "<html lang=\"ru\">\n<body>\n<h1>Hello: generated template msg is [[${test}]]!</h1>\n</body>\n</html>"
    return if (thymeleafTemplate != null)
      super.computeTemplateResource(configuration, ownerTemplate, thymeleafTemplate, templateResolutionAttributes)
    else
      null
  }
}