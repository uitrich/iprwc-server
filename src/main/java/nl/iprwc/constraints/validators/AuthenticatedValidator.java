package nl.iprwc.constraints.validators;

import nl.iprwc.constraints.Authenticated;
import nl.iprwc.model.User;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.sql.SQLException;

public class AuthenticatedValidator implements ConstraintValidator<Authenticated, User> {
    @Override
    public void initialize(Authenticated authenticated) {

    }

    @Override
    public boolean isValid(User principal, ConstraintValidatorContext context) {
        try {
            return principal != null && principal.isAuthenticated();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
