package nl.iprwc.utils;

import javax.ws.rs.core.NewCookie;

public class KillCookie extends NewCookie {
    public KillCookie(String name)
    {
        this(name, "/");
    }

    public KillCookie(String name, String path)
    {
        this(name, path, null);
    }

    public KillCookie(String name, String path, String domain)
    {
        super(name, null, path, domain, null, 0, false);
    }
}
