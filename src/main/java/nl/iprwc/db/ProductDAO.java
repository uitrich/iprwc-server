package nl.iprwc.db;

import javafx.util.Pair;
import nl.iprwc.model.Product;
import nl.iprwc.sql.DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductDAO {
    private List<Product> fromResultSets(ResultSet result) throws SQLException
    {
        List<Product> returns = new ArrayList<>();
        while(result.next()) {
            returns.add(
                    new Product(
                    result.getLong("id"),
                    result.getString("name"),
                    result.getString("description"),
                    result.getString("category"),
                    result.getDouble("price"),
                    result.getLong("colourRangeId")
                )
            );
        }
        return  returns;
    }
    private Product fromResultSet(ResultSet result) throws SQLException
    {
        return new
                Product(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("description"),
                        result.getString("category"),
                        result.getDouble("price"),
                        result.getLong("colourRangeId")
                    );
    }
    public Product getSingle(long id) throws SQLException, ClassNotFoundException {
        return fromResultSet(DatabaseService
                .getInstance()
                .createNamedPreparedStatement("SELECT * FROM Product WHERE id = :id")
                .setParameter("id", id)
                .executeQuery()
        );
    }

    public List<Product> getMultiple(long maxPrice, long minPrice, String category, String name) throws SQLException, ClassNotFoundException {
              String queryContents = maxPrice != 0 && minPrice != 0 ? byPrice(maxPrice, minPrice) : "";
              queryContents += category != null ? byCategory(category) : "";
              queryContents += name != null ? byName(name) : "";
              return fromResultSets(
                      DatabaseService
                              .getInstance()
                              .createPreparedStatement(queryContents)
                              .executeQuery()
              );
    }
    private String byPrice(long maxPrice, long minPrice) {
        return "price > " + minPrice + " AND price < "+ maxPrice;
    }
    private String byCategory(String searchString) {
        return "category IS LIKE %" + searchString + "%";
    }
    private String byName(String searchString) {
        return "name IS LIKE %" +searchString + "%";
    }
}

