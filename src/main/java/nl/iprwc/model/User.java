package nl.iprwc.model;

import java.security.Principal;

public class User implements Principal {
    private static final String NAME_NO_ACCOUNT = "Anonymous User";

    private Account account;

    public User() { }

    public User(Account account)
    {
        this.account = account;
    }


    public Account getAccount() {
        return account;
    }

    public boolean isAnonymous()
    {
        return account == null;
    }

    @Override
    public String getName() {
        return account == null
                ? NAME_NO_ACCOUNT
                : account.getMailAddress();
    }

    public Group[] getGroups() {
        return account == null
                ? new Group[0]
                : account.getGroups().toArray(new Group[0]);
    }

    public boolean hasGroupReference(String reference)
    {
        return checkGroupReference(getGroups(), reference);
    }

    private boolean checkGroupReference(Group[] groups, String reference)
    {
        for (Group group : groups) {
            if (reference.equals(group.getInternalReference())
                    || checkGroupReference(group.getGroups(), reference)
            ) {
                return true;
            }
        }

        return false;
    }
}
