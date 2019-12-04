package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Authenticate {
    @JsonProperty
    private String username;

    @JsonProperty
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
