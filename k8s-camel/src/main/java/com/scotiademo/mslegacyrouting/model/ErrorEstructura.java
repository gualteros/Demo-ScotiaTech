package com.scotiademo.mslegacyrouting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;

public class ErrorEstructura {

    @JsonProperty("error")
    @NotNull(message = "El campo error no puede estar vacio")
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
