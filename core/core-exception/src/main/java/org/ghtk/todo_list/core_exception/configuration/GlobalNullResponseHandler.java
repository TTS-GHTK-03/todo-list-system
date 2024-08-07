package org.ghtk.todo_list.core_exception.configuration;

import java.lang.reflect.Field;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GlobalNullResponseHandler implements ResponseBodyAdvice<Object> {

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
    convertNullsToEmptyStrings(body);
    return body;
  }

  private void convertNullsToEmptyStrings(Object obj) {
    if (obj == null) {
      return;
    }

    Class<?> currentClass = obj.getClass();
    if (currentClass.getName().startsWith("java.") || currentClass.getName().startsWith("javax.")) {
      return;
    }

    while (currentClass != null) {
      try {
        for (Field field : currentClass.getDeclaredFields()) {
          field.setAccessible(true);
          Object fieldValue = field.get(obj);
          if (fieldValue == null && field.getType().equals(String.class)) {
            field.set(obj, "");
          } else if (fieldValue != null && !field.getType().isPrimitive()) {
            convertNullsToEmptyStrings(fieldValue);
          }
        }
      } catch (Exception e) {
        return;
      }
      currentClass = currentClass.getSuperclass();
    }
  }
}
