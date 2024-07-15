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

@Constraint(validatedBy = ValidatePhone.PhoneValidation.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ValidatePhone {

  String message() default "Invalid phone number format!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  class PhoneValidation implements ConstraintValidator<ValidatePhone, String> {

    @Override
    public void initialize(ValidatePhone constraintAnnotation) {}

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
      if (phone == null || phone.trim().isEmpty()) {
        return true;
      }
      String regexPhone = "^\\+84\\d{9,10}$";
      return phone.matches(regexPhone);
    }
  }
}
