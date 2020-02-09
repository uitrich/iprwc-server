package nl.iprwc.resources;


import com.google.gson.Gson;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.LongParam;
import nl.iprwc.controller.GroupController;
import nl.iprwc.controller.ProductController;
import nl.iprwc.controller.SuperController;
import nl.iprwc.model.Product;
import nl.iprwc.model.ProductResponse;
import nl.iprwc.model.User;

import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.IOException;
import java.sql.SQLException;
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
    private GroupController groupController;

    public ProductResource() {
        this.controller = SuperController.getInstance().getProductController();
        this.groupController = SuperController.getInstance().getGroupController();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts(@QueryParam("page")            @DefaultValue("1")  @Min(1) LongParam page,
                                   @QueryParam("page-size")       @DefaultValue("12") @Min(1) @Max(100) LongParam       pageSize,
                                   @QueryParam("category")                                              List<Integer> category,
                                   @QueryParam("company")                                               List<Integer> company,
                                   @QueryParam("bodyLocation")                                          List<Integer> bodyLocation,
                                   @QueryParam("search")                                                String search
    ) {
        try {
            return Response.status(Response.Status.OK).entity(controller.getAll(page, pageSize, search, category, company, bodyLocation)).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch ( Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GET
    @Path("/admin")
    @RolesAllowed("Role_Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts() {
        try {
            return Response.status(Response.Status.OK).entity(controller.getAllIndestinctive()).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch ( Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed("Role_Admin")
    public Response getFromId(@PathParam("id") long id, @Auth User user) {
        try {
            Product result = controller.getFromId(id);
            return Response.status(Response.Status.OK).entity(result).build();
        }
        catch (SQLException | ClassNotFoundException err) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }

    @GET
    @Path("/{id}/image")
    @RolesAllowed("Role_Admin")
    @Produces("image/png")
    public Response getImageFromId(@PathParam("id") long id) {
        try {
            return Response.status(Response.Status.OK).entity(controller.getImageFromId(id)).build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GET
    @Path("/top/{range}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTop(@PathParam("range") long range) {
        try {
            return Response.status(Response.Status.OK).entity(controller.getTop(range)).build();
        } catch (NotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @POST
    @RolesAllowed("Role_Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(String json) {
        System.out.println(json);
        Gson gson = new Gson();
        ProductResponse product = gson.fromJson(json, ProductResponse.class);
        return Response.status(Response.Status.OK).entity(controller.insertAdd(product)).build();
    }

    @PUT
    @RolesAllowed("Role_Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateProduct(String json) {
        System.out.println(json);
        Gson gson = new Gson();
        ProductResponse product = gson.fromJson(json, ProductResponse.class);
        return Response.status(Response.Status.OK).entity(controller.update(controller.splitResponse(product))).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("Role_Admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteProduct(@PathParam("id") long id) {
        return Response.status(Response.Status.OK).entity(controller.delete(id)).build();
    }

    @GET
    @RolesAllowed({"Role_Customer", "Role_Admin"})
    @Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories() {
        return Response.status(Response.Status.OK).entity(controller.getTypeDesc("category")).build();
    }

    @GET
    @RolesAllowed({"Role_Customer", "Role_Admin"})
    @Path("/companies")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompanies() {
        return Response.status(Response.Status.OK).entity(controller.getTypeDesc("company")).build();
    }

    @GET
    @RolesAllowed({"Role_Customer", "Role_Admin"})
    @Path("/bodylocation")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBodyLocations() {
        return Response.status(Response.Status.OK).entity(controller.getTypeDesc("body_location")).build();
    }
}