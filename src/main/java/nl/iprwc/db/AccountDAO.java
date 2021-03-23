package nl.iprwc.db;

import nl.iprwc.Request.AccountRequest;
import nl.iprwc.db.abstractions.DatabaseAccessObjectCRUD;
import nl.iprwc.groups.GroupService;
import nl.iprwc.model.Account;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;

import nl.iprwc.exception.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountDAO implements DatabaseAccessObjectCRUD<Account, String, AccountRequest> {
    public AccountDAO() {
    }

    @Override
    public List<Account> getAll() throws SQLException, ClassNotFoundException {
        ResultSet res = DatabaseService.getInstance().createPreparedStatement("SELECT * FROM account")
                .executeQuery();
        List<Account> result = new ArrayList<>();
        while (res.next()) {

            Account account = new Account(
                    res.getString("firstname"),
                    res.getString("lastname"),
                    res.getString("mailaddress"),
                    res.getString("postal_code"),
                    res.getString("house_number")
            );
            account.setId(res.getString("id"));
            result.add(account);
        }
        return result;
    }

    @Override
    public Account get(String identifier) throws SQLException, ClassNotFoundException {
        return fromResultSet(DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT * FROM Account WHERE id = :id")
                .setParameter("id", identifier)
                .executeQuery());
    }

    public Account getByLogin(String username, String password) throws SQLException, ClassNotFoundException {
        Account account =  fromResultSet(
                DatabaseService
                        .getInstance()
                        .createNamedPreparedStatement("SELECT * FROM Account WHERE username == :username AND password == :password")
                        .setParameter("username", username)
                        .setParameter("password", password)
                        .executeQuery()
        );
        return account.getMailAddress() == null ?  null : account;
    }

    public Account getAccountFromMail(String mail) throws SQLException, ClassNotFoundException, NotFoundException {
        NamedParameterStatement statement = DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT * FROM account WHERE mailaddress = :mailaddress");
        statement.setParameter("mailaddress", mail);
        ResultSet result = statement.executeQuery();
        Account account;
        account = fromResultSet(result);
        if (account != null) return account;
        throw new NotFoundException();
    }


    @Override
    public String create(AccountRequest creationRequest) throws SQLException, ClassNotFoundException {
        NamedParameterStatement statement = DatabaseService.getInstance()
                .createNamedPreparedStatement("INSERT INTO public.account "
                        + "(id, firstname, lastname, mailaddress, password, postal_code, house_number) VALUES "
                        + "(:id, :firstname, :lastname, :mailaddress, :password, :postal_code, :house_number);");
        statement.setString("id", UUID.randomUUID().toString());
        statement.setString("firstname", creationRequest.getFirstName());
        statement.setString("lastname", creationRequest.getLastName());
        statement.setString("postal_code", creationRequest.getPostalCode());
        statement.setString("house_number", creationRequest.getHouseNumber());
        statement.setString("password", creationRequest.getPassword());
        statement.setString("mailaddress", creationRequest.getMailAddress());


        if(statement.executeUpdate() == 0){
            return null;
        }
        ResultSet keys = statement.getGeneratedKeys();

        if(keys.next()){
            return keys.getString("id");
        }else{
            throw new SQLException("id not generated");
        }
    }

    @Override
    public boolean delete(String identifier) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement("DELETE FROM account WHERE id = :id")
                .setParameter("id", identifier)
                .executeUpdate() > 0;
    }

    @Override
    public boolean update(AccountRequest account) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement(
                        "UPDATE account SET" +
                                " firstname = :firstname," +
                                " lastname = :lastname," +
                                " postal_code = :postalCode," +
                                " house_number = :house_number " +
                                "WHERE mailaddress = :mailaddress")
                .setParameter("mailaddress", account.getMailAddress())
                .setParameter("firstname", account.getFirstName())
                .setParameter("lastname", account.getLastName())
                .setParameter("postalCode", account.getPostalCode())
                .setParameter("house_number", account.getHouseNumber()).executeUpdate() > 0;

    }

    @Override
    public Account fromResultSet(ResultSet result) throws SQLException {
        if(result.next()){
            Account account = new
                    Account(
                    result.getString("id"),
                    result.getString("firstname"),
                    result.getString("lastname"),
                    result.getString("mailaddress"),
                    result.getString("postal_code"),
                    result.getString("house_number"),
                    result.getString("password")
            );
            account.setGroups(GroupService.getInstance().getGroups(account));
            return account;
        }
        return null;
    }

}