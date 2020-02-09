package nl.iprwc.controller;

import nl.iprwc.db.BodyLocationDAO;
import nl.iprwc.db.CategoryDAO;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Category;

import java.util.List;

public class CategoryController {
    CategoryDAO dao = new CategoryDAO();

    public int exists(String name) {
        return dao.exists(name);
    }

    public List<Category> getAll() {
        return dao.getAll();
    }

    public Category get(long id) throws NotFoundException {
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

    public long createIfNotExists(Category category) {
        int id = exists(category.getName());
        if ( id == 0) {
            dao.post(category.getName());
            id = exists(category.getName());
        }
        return id;
    }
}
