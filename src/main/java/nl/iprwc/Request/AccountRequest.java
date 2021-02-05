package nl.iprwc.Request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.iprwc.constraints.Validator;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class AccountRequest {
    @JsonIgnore private String id;
    @NotNull @Validator("mailAddress") private String mailAddress;
    @NotNull @Validator("password") private String password;
    private String firstName;
    private String lastName;
    private String postalCode;
    private String houseNumber;

    public AccountRequest() {
        this.id = UUID.randomUUID().toString();
        this.firstName = null;
        this.lastName = null;
        this.mailAddress = null;
        this.password = null;
        this.postalCode = null;
        this.houseNumber = null;
    }
    public AccountRequest(String mailAddress, String passwordHash, String firstName,
                          String lastName, String postalCode, String houseNumber) {

        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress;
        this.password = passwordHash;
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
