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
        return new BCrypt().verifyHash(authentication.getPassword(), account.getPasswordHash()) ?
                sessionController.create(account) : null;
    }

    public void logOut(String sessionKey) throws NotFoundException, InvalidOperationException {
        sessionController.delete(sessionController.fromSessionKey(sessionKey));
    }

    public SessionStateResponse getSessionState(String sessionKey) throws NotFoundException, InvalidOperationException {
        return new SessionStateResponse(sessionController.fromSessionKey(sessionKey));
    }

    public void updateSession(String sessionKey) throws NotFoundException, InvalidOperationException {
        sessionController.updateLastActivity(sessionController.fromSessionKey(sessionKey));
    }
}
