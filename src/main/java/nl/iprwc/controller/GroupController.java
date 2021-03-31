package nl.iprwc.controller;

import nl.iprwc.db.GroupDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Account;
import nl.iprwc.model.Group;

import java.sql.SQLException;

public class GroupController {
    private GroupDAO dao;
    private static GroupController instance;

    public static GroupController getInstance() {
        return instance;
    }

    static {
        instance = new GroupController();
    }

    private GroupController() {
        dao = new GroupDAO();
    }

    public boolean deleteAccountGroup(String id) throws InvalidOperationException, NotFoundException {
        try {
            if (id == null) throw new NotFoundException();
            return dao.deleteAccountGroup(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }
    public Group getGroupIdByName(String name) throws NotFoundException, InvalidOperationException {
        try {
            return dao.getGroupIdByName(name);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    public boolean addGroupToAccount(Account account, String groupName) throws InvalidOperationException, NotFoundException {
        try {
            return dao.addGroupToAccount(account, groupName);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
}
