package nl.iprwc.constraints.validators;

import nl.iprwc.Utils.validation.ValidatorResult;
import nl.iprwc.Utils.validation.Validators;
import nl.iprwc.constraints.Validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidatorValidator implements ConstraintValidator<Validator, String> {
    private nl.iprwc.Utils.validation.Validator validator;

    @Override
    public void initialize(Validator constraintAnnotation) {
        validator = Validators.fromKey(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validator.test(value) == ValidatorResult.ok;
    }
}
