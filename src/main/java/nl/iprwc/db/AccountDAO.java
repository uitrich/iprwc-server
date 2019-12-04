package nl.iprwc.db;

import com.google.common.base.Optional;
import liquibase.database.Database;
import nl.iprwc.model.Account;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public AccountDAO() {
    }

    private Account fromResultSet(ResultSet result) throws SQLException {
        return new
                Account(
                result.getString("username"),
                result.getString("password")
        );
    }

    public Account getFromId(long id) {
        try {
            return
            fromResultSet(DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM Account WHERE id == :id")
                    .setParameter("id", id)
                    .executeQuery());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account tryLogin(String username, String password) throws SQLException, ClassNotFoundException {
        Account account =  fromResultSet(
            DatabaseService
                    .getInstance()
                    .createNamedPreparedStatement("SELECT * FROM Account WHERE username == :username && password == :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .executeQuery()
        );
        return account.getUsername() == null ?  null : account;
    }
}