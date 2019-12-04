package nl.iprwc.resources;


import com.fasterxml.jackson.annotation.JsonView;
import io.dropwizard.hibernate.UnitOfWork;
import nl.iprwc.controller.ProductController;
import nl.iprwc.view.View;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLException;
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
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(View.Public.class)
    public Response getFromId(@PathParam("id") long id) {
        try {
            return Response.status(Response.Status.OK).entity(controller.getFromId(id)).build();
        }
        catch (SQLException | ClassNotFoundException err) {
            return Response.status(Response.Status.NOT_FOUND).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
    }
}