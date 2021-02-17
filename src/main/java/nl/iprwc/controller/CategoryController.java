package nl.iprwc.controller;

import nl.iprwc.db.BodyLocationDAO;
import nl.iprwc.db.CategoryDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryController {
    private CategoryDAO dao;
    private static CategoryController instance;

    public static synchronized CategoryController getInstance() {
        if (instance == null) {
            instance = new CategoryController();
        }

        return instance;
    }
    private CategoryController() {
        dao = new CategoryDAO();
    }

    public boolean exists(String name) throws InvalidOperationException {
        try {
            return dao.exists(name);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public List<Category> getAll() throws InvalidOperationException {
        try {
            return dao.getAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public Category get(long id) throws NotFoundException, InvalidOperationException {
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

    public void post(String name) throws InvalidOperationException {
        try {
            dao.post(name);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public boolean delete(long id) throws InvalidOperationException {
        try {
            return dao.delete(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public long createIfNotExists(Category category) throws InvalidOperationException, NotFoundException {
        try {
            if (!exists(category.getName())) {
                    dao.post(category.getName());
            }
            return dao.getByName(category.getName());
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
}
