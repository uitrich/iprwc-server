package nl.iprwc.db;

import nl.iprwc.db.interfacing.DatabaseAccessObjectCRUD;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Category;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO implements DatabaseAccessObjectCRUD<Category, Long, Category> {

    @Override
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
    @Override
    public Category get(Long id) throws NotFoundException, SQLException, ClassNotFoundException {
        return fromResultSet(DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT * FROM category WHERE id = :id")
                .setParameter("id", id)
                .executeQuery());
    }

    @Override
    public Long create(Category creation) throws SQLException, ClassNotFoundException {
        NamedParameterStatement statement =  DatabaseService.getInstance()
                .createNamedPreparedStatement("INSERT INTO category (name) VALUES (:name)")
                .setParameter("name", creation.getName());
        statement.executeUpdate();
        ResultSet keys = statement.getGeneratedKeys();
        if (keys.next()) return keys.getLong("id");
        else throw new SQLException("No keys were generated in the creation of a category");
    }

    @Override
    public boolean delete(Long identifier) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement("DELETE FROM category WHERE id = :id")
                .setParameter("id", identifier)
                .execute();
    }

    @Override
    public boolean update(Category updateValue) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement("UPDATE category SET name = :name WHERE id = :id")
                .setParameter("name", updateValue.getName())
                .setParameter("id", updateValue.getId())
                .executeUpdate() > 0;
    }

    @Override
    public Category fromResultSet(ResultSet input) throws SQLException {
        if (input.next())
            return new Category(input.getLong("id"), input.getString("name"));
        return null;
    }

    public long getByName(String name) throws SQLException, ClassNotFoundException, NotFoundException {
        ResultSet result = getResultSetFromName(name);
        if (result.next()) {
            return result.getLong("id");
        }
        throw new NotFoundException();
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

