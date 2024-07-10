package org.ghtk.todo_list.core_email.helper.impl;

import jakarta.mail.Message;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.core_email.constant.BaseEmail;
import org.ghtk.todo_list.core_email.helper.EmailHelper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
public class EmailHelperImpl implements EmailHelper {
  private JavaMailSender emailSender;
  private SpringTemplateEngine templateEngine;

  public EmailHelperImpl(JavaMailSender emailSender, SpringTemplateEngine templateEngine) {
    this.emailSender = emailSender;
    this.templateEngine = templateEngine;
  }

  @Async
  @Override
  public void send(String subject, String to, String content) {
    log.info("(send)subject: {}, to: {}, content: {}", subject, to, content);
    try {
      var message = new SimpleMailMessage();
      message.setTo(to);
      message.setSubject(subject);
      message.setText(content);
      emailSender.send(message);
    } catch (Exception ex) {
      log.error("(send)to: {}, subject: {}, ex: {}", to, subject, ex.getMessage());
    }
  }

  @Async
  @Override
  public void send(String subject, String to, String template, Map<String, Object> properties) {
    log.info("(send)subject: {}, to: {}, template: {}, properties: {}", subject, to, template, properties
    );
    try {
      var message = emailSender.createMimeMessage();
      message.setRecipients(Message.RecipientType.TO, to);
      message.setSubject(subject);
      message.setContent(getContent(template, properties), BaseEmail.CONTENT_TYPE_TEXT_HTML);
      emailSender.send(message);
    } catch (Exception ex) {
      log.info("(send)subject: {}, to: {}, ex: {} ", subject, to, ex.getMessage());
    }
  }

  @Override
  public void send(String subject, String to, String content, String fileToAttach) {
    log.info("(send)subject: {}, to: {}, content: {}, fileToAttach: {}", subject, to, content, fileToAttach);
    try {
      var message = emailSender.createMimeMessage();
      var helper = new MimeMessageHelper(message, true);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setText(content);
      FileSystemResource fileSystemResource = new FileSystemResource(fileToAttach);
      helper.addAttachment(Objects.requireNonNull(fileSystemResource.getFilename()), fileSystemResource);
      emailSender.send(message);
    } catch (Exception ex) {
      log.info("(send)subject: {}, to: {}, ex: {} ", subject, to, ex.getMessage());
    }

  }

  private String getContent(String template, Map<String, Object> properties) {
    var context = new Context();
    context.setVariables(properties);
    return templateEngine.process(template, context);
  }
}



