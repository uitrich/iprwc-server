package nl.iprwc.resources;


import nl.iprwc.controller.BodyLocationController;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/bodylocation")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BodyLocationResource {
    private final BodyLocationController controller;

    public BodyLocationResource() {
        controller = BodyLocationController.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAll() throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getAll()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"Role_Customer", "Role_Admin"})
    public Response get(@PathParam("id") Long id) throws InvalidOperationException, NotFoundException {
            return Response.status(Response.Status.OK).entity(controller.get(id)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Admin")
    public Response post(String name) throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.post(name)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Admin")
    public Response update(@PathParam("id") long id, String name) throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.update(id, name)).build();
    }
}