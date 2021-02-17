package nl.iprwc.db;

import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Category;
import nl.iprwc.sql.DatabaseService;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> getAll() throws SQLException, ClassNotFoundException {
            List<Category> result = new ArrayList<>();
            ResultSet unmodeled = DatabaseService.getInstance()
                    .createPreparedStatement("SELECT * FROM category")
                    .executeQuery();
            while (unmodeled.next()) {
                result.add(new Category(unmodeled.getLong("id"), unmodeled.getString("name")));
            }
            return result;
    }

    public Category get(long id) throws NotFoundException, SQLException, ClassNotFoundException {
        ResultSet resultSet =  DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT * FROM category WHERE id = :id")
                .setParameter("id", id)
                .executeQuery();
        if (resultSet.next()) {
            return new Category(resultSet.getLong("id"), resultSet.getString("name"));
        }
        else throw new NotFoundException();
    }

    public void post(String name) throws SQLException, ClassNotFoundException {
        boolean result = !DatabaseService.getInstance()
                .createNamedPreparedStatement("INSERT INTO category (name) VALUES (:name)")
                .setParameter("name", name)
                .execute();
        if (!result) throw new SQLException();
    }

    public long getByName(String name) throws SQLException, ClassNotFoundException, NotFoundException {
        ResultSet result = getResultSetFromName(name);
        if (result.next()) {
            return result.getLong("id");
        }
        throw new NotFoundException();
    }

    public boolean update(long id, String name) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement("UPDATE category SET name = :name WHERE id = :id")
                .setParameter("name", name)
                .setParameter("id", id)
                .execute();
    }

    public boolean delete(long id) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement("DELETE FROM category WHERE id = :id")
                .setParameter("id", id)
                .execute();
    }

    public boolean exists(String name) throws SQLException, ClassNotFoundException {
        return getResultSetFromName(name).next();
    }

    private ResultSet getResultSetFromName(String name) throws SQLException, ClassNotFoundException {
        ResultSet r = DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT * FROM category WHERE name = :name")
                .setParameter("name", name)
                .executeQuery();
        return r;
    }
}

