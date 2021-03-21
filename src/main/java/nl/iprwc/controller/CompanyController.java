package nl.iprwc.controller;

import nl.iprwc.controller.interfacing.SimpleCRUDController;
import nl.iprwc.db.CompanyDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Company;

import java.sql.SQLException;
import java.util.List;

public class CompanyController implements SimpleCRUDController<Company, Long, Company> {
    private CompanyDAO dao;
    private static CompanyController instance;

    static {
        instance = new CompanyController();
    }

    private CompanyController() {
        dao = new CompanyDAO();
    }

    public static CompanyController getInstance() {
        return instance;
    }

    @Override
    public List<Company> getAll() throws InvalidOperationException {
        try {
            return dao.getAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    @Override
    public Company get(Long id) throws NotFoundException, InvalidOperationException {
        try {
            return dao.get(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    @Override
    public boolean update(Company updateValue) throws InvalidOperationException {
        try {
            return dao.update(updateValue);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }
    @Override
    public Long create(Company creation) throws InvalidOperationException {
        try {
            return dao.create(creation);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }
    @Override
    public boolean delete(Long id) throws InvalidOperationException {
        try {
            return dao.delete(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException(e.getMessage());
        }
    }

    public long createIfNotExists(Company company) throws InvalidOperationException {
        int id = exists(company.getName());
        if (id == 0) {
            try {
                dao.create(company);
            } catch (SQLException | ClassNotFoundException e) {
                throw new InvalidOperationException(e.getMessage());
            }
            id = exists(company.getName());
        }
        return id;
    }

    private int exists(String name) throws InvalidOperationException {
        try {
            return dao.exists(name);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
}
