package nl.iprwc.db;

import io.dropwizard.jersey.params.LongParam;
import javafx.util.Pair;
import nl.iprwc.Utils.BaseImageTranslator;
import nl.iprwc.Utils.Paginated;
import nl.iprwc.model.*;
import nl.iprwc.model.ProductResponse;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ProductDAO {
    private List<Product> fromResultSets(ResultSet result) throws SQLException
    {
        List<Product> returns = new ArrayList<>();
        while(result.next()) {
            //name,price,body_location,category,company,id,image
            returns.add(
                    new Product(result.getString("name"),
                            result.getDouble("price"),
                            result.getInt("body_location"),
                            result.getInt("category"),
                            result.getInt("company"),
                            result.getInt("id"),
                            result.getString("image")
                )
            );
        }
        return  returns;
    }
    private Product fromResultSet(ResultSet result) throws SQLException
    {
        result.next();
        return new
                Product(
                    result.getString("name"),
                    result.getDouble("price"),
                    result.getInt("body_location"),
                    result.getInt("category"),
                    result.getInt("company"),
                    result.getInt("id"),
                    result.getString("image")
                    );
    }
    public Product getSingle(long id) {
        try {
            ResultSet result = DatabaseService
                    .getInstance()
                    .createNamedPreparedStatement("SELECT * FROM product WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery();
            return fromResultSet(result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Paginated<List<ProductResponse>> getMultiple(long maxPrice, long minPrice, List<Integer> company, List<Integer> categories, List<Integer> bodyLocation, String search, LongParam page, LongParam pageSize) throws SQLException, ClassNotFoundException {
        String initialString = "SELECT * FROM Product ";
        String queryContents = "";
        boolean categoricalSearch = categories.size() > 0 || company.size() > 0 || bodyLocation.size() > 0;
        if (categoricalSearch || search != null) queryContents += "WHERE (";
        for (int i = 0; i < categories.size(); i++) {
            if (i < categories.size() -1) queryContents += "category = :category" + i + " OR ";
            else queryContents += bodyLocation.size() != 0 && company.size() != 0 ? "category = :category" + i + ") AND (" : "category = :category" + i + ")";
        }

        for (int i = 0; i < company.size(); i++) {
            if (company.get(i) > 46) company.set(i, company.get(i) -1);
            if (i < company.size() -1) queryContents += "company  = :company" + i + " OR ";
            else queryContents += bodyLocation.size() != 0 && categories.size() != 0 ? "company = :company" + i + ") AND (" : "company = :company" + i + ")";
        }

        for (int i = 0; i < bodyLocation.size(); i++) {
            if (i < bodyLocation.size() -1) queryContents += "body_location = :body_location" + i + " OR ";
            else queryContents += company.size() != 0 && categories.size() != 0 ? "body_location = :body_location" + i + ") AND (" : "body_location = :body_location" + i + ")";
        }
        queryContents += !(search == null) ? "name LIKE :search) " : " ";
        String countContent = queryContents;
        String end = "LIMIT :pagesize OFFSET :page";
        System.out.println(queryContents);
        NamedParameterStatement query = initQuery(initialString, queryContents, end, pageSize, page);
        query = populatePreparedStatementWithFilters(query, search, categories, company, bodyLocation);
        List<Product> result = fromResultSets(query.executeQuery());

        NamedParameterStatement counter = initQuery("SELECT COUNT(*) AS count FROM product ", queryContents, "", pageSize, page);
        counter = populatePreparedStatementWithFilters(counter, search, categories, company, bodyLocation);
        ResultSet countResult = counter.executeQuery();

        countResult.next();
        long count = countResult.getLong("count");
        List<ProductResponse> populatedResult = populateProductList(result);
        return new Paginated<>(pageSize.get(), page.get(), count, populatedResult);

    }

    private NamedParameterStatement initQuery(String initialString, String queryContents, String end, LongParam pageSize, LongParam page) throws SQLException, ClassNotFoundException {
        NamedParameterStatement statement =  DatabaseService
                .getInstance()
                .createNamedPreparedStatement(initialString + queryContents + end);
        long getPagesize = pageSize.get();
        if (getPagesize != 0 && end != "") {

                statement
                    .setParameter("pagesize", getPagesize)
                    .setParameter("page", (page.get() -1) * pageSize.get());
        }
        return statement;
    }

    private NamedParameterStatement populatePreparedStatementWithFilters(NamedParameterStatement query, String search, List<Integer> categories, List<Integer> company, List<Integer> bodyLocation) throws SQLException {
        if (search != null) { query.setParameter("search", "%" + search + "%");}
        for (int i = 0; i < categories.size(); i++) {
            query.setParameter("category"+i, categories.get(i));
        }
        for (int i = 0; i < company.size(); i++) {
            query.setParameter("company"+i, company.get(i));
        }
        for (int i = 0; i < bodyLocation.size(); i++) {
            query.setParameter("body_location"+i, bodyLocation.get(i));
        }
        return query;
    }

    public String getImage(long id) {
        try {
            ResultSet result = DatabaseService.getInstance().createNamedPreparedStatement("SELECT image FROM product WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery();
            result.next();
            return result.getString("image");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean update(Product product) {
        product = product.compare(getSingle(product.getId()));
        try {
            new URL(product.getImage());
            product.setImage(BaseImageTranslator.getBase64URL(product.getImage()));
        } catch (Exception ignore) {};
        try {
            //name,price,body_location,category,company,id,image
            int resultkey = DatabaseService.getInstance().createNamedPreparedStatement(
                    "UPDATE product " +
                            "SET name =:name, " +
                            "price = :price, " +
                            "category = :category, " +
                            "body_location = :body_location, " +
                            "company = :company, " +
                            "image = :image" +
                            "WHERE id = :id")
                    .setParameter("name", product.getName())
                    .setParameter("price", product.getPrice())
                    .setParameter("category", product.getCategory())
                    .setParameter("body_location", product.getBody_location())
                    .setParameter("company", product.getCompany())
                    .setParameter("image", product.getImage())
                    .setParameter("id", product.getId())
                    .executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ProductResponse> getTop(long range) throws NotFoundException {
        try {
            ResultSet top = DatabaseService.getInstance().createNamedPreparedStatement(
                    "SELECT " +
                            "product.name, product.price, product.body_location, product.category, product.company, product.id, product.image " +
                            "FROM product, product_stats " +
                            "JOIN product p on id=product_id ORDER BY views LIMIT :range OFFSET 0 "
            )
                    .setParameter("range", range)
                    .executeQuery();
            return populateProductList(fromResultSets(top));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new NotFoundException();
        }
    }
    public List<ProductResponse> populateProductList(List<Product> products) {
        List<ProductResponse> result = new ArrayList<>();
        for (Product product : products) {
            result.add(
                    new ProductResponse(
                            product.getName(),
                            product.getPrice(),
                            getBody_Location(product.getBody_location()),
                            getCategory(product.getCategory()),
                            getCompany(product.getCompany()),
                            product.getId(),
                            product.getImage()
                    )
            );
        }
        return result;
    }
    private Category getCategory(int catId) {
        try {
            ResultSet categoryResult = DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM Category WHERE id = :id")
                    .setParameter("id", catId)
                    .executeQuery();
            categoryResult.next();
            return new Category(catId, categoryResult.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private Company getCompany(int companyId) {
        try {
            ResultSet companyResult = DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM Company WHERE id = :id")
                    .setParameter("id", companyId)
                    .executeQuery();
            companyResult.next();
            return new Company(companyResult.getLong("id"), companyResult.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    private Body_Location getBody_Location(int companyId) {
        try {
            ResultSet bodyLocationResult = DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM Category WHERE id = :id")
                    .setParameter("id", companyId)
                    .executeQuery();
            bodyLocationResult.next();
            return new Body_Location(bodyLocationResult.getLong("id"), bodyLocationResult.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Product insert(Product product) {
        try {new URL(product.getImage()); product.setImage(BaseImageTranslator.getBase64URL(product.getImage()));} catch (Exception ignore) {}
        try {
            DatabaseService.getInstance()
                    .createNamedPreparedStatement("INSERT INTO product VALUES(:name, :price, :body_location, :image, :category, :company)")
                    .setParameter("name", product.getName())
                    .setParameter("price", product.getPrice())
                    .setParameter("body_location", product.getBody_location())
                    .setParameter("image", product.getBody_location())
                    .setParameter("category", product.getCategory())
                    .setParameter("company", product.getCompany())
            .execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return product;
    }

    public List<Category> getTypeDesc(String type) {
        List<Category> categoryResult = new ArrayList<>();
        try {
            ResultSet queryResult = DatabaseService.getInstance()
                    .createPreparedStatement("SELECT * FROM" + type)
            .executeQuery();
            while (queryResult.next()) {
                categoryResult.add(new Category(queryResult.getLong("id"), queryResult.getString("name")));
            }
            return categoryResult;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(long id) {
        try {
            return DatabaseService.getInstance().createNamedPreparedStatement("DELETE FROM product WHERE id = :id")
                    .setParameter("id", id)
                    .execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}

