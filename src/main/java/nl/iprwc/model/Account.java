package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import nl.iprwc.Utils.Validator;
import nl.iprwc.view.View;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {

    private long id;
    private String mailAddress;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String postal_code;
    private String house_number;
    private List<Group> groups;
    private String reference;

    public Account() {
        this.id = 0;
        this.firstName = null;
        this.lastName = null;
        this.mailAddress = null;
        this.passwordHash = null;
        this.postal_code = null;
        this.house_number = null;
        this.groups = new ArrayList<>();
        this.reference = "";
    }

    public Account(long id, String firstName, String lastName, String mailAddress, String postal_code, String house_number, String passwordHash, String reference) {

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

    public Account(String firstName, String lastName, String mailAddress, String passwordHash) {
        this.id = id;
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
     * Check if the field for the model are valid.
     * @return
     */
    public List<FormError> isValid(){
        List<FormError> resultList = new ArrayList<FormError>();

        if(this.getMailAddress() != null){
            this.mailAddress  = this.mailAddress.toLowerCase();
            Validator.MailStatus result = Validator.isMailAddress(this.getMailAddress());
            if(result != Validator.MailStatus.OK){
                resultList.add(new FormError("mailAddress", result));
            }
        }
        if(this.getFirstName() != null){
            Validator.NameStatus result = Validator.isName(this.getFirstName());
            if(result != Validator.NameStatus.OK){
                resultList.add(new FormError("firstName", result));
            }
        }

        if(this.getLastName() != null){
            Validator.NameStatus result = Validator.isName(this.getLastName());
            if(result != Validator.NameStatus.OK){
                resultList.add(new FormError("lastName", result));
            }
        }

        if(this.getPasswordHash() != null){
            Validator.PasswordStatus result = Validator.isPassword(this.getPasswordHash());
            if(result != Validator.PasswordStatus.OK){
                resultList.add(new FormError("password", result));
            }
        }

        if(this.getReference() != null){
            Validator.ReferenceStatus result = Validator.isReference(this.getReference());
            if(result != Validator.ReferenceStatus.OK){
                resultList.add(new FormError("reference", result));
            }
        }

        return resultList;

    }

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

    /**
     * Function to compare. id's used for sorting.
     * @param compareAccount
     * @return
     */
    public int compareTo(Account compareAccount){
        if(this.getId() > compareAccount.getId()) return 1;
        if(this.getId() < compareAccount.getId()) return -1;
        else return 0;
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
    public long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(long id) {
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

    @JsonIgnore
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
