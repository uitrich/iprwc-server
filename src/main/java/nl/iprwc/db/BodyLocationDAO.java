package nl.iprwc.db;

import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.BodyLocation;
import nl.iprwc.sql.DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BodyLocationDAO {

    public List<BodyLocation> getAll() throws SQLException, ClassNotFoundException {
        List<BodyLocation> result = new ArrayList<>();
        ResultSet unmodeled = DatabaseService.getInstance()
                .createPreparedStatement("SELECT * FROM body_location")
        .executeQuery();
        while (unmodeled.next()) {
            result.add(new BodyLocation(unmodeled.getLong("id"), unmodeled.getString("name")));
            }
        return result;
    }

    public BodyLocation get(long id) throws NotFoundException, SQLException, ClassNotFoundException {
            ResultSet resultSet =  DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM body_location WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery();
            if (resultSet.next()) {
                return new BodyLocation(resultSet.getLong("id"), resultSet.getString("name"));
            }
            else throw new NotFoundException();
    }

    public long post(String name) throws SQLException, ClassNotFoundException {
            ResultSet results = DatabaseService.getInstance()
                    .createNamedPreparedStatement("INSERT INTO body_location (name) VALUES (:name)")
                    .setParameter("name", name)
            .executeQuery();
            results.next();
            return results.getLong("id");

    }

    public boolean update(long id, String name) throws SQLException, ClassNotFoundException {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("UPDATE body_location SET name = :name WHERE id = :id")
                    .setParameter("name", name)
                    .setParameter("id", id)
                    .execute();

    }

    public boolean delete(long id) throws SQLException, ClassNotFoundException {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("DELETE FROM body_location WHERE id = :id")
                    .setParameter("id", id)
            .execute();

    }

    public int exists(String name) throws SQLException, ClassNotFoundException {
            ResultSet resultSet =  DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM body_location WHERE name = :name")
                    .setParameter("name", name)
                    .executeQuery();
            if (resultSet.next()) return resultSet.getInt("id");
        return 0;
    }
}

