package nl.iprwc.controller;

import nl.iprwc.Response.ProductResponse;
import nl.iprwc.db.ShoppingCartDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Product;
import nl.iprwc.model.User;
import nl.iprwc.sql.DatabaseService;

import java.sql.SQLException;
import java.util.List;

public class ShoppingCartController {
    private ShoppingCartDAO dao;
    private static ShoppingCartController instance;

    static {
        instance = new ShoppingCartController();
    }

    private ShoppingCartController() {
        dao = new ShoppingCartDAO();
    }

    public static ShoppingCartController getInstance() {
        return instance;
    }

    public void addItem(long id, String user) throws NotFoundException, InvalidOperationException {
        try {
            if (!dao.isItemAlreadyIn(id, user)) {
                dao.AddToCart( user, id);
            }
            else dao.addQuantity(id, user);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
    public List<ProductResponse> getShoppingcart(String user) throws NotFoundException, InvalidOperationException {
        try {
            return ProductController.getInstance().getFromIds(dao.getShoppingCartContents(user));
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public void deleteItem(long id, String user) throws NotFoundException, InvalidOperationException {
        try {
            dao.deleteItem(id, user);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public void updateQuantity(long productid, long amount, String id) throws NotFoundException, InvalidOperationException {
        try {
            dao.updateQuantity(productid, amount, id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public List<List<Long>> getQuantity(String id) throws InvalidOperationException {
        try {
            return dao.getQuantities(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public boolean updateSales(List<List<Long>> quantities, String userId) throws InvalidOperationException {
        boolean success = true;
        try {
            for (List<Long> qtyPair : quantities) {
                success = success && dao.updateSales(qtyPair.get(1), qtyPair.get(0));
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
        if (success) delete(userId);
        return success;
    }

    public boolean delete(String id) throws InvalidOperationException {
        try {
            return dao.deleteAccount(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
}
