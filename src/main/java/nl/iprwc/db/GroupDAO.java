package nl.iprwc.db;

import nl.iprwc.controller.AccountController;
import nl.iprwc.exception.InvalidOperationException;
import nl.iprwc.model.Account;
import nl.iprwc.model.Group;
import nl.iprwc.model.User;
import nl.iprwc.sql.DatabaseService;
import nl.iprwc.sql.NamedParameterStatement;

import nl.iprwc.exception.NotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDAO {
    public boolean deleteAccountGroup(String id) throws SQLException, ClassNotFoundException {
        return DatabaseService.getInstance()
                .createNamedPreparedStatement("DELETE FROM account_group_member WHERE account_id = :id")
                .setParameter("id", id)
        .execute();
    }

    public Group getGroupIdByName(String name) throws SQLException, ClassNotFoundException, NotFoundException {
        NamedParameterStatement statement = DatabaseService
                .getInstance()
                .createNamedPreparedStatement("SELECT * FROM public.group WHERE internal_reference = :reference")
                .setParameter("reference", name);
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

    public boolean addGroupToAccount(Account account, String groupName) throws SQLException, ClassNotFoundException, InvalidOperationException, NotFoundException {
        Group group = getGroupIdByName(groupName);
        if(AccountController.getInstance().get(account.getId()).getId() != null && account.getId() != null && !accountMemberRelationExists(account.getId(), group.getId())) {
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
}
