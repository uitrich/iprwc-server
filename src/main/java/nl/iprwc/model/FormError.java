package nl.iprwc.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to use as a error. When a fiel supplied by the client contains a error. a List of this model will be returned.
 */
public class FormError {
    private String field;
    private Enum error;

    public FormError(String field, Enum error) {
        this.field = field;
        this.error = error;
    }

    @JsonProperty
    public String getField() {
        return field;
    }

    @JsonProperty
    public Enum getError() {
        return error;
    }
}
