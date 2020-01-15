package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import nl.iprwc.Utils.BaseImageTranslator;
import nl.iprwc.view.View;
import org.antlr.stringtemplate.language.Cat;
import org.joda.time.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ProductResponse {
    //name,price,body_location,category,company,id,image
    private String name;
    private double price;
    private Body_Location body_location;
    private Category category;
    private Company company;
    private int id;
    private String image;

    public ProductResponse(String name, double price, Body_Location body_location, Category category, Company company, int id, String image) {
        this.name = name;
        this.price = price == 0.00 ? 10.00 : price;
        this.body_location = body_location;
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
    public int getId() {
        return id;
    }
    @JsonIgnore
    public void setId(int id) {
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
    public Body_Location getBody_location() {
        return body_location;
    }

    public void setBody_location(Body_Location body_location) {
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
}
