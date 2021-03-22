package nl.iprwc.sql;

import io.dropwizard.db.DataSourceFactory;
import nl.iprwc.SoftiServerApplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseService {
    private static DatabaseService instance;

    public synchronized static DatabaseService getInstance() throws SQLException, ClassNotFoundException {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private Connection connection;

    private DatabaseService() throws SQLException, ClassNotFoundException {
        initConnection();
    }

    private void initConnection() throws ClassNotFoundException, SQLException {
        DataSourceFactory dataSourceFactory = SoftiServerApplication
                .getServerConfiguration()
                .getDataSourceFactory();

        Class.forName(dataSourceFactory.getDriverClass());

        connection = DriverManager.getConnection(
            dataSourceFactory.getUrl(),
                dataSourceFactory.getUser(),
                dataSourceFactory.getPassword()
        );
    }

    public NamedParameterStatement createNamedPreparedStatement(String query) throws SQLException
    {
        return new NamedParameterStatement(connection, query);
    }

    public PreparedStatement createPreparedStatement(String query) throws SQLException
    {
        return connection.prepareStatement(query);
    }

    public boolean ping() throws SQLException {
        String validationQuery = SoftiServerApplication
                .getServerConfiguration()
                .getDataSourceFactory()
                .getValidationQuery();

        return createPreparedStatement(validationQuery)
                .executeQuery()
                .next();
    }

}
