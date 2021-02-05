package nl.iprwc.controller;

import nl.iprwc.Request.CredentialsRequest;
import nl.iprwc.Response.SessionStateResponse;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.hash.BCrypt;
import nl.iprwc.model.Account;
import nl.iprwc.model.Authentication;
import nl.iprwc.model.Session;

public class AuthenticationController {
    private AccountController accountController;
    private SessionController sessionController;
    private static AuthenticationController instance;

    /**
     * Create a singleton from supercontroller, this controller has access to all other controller and can be called from anywhere.
     * @return
     */
    public static synchronized AuthenticationController getInstance() {
        if (instance == null) {
            instance = new AuthenticationController();
        }

        return instance;
    }
    private AuthenticationController() {
        accountController = AccountController.getInstance();
        sessionController = SessionController.getInstance();
    }


    public Session logIn(CredentialsRequest authentication)
    {
        Account account;

        try {
            account = accountController.fromMailAddress(authentication.getMailAddress());
        }
        catch (Throwable e) { // NotFoundException
            return null;
        }

        BCrypt bCrypt = new BCrypt();
        boolean bcryptresult = bCrypt.verifyHash(authentication.getPassword(), account.getPasswordHash());
        System.out.println(bcryptresult);
        if (!bcryptresult) {
            return null;
        }

        return sessionController.create(account);
    }

    public void logOut(String sessionKey)
    {
        try {
            Session session = sessionController.fromSessionKey(sessionKey);
            sessionController.delete(session);
        } catch (NotFoundException e) {
            // do nothing
        }
    }

    /**
     * Returns a sesion state, it can be used to see if a user is still logged on.
     * @param sessionKey
     * @return
     */
    public SessionStateResponse getSessionState(String sessionKey)
    {
        try {
            Session session = sessionController.fromSessionKey(sessionKey);
            return new SessionStateResponse(session);
        } catch (NotFoundException e) {
            return new SessionStateResponse(false);
        }
    }

    public void updateSession(String sessionKey) throws NotFoundException {
        Session session = sessionController.fromSessionKey(sessionKey);
        sessionController.updateLastActivity(session);
    }
}
