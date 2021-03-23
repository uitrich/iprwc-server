package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

@Entity
@Table(name = "account")
public class Account {

    private String id;
    private String mailAddress;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String postalCode;
    private String houseNumber;
    private List<Group> groups;

    public Account() {
        this.id = UUID.randomUUID().toString();
        this.firstName = null;
        this.lastName = null;
        this.mailAddress = null;
        this.passwordHash = null;
        this.postalCode = null;
        this.houseNumber = null;
        this.groups = new ArrayList<>();
    }

    public Account(String id, String firstName, String lastName, String mailAddress, String postalCode, String houseNumber, String passwordHash) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress.toLowerCase();
        this.passwordHash = passwordHash;
        this.groups = new ArrayList<>();
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
    }

    public Account(String firstName, String lastName, String mailAddress, String postalCode, String houseNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress.toLowerCase();
        this.postalCode = postalCode;
        this.houseNumber = houseNumber;
    }

    public Account(String firstName, String lastName, String mailAddress, String passwordHash) {
        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.mailAddress = mailAddress.toLowerCase();
        this.passwordHash = passwordHash;
        this.groups = new ArrayList<Group>();
        this.houseNumber = "1a";
        this.postalCode = "1234";
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
        return this;
    }

    public void setGroups(Group[] groups){
        this.groups = new ArrayList<>(Arrays.asList(groups));
    }


    public void removeGroup(Group group){
        this.groups.remove(group);
    }

    @JsonProperty
    public List<Group> getGroups(){
        return this.groups;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonIgnore
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty
    public String getFirstName() {
        return firstName;
    }
    @JsonIgnore
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty
    public String getLastName() {
        return lastName;
    }

    @JsonIgnore
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @JsonProperty
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
    public String getPostalCode() {
        return postalCode;
    }
    @JsonIgnore
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    @JsonProperty
    public String getHouseNumber() {
        return houseNumber;
    }
    @JsonIgnore
    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }
}
