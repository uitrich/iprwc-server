package nl.iprwc.db;

import nl.iprwc.controller.ProductController;
import nl.iprwc.controller.SuperController;
import nl.iprwc.model.Product;
import nl.iprwc.model.ProductResponse;
import nl.iprwc.model.User;
import nl.iprwc.sql.DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ShoppingCartDAO {
    ProductController productController = SuperController.getInstance().getProductController();
    public List<ProductResponse> AddToCart(String accountId, long productId) {
        try {
            boolean result = DatabaseService.getInstance()
                    .createNamedPreparedStatement("INSERT INTO shopping_cart (account_id, product_id) VALUES (:account_id, :product_id)")
                    .setParameter("account_id", accountId)
                    .setParameter("product_id", productId)
            .execute();
            if (result) {
                return getShoppingCartContents(accountId);
            }
            return null;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ProductResponse> getShoppingCartContents(String accountId) {
        List<Product> results = new ArrayList<>();
        try {
            ResultSet result = DatabaseService.getInstance().createNamedPreparedStatement("SELECT * FROM shopping_cart WHERE account_id = :id")
            .setParameter("id", accountId)
            .executeQuery();
            while (result.next()) {
                results.add(productController.getFromId(result.getLong("product_id")));
            }

            return productController.populateList(results);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<ProductResponse> deleteItem(long id, String user) {
        try {
            DatabaseService.getInstance().createNamedPreparedStatement("DELETE FROM shopping_cart WHERE product_id = :id AND account_id = :userId" )
                    .setParameter("id", id)
                    .setParameter("userId", user)
            .executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return getShoppingCartContents(user);
    }

    public List<List<Long>> addQuantity(long productid, String id) {
        try {
            DatabaseService.getInstance()
                    .createNamedPreparedStatement("UPDATE shopping_cart SET quantity = quantity +1 WHERE account_id = :id AND product_id = :productid")
                    .setParameter("id", id)
                    .setParameter("productid", productid)
                    .executeUpdate();
            return getQuantities(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public long updateQuantity(long productid, long amount, String id) {
        try {
            DatabaseService.getInstance()
                    .createNamedPreparedStatement("UPDATE shopping_cart SET quantity = :amount WHERE account_id = :id AND product_id = :productid")
                    .setParameter("amount", amount)
                    .setParameter("id", id)
                    .setParameter("productid", productid)
            .executeUpdate();
            return getItemQuantity(id, productid);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    public List<List<Long>> getQuantities(String accountId) {
        try {
            ResultSet results = DatabaseService.getInstance().createNamedPreparedStatement("SELECT * FROM shopping_cart WHERE account_id = :id")
                    .setParameter("id", accountId)
            .executeQuery();
            List<List<Long>> result = new ArrayList<>();
            while (results.next()) {
                List<Long> nestedList = new ArrayList<>();
                nestedList.add(results.getLong("quantity"));
                nestedList.add(results.getLong("product_id"));
                result.add(nestedList);
            }
            System.out.println(result.toString());
            return result;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean isItemAlreadyIn(long id, String userId) {
        try {
            ResultSet results = DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT id FROM shopping_cart " +
                            "WHERE account_id = :userId AND product_id = :id")
                    .setParameter("userId", userId)
                    .setParameter("id", id)
            .executeQuery();
            return results.next();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    private long getItemQuantity(String accountId, long productId) {
        try {
            ResultSet resultSet = DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT quantity FROM shopping_cart " +
                            "WHERE account_id = :accountId AND product_id = :productId")
                    .setParameter("accountId", accountId)
                    .setParameter("productId", productId)
            .executeQuery();
            resultSet.next();
            return resultSet.getLong("quantity");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return 1;
        }
    }

}
