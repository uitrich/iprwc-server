package nl.iprwc.groups;

import nl.iprwc.sql.DatabaseService;
import nl.iprwc.model.Account;
import nl.iprwc.model.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manages the permission groups in the appllication. The groups are cached to increase performance.
 */
public class GroupService {
    private static GroupService instance;

    public static synchronized GroupService getInstance() {
        if (instance == null) {
            instance = new GroupService();
        }

        return instance;
    }

    private RawGroup[] groups;

    private GroupService() { }

    public synchronized void clearCache()
    {
        groups = null;
    }

    public synchronized void load()
    {
        if (groups == null) {
            try {
                ResultSet resultGroups = DatabaseService
                        .getInstance()
                        .createNamedPreparedStatement("SELECT * FROM \"group\"")
                        .executeQuery();

                ResultSet resultGroupRelations = DatabaseService
                         .getInstance()
                         .createNamedPreparedStatement("SELECT * FROM group_group_member")
                         .executeQuery();

                List<RawGroup> groupList = new ArrayList<>();

                while (resultGroups.next()) {
                    RawGroup group = new RawGroup();
                    group.id = resultGroups.getLong("id");
                    group.name = resultGroups.getString("name");
                    group.system = resultGroups.getBoolean("system");
                    group.internalReference = resultGroups.getString("internal_reference");
                    group.editable = resultGroups.getBoolean("editable");
                    group.memberGroupIds = new ArrayList<>();

                    groupList.add(group);
                }

                groups = groupList.toArray(new RawGroup[0]);

                while (resultGroupRelations.next()) {
                    for (int i = 0; i < this.groups.length; i++) {
                        if (groups[i].id == resultGroupRelations.getLong("group_id")) {
                            groups[i].memberGroupIds.add(resultGroupRelations.getLong("member_group_id"));
                            break;
                        }
                    }
                }
            }
            catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private Group[] getMemberGroups(List<Long> ids)
    {
        List<Group> groupList = new ArrayList<>();

        for (long id : ids) {
            for (RawGroup group : groups) {
                if (group.id == id) {
                    groupList.add(fromRawId(id));
                }
            }
        }

        return groupList.toArray(new Group[0]);
    }

    public Group fromRawId(long id)
    {
        load();

        for (RawGroup rawGroup : groups) {
            if (rawGroup.id == id) {
                return new Group(rawGroup, getMemberGroups(rawGroup.memberGroupIds));
            }
        }

        return null;
    }

    public Group[] getGroups(Group group)
    {
        return Objects.requireNonNull(
                fromRawId(group.getId())
        ).getGroups();
    }

    public Group[] getGroups(Account account)
    {
        List<Group> groupList = new ArrayList<>();

        try {
            ResultSet result = DatabaseService
                    .getInstance()
                    .createNamedPreparedStatement("SELECT group_id FROM account_group_member WHERE account_id = :id")
                    .setParameter("id", account.getId())
                    .executeQuery();

            while (result.next()) {
                groupList.add(fromRawId(result.getLong("group_id")));
            }

            return groupList.toArray(new Group[0]);
        }
        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
