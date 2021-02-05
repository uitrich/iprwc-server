package nl.iprwc.controller;

import com.sun.media.sound.InvalidDataException;
import nl.iprwc.Request.AccountRequest;
import nl.iprwc.db.AccountDAO;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.groups.GroupService;
import nl.iprwc.hash.BCrypt;
import nl.iprwc.model.*;

import javax.ws.rs.InternalServerErrorException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AccountController {
    private final AccountDAO dao;
    private static AccountController instance;
    public static synchronized AccountController getInstance() {
        if (instance == null) {
            instance = new AccountController();
        }

        return instance;
    }
    public AccountController() {
        dao = new AccountDAO();
    }

    public Account getFromId(String id) {
        return dao.getFromId(id);
    }
    public Account tryLogin(Authentication auth) throws SQLException, ClassNotFoundException {
        return dao.tryLogin(auth.getMailAddress(), auth.getPassword());
    }

    public boolean makeAccount(Authentication value) {
        return dao.makeAccount(value);
    }

    public Account fromMailAddress(String mailAddress) {
        return dao.getAccountFromMail(mailAddress);
    }

    public Account create(AccountRequest account) throws InvalidDataException, SQLException, NotFoundException, ClassNotFoundException  {
            account.setPassword(new BCrypt().hash(account.getPassword()));

            String generatedKey = dao.create(account);

            Account generatedAccount;
            if(generatedKey != null){
                generatedAccount = getFromId(generatedKey);

                long groupId = 1;
                String internalReference = null;
                internalReference = GroupService.getInstance().fromRawId(groupId).getInternalReference();

                if(dao.addGroupToAccount(generatedAccount, internalReference)){
                    return getFromId(generatedAccount.getId());
                }
            }else{
                throw new InternalServerErrorException("Something went wrong creating the account");
            }
            return null;
    }

    public Account updateAccount(Account account){
        return dao.updateAccount(account);
    }
    public void UpdateAccountId(String oldId) throws SQLException, ClassNotFoundException {
        dao.UpdateId(oldId, UUID.randomUUID().toString());
    }

    public boolean deleteAccount(String account) {
        ShoppingCartController.getInstance().delete(account);
        GroupController.getInstance().deleteAccountGroup(account);
        return dao.delete(account);
    }

    public List<Account> getAll() throws SQLException, ClassNotFoundException {
        return dao.getAll();
    }
}
