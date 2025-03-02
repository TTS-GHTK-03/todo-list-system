package org.ghtk.todo_list.core_exception.configuration;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.core_exception.dto.ErrorResponse;
import org.ghtk.todo_list.core_exception.dto.MessageResponse;
import org.ghtk.todo_list.core_exception.exception.BaseException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final MessageSource messageSource;

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, WebRequest webRequest) {
    log.info("(handleBaseException)ex: {}, locale: {}", ex.getCode(), webRequest.getLocale());
    MessageResponse messageResponse = getError(ex.getCode(), webRequest.getLocale(), ex.getParams());
    HttpStatus status = HttpStatus.valueOf(ex.getStatus());
    String statusCodeMessage = getStatusCodeMessage(status);
    ErrorResponse errorResponse = ErrorResponse.of(statusCodeMessage, Instant.now().getEpochSecond(), messageResponse);
    return new ResponseEntity<>(errorResponse, status);
  }

  private MessageResponse getError(String code, Locale locale, Map<String, String> params) {
    String message = getMessage(code, locale, params);
    return MessageResponse.of(code, message);
  }

  private String getMessage(String code, Locale locale, Map<String, String> params) {
    log.info("country is " + locale.getCountry());
    String message = getMessage(code, locale);
    if (params != null && !params.isEmpty()) {
      for (Map.Entry<String, String> param : params.entrySet()) {
        log.info("param is: " + param);
        message = message.replace(getMessageByParamKey(param.getKey()), param.getValue());
      }
    }
    return message;
  }

  private String getMessage(String code, Locale locale) {
    try {
      return messageSource.getMessage(code, null, locale);
    } catch (Exception e) {
      log.info("exception is: " + e);
      return code;
    }
  }

  private String getMessageByParamKey(String key) {
    return "%" + key + "%";
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
    log.info("(handleValidationExceptions)exception: {}", exception.getMessage());
    Map<String, String> errors = new HashMap<>();
    exception.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    log.info("(handleValidationExceptions) {}", errors);
    MessageResponse messageResponse = MessageResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), Instant.now().getEpochSecond(), messageResponse);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException exception) {
    log.info("(handleConstraintViolationException)exception: {}", exception.getMessage());
    Map<String, String> errors = new HashMap<>();
    for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
      String fieldName = constraintViolation.getPropertyPath().toString();
      String errorMessage = constraintViolation.getMessage();
      errors.put(fieldName, errorMessage);
    }
    MessageResponse messageResponse = MessageResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
    ErrorResponse errorResponse = ErrorResponse.of(HttpStatus.BAD_REQUEST.getReasonPhrase(), Instant.now().getEpochSecond(), messageResponse);
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  private String getStatusCodeMessage(HttpStatus status) {
    return status.getReasonPhrase().toLowerCase().replace(" ", "_");
  }
}
