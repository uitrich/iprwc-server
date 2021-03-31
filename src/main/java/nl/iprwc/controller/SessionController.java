package nl.iprwc.controller;

import nl.iprwc.db.SessionDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.hash.Sha512;
import nl.iprwc.model.Account;
import nl.iprwc.model.Session;
import nl.iprwc.utils.Random;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.DateTime;

import java.sql.SQLException;

public class SessionController {
    private SessionDAO dao;
    private static SessionController instance;
    private static Sha512 sha512;

    static {
        instance = new SessionController();
    }

    private SessionController() {
        dao = new SessionDAO();
        sha512 = new Sha512();
    }

    public static SessionController getInstance() {
        return instance;
    }

    public Session fromSessionKey(String key) throws NotFoundException, InvalidOperationException {
        try {
            Session output = dao.fromHashedSessionKey(sha512.hash(key), key);
            output.setAccount(AccountController.getInstance().get(output.getAccount().getId()));
            return output;
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    public Session create(Account account)
    {
        String sessionKey = Random.randomString(256);
        String sessionHash = sha512.hash(sessionKey);
        DateTime lastActivity = DateTime.now();
        try {
            long id = dao.create(account.getId(), sessionHash, lastActivity);
            return new Session(id, account, sessionKey, sessionHash, lastActivity);
        } catch (ConstraintViolationException e) {
            return create(account);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void delete(Session session)
    {
        delete(session.getId());
    }

    public void delete(long id)
    {
        dao.delete(id);
    }

    public void updateLastActivity(Session session)
    {
        updateLastActivity(session.getId());
    }

    public void updateLastActivity(long id)
    {
        dao.updateLastActivity(id);
    }

    public boolean hasSessionCurrently(Account account) throws SQLException, ClassNotFoundException {
        return dao.hasSession(account);
    }
}
