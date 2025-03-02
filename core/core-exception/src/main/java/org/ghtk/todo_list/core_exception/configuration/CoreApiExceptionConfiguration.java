package org.ghtk.todo_list.core_exception.configuration;

import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.core_exception.constant.ExceptionApiConstant;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
@ComponentScan(basePackages = {"org.ghtk.todo_list.core_exception"})
@Slf4j
public class CoreApiExceptionConfiguration {

  @Bean
  public GlobalExceptionHandler apiExceptionHandler(MessageSource messageSource) {
    return new GlobalExceptionHandler(messageSource);
  }

  @Bean
  public MessageSource messageSource(MessageResourcesProperties resource) {
    log.info("(messageSource)resource: {}", resource.getResources());
    var messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasenames(resource.getResources().toArray(String[]::new));
    messageSource.setDefaultEncoding(ExceptionApiConstant.UTF_8_ENCODING);
    return messageSource;
  }
}
