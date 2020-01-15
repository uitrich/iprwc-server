package nl.iprwc.core;

import nl.iprwc.controller.SessionController;
import nl.iprwc.controller.SuperController;
import nl.iprwc.controller.UserController;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Session;
import nl.iprwc.model.User;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
@Priority(Priorities.AUTHORIZATION)
public class AccountAuthorizationFilter implements ContainerRequestFilter {
    private SessionController sessionController;
    private UserController userController;

    public AccountAuthorizationFilter()
    {
        sessionController = SuperController.getInstance().getSessionController();
        userController = SuperController.getInstance().getUserController();
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getCookies().containsKey("session-key")) {
            String sessionKey = requestContext.getCookies().get("session-key").getValue();

            try {
                Session session = sessionController.fromSessionKey(sessionKey);

                if (session.isExpired()) {
                    sessionController.delete(session);
                }

                sessionController.updateLastActivity(session);

                User user = userController.fromAccount(session.getAccount());
                String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
                SecurityContext securityContext = new AccountSecurityContext(user, scheme);
                requestContext.setSecurityContext(securityContext);
                return;
            }
            catch (NotFoundException e) {
                // do nothing
            }
        }

        User user = userController.getAnonymousUser();
        String scheme = requestContext.getUriInfo().getRequestUri().getScheme();
        SecurityContext securityContext = new AccountSecurityContext(user, scheme);
        requestContext.setSecurityContext(securityContext);
    }
}
