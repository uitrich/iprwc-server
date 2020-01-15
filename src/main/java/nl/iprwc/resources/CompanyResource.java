package nl.iprwc.resources;


import com.fasterxml.jackson.annotation.JsonView;
import com.sun.media.sound.InvalidDataException;
import io.dropwizard.auth.Auth;
import nl.iprwc.controller.AccountController;
import nl.iprwc.controller.BodyLocationController;
import nl.iprwc.controller.CompanyController;
import nl.iprwc.controller.SuperController;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Account;
import nl.iprwc.model.Authentication;
import nl.iprwc.model.Body_Location;
import nl.iprwc.model.User;
import nl.iprwc.view.View;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;
import java.util.List;

@Path("/api/company")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompanyResource {
    private final CompanyController controller;

    public CompanyResource() {
        controller = SuperController.getInstance().getCompanyController();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.status(Response.Status.OK).entity(controller.getAll()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Role_Customer", "Role_Admin"})
    public Response get(@PathParam("id") Long id) {
        try {
            return Response.status(Response.Status.OK).entity(controller.get(id)).build();
        } catch (NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Admin")
    public Response post(String name) {
        return Response.status(Response.Status.OK).entity(controller.post(name)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("Role_Admin")
    public Response update(@PathParam("id") long id, String name) {
        return Response.status(Response.Status.OK).entity(controller.update(id, name)).build();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("Role_Admin")
    public Response update(@PathParam("id") long id, String name) {
        return Response.status(Response.Status.OK).entity(controller.update(id, name))
    }
}