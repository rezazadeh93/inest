package ink.nest.inest.annotation;

import ink.nest.inest.api.v1.request.RegisterRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
    public void initialize(PasswordMatches constraint) {
    }

    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        RegisterRequest user = (RegisterRequest) obj;
        return Objects.equals(user.getPassword(), user.getConfirmPassword());
    }
}
