package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import nl.iprwc.view.View;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "account")
public class Account {
    private String username;
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }
    @JsonProperty
    public String getUsername() {
        return username;
    }
    @JsonIgnore
    public void setPassword(String password) {
        this.password = password;
    }
    @JsonIgnore
    public void setUsername(String username) {
        this.username = username;
    }
}
