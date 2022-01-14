package com.scotiademo.mslegacyrouting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotNull;

public class Error {

    @JsonProperty("code")
    @NotNull(message = "El campo code no puede estar vacio")
    private String code;

    @JsonProperty("message")
    @NotNull(message = "El campo message no puede estar vacio")
    private String message;

    @JsonProperty("logID")
    @NotNull(message = "El campo logID no puede estar vacio")
    private String logID;

    @ApiModelProperty(required = true, value = "Código del error", example = "400")
    public String getCode() {
        return code;
    }

    @ApiModelProperty(required = true, value = "ID del log presentado", example = "C0000AD60009198A0000009E0000544C")
    public String getLogID() {
        return logID;
    }

    @ApiModelProperty(required = true, value = "Descripción del error técnico", example = "El cliente se encuentra bloqueado")
    public String getMessage() {
        return message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setLogID(String logID) {
        this.logID = logID;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
