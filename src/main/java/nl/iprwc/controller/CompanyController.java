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

    public long post(String name) {
        return dao.post(name);
    }

    public boolean delete(long id) {
        return dao.delete(id);
    }

    public long createIfNotExists(Company company) {
        int id = exists(company.getName());
        if (id == 0) {
            dao.post(company.getName());
            id = exists(company.getName());
        }
        return id;
    }

    private int exists(String name) {
        return dao.exists(name);
    }
}
