package nl.iprwc.db;

import io.dropwizard.jersey.params.LongParam;
import nl.iprwc.Response.ProductResponse;
import nl.iprwc.utils.BaseImageTranslator;
import nl.iprwc.utils.Paginated;
import nl.iprwc.controller.ProductController;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.*;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;

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
    public Product getSingle(long id) throws SQLException, ClassNotFoundException {
            ResultSet result = DatabaseService
                    .getInstance()
                    .createNamedPreparedStatement("SELECT * FROM product WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery();
            return fromResultSet(result);
    }

    public Paginated<List<ProductResponse>> getMultiple(long maxPrice, long minPrice, List<Integer> company, List<Integer> categories, List<Integer> bodyLocation, String search, LongParam page, LongParam pageSize) throws SQLException, ClassNotFoundException, nl.iprwc.exception.NotFoundException, InvalidOperationException {
        String initialString = "SELECT * FROM Product ";
        String queryContents = "";
        boolean categoricalSearch = categories.size() > 0 || company.size() > 0 || bodyLocation.size() > 0;
        if (categoricalSearch || search != null) queryContents += "WHERE (";
        for (int i = 0; i < categories.size(); i++) {
            if (i < categories.size() -1) queryContents += "category = :category" + i + " OR ";
            else queryContents += bodyLocation.size() != 0 && company.size() != 0 ? "category = :category" + i + ") AND (" : "category = :category" + i + ")";
        }

        for (int i = 0; i < company.size(); i++) {
            if (i < company.size() -1) queryContents += "company  = :company" + i + " OR ";
            else queryContents += bodyLocation.size() != 0 && categories.size() != 0 ? "company = :company" + i + ") AND (" : "company = :company" + i + ")";
        }

        for (int i = 0; i < bodyLocation.size(); i++) {
            if (i < bodyLocation.size() -1) queryContents += "body_location = :body_location" + i + " OR ";
            else queryContents += company.size() != 0 && categories.size() != 0 ? "body_location = :body_location" + i + ") AND (" : "body_location = :body_location" + i + ")";
        }
        queryContents += !(search == null) ? "name LIKE :search) " : " ";
        String countContent = queryContents;
        String end = "ORDER BY id LIMIT :pagesize OFFSET :page";
        NamedParameterStatement query = initQuery(initialString, queryContents, end, pageSize, page);
        query = populatePreparedStatementWithFilters(query, search, categories, company, bodyLocation);
        List<Product> result = fromResultSets(query.executeQuery());

        NamedParameterStatement counter = initQuery("SELECT COUNT(*) AS count FROM product ", queryContents, "", pageSize, page);
        counter = populatePreparedStatementWithFilters(counter, search, categories, company, bodyLocation);
        ResultSet countResult = counter.executeQuery();

        countResult.next();
        long count = countResult.getLong("count");
        List<ProductResponse> populatedResult = ProductController.getInstance().populateProductList(result);
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

    public String getImage(long id) throws SQLException, ClassNotFoundException {
            ResultSet result = DatabaseService.getInstance().createNamedPreparedStatement("SELECT image FROM product WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery();
            result.next();
            return result.getString("image");
    }

    public boolean update(Product product) throws SQLException, ClassNotFoundException {
        try {
            new URL(product.getImage());
            product.setImage(BaseImageTranslator.getBase64URL(product.getImage()));
        } catch (Exception ignore) {};
            String image = product.getImage();
            String id = String.valueOf(product.getId());
            id = id.replace("\nRETURNING", "");
            NamedParameterStatement resultkey = DatabaseService.getInstance().createNamedPreparedStatement(
                    "UPDATE product " +
                            "SET name = :name, " +
                            "id = :id, " +
                            "price = :price, " +
                            "category = :category, " +
                            "body_location = :body_location, " +
                            "company = :company, " +
                            "image = :image " +
                            "WHERE id = :id")
                    .setInt("id", Integer.parseInt(id))
                    .setParameter("name", product.getName())
                    .setParameter("price", product.getPrice())
                    .setParameter("category", product.getCategory())
                    .setParameter("body_location", product.getBodyLocation())
                    .setParameter("company", product.getCompany())
                    .setParameter("image", image);
            return resultkey.execute();
    }

    public List<Product> getTop(long range) throws NotFoundException, SQLException, ClassNotFoundException {
        ResultSet top = DatabaseService.getInstance().createNamedPreparedStatement(
        "select ps.product_id as id, p.name, p.price, p.body_location, p.image, p.category, p.company from product_stats ps " +
                "join product p on ps.product_id=p.id " +
                "order by views desc " +
                "offset 0 " +
                "limit :range") .setParameter("range", range)
                .executeQuery();
        return (fromResultSets(top));
    }

    public Product insert(Product product) throws SQLException, ClassNotFoundException {
        try {new URL(product.getImage()); product.setImage(BaseImageTranslator.getBase64URL(product.getImage()));} catch (Exception ignore) {}
        NamedParameterStatement namedParameterStatement = DatabaseService.getInstance()
            .createNamedPreparedStatement("INSERT INTO product (name, price, body_location, image, category, company) VALUES(:name, :price, :body_location, :image, :category, :company)")
            .setParameter("name", product.getName())
            .setParameter("price", product.getPrice())
            .setParameter("body_location", product.getBodyLocation())
            .setParameter("image", product.getImage())
            .setParameter("category", product.getCategory())
            .setParameter("company", product.getCompany());
        ResultSet rs = namedParameterStatement.executeGetReturning();
        if (rs.next())
            product.setId(rs.getLong("id"));
        return product;
    }

    public List<Category> getTypeDesc(String type) throws SQLException, ClassNotFoundException {
        List<Category> categoryResult = new ArrayList<>();
            ResultSet queryResult = DatabaseService.getInstance()
                    .createPreparedStatement("SELECT * FROM" + type)
            .executeQuery();
            while (queryResult.next()) {
                categoryResult.add(new Category(queryResult.getLong("id"), queryResult.getString("name")));
            }
            return categoryResult;
    }

    public boolean delete(long id) throws SQLException, ClassNotFoundException {
            DatabaseService.getInstance().createNamedPreparedStatement("DELETE FROM product_stats WHERE product_id = :id").setParameter("id", id).execute();
            return DatabaseService.getInstance().createNamedPreparedStatement("DELETE FROM product WHERE id = :id")
                    .setParameter("id", id)
                    .execute();
    }

    public List<Product> getAll() throws SQLException, ClassNotFoundException {
        ResultSet res = DatabaseService.getInstance().createPreparedStatement("SELECT * FROM product ORDER BY id")
                .executeQuery();
        List<Product> productResponse = new ArrayList<>();
        while (res.next()) {
            productResponse.add(new Product(
                    res.getString("name"),
                    res.getDouble("price"),
                    res.getLong("body_location"),
                    res.getLong("category"),
                    res.getLong("company"),
                    res.getInt("id"),
                    res.getString("image")
            )
            );
        }
        return (productResponse);
    }

    public void addView(long productId) throws SQLException, ClassNotFoundException {
        DatabaseService.getInstance().createNamedPreparedStatement(
            "UPDATE product_stats SET views = views + 1 WHERE product_id = :id"
        ).setParameter("id", productId).executeUpdate();
    }
}

