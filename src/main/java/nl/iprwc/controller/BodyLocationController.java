package nl.iprwc.controller;

import nl.iprwc.controller.abstractions.SimpleCRUDController;
import nl.iprwc.db.BodyLocationDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.BodyLocation;

import java.sql.SQLException;
import java.util.List;

public class BodyLocationController implements SimpleCRUDController<BodyLocation, Long, BodyLocation> {
    private static BodyLocationController instance;
    private BodyLocationDAO dao;

    static {
        instance = new BodyLocationController();
    }

    private BodyLocationController() {
        dao = new BodyLocationDAO();
    }

    public static BodyLocationController getInstance() {
        return instance;
    }

    @Override
    public List<BodyLocation> getAll() throws InvalidOperationException {
        try {
            return dao.getAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    @Override
    public BodyLocation get(Long id) throws InvalidOperationException {
        try {
            return dao.get(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }
    @Override
    public boolean update(BodyLocation bodyLocation) throws InvalidOperationException {
        try {
            return dao.update(bodyLocation);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    @Override
    public Long create(BodyLocation creationValue) throws InvalidOperationException {
        try {
            return dao.create(creationValue);
        }  catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    @Override
    public boolean delete(Long id) throws InvalidOperationException {
        try {
            return dao.delete(id);
        }  catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    public long createIfNotExists(BodyLocation body_location) throws InvalidOperationException, NotFoundException {
        int id = exists(body_location.getName());
        if (id == 0) {
            try {
                dao.create(body_location);
            } catch (SQLException | ClassNotFoundException e) {
                throw new InvalidOperationException(e.getMessage());
            }
            id = exists(body_location.getName());
        }
        return id;
    }

    private int exists(String name) throws InvalidOperationException, NotFoundException {
        try {
            return dao.exists(name);
        }  catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }
}
