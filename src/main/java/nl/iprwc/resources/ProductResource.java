package nl.iprwc.resources;


import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.jersey.params.BooleanParam;
import io.dropwizard.jersey.params.LongParam;
import io.dropwizard.jersey.params.NonEmptyStringParam;
import nl.iprwc.controller.ProductController;
import nl.iprwc.model.Product;
import nl.iprwc.view.View;

import javax.print.attribute.standard.Media;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.File;
import java.io.FileInputStream;
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

    public ProductResource() {
        this.controller = new ProductController();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllProducts(@QueryParam("page")            @DefaultValue("1")  @Min(1) LongParam page,
                                   @QueryParam("page-size")       @DefaultValue("10") @Min(1) @Max(100) LongParam       pageSize,
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFromId(@PathParam("id") long id) {
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
    @Produces("image/png")
    public Response getImageFromId(@PathParam("id") long id) {
        try {
            return Response.status(Response.Status.OK).entity(controller.getImageFromId(id)).build();
        } catch (IOException e) {
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

    @GET
    @Path("/{id}/insert")
    @Produces(MediaType.APPLICATION_JSON)
    public Response UpdateAuto(@PathParam("id") long id) {
        return Response.status(Response.Status.OK).entity(controller.update(id)).build();
    }
}