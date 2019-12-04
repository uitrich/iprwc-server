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

    public Connection getConnection() {
        return connection;
    }

    public NamedParameterStatement createNamedPreparedStatement(String query) throws SQLException
    {
        return new NamedParameterStatement(connection, query);
    }

    public NamedParameterStatement createNamedPreparedStatement(String query, Map<String, Object> params) throws SQLException
    {
        NamedParameterStatement statement = createNamedPreparedStatement(query);
        statement.setParameterMap(params);
        return statement;
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

    public synchronized TransactionResult transaction(Transaction tr) {
        return transaction(s -> {
            tr.run(s);
            return null;
        });
    }

    public synchronized TransactionResult transaction(TransactionEmpty tr) {
        return transaction(s -> {
            tr.run();
            return null;
        });
    }

    public synchronized TransactionResult transaction(TransactionEmptyReturn tr)
    {
        return transaction(s -> {
            tr.run();
            return null;
        });
    }

    public synchronized TransactionResult transaction(TransactionReturn tr) {
        try {
            boolean autoCommit = connection.getAutoCommit();
            connection.setAutoCommit(false);

            Object returned = null;

            try {
                returned = tr.run(this);
                connection.commit();
                return new TransactionResult(returned);
            } catch (Throwable e) {
                connection.rollback();
                return new TransactionResult(returned, e);
            }
            finally {
                connection.setAutoCommit(autoCommit);
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return new TransactionResult(e);
        }
    }
}
