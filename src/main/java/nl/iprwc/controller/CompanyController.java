package nl.iprwc.controller;

import nl.iprwc.db.BodyLocationDAO;
import nl.iprwc.db.CompanyDAO;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Company;

import java.util.List;

public class CompanyController {
    CompanyDAO dao = new CompanyDAO();
    public List<Company> getAll() {
        return dao.getAll();
    }

    public Company get(long id) throws NotFoundException {
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
