package nl.iprwc.hash;

import java.util.function.Function;

public class BCrypt {
    private static final int ROUNDS = 14;

    private final int logRounds;

    public BCrypt()
    {
        logRounds = ROUNDS;
    }

    public BCrypt(int logRounds)
    {
        this.logRounds = logRounds;
    }

    public String hash(String password)
    {
        String salt = org.mindrot.jbcrypt.BCrypt.gensalt(logRounds);
        return org.mindrot.jbcrypt.BCrypt.hashpw(password, salt);
    }

    public boolean verifyHash(String password, String hash)
    {
        return org.mindrot.jbcrypt.BCrypt.checkpw(password, hash);
    }

    public boolean verifyHashAndUpdate(String password, String hash, Function<String, Boolean> updater)
    {
        if (verifyHash(password, hash)) {
            int rounds = getRounds(hash);

            if (rounds != logRounds) {
                hash = hash(password);
                return updater.apply(hash);
            }

            return true;
        }

        return false;
    }

    private int getRounds(String salt) {
        char minor = (char)0;
        int off = 0;

        if (salt.charAt(0) != '$' || salt.charAt(1) != '2')
            throw new IllegalArgumentException ("Invalid salt version");
        if (salt.charAt(2) == '$')
            off = 3;
        else {
            minor = salt.charAt(2);
            if (minor != 'a' || salt.charAt(3) != '$')
                throw new IllegalArgumentException ("Invalid salt revision");
            off = 4;
        }

        // Extract number of rounds
        if (salt.charAt(off + 2) > '$')
            throw new IllegalArgumentException ("Missing salt rounds");
        return Integer.parseInt(salt.substring(off, off + 2));
    }
}
