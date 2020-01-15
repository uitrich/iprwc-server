package nl.iprwc.Response;

import nl.iprwc.model.Session;
import org.joda.time.DateTime;

public class SessionStateResponse {
    private boolean valid;
    private DateTime lastActivity = null;
    private long accountId;

    public SessionStateResponse(Session session) {
        this(!session.isExpired(), session.getLastActivity(), session.getAccount().getId());
    }

    public SessionStateResponse(boolean valid) {
        this.valid = valid;
    }

    public SessionStateResponse(boolean valid, DateTime lastActivity, long accountId) {
        this.valid = valid;
        this.lastActivity = lastActivity;
        this.accountId = accountId;
    }

    public boolean isValid() {
        return valid;
    }

    public DateTime getLastActivity() {
        return lastActivity;
    }

    public long getAccountId() {
        return accountId;
    }
}
