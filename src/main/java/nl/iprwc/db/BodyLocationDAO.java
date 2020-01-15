package nl.iprwc.db;

import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Body_Location;
import nl.iprwc.sql.DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BodyLocationDAO {

    public List<Body_Location> getAll() {
        try {
        List<Body_Location> result = new ArrayList<>();
        ResultSet unmodeled = DatabaseService.getInstance()
                .createPreparedStatement("SELECT * FROM body_location")
        .executeQuery();
        while (unmodeled.next()) {
            result.add(new Body_Location(unmodeled.getLong("id"), unmodeled.getString("name")));
            }
        return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Body_Location get(long id) throws NotFoundException{
        try {
            ResultSet resultSet =  DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM body_location WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery();
            if (resultSet.next()) {
                return new Body_Location(resultSet.getLong("id"), resultSet.getString("name"));
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
                    .createNamedPreparedStatement("INSERT INTO body_location VALUES (:name)")
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
                    .createNamedPreparedStatement("UPDATE body_location SET name = :name WHERE id = :id")
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
                    .createNamedPreparedStatement("DELETE FROM body_location WHERE id = :id")
                    .setParameter("id", id)
            .execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

