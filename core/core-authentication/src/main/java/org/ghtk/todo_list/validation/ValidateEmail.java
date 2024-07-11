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

@Constraint(validatedBy = ValidateEmail.EmailValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ValidateEmail {

  String message() default "Invalid email format!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class EmailValidation implements ConstraintValidator<ValidateEmail, String> {

    @Override
    public void initialize(ValidateEmail constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
      if (email == null || email.isEmpty()) {
        return true;
      }
      String regexEmail = "^[A-Za-z0-9._%+-]+@gmail\\.com$";
      return email.matches(regexEmail);
    }
  }
}
