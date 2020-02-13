package nl.iprwc.db;

import com.google.common.base.Optional;
import nl.iprwc.groups.GroupService;
import nl.iprwc.model.Account;
import nl.iprwc.model.Authentication;
import nl.iprwc.model.Group;
import nl.iprwc.model.ProductResponse;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;

import javax.ws.rs.NotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    public AccountDAO() {
    }

    private Account fromResultSet(ResultSet result) throws SQLException {
        //long id, String firstName, String lastName, String mailAddress, String passwordHash, String reference
        Account account;
        if(result.next()){
            account = new
                    Account(
                    result.getLong("id"),
                    result.getString("firstname"),
                    result.getString("lastname"),
                    result.getString("mailaddress"),
                    result.getString("postal_code"),
                    result.getString("house_number"),
                    result.getString("password"),
                    result.getString("reference")
            );
            account.setGroups(GroupService.getInstance().getGroups(account));
            return account;
        }
        return null;
    }

    public Account getFromId(long id) {
        try {
            return
            fromResultSet(DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM Account WHERE id = :id")
                    .setParameter("id", id)
                    .executeQuery());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account tryLogin(String username, String password) throws SQLException, ClassNotFoundException {
        Account account =  fromResultSet(
            DatabaseService
                    .getInstance()
                    .createNamedPreparedStatement("SELECT * FROM Account WHERE username == :username && password == :password")
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .executeQuery()
        );
        return account.getMailAddress() == null ?  null : account;
    }

    public boolean makeAccount(Authentication value) {
        try {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("INSERT INTO Account(mailaddress, password, postal_code, house_number)" +
                            " VALUES (:username, :password, '3214', '2a')")
                    .setParameter("username", value.getMailAddress())
                    .setParameter("password", value.getPassword())
            .execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public long create(Account account) throws SQLException, ClassNotFoundException {
        NamedParameterStatement statement = DatabaseService.getInstance()
                .createNamedPreparedStatement("INSERT INTO public.account "
                + "(firstname, lastname, mailaddress, password, postal_code, house_number, reference) VALUES "
                + "(:firstname, :lastname, :mailaddress, :password, :postal_code, :house_number, :reference);");

        statement.setString("firstname", account.getFirstName());
        statement.setString("lastname", account.getLastName());
        statement.setString("postal_code", account.getPostal_code());
        statement.setString("house_number", account.getHouse_number());
        statement.setString("password", account.getPasswordHash());
        statement.setString("reference", account.getReference());
        statement.setString("mailaddres s", account.getMailAddress());


        if(statement.executeUpdate() == 0){
            return 0;
        }
        ResultSet keys = statement.getGeneratedKeys();


        if(keys.next()){
            return keys.getLong("id");
        }else{
            return 0;
        }


        //return Optional.of(get(id));
    }

    public Group getGroupIdByName(String name) throws SQLException, ClassNotFoundException, NotFoundException {
        NamedParameterStatement statement = DatabaseService
                .getInstance()
                .createNamedPreparedStatement("SELECT * FROM public.group WHERE internal_reference = :reference").setParameter("reference", name);
        ResultSet result = statement.executeQuery();
        if (result.next()) {
            Group group = new Group(result.getInt("id"),
                    result.getString("name"),
                    result.getBoolean("system"),
                    result.getString("internal_reference"),
                    result.getBoolean("editable"),
                    new Group[0]);
            return group;
        } else {
            throw new NotFoundException();
        }

    }

    public boolean addGroupToAccount(Account account, String groupName) throws SQLException, ClassNotFoundException {
        Group group = getGroupIdByName(groupName);
        if(getFromId(account.getId()).getId() != 0 && account.getId() != 0 && !accountMemberRelationExists(account.getId(), group.getId())) {
            NamedParameterStatement statement = DatabaseService.getInstance().createNamedPreparedStatement("INSERT INTO account_group_member (account_id, group_id) " +
                    "VALUES (:accountId, :groupId)");
            statement.setParameter("accountId", account.getId());
            statement.setParameter("groupId", group.getId());
            int affectedRows = statement.executeUpdate();
            if(affectedRows > 0){
                return true;
            }else{
                return false;
            }
        }else{
            throw new NotFoundException("Account not found");
        }
    }

    private boolean accountMemberRelationExists(long accountId, long groupId) throws SQLException, ClassNotFoundException {
        NamedParameterStatement statement = DatabaseService
                .getInstance()
                .createNamedPreparedStatement("SELECT * FROM account_group_member WHERE account_id = :accountId AND group_id = :groupId")
                .setParameter("accountId", accountId)
                .setParameter("groupId", groupId);
        ResultSet result = statement.executeQuery();
        if(result.next()){
            return true;
        }else{
            return false;
        }

    }

    public Account getAccountFromMail(String mail){
        try{
            NamedParameterStatement statement = DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM account WHERE mailaddress = :mailaddress");
            statement.setParameter("mailaddress", mail);
            ResultSet result = statement.executeQuery();
            Account account;
            account = fromResultSet(result);
            return account;
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //return Optional.of(get(id));
        throw new NullPointerException();
    }

    public Account updateAccount(Account account) {
        account = replaceNull(account);
        try {
            NamedParameterStatement statement = DatabaseService.getInstance()
                    .createNamedPreparedStatement(
                            "UPDATE account SET" +
                                    " mailaddress = :mailaddress," +
                                    " password = :password," +
                                    " firstname = :firstname," +
                                    " lastname = :lastname," +
                                    " postal_code = :postalCode," +
                                    " house_number = :house_number " +
                                    "WHERE id = :id")
                    .setParameter("mailaddress", account.getMailAddress())
                    .setParameter("password", account.getPasswordHash())
                    .setParameter("firstname", account.getFirstName())
                    .setParameter("lastname", account.getLastName())
                    .setParameter("postalCode", account.getPostal_code())
                    .setParameter("house_number", account.getHouse_number())
                    .setParameter("id", account.getId());
            if(statement.executeUpdate() == 0){
                throw new NotFoundException();
            }
            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()){
                return fromResultSet(keys);
            }else{
                return null;
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean delete(long account) {
        try {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("DELETE FROM account WHERE id = :id")
                    .setParameter("id", account)
            .execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Account> getAll() throws SQLException, ClassNotFoundException {
        ResultSet res = DatabaseService.getInstance().createPreparedStatement("SELECT * FROM account")
                .executeQuery();
        List<Account> result = new ArrayList<>();
        while (res.next()) {
            result.add(new Account(
                    res.getString("firstname"),
                    res.getString("lastname"),
                    res.getString("mailaddress"),
                    res.getString("postal_code"),
                    res.getString("house_number")
            ));
        }
        return result;
    }

    private Account replaceNull(Account account) {
        Account currentAccount = getAccountFromMail(account.getMailAddress());
        currentAccount.setMailAddress(account.getMailAddress());
        currentAccount.setFirstName(account.getFirstName());
        currentAccount.setLastName(account.getLastName());
        currentAccount.setPostal_code(account.getPostal_code());
        currentAccount.setHouse_number(account.getHouse_number());
        return currentAccount;
    }
}