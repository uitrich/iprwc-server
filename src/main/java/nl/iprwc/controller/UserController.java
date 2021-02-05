package nl.iprwc.controller;

import nl.iprwc.model.Account;
import nl.iprwc.model.User;

public class UserController {
 private static UserController instance;
    public static synchronized UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }

        return instance;
    }
    private UserController() {    }
    /**
     * Make a new user model based on account.
     * @param account
     * @return
     */
    public User fromAccount(Account account)
    {
        return new User(account);
    }

    /**
     * The the user if it's not logged in.
     * @return
     */
    public User getAnonymousUser()
    {
        return new User();
    }
}
