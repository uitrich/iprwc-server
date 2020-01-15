package nl.iprwc.Utils;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

public class SetCookie extends NewCookie {
    public SetCookie(String name, String value) {
        super(name, value, "/", null, DEFAULT_VERSION,
                null, DEFAULT_MAX_AGE, null, false, false);
    }

    public SetCookie(String name, String value, boolean httpOnly) {
        super(name, value, "/", null, DEFAULT_VERSION,
                null, DEFAULT_MAX_AGE, null, false, httpOnly);
    }
}

