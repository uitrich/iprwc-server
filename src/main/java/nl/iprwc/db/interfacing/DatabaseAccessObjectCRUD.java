package nl.iprwc.db.interfacing;

import nl.iprwc.exception.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseAccessObjectCRUD<T,I,R> {
    List<T> getAll() throws SQLException, ClassNotFoundException;
    T get(I identifier) throws SQLException, ClassNotFoundException, NotFoundException;
    I create(R creationRequest) throws SQLException, ClassNotFoundException;
    boolean delete(I identifier) throws SQLException, ClassNotFoundException;
    boolean update(R updateValue) throws SQLException, ClassNotFoundException;

    T fromResultSet(ResultSet input) throws SQLException;
}
