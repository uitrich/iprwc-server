package nl.iprwc.controller;

import nl.iprwc.model.Account;
import nl.iprwc.model.User;

public class UserController {
 private static UserController instance;
    static {
        instance = new UserController();
    }

    private UserController() {}

    public static UserController getInstance() {
        return instance;
    }

    public User fromAccount(Account account)
    {
        return new User(account);
    }
    public User getAnonymousUser()
    {
        return new User();
    }
}
