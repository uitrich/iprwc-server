package nl.iprwc.controller;

import nl.iprwc.controller.interfacing.SimpleCRUDController;
import nl.iprwc.db.CategoryDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryController implements SimpleCRUDController<Category, Long, Category> {
    private CategoryDAO dao;
    private static CategoryController instance;

    static {
        instance = new CategoryController();
    }

    private CategoryController() {
        dao = new CategoryDAO();
    }

    public static CategoryController getInstance() {
        return instance;
    }

    public boolean exists(String name) throws InvalidOperationException {
        try {
            return dao.exists(name);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    @Override
    public List<Category> getAll() throws InvalidOperationException {
        try {
            return dao.getAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    @Override
    public Category get(Long id) throws NotFoundException, InvalidOperationException {
        try {
            return dao.get(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    @Override
    public boolean update(Category updateValue) throws InvalidOperationException {
        try {
            return dao.update(updateValue);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    @Override
    public Long create(Category creation) throws InvalidOperationException {
        try {
            return dao.create(creation);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
    @Override
    public boolean delete(Long id) throws InvalidOperationException {
        try {
            return dao.delete(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public long createIfNotExists(Category category) throws InvalidOperationException, NotFoundException {
        try {
            if (!exists(category.getName())) {
                    dao.create(category);
            }
            return dao.getByName(category.getName());
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
}
