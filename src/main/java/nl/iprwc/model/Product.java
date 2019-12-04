package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import nl.iprwc.view.View;
import org.joda.time.DateTime;

import java.util.List;

public class Product {
    private long id;
    private String name;
    private String description;
    private String category;
    private double price;
    private long colourRangeId;

    public Product(long id, String name, String description, String category, double price, long colourRangeId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.colourRangeId = colourRangeId;
    }
    @JsonProperty
    public long getId() {
        return id;
    }
    @JsonProperty
    public String getName() {
        return name;
    }
    @JsonProperty
    public String getDescription() {
        return description;
    }
    @JsonProperty
    public String getCategory() {
        return category;
    }
    @JsonProperty
    public double getPrice() {
        return price;
    }
    @JsonProperty
    public long getColourRangeId() {
        return colourRangeId;
    }
    @JsonIgnore
    public void setId(long id) {
        this.id = id;
    }
    @JsonIgnore
    public void setName(String name) {
        this.name = name;
    }
    @JsonIgnore
    public void setDescription(String description) {
        this.description = description;
    }
    @JsonIgnore
    public void setCategory(String category) {
        this.category = category;
    }
    @JsonIgnore
    public void setPrice(double price) {
        this.price = price;
    }
    @JsonIgnore
    public void setColourRangeId(long colourRangeId) {
        this.colourRangeId = colourRangeId;
    }


}
