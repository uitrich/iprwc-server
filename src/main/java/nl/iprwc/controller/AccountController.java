package nl.iprwc.controller;

import nl.iprwc.Request.AccountRequest;
import nl.iprwc.controller.abstractions.SimpleCRUDController;
import nl.iprwc.db.AccountDAO;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.groups.GroupService;
import nl.iprwc.hash.BCrypt;
import nl.iprwc.model.*;

import nl.iprwc.exception.NotFoundException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AccountController implements SimpleCRUDController<Account, String, AccountRequest> {
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


    @Override
    public List<Account> getAll() throws InvalidOperationException {
        try {
            return dao.getAll();
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
    @Override
    public Account get(String id) throws InvalidOperationException {
        try {
            return dao.get(id);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
    public Account tryLogin(Authentication auth) throws InvalidOperationException {
        try {
            return dao.getByLogin(auth.getMailAddress(), auth.getPassword());
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public Account fromMailAddress(String mailAddress) throws InvalidOperationException, NotFoundException{
        try {
            return dao.getAccountFromMail(mailAddress);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }

    public Account createAccount(AccountRequest request) throws InvalidOperationException, NotFoundException {
        return get(create(request));
    }

    @Override
    public String create(AccountRequest account) throws InvalidOperationException, NotFoundException {
        try {
            if (get(account.getId()) == null) {
                //This should throw an exception if the email does not exist within the account table
                fromMailAddress(account.getMailAddress());
            } else {
                // If by some unlucky miracle a UUID is generated that already exists,
                // give the accountRequest parameter a new one and try again.
                account.setId(UUID.randomUUID().toString());
                return create(account);
            }
            throw new InvalidOperationException("Duplicate mail address or id");
        } catch (NotFoundException e) {
            try {
                account.setPassword(new BCrypt().hash(account.getPassword()));

                String generatedKey = dao.create(account);

                Account generatedAccount;
                if (generatedKey != null) {
                    generatedAccount = get(generatedKey);

                    long groupId = 1;
                    String internalReference = null;
                    internalReference = GroupService.getInstance().fromRawId(groupId).getInternalReference();

                    if (GroupController.getInstance().addGroupToAccount(generatedAccount, internalReference)) {
                        return generatedAccount.getId();
                    }
                }
                throw new InvalidOperationException();
            } catch (ClassNotFoundException | SQLException er) {
                throw new InvalidOperationException();
            }
        }
    }

    @Override
    public boolean update(AccountRequest account) throws InvalidOperationException, NotFoundException {
        try {
            return dao.update(account);
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }
    @Override
    public boolean delete(String email) throws InvalidOperationException, NotFoundException {
        try {
            Account account = fromMailAddress(email);
            ShoppingCartController.getInstance().delete(account.getId());
            GroupController.getInstance().deleteAccountGroup(account.getId());
            return dao.delete(account.getId());
        } catch (SQLException | ClassNotFoundException e) {
            throw new InvalidOperationException();
        }
    }


}
