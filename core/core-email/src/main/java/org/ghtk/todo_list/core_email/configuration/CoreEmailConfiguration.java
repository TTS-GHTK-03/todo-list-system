package org.ghtk.todo_list.core_email.configuration;

import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.ghtk.todo_list.core_email.helper.impl.EmailHelperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Configuration
public class CoreEmailConfiguration {

  @Bean
  public EmailHelper emailService(JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
    return new EmailHelperImpl(emailSender, templateEngine);
  }

}
