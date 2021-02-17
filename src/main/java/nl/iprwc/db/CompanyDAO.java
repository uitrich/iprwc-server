package nl.iprwc.db;

import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Company;
import nl.iprwc.sql.DatabaseService;

import javax.ws.rs.NotAuthorizedException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompanyDAO {
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

    public Company get(long id) throws NotFoundException, SQLException, ClassNotFoundException {
            ResultSet resultSet =  DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM company WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery();
            if (resultSet.next()) {
                return new Company(resultSet.getLong("id"), resultSet.getString("name"));
            }
            else throw new NotFoundException();
    }


    public long post(String name) throws SQLException, ClassNotFoundException {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("INSERT INTO company (name) VALUES (:name)")
                    .setParameter("name", name)
                    .executeUpdate();
    }

    public boolean update(long id, String name) throws SQLException, ClassNotFoundException {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("UPDATE company SET name = :name WHERE id = :id")
                    .setParameter("name", name)
                    .setParameter("id", id)
                    .execute();

    }

    public boolean delete(long id) throws SQLException, ClassNotFoundException {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("DELETE FROM Company WHERE id = :id")
                    .setParameter("id", id)
                    .execute();

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

