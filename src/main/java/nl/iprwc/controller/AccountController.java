package nl.iprwc.controller;

import com.sun.media.sound.InvalidDataException;
import nl.iprwc.Request.AccountRequest;
import nl.iprwc.db.AccountDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.groups.GroupService;
import nl.iprwc.hash.BCrypt;
import nl.iprwc.model.*;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AccountController {
    private final AccountDAO dao;
    private static AccountController instance;

    static {
        instance = new AccountController();
    }

    public AccountController() {
        dao = new AccountDAO();
    }

    public static AccountController getInstance() {
        return instance;
    }

    public Account getFromId(String id) throws InvalidOperationException {
        try {
            return dao.getFromId(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
    public Account tryLogin(Authentication auth) throws InvalidOperationException {
        try {
            return dao.tryLogin(auth.getMailAddress(), auth.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public Account fromMailAddress(String mailAddress) throws InvalidOperationException {
        try {
            return dao.getAccountFromMail(mailAddress);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public Account create(AccountRequest account) throws InvalidOperationException {
        try {
            if (getFromId(account.getId()) == null) {
                fromMailAddress(account.getMailAddress());
            }
            throw new InvalidOperationException();
        } catch (NotFoundException e) {
            //ignore
        }
        try {
            account.setPassword(new BCrypt().hash(account.getPassword()));

            String generatedKey = dao.create(account);

            Account generatedAccount;
            if (generatedKey != null) {
                generatedAccount = getFromId(generatedKey);

                long groupId = 1;
                String internalReference = null;
                internalReference = GroupService.getInstance().fromRawId(groupId).getInternalReference();

                if (dao.addGroupToAccount(generatedAccount, internalReference)) {
                    return getFromId(generatedAccount.getId());
                }
            }
            throw new InvalidOperationException();
        } catch (ClassNotFoundException | SQLException e) {
            throw new InvalidOperationException();
        }
    }

    public Account updateAccount(AccountRequest account) throws InvalidOperationException {
        try {
            return dao.updateAccount(account);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public boolean deleteAccount(String email) throws InvalidOperationException, nl.iprwc.exception.NotFoundException {
        try {
            Account account = fromMailAddress(email);
            ShoppingCartController.getInstance().delete(account.getId());
            GroupController.getInstance().deleteAccountGroup(account.getId());
            return dao.delete(account.getId());
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public List<Account> getAll() throws InvalidOperationException {
        try {
            return dao.getAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
}
