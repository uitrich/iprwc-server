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

    /**
     * Get the session based on the id.
     * @param id
     * @return
     * @throws NotFoundException
     */
    public Session fromId(long id) throws NotFoundException, InvalidOperationException {
        try {
            Session output = dao.fromId(id);
            output.setAccount(AccountController.getInstance().getFromId(output.getAccount().getId()));
            return output;
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    /**
     * Get the session based on the session-key
     * @param key
     * @return
     * @throws NotFoundException
     */
    public Session fromSessionKey(String key) throws NotFoundException, InvalidOperationException {
        try {
            Session output = dao.fromHashedSessionKey(new Sha512().hash(key), key);
            output.setAccount(AccountController.getInstance().getFromId(output.getAccount().getId()));
            return output;
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public Session fromHashedSessionKey(String hashedKey) throws NotFoundException, InvalidOperationException {
        try {
            Session output = dao.fromHashedSessionKey(hashedKey);
            output.setAccount(AccountController.getInstance().getFromId(output.getAccount().getId()));
            return output;
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
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

    /**
     * Update the last activity so that users can stay logged in.
     * @param session
     */
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
