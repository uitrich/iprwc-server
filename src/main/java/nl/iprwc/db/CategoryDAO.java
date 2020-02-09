package nl.iprwc.db;

import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Category;
import nl.iprwc.sql.DatabaseService;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<Category> getAll() {
        try {
            List<Category> result = new ArrayList<>();
            ResultSet unmodeled = DatabaseService.getInstance()
                    .createPreparedStatement("SELECT * FROM category")
                    .executeQuery();
            while (unmodeled.next()) {
                result.add(new Category(unmodeled.getLong("id"), unmodeled.getString("name")));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Category get(long id) throws NotFoundException{
        try {
            ResultSet resultSet =  DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM category WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery();
            if (resultSet.next()) {
                return new Category(resultSet.getLong("id"), resultSet.getString("name"));
            }
            else throw new NotFoundException();
        } catch (ClassNotFoundException | SQLException e ) {
            e.printStackTrace();
            return null;
        }
    }

    public long post(String name) {
        try {
            boolean result = !DatabaseService.getInstance()
                    .createNamedPreparedStatement("INSERT INTO category (name) VALUES (:name)")
                    .setParameter("name", name)
                    .executeQuery().getString("id").equals("");
            if (result) return getByName(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private long getByName(String name) {
        try {ResultSet result = DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT * FROM category WHERE name = :name")
                .setParameter("name", name)
                .executeQuery();
            if (result.next()) {
                return result.getLong("id");
            }
            System.out.println("cant find " + name + " in database");
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean update(long id, String name) {
        try {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("UPDATE category SET name = :name WHERE id = :id")
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
                    .createNamedPreparedStatement("DELETE FROM category WHERE id = :id")
                    .setParameter("id", id)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int exists(String name) {
        try {
            ResultSet res = DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM category WHERE name = :name")
                    .setParameter("name", name)
                    .executeQuery();
            if (res.next()) return res.getInt("id");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

