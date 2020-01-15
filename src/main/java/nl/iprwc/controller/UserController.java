package nl.iprwc.controller;

import nl.iprwc.model.Account;
import nl.iprwc.model.User;

public class UserController {

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
