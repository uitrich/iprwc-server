package nl.iprwc.controller;

import nl.iprwc.utils.Random;
import nl.iprwc.db.SessionDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.hash.Sha512;
import nl.iprwc.model.Account;
import nl.iprwc.model.Session;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.DateTime;

import java.sql.SQLException;

public class SessionController {
    private SessionDAO dao;
    private static SessionController instance;

    static {
        instance = new SessionController();
    }

    private SessionController() {
        dao = new SessionDAO();
    }

    public static SessionController getInstance() {
        return instance;
    }

    public Session fromSessionKey(String key) throws NotFoundException, InvalidOperationException {
        try {
            Session output = dao.fromHashedSessionKey(new Sha512().hash(key), key);
            output.setAccount(AccountController.getInstance().get(output.getAccount().getId()));
            return output;
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    public Session create(Account account)
    {
        String sessionKey;
        String sessionHash;
        long id;
        Sha512 sha512 = new Sha512();
        String accountId = account.getId();
        DateTime lastActivity = DateTime.now();

        do {
            sessionKey = Random.randomString(256);
            sessionHash = sha512.hash(sessionKey);

            try {
                id = dao.create(accountId, sessionHash, lastActivity);
            }
            catch (ConstraintViolationException e) {
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            break;
        }
        while (true);

        return new Session(id, account, sessionKey, sessionHash, lastActivity);
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
