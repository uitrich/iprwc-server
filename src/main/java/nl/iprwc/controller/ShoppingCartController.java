package nl.iprwc.controller;

import nl.iprwc.db.ShoppingCartDAO;
import nl.iprwc.model.Product;
import nl.iprwc.model.ProductResponse;
import nl.iprwc.model.User;
import nl.iprwc.sql.DatabaseService;

import java.sql.SQLException;
import java.util.List;

public class ShoppingCartController {
    ShoppingCartDAO dao;

    ShoppingCartController() {
        dao = new ShoppingCartDAO();
    }
    public List<ProductResponse> addItem(long id, long user) {
        if (!dao.isItemAlreadyIn(id, user)) {
            return dao.AddToCart( user, id);
        }
        dao.addQuantity(id, user);
        return dao.getShoppingCartContents(user);
    }
    public List<ProductResponse> getShoppingcart(User user) {
     return dao.getShoppingCartContents(user.getAccount().getId());
    }
    public List<ProductResponse> getShoppingcart(long user) {
        return dao.getShoppingCartContents(user);
    }

    public List<ProductResponse> deleteItem(long id, long user) {
        return dao.deleteItem(id, user);
    }

    public long updateQuantity(long productid, long amount, long id) {
        return dao.updateQuantity(productid, amount, id);
    }

    public List<List<Long>> getQuantity(long id) {
        return dao.getQuantities(id);
    }

    public boolean delete(long id) {
        try {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("DELETE FROM shoppingcart WHERE account_id = :id")
                    .setParameter("id", id)
            .execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
