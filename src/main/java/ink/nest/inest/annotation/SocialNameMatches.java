package ink.nest.inest.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({FIELD,ANNOTATION_TYPE})
@Constraint(validatedBy = SocialNameMatchesValidator.class)
public @interface SocialNameMatches {
    String message() default "Name doesn't exist in SocialName Table";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
