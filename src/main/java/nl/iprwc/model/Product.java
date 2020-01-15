package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import nl.iprwc.Utils.BaseImageTranslator;
import nl.iprwc.view.View;
import org.joda.time.DateTime;

import javax.ws.rs.Produces;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class Product {
    //name,price,body_location,category,company,id,image
    private String name;
    private double price;
    private int body_location;
    private int category;
    private int company;
    private int id;
    private String image;

    public Product(String name, double price, int body_location, int category, int company, int id, String image) {
        this.name = name;
        this.price = price == 0.00 ? 10.00 : price;
        this.body_location = body_location;
        this.category = category;
        this.company = company;
        this.id = id;
        this.image = image;
    }
    public Product(String name, double price, int body_location, int category, int company, String image) {
        this.name = name;
        this.price = price == 0.00 ? 10.00 : price;
        this.body_location = body_location;
        this.category = category;
        this.company = company;
        this.image = image;
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
    public int getId() {
        return id;
    }
    @JsonIgnore
    public void setId(int id) {
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
    public int getBody_location() {
        return body_location;
    }

    public void setBody_location(int body_location) {
        this.body_location = body_location;
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
        if (product.body_location == 0) product.body_location = this.body_location;
        if (product.image == null) product.image = this.image;
        if (product.price == 0) product.price = this.price;
        return product;
    }
}
