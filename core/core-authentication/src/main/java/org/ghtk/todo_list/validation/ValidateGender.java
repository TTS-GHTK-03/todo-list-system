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
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.ghtk.todo_list.constant.Gender;

@Documented
@Constraint(validatedBy = ValidateGender.GenderValidation.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD})
public @interface ValidateGender {

  String message() default "Invalid gender!";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  @Slf4j
  class GenderValidation implements ConstraintValidator<ValidateGender, String> {

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext context) {
      log.info("(isValid) is called with gender {}", gender);
      if (gender == null) {
        return true;
      }
      return Arrays.stream(Gender.values())
          .anyMatch(genderValue -> genderValue.name().equalsIgnoreCase(gender));
    }
  }
}
