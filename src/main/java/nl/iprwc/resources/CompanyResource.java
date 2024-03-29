package nl.iprwc.resources;


import nl.iprwc.controller.CompanyController;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Company;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/company")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CompanyResource {
    private final CompanyController controller;

    public CompanyResource() {
        controller = CompanyController.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
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
    public Response create(String name) throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.create(new Company(name))).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @RolesAllowed("Role_Admin")
    public Response update(@PathParam("id") long id, String name) throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.update(new Company(id, name))).build();
    }
}