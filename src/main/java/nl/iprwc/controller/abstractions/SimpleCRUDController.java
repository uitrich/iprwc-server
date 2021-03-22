package nl.iprwc.controller.abstractions;

import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface SimpleCRUDController<T,I,R> {
    List<T> getAll() throws SQLException, ClassNotFoundException, InvalidOperationException;
    T get(I identifier) throws SQLException, ClassNotFoundException, InvalidOperationException, NotFoundException;
    I create(R creationRequest) throws SQLException, ClassNotFoundException, InvalidOperationException, NotFoundException;
    boolean delete(I identifier) throws SQLException, ClassNotFoundException, InvalidOperationException, NotFoundException;
    boolean update(R updateValue) throws SQLException, ClassNotFoundException, InvalidOperationException, NotFoundException;
}
