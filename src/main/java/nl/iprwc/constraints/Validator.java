package nl.iprwc.constraints;

import nl.iprwc.constraints.validators.ValidatorValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = ValidatorValidator.class)
@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface Validator {
    String message() default "{nl.plnt.utils.validation.Validator.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };

    String value();
}
