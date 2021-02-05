package nl.iprwc.controller;

import nl.iprwc.db.BodyLocationDAO;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Body_Location;

import java.util.List;

public class BodyLocationController {
    private static BodyLocationController instance;

    /**
     * Create a singleton from supercontroller, this controller has access to all other controller and can be called from anywhere.
     * @return
     */
    public static synchronized BodyLocationController getInstance() {
        if (instance == null) {
            instance = new BodyLocationController();
        }

        return instance;
    }
    private BodyLocationController() {
        dao = new BodyLocationDAO();
    }

    BodyLocationDAO dao = new BodyLocationDAO();
    public List<Body_Location> getAll() {
        return dao.getAll();
    }

    public Body_Location get(long id) throws NotFoundException {
        return dao.get(id);
    }

    public boolean update(long id, String name) {
        return dao.update(id, name);
    }

    public long post(String name) {
        return dao.post(name);
    }

    public boolean delete(long id) {
        return dao.delete(id);
    }

    public long createIfNotExists(Body_Location body_location) {
        int id = exists(body_location.getName());
        if (id == 0) {
            dao.post(body_location.getName());
            id = exists(body_location.getName());
        }
        return id;
    }

    private int exists(String name) {
        return dao.exists(name);
    }
}
