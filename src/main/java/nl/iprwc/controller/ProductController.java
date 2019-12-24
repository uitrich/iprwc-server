package nl.iprwc.controller;

import io.dropwizard.jersey.params.LongParam;
import nl.iprwc.Utils.BaseImageTranslator;
import nl.iprwc.Utils.Paginated;
import nl.iprwc.db.ProductDAO;
import nl.iprwc.model.Product;
import nl.iprwc.model.ProductResponse;

import javax.ws.rs.NotFoundException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class ProductController {
    private final ProductDAO dao;


    public ProductController() {
        dao = new ProductDAO();
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

    public boolean update(long id) {
        return dao.update(id);
    }

    public List<ProductResponse> getTop(long range) throws NotFoundException {
        return dao.getTop(range);
    }
}
