package nl.iprwc.db;


import nl.iprwc.Utils.JodaDateTime;
import nl.iprwc.controller.AccountController;
import nl.iprwc.controller.SuperController;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Session;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;
import org.joda.time.DateTime;

import javax.validation.ConstraintViolationException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionDAO {
    public Session fromId(long id) throws NotFoundException {
        try {
            ResultSet result = DatabaseService
                    .getInstance()
                    .createNamedPreparedStatement(
                            "SELECT account_id, session_key, last_activity FROM session WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery();

            if (!result.next()) {
                throw new NotFoundException();
            }

            AccountController accountController = SuperController.getInstance().getAccountController();

            return new Session(
                    id,
                    accountController.getFromId(result.getLong("account_id")),
                    result.getString("session_key"),
                    JodaDateTime.fromTimestamp(result.getTimestamp("last_activity")));
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Session fromHashedSessionKey(String key) throws NotFoundException {
        return fromHashedSessionKey(key, null);
    }

    public Session fromHashedSessionKey(String key, String plainKey) throws NotFoundException {
        try {
            ResultSet result = DatabaseService
                    .getInstance()
                    .createNamedPreparedStatement(
                            "SELECT id, account_id, last_activity FROM session WHERE session_key = :key")
                    .setParameter("key", key)
                    .executeQuery();

            if (!result.next()) {
                throw new NotFoundException();
            }

            AccountController accountController = SuperController.getInstance().getAccountController();

            return new Session(
                    result.getLong("id"),
                    accountController.getFromId(result.getLong("account_id")),
                    key,
                    plainKey,
                    JodaDateTime.fromTimestamp(result.getTimestamp("last_activity")));
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void delete(long id)
    {
        try {
            DatabaseService
                    .getInstance()
                    .createNamedPreparedStatement("DELETE FROM session WHERE id = :id")
                    .setParameter("id", id)
                    .execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void updateLastActivity(long id)
    {
        try {
            DatabaseService
                    .getInstance()
                    .createNamedPreparedStatement("UPDATE session SET last_activity = NOW() WHERE id = :id")
                    .setParameter("id", id)
                    .execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public long create(long accountId, String sessionHash, DateTime lastActivity)
    {
        NamedParameterStatement statement = null;
        try {
            statement = DatabaseService
                    .getInstance()
                    .createNamedPreparedStatement("INSERT INTO session (account_id, session_key, last_activity) VALUES (:accountId, :hash, :lastActivity)")
                    .setParameter("accountId", accountId)
                    .setParameter("hash", sessionHash)
                    .setTimestamp("lastActivity", JodaDateTime.toTimestamp(lastActivity));

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            generatedKeys.next();
            return generatedKeys.getLong("id");
        } catch (ConstraintViolationException e) {
            // throw it again to be sure
            throw e;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
