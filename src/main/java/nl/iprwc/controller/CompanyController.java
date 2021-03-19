package nl.iprwc.controller;

import nl.iprwc.db.CompanyDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Company;

import java.sql.SQLException;
import java.util.List;

public class CompanyController {
    private CompanyDAO dao;
    private static CompanyController instance;

    public static synchronized CompanyController getInstance() {
        if (instance == null) {
            instance = new CompanyController();
        }

        return instance;
    }
    private CompanyController() {
        dao = new CompanyDAO();
    }
    public List<Company> getAll() throws InvalidOperationException {
        try {
            return dao.getAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public Company get(long id) throws NotFoundException, InvalidOperationException {
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

    public long create(String name) throws InvalidOperationException {
        try {
            return dao.create(name);
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

    public long createIfNotExists(Company company) throws InvalidOperationException {
        int id = exists(company.getName());
        if (id == 0) {
            try {
                dao.create(company.getName());
            } catch (SQLException | ClassNotFoundException e) {
                throw new InvalidOperationException();
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
