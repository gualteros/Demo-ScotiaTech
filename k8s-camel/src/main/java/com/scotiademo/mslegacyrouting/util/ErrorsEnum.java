package com.scotiademo.mslegacyrouting.util;

public enum ErrorsEnum {

    DEFAULT_LOG_ERROR_ROUTE("direct:logResponseWithErrorMS"),
    TOKEN_FAIL_STACK_DRIVER_HEADERS_ROUTE("direct:logGeneralHeaders"),
    ERROR_STACKTRACE("StackTrace: ${exception.stacktrace}"),
    ERROR_EXCEPTION("Exception: ${exception.message}"),
    ERROR_HEADER("error"),
    BEAN_TRANSFORMATION("transformationComponent"),
    BEAN_CREATE_RSERROR("createRsError"),
    ERROR_EXCEPTIONCLASS("ExceptionClass: ${exchangeProperty.CamelExceptionCaught.class}"),
    ERROR_EXCEPTIONCLASS_NAME("ExceptionClassName: ${exchangeProperty.CamelExceptionCaught.class.name}"),
    ERROR_VALIDACION("Error de validacion: ${routeId}"),
    ERROR_ESTRUCTURA("La estructura del mensaje a procesar presenta errores en la ruta ${routeId}"),
    APPLICATION_JSON("application/json"),
    BODY("${body}"),
    EXCHANGE_BODY("${exchangeProperty.body}"),
    APPLICATION_JSON_CHAR("application/json;charset=UTF-8"),
    LOG_PRODUCER("direct:logRequestProducer"),
    LOGRESPONSE("direct:logResponseMS");

    private final String message;

    private ErrorsEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
