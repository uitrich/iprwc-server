package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Product {
    //name,price,body_location,category,company,id,image
    private String name;
    private double price;
    private int bodyLocation;
    private int category;
    private int company;
    private long id;
    private String image;

    public Product(String name, double price, long body_location, long category, long company, long id, String image) {
        this.name = name;
        this.price = price == 0.00 ? 10.00 : price;
        this.bodyLocation = Math.toIntExact(body_location);
        this.category = Math.toIntExact(category);
        this.company = Math.toIntExact(company);
        this.id = id;
        this.image = image;
    }
    public Product(String name, double price, int body_location, int category, int company, String image) {
        this.name = name;
        this.price = price == 0.00 ? 10.00 : price;
        this.bodyLocation = body_location;
        this.category = category;
        this.company = company;
        this.image = image;
    }
    @JsonProperty
    public String getImage() {
        return this.image;
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
    public int getCompany() {
        return company;
    }

    public void setCompany(int company) {
        this.company = company;
    }
    @JsonProperty
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
    @JsonProperty
    public int getBodyLocation() {
        return bodyLocation;
    }

    public void setBodyLocation(int bodyLocation) {
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

    public Product compare(Product product) {
        if (product.name == null) product.name = this.name;
        if (product.company == 0) product.company = this.company;
        if (product.category == 0) product.category = this.category;
        if (product.bodyLocation == 0) product.bodyLocation = this.bodyLocation;
        if (product.image == null) product.image = this.image;
        if (product.price == 0) product.price = this.price;
        return product;
    }
}
