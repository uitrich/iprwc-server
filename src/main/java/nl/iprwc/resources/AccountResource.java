package nl.iprwc.resources;


import com.fasterxml.jackson.annotation.JsonView;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.media.sound.InvalidDataException;
import io.dropwizard.auth.Auth;
import nl.iprwc.Request.AccountRequest;
import nl.iprwc.controller.AccountController;
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
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Role_Customer", "Role_Admin"})
    public Response getAccount(@Auth User user) {
        try {
            return Response.status(Response.Status.OK).entity(controller.getFromId(user.getAccount().getId())).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/admin")
    @RolesAllowed("Role_Admin")
    public Response getAccounts(@Auth User user) {
        try {
            return Response.status(Response.Status.OK).entity(controller.getAll()).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.CONFLICT).build();
        } catch (ClassNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
    }

    @GET
    @Path("/admin/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Admin")
    public Response getAccount(@PathParam("id") String personId) {
        try {
            return Response.status(Response.Status.OK).entity(controller.getFromId(personId)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @JsonView(View.Public.class)
    public Response getAccount(Authentication loginAttempt) {
        try {
            return Response.status(Response.Status.OK).entity(controller.tryLogin(loginAttempt)).build();
        } catch (NotFoundException | SQLException | ClassNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }
    @POST
    @Path("/makeAccount")
    @JsonView(View.Public.class)
    public Response createAccount(@NotNull @Valid AccountRequest account) {
        try{
            Account newAccount = controller.create(account);
            return Response.status(Response.Status.OK).entity(newAccount).build();
        } catch (InvalidDataException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }  catch (SQLException | ClassNotFoundException | NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        } catch (nl.iprwc.exception.NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Customer")
    public Response updateAccount( @Auth User user) {
        return Response.status(Response.Status.OK).entity(controller.updateAccount(user.getAccount())).build();
    }
    @PUT
    @Path("admin/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Admin")
    public Response updateAccount(Account account) {
        return Response.status(Response.Status.OK).entity(controller.updateAccount(account)).build();
    }

    @DELETE
    @Path("/delete")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Customer")
    public Response deleteAccount(@Auth User user) {
        return Response.status(Response.Status.OK).entity(controller.deleteAccount(user.getAccount().getId())).build();
    }

    @DELETE
    @Path("/admin/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Admin")
    public Response deleteAccount(@PathParam("id") String account) {
        return Response.status(Response.Status.OK).entity(controller.deleteAccount(account)).build();
    }

    @GET
    @Path("/reset/users")
    public Response resetUserIds() throws SQLException, ClassNotFoundException {
        for (Account account : controller.getAll()) {
            controller.UpdateAccountId(account.getId());
        }
        return Response.status(Response.Status.OK).build();
    }

    private Account fromObject(JsonObject in) {
        return new Account(
                in.get("firstName").getAsString(),
                in.get("lastName").getAsString(),
                in.get("mailAddress").getAsString(),
                in.get("postal_code").getAsString(),
                in.get("house_number").getAsString()
        );
    }

}