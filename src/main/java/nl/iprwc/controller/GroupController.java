package nl.iprwc.controller;

import nl.iprwc.db.GroupDAO;
import nl.iprwc.model.User;

public class GroupController {
    GroupDAO dao;
    GroupController() {
        dao = new GroupDAO();
    }

    public boolean deleteAccountGroup(long id) {
        return dao.deleteAccountGroup(id);
    }
}
