package nl.iprwc.controller;

import nl.iprwc.Request.CredentialsRequest;
import nl.iprwc.Response.SessionStateResponse;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.hash.BCrypt;
import nl.iprwc.model.Account;
import nl.iprwc.model.Authentication;
import nl.iprwc.model.Session;

public class AuthenticationController {
    private AccountController accountController;
    private SessionController sessionController;
    private static AuthenticationController instance;


    static {
         instance = new AuthenticationController();
    }
    /**
     * Create a singleton from supercontroller, this controller has access to all other controller and can be called from anywhere.
     * @return
     */
    public static AuthenticationController getInstance() {
        return instance;
    }

    private AuthenticationController() {
        accountController = AccountController.getInstance();
        sessionController = SessionController.getInstance();
    }


    public Session logIn(CredentialsRequest authentication) throws InvalidOperationException, NotFoundException {
        Account account = accountController.fromMailAddress(authentication.getMailAddress());
        if (!new BCrypt().verifyHash(authentication.getPassword(), account.getPasswordHash())) {
            return null;
        }

        return sessionController.create(account);
    }

    public void logOut(String sessionKey) throws NotFoundException, InvalidOperationException {
        Session session = sessionController.fromSessionKey(sessionKey);
        sessionController.delete(session);
    }

    public SessionStateResponse getSessionState(String sessionKey) throws NotFoundException, InvalidOperationException {
        Session session = sessionController.fromSessionKey(sessionKey);
        return new SessionStateResponse(session);
    }

    public void updateSession(String sessionKey) throws NotFoundException, InvalidOperationException {
        Session session = sessionController.fromSessionKey(sessionKey);
        sessionController.updateLastActivity(session);
    }
}
