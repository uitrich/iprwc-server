package nl.iprwc.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import nl.iprwc.controller.AuthenticationController;
import nl.iprwc.controller.SuperController;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Authentication;
import nl.iprwc.model.Session;
import nl.iprwc.Response.SessionStateResponse;
import nl.iprwc.Utils.KillCookie;
import nl.iprwc.Response.SuccessResponse;
import nl.iprwc.Utils.SetCookie;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;
import java.io.IOException;

@Path("/api/authentication")
public class AuthenticationResource
{
    private static final String COOKIE_KEY = "session-key";

    private final AuthenticationController controller;

    public AuthenticationResource()
    {
        controller = SuperController.getInstance().getAuthenticationController();
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(String auth) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Authentication authentication = objectMapper.readValue(auth, Authentication.class);
        Session session = controller.logIn(authentication);
        if (session == null) {
            throw new WebApplicationException(org.eclipse.jetty.server.Response.SC_UNAUTHORIZED);
        }

        return Response
                .ok()
                .entity(new SessionStateResponse(session))
                .cookie(new SetCookie(COOKIE_KEY, session.getSessionKey(), true))
                .build();
    }

    @DELETE
    public Response logOut(
            @Context ContainerRequestContext requestContext)
    {
        if (requestContext.getCookies().containsKey(COOKIE_KEY)) {
            String sessionKey = requestContext.getCookies().get(COOKIE_KEY).getValue();
            controller.logOut(sessionKey);

            return Response
                    .ok()
                    .entity(new SuccessResponse())
                    .cookie(new KillCookie(COOKIE_KEY))
                    .build();
        }

        return Response
                .ok()
                .entity(new SuccessResponse())
                .build();
    }

    @GET
    public SessionStateResponse isActive(
            @Context ContainerRequestContext requestContext,
            @DefaultValue("false") @QueryParam("update") boolean update
    ) {
        if (!requestContext.getCookies().containsKey(COOKIE_KEY)) {
            return new SessionStateResponse(false);
        }

        String sessionKey = requestContext.getCookies().get(COOKIE_KEY).getValue();
        SessionStateResponse state = controller.getSessionState(sessionKey);

        if (state.isValid() && update) {
            try {
                controller.updateSession(sessionKey);
                state = controller.getSessionState(sessionKey);
            } catch (NotFoundException e) {
                // do nothing
            }
        }

        return state;
    }
}
