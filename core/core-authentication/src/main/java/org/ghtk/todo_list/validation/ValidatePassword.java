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

@Documented
@Constraint(validatedBy = ValidatePassword.PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})

public @interface ValidatePassword {

  String message() default "Invalid password format!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class PasswordValidator implements ConstraintValidator<ValidatePassword, String> {
    @Override
    public void initialize(ValidatePassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
      if (password == null || password.isEmpty()) {
        return true;
      }
      String regexPassword = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[^a-zA-Z0-9]).{8,20}$";
      return password.matches(regexPassword);
    }
  }
}
