package nl.iprwc.model;

import nl.iprwc.SoftiServerApplication;
import nl.iprwc.SoftiServerConfiguration;
import nl.iprwc.hash.Sha512;
import org.joda.time.DateTime;
import org.joda.time.Duration;

public class Session {
    private long id;
    private Account account;
    private String sessionKey;
    private String sessionKeyHashed;
    private DateTime lastActivity;

    public Session(long id, Account account, String sessionKey, String sessionKeyHashed, DateTime lastActivity)
    {
        this.id = id;
        this.account = account;
        this.sessionKey = sessionKey;
        this.sessionKeyHashed = sessionKeyHashed;
        this.lastActivity = lastActivity;
    }

    public Session(long id, Account account, String sessionKeyHashed, DateTime lastActivity)
    {
        this(id, account, null, sessionKeyHashed, lastActivity);
    }

    public Session(Account account, String sessionKey, DateTime lastActivity)
    {
        this(0, account, sessionKey, new Sha512().hash(sessionKey), lastActivity);
    }

    public long getId() {
        return id;
    }

    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) { this.account = account; }

    public String getSessionKey() {
        return sessionKey;
    }

    public DateTime getLastActivity() {
        return lastActivity;
    }

    public String getSessionKeyHashed() {
        return sessionKeyHashed;
    }

    public boolean isExpired()
    {
        Duration sessionDuration = SoftiServerApplication.getServerConfiguration().getSessionDuration();
        DateTime expireDateTime = DateTime.now().minus(sessionDuration);
        return lastActivity.isBefore(expireDateTime);
    }
}
