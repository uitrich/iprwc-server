package nl.iprwc.db;

import nl.iprwc.Request.AccountRequest;
import nl.iprwc.groups.GroupService;
import nl.iprwc.model.Account;
import nl.iprwc.model.Group;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;

import javax.naming.Name;
import javax.ws.rs.NotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountDAO {
    public AccountDAO() {
    }

    private Account fromResultSet(ResultSet result) throws SQLException {
        //long id, String firstName, String lastName, String mailAddress, String passwordHash, String reference
        Account account;
        if(result.next()){
            account = new
                    Account(
                    result.getString("id"),
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

    public Account getFromId(String id) throws SQLException, ClassNotFoundException {
        return
        fromResultSet(DatabaseService.getInstance()
                .createNamedPreparedStatement("SELECT * FROM Account WHERE id = :id")
                .setParameter("id", id)
                .executeQuery());

    }

    public Account tryLogin(String username, String password) throws SQLException, ClassNotFoundException {
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

    public String create(AccountRequest account) throws SQLException, ClassNotFoundException {
        NamedParameterStatement statement = DatabaseService.getInstance()
                .createNamedPreparedStatement("INSERT INTO public.account "
                + "(id, firstname, lastname, mailaddress, password, postal_code, house_number, reference) VALUES "
                + "(:id, :firstname, :lastname, :mailaddress, :password, :postal_code, :house_number, :reference);");
        statement.setString("id", UUID.randomUUID().toString());
        statement.setString("firstname", account.getFirstName());
        statement.setString("lastname", account.getLastName());
        statement.setString("postal_code", account.getPostalCode());
        statement.setString("house_number", account.getHouseNumber());
        statement.setString("password", account.getPassword());
        statement.setString("reference", "Role_Customer");
        statement.setString("mailaddress", account.getMailAddress());


        if(statement.executeUpdate() == 0){
            return null;
        }
        ResultSet keys = statement.getGeneratedKeys();


        if(keys.next()){
            return keys.getString("id");
        }else{
            return null;
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
        if(getFromId(account.getId()).getId() != null && account.getId() != null && !accountMemberRelationExists(account.getId(), group.getId())) {
            NamedParameterStatement statement = DatabaseService.getInstance()
                    .createNamedPreparedStatement(
                            "INSERT INTO account_group_member (id, account_id, group_id) VALUES (:id, :accountId, :groupId)"
                    );
            statement.setParameter("id", getAccountGroupMembersMax());
            statement.setParameter("accountId", account.getId());
            statement.setParameter("groupId", group.getId());


            PreparedStatement preparedStatement = DatabaseService.getInstance().createPreparedStatement(statement.toString());
            return preparedStatement.execute();
        }else{
            throw new NotFoundException("Account not found");
        }
    }
    private long getAccountGroupMembersMax() throws SQLException, ClassNotFoundException {
        List<Long> longs= new ArrayList<>();
        ResultSet resultSet = DatabaseService.getInstance()
                .createPreparedStatement("SELECT id FROM account_group_member").executeQuery();
        while (resultSet.next())
            longs.add(resultSet.getLong("id"));
        return longs.size() + 2;
    }

    private boolean accountMemberRelationExists(String accountId, long groupId) throws SQLException, ClassNotFoundException {
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

    public Account getAccountFromMail(String mail) throws SQLException, ClassNotFoundException, NotFoundException {
            NamedParameterStatement statement = DatabaseService.getInstance()
                    .createNamedPreparedStatement("SELECT * FROM account WHERE mailaddress = :mailaddress");
            statement.setParameter("mailaddress", mail);
            ResultSet result = statement.executeQuery();
            Account account;
            account = fromResultSet(result);
            if (account != null) return account;
        //return Optional.of(get(id));
        throw new NotFoundException();
    }

    public Account updateAccount(AccountRequest account) throws SQLException, ClassNotFoundException {
            NamedParameterStatement statement = DatabaseService.getInstance()
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
                    .setParameter("house_number", account.getHouseNumber());
            if(statement.executeUpdate() == 0){
                throw new NotFoundException();
            }
            ResultSet keys = statement.getGeneratedKeys();

            if(keys.next()){
                return fromResultSet(keys);
            }else{
                return null;
            }
    }

    public boolean delete(String account) throws SQLException, ClassNotFoundException {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("DELETE FROM account WHERE id = :id")
                    .setParameter("id", account)
                    .executeUpdate() > 0;
    }

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

    private Account replaceNull(Account account) throws SQLException, ClassNotFoundException {
        Account currentAccount = getAccountFromMail(account.getMailAddress());
        currentAccount.setMailAddress(account.getMailAddress());
        currentAccount.setFirstName(account.getFirstName());
        currentAccount.setLastName(account.getLastName());
        currentAccount.setPostalCode(account.getPostalCode());
        currentAccount.setHouseNumber(account.getHouseNumber());
        return currentAccount;
    }
}