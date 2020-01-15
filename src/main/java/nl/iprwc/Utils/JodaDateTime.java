package nl.iprwc.Utils;

import org.joda.time.DateTime;

import java.sql.Timestamp;

public class JodaDateTime {
    public static DateTime fromTimestamp(Timestamp timestamp)
    {
        return fromTimestamp(timestamp, false);
    }

    public static DateTime fromTimestamp(Timestamp timestamp, boolean allowNull)
    {
        if (null == timestamp) {
            if (allowNull)
                return null;

            throw new NullPointerException("Timestamp is null");
        }

        return new DateTime(timestamp.getTime());
    }

    public static Timestamp toTimestamp(DateTime dateTime)
    {
        return new Timestamp(dateTime.getMillis());
    }
}
