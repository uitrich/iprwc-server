package nl.iprwc;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.db.DataSourceFactory;
import org.hibernate.validator.constraints.*;
import org.joda.time.Duration;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class SoftiServerConfiguration extends Configuration {
    @NotEmpty
    private String sessionDurationRaw = "PT7200S"; // Java seems a bit stupid here, why doesn't PT2H work?
    private Duration sessionDuration;

    private String dateFormat = "yyyy-MM-dd";
    private String timeFormat = "HH:mm:ss";
    private String dateTimeFormat = dateFormat + "'T'" + timeFormat;

    private String defaultTimeZone = "UTC";
    private String filePath;

    private String webUrl;

    @NotNull
    @JsonProperty("webUrl")
    public String getWebUrl() {
        return webUrl;
    }

    @JsonProperty("webUrl")
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @JsonProperty("filePath")
    public String getFilePath() {
        return filePath;
    }

    @JsonProperty("filePath")
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }




    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }



    public Duration getSessionDuration()
    {
        if (sessionDuration == null) {
            sessionDuration = Duration.parse(sessionDurationRaw);
        }

        return sessionDuration;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public String getDateTimeFormat() {
        return dateTimeFormat;
    }

    public DateFormat getDateTimeFormatter()
    {
        DateFormat format = new SimpleDateFormat(dateTimeFormat);
        format.setTimeZone(TimeZone.getTimeZone(defaultTimeZone));
        return format;
    }
}
