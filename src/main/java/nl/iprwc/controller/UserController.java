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
    private UserController() {}

    public User fromAccount(Account account)
    {
        return new User(account);
    }
    public User getAnonymousUser()
    {
        return new User();
    }
}
