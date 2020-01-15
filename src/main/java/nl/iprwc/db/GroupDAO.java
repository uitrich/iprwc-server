package nl.iprwc.db;

import nl.iprwc.model.Account;
import nl.iprwc.model.User;
import nl.iprwc.sql.DatabaseService;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupDAO {
    public boolean deleteAccountGroup(long id) {
        try {
            return DatabaseService.getInstance()
                    .createNamedPreparedStatement("DELETE FROM account_group_member WHERE account_id = :id")
                    .setParameter("account_id", id)
            .execute();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
