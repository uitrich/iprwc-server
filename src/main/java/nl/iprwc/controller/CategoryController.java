package nl.iprwc.controller;

import nl.iprwc.db.BodyLocationDAO;
import nl.iprwc.db.CategoryDAO;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Category;

import java.util.List;

public class CategoryController {
    CategoryDAO dao = new CategoryDAO();
    public List<Category> getAll() {
        return dao.getAll();
    }

    public Category get(long id) throws NotFoundException {
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
