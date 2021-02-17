package nl.iprwc.db;

import nl.iprwc.controller.ProductController;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Product;
import nl.iprwc.model.ProductResponse;
import nl.iprwc.model.User;
import nl.iprwc.sql.DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDAO {
    ProductController productController = ProductController.getInstance();
    public void AddToCart(String accountId, long productId) throws SQLException, ClassNotFoundException, NotFoundException {
        boolean result = DatabaseService.getInstance()
                .createNamedPreparedStatement("INSERT INTO shopping_cart (account_id, product_id) VALUES (:account_id, :product_id)")
                .setParameter("account_id", accountId)
                .setParameter("product_id", productId)
        .execute();
        if (!result)
            throw new NotFoundException();
    }

    public List<Long> getShoppingCartContents(String accountId) throws SQLException, ClassNotFoundException {
        List<Long> productIds = new ArrayList<>();
            ResultSet result = DatabaseService.getInstance().createNamedPreparedStatement("SELECT * FROM shopping_cart WHERE account_id = :id")
            .setParameter("id", accountId)
            .executeQuery();
            while (result.next()) {
                productIds.add(result.getLong("product_id"));
            }
        return productIds;
    }

    public void deleteItem(long id, String user) throws SQLException, ClassNotFoundException, NotFoundException {
        if (!DatabaseService.getInstance().createNamedPreparedStatement("DELETE FROM shopping_cart WHERE product_id = :id AND account_id = :userId" )
                .setParameter("id", id)
                .setParameter("userId", user)
        .execute()) throw new NotFoundException();
    }

    public void addQuantity(long productid, String id) throws SQLException, ClassNotFoundException, NotFoundException {
            int affectedRows = DatabaseService.getInstance()
                    .createNamedPreparedStatement("UPDATE shopping_cart SET quantity = quantity +1 WHERE account_id = :id AND product_id = :productid")
                    .setParameter("id", id)
                    .setParameter("productid", productid)
                    .executeUpdate();
            if (affectedRows <= 0) throw new NotFoundException();
    }

    public void updateQuantity(long productid, long amount, String id) throws SQLException, ClassNotFoundException, NotFoundException {
        int affectedRows = DatabaseService.getInstance()
                .createNamedPreparedStatement("UPDATE shopping_cart SET quantity = :amount WHERE " +
                        "account_id = :id AND product_id = :productid")
                .setParameter("amount", amount)
                .setParameter("id", id)
                .setParameter("productid", productid)
        .executeUpdate();
        if (affectedRows <= 0)
            throw new NotFoundException();
    }
    public List<List<Long>> getQuantities(String accountId) throws SQLException, ClassNotFoundException {
        ResultSet results = DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT * FROM shopping_cart WHERE account_id = :id")
                .setParameter("id", accountId)
        .executeQuery();
        List<List<Long>> result = new ArrayList<>();
        while (results.next()) {
            List<Long> nestedList = new ArrayList<>();
            nestedList.add(results.getLong("quantity"));
            nestedList.add(results.getLong("product_id"));
            result.add(nestedList);
        }
        return result;
    }

    public boolean isItemAlreadyIn(long id, String userId) throws SQLException, ClassNotFoundException {
        ResultSet results = DatabaseService.getInstance()
            .createNamedPreparedStatement("SELECT id FROM shopping_cart " +
                    "WHERE account_id = :userId AND product_id = :id")
            .setParameter("userId", userId)
            .setParameter("id", id)
            .executeQuery();
        return results.next();
    }
    private long getItemQuantity(String accountId, long productId) throws SQLException, ClassNotFoundException, NotFoundException {
        ResultSet resultSet = DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT quantity FROM shopping_cart " +
                        "WHERE account_id = :accountId AND product_id = :productId")
                .setParameter("accountId", accountId)
                .setParameter("productId", productId)
        .executeQuery();
        if (resultSet.next())
            return resultSet.getLong("quantity");
        throw new NotFoundException();
    }

    public boolean deleteAccount(String id) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement("DELETE FROM shoppingcart WHERE account_id = :id")
                .setParameter("id", id)
                .execute();
    }
}
