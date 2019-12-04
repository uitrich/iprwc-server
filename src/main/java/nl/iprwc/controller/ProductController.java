package nl.iprwc.controller;

import nl.iprwc.db.ProductDAO;
import nl.iprwc.model.Product;

import java.sql.SQLException;

public class ProductController {
    private final ProductDAO dao;


    public ProductController() {
        dao = new ProductDAO();
    }

    public Product getFromId(long id) throws SQLException, ClassNotFoundException {
        return dao.getSingle(id);
    }

}
