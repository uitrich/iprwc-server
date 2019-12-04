package nl.iprwc.controller;

import nl.iprwc.db.AccountDAO;
import nl.iprwc.model.*;

import java.sql.SQLException;

public class AccountController {
    private final AccountDAO dao;
    private final SuperController superController;

    public AccountController() {
        dao = new AccountDAO();
        superController = SuperController.getInstance();
    }
    public Account getFromId(long id) {
        return dao.getFromId(id);
    }
    public Account tryLogin(Authenticate auth) throws SQLException, ClassNotFoundException {
        return dao.tryLogin(auth.getUsername(), auth.getPassword());
    }
}
