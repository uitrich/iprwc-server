package nl.iprwc.core;

import nl.iprwc.model.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class AccountSecurityContext implements SecurityContext {
    private User user;
    private String scheme;

    public AccountSecurityContext(User user, String scheme)
    {
        this.user = user;
        this.scheme = scheme;
    }

    @Override
    public Principal getUserPrincipal() {
        return user;
    }

    @Override
    public boolean isUserInRole(String role) {
        return user.hasGroupReference(role);
    }

    @Override
    public boolean isSecure() {
        return "https".equals(scheme);
    }

    @Override
    public String getAuthenticationScheme() {
        return "CUSTOM";
    }
}
