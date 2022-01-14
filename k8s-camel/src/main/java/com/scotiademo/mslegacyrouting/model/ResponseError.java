
package com.scotiademo.mslegacyrouting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

public class ResponseError {

    @JsonProperty(value = "tipoError")
    private String errorType;

    @JsonProperty(value = "codigoError")
    @JsonPropertyDescription(value = "Código del error que se presentó en el servicio, "
            + "se deben basar en la tabla de errores definida por el proveedor del servicio "
            + "y con base en los lineamientos para integraciones defido por scotiademo")
    private String errorCode;

    @JsonProperty(value = "mensajeError")
    @JsonPropertyDescription(value = "Descripción del mensaje de error")
    private String errorMesagge;

    public ResponseError() {
        super();
    }

    public ResponseError(String errorType, String errorCode, String errorMesagge) {
        this.errorType = errorType;
        this.errorCode = errorCode;
        this.errorMesagge = errorMesagge;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMesagge() {
        return errorMesagge;
    }

    public void setErrorMesagge(String errorMesagge) {
        this.errorMesagge = errorMesagge;
    }
}
