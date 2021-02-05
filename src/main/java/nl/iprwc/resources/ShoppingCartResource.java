package nl.iprwc.resources;

import com.fasterxml.jackson.annotation.JsonView;
import com.sun.media.sound.InvalidDataException;
import io.dropwizard.auth.Auth;
import nl.iprwc.controller.AccountController;
import nl.iprwc.controller.GroupController;
import nl.iprwc.controller.ShoppingCartController;
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
    private final GroupController groupController;

    public ShoppingCartResource() {
        controller = ShoppingCartController.getInstance();
        groupController = GroupController.getInstance();
    }

    @GET
    @JsonView(View.Public.class)
    @RolesAllowed("Role_Customer")
    public Response getCartContents(@Auth User user) {
        return Response.status(Response.Status.OK).entity(controller.getShoppingcart(user)).build();
    }
    @GET
    @Path("/admin/{id}")
    @RolesAllowed("Role_Admin")
    @JsonView(View.Public.class)
    public Response getCartContents(@PathParam("id") String accountId) {
        return Response.status(Response.Status.OK).entity(controller.getShoppingcart(accountId)).build();
    }

    @POST
    @Path("/{productId}")
    @JsonView(View.Public.class)
    @RolesAllowed("Role_Customer")
    public Response addCartItem(@PathParam("productId") long productId, @Auth User user) {
        return Response.status(Response.Status.OK).entity(controller.addItem(productId, user.getAccount().getId())).build();
    }

    @POST
    @Path("/admin/{id}/{user}")
    @JsonView(View.Public.class)
    @RolesAllowed("Role_Admin")
    public Response addCartItem(@PathParam("id") long id, @PathParam("user") String user) {
        return Response.status(Response.Status.OK).entity(controller.addItem(id, user)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Role_Customer")
    public Response deleteCartItem(@PathParam("id") long id, @Auth User user) {
        return Response.status(Response.Status.OK).entity(controller.deleteItem(id, user.getAccount().getId())).build();
    }

    @DELETE
    @Path("full/{id}")
    @RolesAllowed("Role_Customer")
    public Response deleteFull( @Auth User user) {
        return Response.status(Response.Status.OK).entity(controller.delete(user.getAccount().getId())).build();
    }

    @DELETE
    @RolesAllowed("Role_Admin")
    @Path("/admin/{userId}/{id}")
    public Response deleteCartItem(@PathParam("id") long id, @PathParam("userId") String user) {
        return Response.status(Response.Status.OK).entity(controller.deleteItem(id, user)).build();
    }

    @POST
    @RolesAllowed("Role_Customer")
    @Path("/updatequantity/{id}/{amount}")
    public Response updateQuantity(@PathParam("id") long productid, @PathParam("amount") long amount, @Auth User user) {
        return Response.status(Response.Status.OK).entity(controller.updateQuantity(productid, amount, user.getAccount().getId())).build();
    }

    @POST
    @RolesAllowed("Role_Admin")
    @Path("/admin/updatequantity/{id}/{amount}")
    public Response updateQuantity(@PathParam("id") long productid, @Auth User auth,@PathParam("amount") long amount, String user) {
        return Response.status(Response.Status.OK).entity(controller.updateQuantity(productid, amount, user)).build();
    }

    @GET
    @RolesAllowed("Role_Customer")
    @Path("/quantity")
    public Response getQuantity(@Auth User user) {
        return Response.status(Response.Status.OK).entity(controller.getQuantity(user.getAccount().getId())).build();
    }
    @POST
    @Path("/admin/quantity/{id}")
    public Response getQuantity(@Auth User auth, String userId) {
        if (auth.getAccount().getId() == userId || auth.hasGroupReference("Role_Admin"))
            return Response.status(Response.Status.OK).entity(controller.getQuantity(userId)).build();
        throw new NotAuthorizedException("You do not have permission to alter another user's account");
    }
}
