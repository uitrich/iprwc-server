package nl.iprwc.controller;

import io.dropwizard.jersey.params.LongParam;
import nl.iprwc.Utils.BaseImageTranslator;
import nl.iprwc.Utils.Paginated;
import nl.iprwc.db.ProductDAO;
import nl.iprwc.model.Product;
import nl.iprwc.model.ProductResponse;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class ProductController {
    private final ProductDAO dao;
    private final GroupController groupController;


    public ProductController() {
        dao = new ProductDAO();
        groupController = SuperController.getInstance().getGroupController();
    }

    public Product getFromId(long id) throws SQLException, ClassNotFoundException {
        return dao.getSingle(id);
    }

    public Object getImageFromId(long id) throws IOException {
        String image = dao.getImage(id);
        boolean isUrl;
        try {
            new URL(image);
            return BaseImageTranslator.getByteArrayFromImageURL(image);
        } catch (MalformedURLException e) {
            isUrl = false;
        }
        return BaseImageTranslator.convert(image, isUrl);
    }

    public Paginated<List<ProductResponse>> getAll(LongParam page, LongParam pageSize, String search, List<Integer> category, List<Integer> company, List<Integer> bodyLocation) throws SQLException, ClassNotFoundException {

        return dao.getMultiple(0, 0, company, category, bodyLocation, search,  page, pageSize);
    }

    public boolean update(Product product) {
        return dao.update(product);
    }

    public List<ProductResponse> getTop(long range) throws NotFoundException {
        return dao.getTop(range);
    }

    public List<ProductResponse> populateList(List<Product> input) {
        return dao.populateProductList(input);
    }

    public Product insertAdd(ProductResponse input) {
        Product product = splitResponse(input);
        String image = product.getImage();
        boolean isUrl = false;
        try {
            new URL(image);
            product.setImage(BaseImageTranslator.getBase64URL(image));
        } catch (IOException ignored) {
        }
        if (product.getCategory() == 0 ) System.out.println("category has 0, aborting..");
        else if (product.getCompany() == 0 ) System.out.println("company has 0, aborting..");
        else if (product.getBody_location() == 0 ) { System.out.println("body location has 0, aborting.."); }
        else return dao.insert(product);
        return null;
    }

    public Object getTypeDesc(String type) {
        return dao.getTypeDesc(type);
    }

    public boolean delete(long id) {
        return dao.delete(id);
    }

    public Product splitResponse(ProductResponse product) {
        long catId = SuperController.getInstance().getCategoryController().createIfNotExists(product.getCategory());
        long comId = SuperController.getInstance().getCompanyController().createIfNotExists(product.getCompany());
        long bodId = SuperController.getInstance().getBodyLocationController().createIfNotExists(product.getBody_location());

        return new Product(product.getName(), product.getPrice(), bodId, catId, comId, product.getId(), product.getImage());
    }

    public List<ProductResponse> getAllIndestinctive() throws SQLException, ClassNotFoundException {
        return dao.getAll();
    }
}
