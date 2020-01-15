package nl.iprwc.Response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SuccessResponse {
    @JsonProperty
    public boolean isSuccess() {
        return true;
    }
}
