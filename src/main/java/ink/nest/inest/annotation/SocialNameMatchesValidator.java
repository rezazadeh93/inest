package ink.nest.inest.annotation;

import ink.nest.inest.service.SocialNameCrudService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SocialNameMatchesValidator implements ConstraintValidator<SocialNameMatches, String> {
    private final SocialNameCrudService socialNameCrudService;

    public SocialNameMatchesValidator(SocialNameCrudService socialNameCrudService) {
        this.socialNameCrudService = socialNameCrudService;
    }

    @Override
    public void initialize(SocialNameMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String value, ConstraintValidatorContext context) {
        return socialNameCrudService.getAll()
                .stream()
                .anyMatch(name -> name.equalsIgnoreCase(value));
    }
}
