package nl.iprwc;

import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import nl.iprwc.controller.SuperController;
import nl.iprwc.resources.*;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;


public class SoftiServerApplication extends Application<SoftiServerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SoftiServerApplication().run(args);
    }

    private static SoftiServerConfiguration docciServerConfiguration;

    public static SoftiServerConfiguration getServerConfiguration()
    {
        return docciServerConfiguration;
    }

    @Override
    public String getName() {
        return "docci";
    }

    @Override
    public void initialize(Bootstrap<SoftiServerConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        SuperController.getInstance(); // init controllers
    }


    @Override
    public void run(final SoftiServerConfiguration configuration,
                    final Environment environment) {
        docciServerConfiguration = configuration;
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        // Object mapper config
        environment
                .getObjectMapper()
                .setDateFormat(configuration.getDateTimeFormatter());

        // Resources
        environment.jersey().register(new AccountResource());
        environment.jersey().register(new ProductResource());
    }
}
