package nl.iprwc.resources;

import com.fasterxml.jackson.annotation.JsonView;
import com.sun.media.sound.InvalidDataException;
import io.dropwizard.auth.Auth;
import nl.iprwc.controller.AccountController;
import nl.iprwc.controller.GroupController;
import nl.iprwc.controller.ShoppingCartController;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Account;
import nl.iprwc.model.Authentication;
import nl.iprwc.model.User;
import nl.iprwc.view.View;

import javax.annotation.security.RolesAllowed;
import javax.persistence.PostUpdate;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("api/shoppingcart")
@Produces(MediaType.APPLICATION_JSON)
public class ShoppingCartResource {
    private final ShoppingCartController controller;

    public ShoppingCartResource() {
        controller = ShoppingCartController.getInstance();
    }

    @GET
    @JsonView(View.Public.class)
    @RolesAllowed("Role_Customer")
    public Response getCartContents(@Auth User user) throws NotFoundException, InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getShoppingcart(user.getAccount().getId())).build();
    }
    @GET
    @Path("/admin/{id}")
    @RolesAllowed("Role_Admin")
    @JsonView(View.Public.class)
    public Response getCartContents(@PathParam("id") String accountId) throws NotFoundException, InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getShoppingcart(accountId)).build();
    }

    @POST
    @Path("/{productId}")
    @JsonView(View.Public.class)
    @RolesAllowed("Role_Customer")
    public Response addCartItem(@PathParam("productId") long productId, @Auth User user) throws NotFoundException, InvalidOperationException {
        controller.addItem(productId, user.getAccount().getId());
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/admin/{id}/{user}")
    @JsonView(View.Public.class)
    @RolesAllowed("Role_Admin")
    public Response addCartItem(@PathParam("id") long id, @PathParam("user") String user) throws NotFoundException, InvalidOperationException {
        controller.addItem(id, user);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Role_Customer")
    public Response deleteCartItem(@PathParam("id") long id, @Auth User user) throws NotFoundException, InvalidOperationException {
        controller.deleteItem(id, user.getAccount().getId());
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("full")
    @RolesAllowed("Role_Customer")
    public Response deleteFull( @Auth User user) throws InvalidOperationException {
        controller.delete(user.getAccount().getId());
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @RolesAllowed("Role_Admin")
    @Path("/admin/{userId}/{id}")
    public Response deleteCartItem(@PathParam("id") long id, @PathParam("userId") String user) throws NotFoundException, InvalidOperationException {
        controller.deleteItem(id, user);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @RolesAllowed("Role_Customer")
    @Path("/updatequantity/{id}/{amount}")
    public Response updateQuantity(@PathParam("id") long productid, @PathParam("amount") long amount, @Auth User user) throws NotFoundException, InvalidOperationException {
        controller.updateQuantity(productid, amount, user.getAccount().getId());
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @RolesAllowed("Role_Admin")
    @Path("/admin/updatequantity/{id}/{amount}")
    public Response updateQuantity(@PathParam("id") long productid, @Auth User auth,@PathParam("amount") long amount, String user) throws NotFoundException, InvalidOperationException {
        controller.updateQuantity(productid, amount, user);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @RolesAllowed("Role_Customer")
    @Path("/quantity")
    public Response getQuantity(@Auth User user) throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getQuantity(user.getAccount().getId())).build();
    }
    @POST
    @Path("/admin/quantity/{id}")
    public Response getQuantity(@Auth User auth, String userId) throws InvalidOperationException {
        if (auth.getAccount().getId() == userId || auth.hasGroupReference("Role_Admin"))
            return Response.status(Response.Status.OK).entity(controller.getQuantity(userId)).build();
        throw new NotAuthorizedException("You do not have permission to alter another user's account");
    }
}
