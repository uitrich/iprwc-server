package nl.iprwc.controller;

import nl.iprwc.db.BodyLocationDAO;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Body_Location;

import java.util.List;

public class BodyLocationController {
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

    public boolean post(String name) {
        return dao.post(name);
    }

    public boolean delete(long id) {
        return dao.delete(id);
    }
}
