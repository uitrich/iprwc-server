package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Company {
    private long id;
    private String name;

    public Company(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Company(String name) {
        this.id = 0;
        this.name = name;
    }
    public Company() {
        this.id = 0;
        this.name = "";
    }

    @JsonProperty
    public String getName() {
        return name;
    }
    @JsonIgnore
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public long getId() {
        return id;
    }
    @JsonIgnore
    public void setId(long id) {
        this.id = id;
    }
}
