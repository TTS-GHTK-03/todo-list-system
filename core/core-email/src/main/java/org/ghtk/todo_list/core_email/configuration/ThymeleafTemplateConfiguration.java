package org.ghtk.todo_list.core_email.configuration;

import java.nio.charset.StandardCharsets;
import org.ghtk.todo_list.core_email.constant.BaseEmail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafTemplateConfiguration {
  @Bean
  public SpringTemplateEngine springTemplateEngine() {
    var templateEngine = new SpringTemplateEngine();
    templateEngine.addTemplateResolver(emailTemplateSolver());
    return templateEngine;
  }

  @Bean
  public ClassLoaderTemplateResolver emailTemplateSolver() {
    var emailTemplateSolver = new ClassLoaderTemplateResolver();
    emailTemplateSolver.setPrefix(BaseEmail.EMAIL_TEMPLATE_PREFIX);
    emailTemplateSolver.setSuffix(BaseEmail.EMAIL_TEMPLATE_SUFFIX);
    emailTemplateSolver.setTemplateMode(TemplateMode.HTML);
    emailTemplateSolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
    emailTemplateSolver.setCacheable(false);
    return emailTemplateSolver;
  }
}

