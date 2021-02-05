package nl.iprwc.controller;

import nl.iprwc.db.GroupDAO;
import nl.iprwc.model.Group;
import nl.iprwc.model.User;

public class GroupController {
    private GroupDAO dao;
    private static GroupController instance;

    public static synchronized GroupController getInstance() {
        if (instance == null) {
            instance = new GroupController();
        }

        return instance;
    }
    private GroupController() {
        dao = new GroupDAO();
    }

    public boolean deleteAccountGroup(String id) {
        return dao.deleteAccountGroup(id);
    }
}
