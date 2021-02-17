package nl.iprwc.constraints.validators;

import nl.iprwc.utils.validation.ValidatorResult;
import nl.iprwc.utils.validation.Validators;
import nl.iprwc.constraints.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatorValidator implements ConstraintValidator<Validator, String> {
    private nl.iprwc.utils.validation.Validator validator;

    @Override
    public void initialize(Validator constraintAnnotation) {
        validator = Validators.fromKey(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validator.test(value) == ValidatorResult.ok;
    }
}
