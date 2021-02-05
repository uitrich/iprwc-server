package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import nl.iprwc.constraints.Validator;
import nl.iprwc.view.View;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "account")
public class Account {

    private String id;
    private String mailAddress;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String postal_code;
    private String house_number;
    private List<Group> groups;
    private String reference;

    public Account() {
        this.id = UUID.randomUUID().toString();
        this.firstName = null;
        this.lastName = null;
        this.mailAddress = null;
        this.passwordHash = null;
        this.postal_code = null;
        this.house_number = null;
        this.groups = new ArrayList<>();
        this.reference = "";
    }

    public Account(String id, String firstName, String lastName, String mailAddress, String postal_code, String house_number, String passwordHash, String reference) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress.toLowerCase();
        this.passwordHash = passwordHash;
        this.groups = new ArrayList<>();
        this.reference = reference;
        this.postal_code = postal_code;
        this.house_number = house_number;
    }

    public Account(String firstName, String lastName, String mailAddress, String postal_code, String house_number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress.toLowerCase();
        this.postal_code = postal_code;
        this.house_number = house_number;
    }

    public Account(String firstName, String lastName, String mailAddress, String passwordHash) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress.toLowerCase();
        this.passwordHash = passwordHash;
        this.groups = new ArrayList<Group>();
        this.reference = "";
        this.house_number = "1a";
        this.postal_code = "1234";
    }


    /**
     * Recursive function to check if the users has a permission.
     * @param groups
     * @param internalReference
     * @return
     */
    public boolean hasPermission(Group[] groups, String internalReference){
        for(Group group : groups){
            if(group.getInternalReference().equals(internalReference)){
                return true;
            }else{
                hasPermission(group.getGroups(), internalReference);
            }
        }
        return false;
    }

    /**
     * Call recursive function the to check if a user has a permission.
     * @param internalReference
     * @return
     */
    public boolean hasPermission(String internalReference){
        for(Group group : this.getGroups()){
            if(group.getInternalReference().equals(internalReference)){
                return true;
            }else{
                hasPermission(group.getGroups(), internalReference);
            }
        }
        return false;
    };

    /**
     * Take a account, and combine it with a new account.
     * @param newAccount
     * @return
     */
    public Account updateAccount(Account newAccount){
        if(newAccount.getFirstName() != null){
            this.firstName = newAccount.getFirstName();
        }
        if(newAccount.getLastName() != null){
            this.lastName = newAccount.getLastName();
        }
        newAccount.setMailAddress(newAccount.getMailAddress().toLowerCase());
        if(newAccount.getMailAddress() != null){
            this.mailAddress = newAccount.getMailAddress();
        }
        if(newAccount.getPasswordHash() != null){
            this.passwordHash = newAccount.getPasswordHash();
        }
        if(newAccount.getReference() != null){
            this.reference = newAccount.getReference();
        }
        return this;
    }

    public void setGroups(Group[] groups){
        this.groups = new ArrayList<>(Arrays.asList(groups));
    }


    public void removeGroup(Group group){
        this.groups.remove(group);
    }

    @JsonProperty
    @JsonView(View.Private.class)
    public List<Group> getGroups(){
        return this.groups;
    }

    @JsonProperty
    @JsonView(View.Public.class)
    public String getId() {
        return id;
    }

    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

    @JsonView(View.Public.class)
    @JsonProperty
    public String getFirstName() {
        return firstName;
    }
    @JsonIgnore
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonView(View.Public.class)
    @JsonProperty
    public String getLastName() {
        return lastName;
    }

    @JsonIgnore
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty
    @JsonView(View.Private.class)
    public String getMailAddress() {
        return mailAddress;
    }

    @JsonIgnore
    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress.toLowerCase();
    }

    @JsonProperty
    public String getPasswordHash() {
        return passwordHash;
    }

    @JsonIgnore
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @JsonProperty
    @JsonView(View.Private.class)
    public String getReference() {
        return reference;
    }

    @JsonIgnore
    public void setReference(String reference) {
        this.reference = reference;
    }

    @JsonView(View.Public.class)
    @JsonProperty
    public String getPostal_code() {
        return postal_code;
    }
    @JsonIgnore
    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }
    @JsonView(View.Public.class)
    @JsonProperty
    public String getHouse_number() {
        return house_number;
    }
    @JsonIgnore
    public void setHouse_number(String house_number) {
        this.house_number = house_number;
    }
}
