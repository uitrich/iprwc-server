package nl.iprwc.db;

import nl.iprwc.db.interfacing.DatabaseAccessObjectCRUD;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Company;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO implements DatabaseAccessObjectCRUD<Company, Long, Company> {
    @Override
    public List<Company> getAll() throws SQLException, ClassNotFoundException {
        List<Company> result = new ArrayList<>();
        ResultSet unmodeled = DatabaseService.getInstance()
                .createPreparedStatement("SELECT * FROM company")
                .executeQuery();
        while (unmodeled.next()) {
            result.add(new Company(unmodeled.getLong("id"), unmodeled.getString("name")));
        }
        return result;
    }
    @Override
    public Company get(Long id) throws NotFoundException, SQLException, ClassNotFoundException {
        return fromResultSet(DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT * FROM company WHERE id = :id")
                .setParameter("id", id)
                .executeQuery());
    }

    @Override
    public boolean delete(Long identifier) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement("DELETE FROM Company WHERE id = :id")
                .setParameter("id", identifier)
                .executeUpdate() > 0;
    }


    @Override
    public Long create(Company creation) throws SQLException, ClassNotFoundException {
        NamedParameterStatement statement =  DatabaseService.getInstance()
                .createNamedPreparedStatement("INSERT INTO company (name) VALUES (:name)")
                .setParameter("name", creation.getName());
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        if (keys.next()) return keys.getLong("id");
        else throw new SQLException("No keys were generated in the creation of a category");
    }

    @Override
    public boolean update(Company updateValue) throws SQLException, ClassNotFoundException {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("UPDATE company SET name = :name WHERE id = :id")
                    .setParameter("name", updateValue.getName())
                    .setParameter("id", updateValue.getId())
                    .executeUpdate() > 0;
    }

    @Override
    public Company fromResultSet(ResultSet input) throws SQLException {
        if (input.next())
            return new Company(input.getLong("id"), input.getString("name"));
        return null;
    }


    public int exists(String name) throws SQLException, ClassNotFoundException {
            ResultSet res = DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM company WHERE name = :name")
                    .setParameter("name", name)
                    .executeQuery();
            if (res.next()) return res.getInt("id");
        return 0;
    }
}

