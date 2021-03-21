package nl.iprwc.resources;

import io.dropwizard.hibernate.UnitOfWork;
import nl.iprwc.Request.CredentialsRequest;
import nl.iprwc.controller.AuthenticationController;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Session;
import nl.iprwc.Response.SessionStateResponse;
import nl.iprwc.utils.KillCookie;
import nl.iprwc.Response.SuccessResponse;
import nl.iprwc.utils.SetCookie;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;

@Path("/api/authentication")
public class AuthenticationResource
{
    private static final String COOKIE_KEY = "session-key";

    private final AuthenticationController controller;

    public AuthenticationResource()
    {
        controller = AuthenticationController.getInstance();
    }

    @POST
    @UnitOfWork
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response logIn(@NotNull @Valid CredentialsRequest auth) throws InvalidOperationException {
        Session session = null;
        try {
            session = controller.logIn(auth);
            if (session == null) throw new NotFoundException();
        } catch (NotFoundException e) {
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
            @Context ContainerRequestContext requestContext) throws NotFoundException, InvalidOperationException {
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
                .status(Response.Status.BAD_REQUEST)
                .build();
    }

    @GET
    public SessionStateResponse isActive(
            @Context ContainerRequestContext requestContext,
            @DefaultValue("false") @QueryParam("update") boolean update
    ) throws NotFoundException, InvalidOperationException {
        if (!requestContext.getCookies().containsKey(COOKIE_KEY)) {
            return new SessionStateResponse(false);
        }

        String sessionKey = requestContext.getCookies().get(COOKIE_KEY).getValue();
        SessionStateResponse state;
        try {
            state = controller.getSessionState(sessionKey);
            if (state.isValid() && update) {
                controller.updateSession(sessionKey);
                state = controller.getSessionState(sessionKey);
            }
        } catch (NotFoundException e) {
            return new SessionStateResponse(false);
        }

        return state;
    }
}
