package nl.iprwc.resources;


import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import nl.iprwc.controller.AccountController;
import nl.iprwc.controller.SuperController;
import nl.iprwc.model.Account;
import nl.iprwc.model.Authenticate;
import nl.iprwc.view.View;

import javax.annotation.security.RolesAllowed;
import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/api/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    private final AccountController controller;

    public AccountResource() {
        controller = SuperController.getInstance().getAccountController();
    }

    @GET
    @UnitOfWork
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(View.Public.class)
    public Response getAccount(@PathParam("id") long personId) {
        try {
            return Response.status(Response.Status.OK).entity(controller.getFromId(personId)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }
    @POST
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JsonView(View.Public.class)
    public Response getAccount(Authenticate loginAttempt) {
        try {
            return Response.status(Response.Status.OK).entity(controller.tryLogin(loginAttempt)).build();
        } catch (NotFoundException | SQLException | ClassNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }
}