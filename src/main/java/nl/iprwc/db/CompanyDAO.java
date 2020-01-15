package nl.iprwc.db;

import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Company;
import nl.iprwc.sql.DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
    public List<Company> getAll() {
        try {
            List<Company> result = new ArrayList<>();
            ResultSet unmodeled = DatabaseService.getInstance()
                    .createPreparedStatement("SELECT * FROM company")
                    .executeQuery();
            while (unmodeled.next()) {
                result.add(new Company(unmodeled.getLong("id"), unmodeled.getString("name")));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Company get(long id) throws NotFoundException{
        try {
            ResultSet resultSet =  DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM company WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery();
            if (resultSet.next()) {
                return new Company(resultSet.getLong("id"), resultSet.getString("name"));
            }
            else throw new NotFoundException();
        } catch (ClassNotFoundException | SQLException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean post(String name) {
        try {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("INSERT INTO company VALUES (:name)")
                    .setParameter("name", name)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(long id, String name) {
        try {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("UPDATE company SET name = :name WHERE id = :id")
                    .setParameter("name", name)
                    .setParameter("id", id)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(long id) {
        try {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("DELETE FROM Company WHERE id = :id")
                    .setParameter("id", id)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

