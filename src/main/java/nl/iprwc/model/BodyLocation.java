package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BodyLocation {
    private long id;
    private String name;

    public BodyLocation(long id, String name) {
        this.id = id;
        this.name = name;
    }
    public BodyLocation(String name) {
        this.id = 0;
        this.name = name;
    }
    public BodyLocation() {
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
