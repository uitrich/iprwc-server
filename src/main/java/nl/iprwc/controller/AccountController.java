package nl.iprwc.controller;

import com.sun.media.sound.InvalidDataException;
import io.dropwizard.auth.Auth;
import nl.iprwc.Utils.Validator;
import nl.iprwc.db.AccountDAO;
import nl.iprwc.exception.NotFoundException;
import nl.iprwc.groups.GroupService;
import nl.iprwc.hash.BCrypt;
import nl.iprwc.model.*;
import nl.iprwc.sql.DatabaseService;

import javax.ws.rs.InternalServerErrorException;
import java.sql.SQLException;
import java.util.List;

public class AccountController {
    private final AccountDAO dao;
    private final SuperController superController;
    private final GroupController groupController;
    private final ShoppingCartController shoppingCartController;

    public AccountController() {
        dao = new AccountDAO();
        superController = SuperController.getInstance();
        shoppingCartController = superController.getShoppingCartController();
        groupController = superController.getGroupController();
    }

    public Account getFromId(long id) {
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

    public Account create(Account account) throws InvalidDataException, SQLException, NotFoundException, ClassNotFoundException  {

            List<FormError> list = account.isValid();
            account.setPasswordHash(new BCrypt().hash(account.getPasswordHash()));

            if(list.size() > 0){
                throw new InvalidDataException(list.toString());
            }

            long generatedKey = dao.create(account);

            Account generatedAccount;
            if(generatedKey != 0){
                generatedAccount = getFromId(generatedKey);

                long groupId = 1;
                String internalReferference = null;
                internalReferference = GroupService.getInstance().fromRawId(groupId).getInternalReference();

                if(dao.addGroupToAccount(generatedAccount, internalReferference)){
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

    public boolean deleteAccount(long account) {
        shoppingCartController.delete(account);
        groupController.deleteAccountGroup(account);
        return dao.delete(account);
    }
}
