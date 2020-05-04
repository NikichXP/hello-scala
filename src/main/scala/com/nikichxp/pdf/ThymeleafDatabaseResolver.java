package com.nikichxp.pdf;

import com.datastax.oss.driver.shaded.guava.common.collect.Sets;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.templateresolver.StringTemplateResolver;
import org.thymeleaf.templateresource.ITemplateResource;

import java.util.Map;

public class ThymeleafDatabaseResolver extends StringTemplateResolver {

    public ThymeleafDatabaseResolver() {
        setResolvablePatterns(Sets.newHashSet("db:*"));
    }

    @Override
    protected ITemplateResource computeTemplateResource(IEngineConfiguration configuration, String ownerTemplate, String template, Map<String, Object> templateResolutionAttributes) {

        String thymeleafTemplate = "<html lang=\"ru\">\n<body>\n<h1>Hello: generated template msg is [[${test}]]!</h1>\n</body>\n</html>";

        if (thymeleafTemplate != null) {
            return super.computeTemplateResource(configuration, ownerTemplate, thymeleafTemplate, templateResolutionAttributes);
        }
        return null;
    }
}