package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.iprwc.utils.BaseImageTranslator;
import nl.iprwc.controller.BodyLocationController;
import nl.iprwc.controller.CategoryController;
import nl.iprwc.controller.CompanyController;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.exception.NotFoundException;

import java.net.URL;

public class ProductResponse {
    //name,price,body_location,category,company,id,image
    private String name;
    private double price;
    private BodyLocation bodyLocation;
    private Category category;
    private Company company;
    private long id;
    private String image;

    public static ProductResponse CreateFromProduct(Product p) throws NotFoundException, InvalidOperationException {
        return new ProductResponse(
                p.getName(),
                p.getPrice(),
                BodyLocationController.getInstance().get(p.getBodyLocation()),
                CategoryController.getInstance().get(p.getCategory()),
                CompanyController.getInstance().get(p.getCompany()),
                p.getId(),
                p.getImage());
    }

    public ProductResponse(String name, double price, BodyLocation bodyLocation, Category category, Company company, long id, String image) {
        this.name = name;
        this.price = price == 0.00 ? 10.00 : price;
        this.bodyLocation = bodyLocation;
        this.category = category;
        this.company = company;
        this.id = id;
        this.image = image;
        try {new URL(image); this.image = "data:image/jpg;base64," + BaseImageTranslator.getBase64URL(image);} catch (Exception e) {
            //ignore
        }
    }
    @JsonProperty
    public String getImage() {
        return image;
    }
    @JsonIgnore
    public void setImage(String image) {
        this.image = image;
    }
    @JsonProperty
    public long getId() {
        return id;
    }
    @JsonIgnore
    public void setId(long id) {
        this.id = id;
    }
    @JsonProperty
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
    @JsonProperty
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    @JsonProperty
    public BodyLocation getBodyLocation() {
        return bodyLocation;
    }

    public void setBodyLocation(BodyLocation bodyLocation) {
        this.bodyLocation = bodyLocation;
    }
    @JsonProperty
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    @JsonProperty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
