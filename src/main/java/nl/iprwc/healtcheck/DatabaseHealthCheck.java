package nl.iprwc.healtcheck;

import com.codahale.metrics.health.HealthCheck;
import nl.iprwc.sql.DatabaseService;

public class DatabaseHealthCheck extends HealthCheck {
    @Override
    protected Result check() throws Exception {
        if (DatabaseService
                .getInstance()
                .ping()
        ) {
            return Result.healthy();
        }

        return Result.unhealthy("Couldn't execute database ping");
    }
}
