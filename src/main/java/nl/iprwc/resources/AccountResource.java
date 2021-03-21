package nl.iprwc.resources;


import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.media.sound.InvalidDataException;
import io.dropwizard.auth.Auth;
import nl.iprwc.Request.AccountRequest;
import nl.iprwc.controller.AccountController;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Account;
import nl.iprwc.model.Authentication;
import nl.iprwc.model.User;
import nl.iprwc.view.View;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

@Path("/api/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {
    private final AccountController controller;

    public AccountResource() {
        controller = AccountController.getInstance();
    }

    @GET
    @RolesAllowed({"Role_Customer", "Role_Admin"})
    public Response getAccount(@Auth User user) throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getFromId(user.getAccount().getId())).build();
    }
    @GET
    @Path("/admin")
    @RolesAllowed("Role_Admin")
    public Response getAccounts(@Auth User user) throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getAll()).build();
    }

    @GET
    @Path("/admin/{id}")
    @RolesAllowed("Role_Admin")
    public Response getAccount(@PathParam("id") String personId) throws InvalidOperationException {
            return Response.status(Response.Status.OK).entity(controller.getFromId(personId)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAccount(Authentication loginAttempt) throws InvalidOperationException {
            return Response.status(Response.Status.OK).entity(controller.tryLogin(loginAttempt)).build();
    }
    @POST
    @Path("/makeAccount")
    public Response createAccount(@NotNull @Valid AccountRequest account) throws InvalidOperationException {
            Account newAccount = controller.create(account);
            return Response.status(Response.Status.OK)
                    .entity(newAccount)
                    .build();
    }

    @PUT
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Customer")
    public Response updateAccount( @Auth User user, AccountRequest account) throws InvalidOperationException {
        if (user.getAccount().getId().equals(account.getId()))
            return Response.status(Response.Status.OK)
                    .entity(controller.updateAccount(account)).build();
        return Response.status(Response.Status.FORBIDDEN).build();
    }
    @PUT
    @Path("admin/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Admin")
    public Response updateAccount(AccountRequest accountRequest) throws InvalidOperationException, IOException {
        return Response.status(Response.Status.OK)
                .entity(controller.updateAccount(accountRequest)).build();
    }

    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Customer")
    public Response deleteAccount(@Auth User user) throws InvalidOperationException, NotFoundException {
        return Response.status(Response.Status.OK)
                .entity(controller.deleteAccount(user.getAccount().getMailAddress())).build();
    }

    @DELETE
    @Path("/admin/delete/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Admin")
    public Response deleteAccount(@PathParam("email") String accountEmail) throws InvalidOperationException, NotFoundException {
        return Response.status(Response.Status.OK).entity(controller.deleteAccount(accountEmail)).build();
    }

}