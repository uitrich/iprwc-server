package nl.iprwc.constraints;

import nl.iprwc.constraints.validators.AuthenticatedValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = AuthenticatedValidator.class)
@Target({ PARAMETER })
@Retention(RUNTIME)
public @interface Authenticated {
    String message() default "{nl.plnt.utils.validation.Authenticated.message}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
