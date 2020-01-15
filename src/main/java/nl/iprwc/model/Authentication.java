package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Authentication {
    @JsonProperty
    private String mailAddress;

    @JsonProperty
    private String password;

    public String getMailAddress() {
        return mailAddress;
    }

    public String getPassword() {
        return password;
    }
}
