package org.ghtk.todo_list.validation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Documented
@Constraint(validatedBy = ValidateLocalDate.LocalDateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ValidateLocalDate {
  String message() default "Date must be in the format yyyy-MM-dd and a valid date";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class LocalDateValidator implements ConstraintValidator<ValidateLocalDate, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      if (s == null || s.trim().isEmpty()) {
        return true;
      }
      try {
        LocalDate.parse(s, formatter);
      } catch (DateTimeParseException ex) {
        return false;
      }
      return true;
    }
  }
}
