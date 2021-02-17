package nl.iprwc.controller;

import nl.iprwc.db.BodyLocationDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.BodyLocation;

import java.sql.SQLException;
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
    public List<BodyLocation> getAll() throws InvalidOperationException {
        try {
            return dao.getAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public BodyLocation get(long id) throws NotFoundException, InvalidOperationException {
        try {
            return dao.get(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public boolean update(long id, String name) throws InvalidOperationException {
        try {
            return dao.update(id, name);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public long post(String name) throws InvalidOperationException {
        try {
            return dao.post(name);
        }  catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public boolean delete(long id) throws InvalidOperationException {
        try {
            return dao.delete(id);
        }  catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public long createIfNotExists(BodyLocation body_location) throws InvalidOperationException, NotFoundException {
        int id = exists(body_location.getName());
        if (id == 0) {
            try {
                dao.post(body_location.getName());
            } catch (SQLException | ClassNotFoundException e) {
                throw new InvalidOperationException();
            }
            id = exists(body_location.getName());
        }
        return id;
    }

    private int exists(String name) throws InvalidOperationException, NotFoundException {
        try {
            return dao.exists(name);
        }  catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
}
