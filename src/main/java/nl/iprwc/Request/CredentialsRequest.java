package nl.iprwc.Request;

import nl.iprwc.constraints.Validator;

public class CredentialsRequest {
    private String mailAddress;
    private String password;

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
}
