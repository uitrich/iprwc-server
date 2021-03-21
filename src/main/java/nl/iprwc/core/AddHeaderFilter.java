package nl.iprwc.core;

import io.dropwizard.Configuration;
import nl.iprwc.SoftiServerApplication;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class AddHeaderFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {

        responseContext.getHeaders().add("Access-Control-Allow-Origin", SoftiServerApplication.getServerConfiguration().getWebUrl());
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "POST, PUT, DELETE, GET, OPTIONS");
        responseContext.getHeaders().add("SameSite", "None");
    }
}