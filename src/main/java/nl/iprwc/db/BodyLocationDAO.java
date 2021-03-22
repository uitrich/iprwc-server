package nl.iprwc.db;

import nl.iprwc.db.abstractions.DatabaseAccessObjectCRUD;
import nl.iprwc.model.BodyLocation;
import nl.iprwc.sql.DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BodyLocationDAO implements DatabaseAccessObjectCRUD<BodyLocation, Long, BodyLocation> {

    @Override
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

    @Override
    public BodyLocation get(Long identifier) throws SQLException, ClassNotFoundException {
        return fromResultSet(DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT * FROM body_location WHERE id = :id")
                .setParameter("id", identifier)
                .executeQuery());
    }

    @Override
    public Long create(BodyLocation creationRequest) throws SQLException, ClassNotFoundException {
        ResultSet results = DatabaseService.getInstance()
                .createNamedPreparedStatement("INSERT INTO body_location (name) VALUES (:name)")
                .setParameter("name", creationRequest.getName())
                .executeQuery();
        results.next();
        return results.getLong("id");
    }

    @Override
    public boolean delete(Long identifier) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement("DELETE FROM body_location WHERE id = :id")
                .setParameter("id", identifier)
                .execute();
    }

    @Override
    public boolean update(BodyLocation updateValue) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement("UPDATE body_location SET name = :name WHERE id = :id")
                .setParameter("name", updateValue.getName())
                .setParameter("id", updateValue.getId())
                .executeUpdate() > 0;
    }

    @Override
    public BodyLocation fromResultSet(ResultSet input) throws SQLException {
        if (input.next()) {
            return new BodyLocation(input.getLong("id"), input.getString("name"));
        }
        else return null;
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

