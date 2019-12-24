package nl.iprwc.db;

import io.dropwizard.jersey.params.LongParam;
import javafx.util.Pair;
import nl.iprwc.Utils.Paginated;
import nl.iprwc.model.*;
import nl.iprwc.model.ProductResponse;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;

import javax.ws.rs.NotFoundException;
import javax.xml.crypto.Data;
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
            result.next();
            return fromResultSet(result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Paginated<List<ProductResponse>> getMultiple(long maxPrice, long minPrice, List<Integer> company, List<Integer> categories, List<Integer> bodyLocation, String search, LongParam page, LongParam pageSize) throws SQLException, ClassNotFoundException {
        String queryContents = "SELECT * FROM Product ";
        boolean categoricalSearch = categories.size() > 0 || company.size() > 0 || bodyLocation.size() > 0;
        if (categoricalSearch || search != null) queryContents += "WHERE (";
        for (int i = 0; i < categories.size(); i++) {
            if (i < categories.size() -1) queryContents += "category = :category" + i + " OR ";
            else queryContents += "category = :category" + i + ") AND (";
        }

        for (int i = 0; i < company.size(); i++) {
            if (i < company.size() -1) queryContents += "company  = :company" + i + " OR ";
            else queryContents += "company = :company" + i + ") AND (";
        }

        for (int i = 0; i < bodyLocation.size(); i++) {
            if (i < bodyLocation.size() -1) queryContents += "body_location = :body_location" + i + " OR ";
            else queryContents += "body_location = :body_location" + i + ") AND (";
        }
        queryContents += !(search == null) ? "name LIKE :search) " : " ";
        queryContents += "LIMIT :pageSize OFFSET :page";
        System.out.println(queryContents);
        NamedParameterStatement query = DatabaseService
                .getInstance()
                .createNamedPreparedStatement(queryContents)
                .setParameter("pageSize", pageSize.get())
                .setParameter("page", (page.get() -1) * pageSize.get());
        if (search != null) query.setParameter("search", "%" + search + "%");
        for (int i = 0; i < categories.size(); i++) {
            query.setParameter("category"+i, categories.get(i));
        }
        for (int i = 0; i < company.size(); i++) {
            query.setParameter("company"+i, company.get(i));
        }
        for (int i = 0; i < bodyLocation.size(); i++) {
            query.setParameter("body_location"+i, bodyLocation.get(i));
        }
        List<Product> result = fromResultSets(query.executeQuery());
        
        ResultSet counter = DatabaseService.getInstance().createPreparedStatement("SELECT COUNT(*) as results FROM product ").executeQuery();
        counter.next();
        long count = counter.getLong("results");
        List<ProductResponse> populatedResult = populateProductList(result);
        return new Paginated<>(pageSize.get(), page.get(), count, populatedResult);

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

    public boolean update(long id) {
        try {
            DatabaseService.getInstance().createNamedPreparedStatement("INSERT INTO product_stats (product_id) VALUES (:id)")
            .setParameter("id", id).executeUpdate();
            return true;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<ProductResponse> getTop(long range) throws NotFoundException {
        try {
            ResultSet top = DatabaseService.getInstance().createNamedPreparedStatement(
                    "SELECT product.name, product.price, product.body_location, product.category, product.company, product.id, product.image FROM product, product_stats JOIN product p on id=product_id ORDER BY views LIMIT :range OFFSET 0 "
            )
                    .setParameter("range", range)
                    .executeQuery();
            return populateProductList(fromResultSets(top));
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new NotFoundException();
        }
    }
    private List<ProductResponse> populateProductList(List<Product> products) {
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
}

