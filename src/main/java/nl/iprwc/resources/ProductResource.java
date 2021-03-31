package nl.iprwc.resources;


import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.LongParam;
import nl.iprwc.Response.ProductResponse;
import nl.iprwc.controller.ProductController;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.User;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.regex.Pattern;


@Path("/api/product")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductResource {
    private static final String SORT_REGEX = "^(asc|desc)\\-(.+)$";
    private static final int SORT_REGEX_FLAGS = Pattern.CASE_INSENSITIVE;
    private static final Pattern SORT_PATTERN = Pattern.compile(SORT_REGEX, SORT_REGEX_FLAGS);

    private final ProductController controller;

    public ProductResource() {
        this.controller = ProductController.getInstance();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProductsFiltered(@QueryParam("page")            @DefaultValue("1")  @Min(1) LongParam page,
                                           @QueryParam("page-size")       @DefaultValue("12") @Min(1) @Max(100) LongParam     pageSize,
                                           @QueryParam("category")                                              List<Integer> category,
                                           @QueryParam("company")                                               List<Integer> company,
                                           @QueryParam("bodyLocation")                                          List<Integer> bodyLocation,
                                           @QueryParam("search")                                                String search
    ) throws NotFoundException, InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getAll(page, pageSize, search, category, company, bodyLocation)).build();

    }
    @GET
    @Path("/admin")
    @RolesAllowed("Role_Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() throws NotFoundException, InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getAllIndistinctly()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFromId(@PathParam("id") long id, @Auth User user) throws InvalidOperationException, NotFoundException {
        return Response.status(Response.Status.OK).entity(controller.getFromId(id)).build();
    }

    @GET
    @Path("/{id}/image")
    @RolesAllowed("Role_Admin")
    @Produces("image/png")
    public Response getImageFromId(@PathParam("id") long id) throws InvalidOperationException {
            return Response.status(Response.Status.OK).entity(controller.getImageFromId(id)).build();
    }

    @GET
    @Path("/top/{range}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTop(@PathParam("range") long range) throws NotFoundException, InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getTop(range)).build();
    }

    @POST
    @RolesAllowed("Role_Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(ProductResponse product) throws InvalidOperationException, NotFoundException {
        return Response.status(Response.Status.OK).entity(controller.insertAdd(product)).build();
    }

    @PUT
    @RolesAllowed("Role_Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(ProductResponse product) throws InvalidOperationException, NotFoundException {
        return Response.status(Response.Status.OK).entity(controller.update(controller.splitResponse(product))).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Role_Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("id") long id) throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.delete(id)).build();
    }

    @GET
    @RolesAllowed({"Role_Customer"})
    @Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories() throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getTypeDesc("category")).build();
    }

    @GET
    @RolesAllowed({"Role_Customer"})
    @Path("/companies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompanies() throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getTypeDesc("company")).build();
    }

    @GET
    @RolesAllowed({"Role_Customer"})
    @Path("/bodylocation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBodyLocations() throws InvalidOperationException {
        return Response.status(Response.Status.OK).entity(controller.getTypeDesc("body_location")).build();
    }
}