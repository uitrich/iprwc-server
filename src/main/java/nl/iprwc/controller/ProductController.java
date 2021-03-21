package nl.iprwc.controller;

import io.dropwizard.jersey.params.LongParam;
import nl.iprwc.Response.ProductResponse;
import nl.iprwc.utils.BaseImageTranslator;
import nl.iprwc.utils.Paginated;
import nl.iprwc.db.ProductDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.model.Product;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductController {
    private ProductDAO dao;
    private static ProductController instance;

    static {
        instance = new ProductController();
    }

    private ProductController() {
        dao = new ProductDAO();
    }

    public static ProductController getInstance() {
        return instance;
    }

    public ProductResponse getFromId(long id) throws InvalidOperationException, NotFoundException {
        try {
            addView(id);
            return ProductResponse.CreateFromProduct(dao.getSingle(id));
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public Object getImageFromId(long id) throws InvalidOperationException {
        String image = null;
        try {
            image = dao.getImage(id);
            boolean isUrl;
            try {
                new URL(image);
                return BaseImageTranslator.getByteArrayFromImageURL(image);
            } catch (MalformedURLException e) {
                isUrl = false;
            }
            return BaseImageTranslator.convert(image, isUrl);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new InvalidOperationException();
        }
    }

    public Paginated<List<ProductResponse>> getAll(LongParam page, LongParam pageSize, String search, List<Integer> category, List<Integer> company, List<Integer> bodyLocation) throws NotFoundException, InvalidOperationException {

        try {
            return dao.getMultiple(0, 0, company, category, bodyLocation, search,  page, pageSize);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new InvalidOperationException();
        }
    }

    public boolean update(Product product) throws InvalidOperationException {
        try {
            return dao.update(product);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public List<ProductResponse> getTop(long range) throws NotFoundException, InvalidOperationException {
        try {
            return populateProductList(dao.getTop(range));
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public ProductResponse insertAdd(ProductResponse input) throws InvalidOperationException, nl.iprwc.exception.NotFoundException {
        Product product = splitResponse(input);
        String image = product.getImage();
        try {
            new URL(image);
            product.setImage(BaseImageTranslator.getBase64URL(image));
        } catch (IOException ignored) {
            //ignore
        }
        if (product.getCategory() == 0 ) System.out.println("category has 0, aborting..");
        else if (product.getCompany() == 0 ) System.out.println("company has 0, aborting..");
        else if (product.getBodyLocation() == 0 ) { System.out.println("body location has 0, aborting.."); }
        else {
            try {
                return ProductResponse.CreateFromProduct(dao.insert(product));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                throw new InvalidOperationException();
            }
        }
        return null;
    }

    public Object getTypeDesc(String type) throws InvalidOperationException {
        try {
            return dao.getTypeDesc(type);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public boolean delete(long id) throws InvalidOperationException {
        try {
            return dao.delete(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public Product splitResponse(ProductResponse product) throws InvalidOperationException, nl.iprwc.exception.NotFoundException {
        long catId = CategoryController.getInstance().createIfNotExists(product.getCategory());
        long comId = CompanyController.getInstance().createIfNotExists(product.getCompany());
        long bodId = BodyLocationController.getInstance().createIfNotExists(product.getBodyLocation());

        return new Product(product.getName(), product.getPrice(), bodId, catId, comId, product.getId(), product.getImage());
    }

    public List<ProductResponse> getAllIndistinctly() throws NotFoundException, InvalidOperationException {
        try {
            return makeResponseList(dao.getAll());
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public List<ProductResponse> getFromIds(List<Long> productIds) throws nl.iprwc.exception.NotFoundException, InvalidOperationException {
        List<ProductResponse> responseProducts = new ArrayList<>();
        for (Long id : productIds) {
            try {
                responseProducts.add(ProductResponse.CreateFromProduct(dao.getSingle(id)));
            } catch (SQLException | ClassNotFoundException e) {
                throw new InvalidOperationException();
            }
        }
        return responseProducts;
    }
    public List<ProductResponse> populateProductList(List<Product> products) throws nl.iprwc.exception.NotFoundException, InvalidOperationException {
        List<ProductResponse> result = new ArrayList<>();
        for (Product product : products) {
            result.add(
                    new ProductResponse(
                            product.getName(),
                            product.getPrice(),
                            BodyLocationController.getInstance().get(product.getBodyLocation()),
                            CategoryController.getInstance().get(product.getCategory()),
                            CompanyController.getInstance().get(product.getCompany()),
                            product.getId(),
                            product.getImage()
                    )
            );
        }
        return result;
    }
    public List<ProductResponse> makeResponseList(List<Product> input) throws NotFoundException, InvalidOperationException {
        List<ProductResponse> output = new ArrayList<>();
        for (Product p : input) {
            output.add(ProductResponse.CreateFromProduct(p));
        }
        return output;
    }
    private void addView(long productId) throws SQLException, ClassNotFoundException {
        dao.addView(productId);
    }
}
