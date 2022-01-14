
package com.scotiademo.mslegacyrouting.routes;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import net.minidev.json.JSONObject;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;

import javax.annotation.Resource;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.time.format.DateTimeParseException;

import com.scotiademo.mslegacyrouting.properties.RestProducer;
import com.scotiademo.mslegacyrouting.configurator.ConfigurationRoute;
import com.scotiademo.mslegacyrouting.util.ErrorsEnum;

@Component
public class RestProducerRoute extends ConfigurationRoute {

	@Resource
	private RestProducer restConfig;

	@Override
	public void configure() throws Exception {
		super.configure();

		onException(DateTimeParseException.class)
				.handled(true)
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_VALIDACION.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTION.getMessage())
				.setBody(simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.setHeader(ErrorsEnum.ERROR_HEADER.getMessage(), simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.bean(ErrorsEnum.BEAN_TRANSFORMATION.getMessage(), ErrorsEnum.BEAN_CREATE_RSERROR.getMessage())
				.end();

		onException(HttpOperationFailedException.class)
				.handled(true)
				.log(LoggingLevel.WARN, ErrorsEnum.ERROR_EXCEPTIONCLASS.getMessage())
				.log(LoggingLevel.WARN, ErrorsEnum.ERROR_EXCEPTIONCLASS_NAME.getMessage())
				.setBody(simple("${exception.responseBody}"))
				.log(LoggingLevel.WARN, ErrorsEnum.BODY.getMessage())
				.convertBodyTo(String.class)
				.end();

		onException(HttpHostConnectException.class, SocketTimeoutException.class)
				.handled(true)
				.maximumRedeliveries(4)
				.redeliveryDelay(2000)
				.logRetryAttempted(true)
				.retryAttemptedLogLevel(LoggingLevel.WARN)
				.log(LoggingLevel.ERROR,
						"Durante la comunicación por protocolo HTTP al host destino se presentan errores en la ruta ${routeId}")
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTIONCLASS.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTIONCLASS_NAME.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_STACKTRACE.getMessage())
				.setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON.toString()))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, simple("500"))
				.setHeader(ErrorsEnum.ERROR_HEADER.getMessage(), simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.bean(ErrorsEnum.BEAN_TRANSFORMATION.getMessage(), ErrorsEnum.BEAN_CREATE_RSERROR.getMessage())
				.wireTap(ErrorsEnum.DEFAULT_LOG_ERROR_ROUTE.getMessage())
				.end();

		onException(BeanValidationException.class).handled(true)
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_VALIDACION.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTION.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTIONCLASS.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTIONCLASS_NAME.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_STACKTRACE.getMessage())
				.setHeader(ErrorsEnum.ERROR_HEADER.getMessage(), simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, simple("500", Integer.class))
				.bean(ErrorsEnum.BEAN_TRANSFORMATION.getMessage(), ErrorsEnum.BEAN_CREATE_RSERROR.getMessage())
				.wireTap(ErrorsEnum.DEFAULT_LOG_ERROR_ROUTE.getMessage())
				.end();

		onException(InvalidFormatException.class, NullPointerException.class)
				.handled(true)
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_VALIDACION.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTION.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_STACKTRACE.getMessage())
				.setBody(simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.setHeader("mensajePer").simple(ErrorsEnum.BODY.getMessage())
				.setHeader(ErrorsEnum.ERROR_HEADER.getMessage(), simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, simple("500", Integer.class))
				.bean(ErrorsEnum.BEAN_TRANSFORMATION.getMessage(), ErrorsEnum.BEAN_CREATE_RSERROR.getMessage())
				.wireTap(ErrorsEnum.DEFAULT_LOG_ERROR_ROUTE.getMessage())
				.end();

		onException(UnknownHostException.class)
				.handled(true)
				.maximumRedeliveries(4)
				.redeliveryDelay(2000)
				.logRetryAttempted(true)
				.retryAttemptedLogLevel(LoggingLevel.WARN)
				.log(LoggingLevel.ERROR,
						"Durante la comunicación por protocolo HTTP al host destino se presentan errores en la ruta ${routeId}")
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTIONCLASS.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTIONCLASS_NAME.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_STACKTRACE.getMessage())
				.setBody(simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.setHeader("mensajePer").simple(ErrorsEnum.BODY.getMessage())
				.setHeader(ErrorsEnum.ERROR_HEADER.getMessage(), simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, simple("500", Integer.class))
				.bean(ErrorsEnum.BEAN_TRANSFORMATION.getMessage(), ErrorsEnum.BEAN_CREATE_RSERROR.getMessage())
				.wireTap(ErrorsEnum.DEFAULT_LOG_ERROR_ROUTE.getMessage())
				.end();

		onException(java.lang.Exception.class).handled(true)
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_VALIDACION.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTION.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_STACKTRACE.getMessage())
				.setHeader(ErrorsEnum.ERROR_HEADER.getMessage(), simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, simple("500", Integer.class))
				.setHeader(ErrorsEnum.ERROR_HEADER.getMessage(), simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.bean(ErrorsEnum.BEAN_TRANSFORMATION.getMessage(), ErrorsEnum.BEAN_CREATE_RSERROR.getMessage())
				.wireTap(ErrorsEnum.DEFAULT_LOG_ERROR_ROUTE.getMessage())
				.end();

		from("direct:restProducerRoute").routeId("rest_producer")
				.log(".:: Producer (Demo Scotia)::.")
				.setHeader(Exchange.HTTP_URI).simple(restConfig.getPathDemo())
				.setHeader(Exchange.CONTENT_TYPE, constant(ErrorsEnum.APPLICATION_JSON_CHAR.getMessage()))
				.setHeader(Exchange.HTTP_METHOD, constant("GET"))
				.log(".::  Enviando peticion a Producer::.")
				.to(ErrorsEnum.LOG_PRODUCER.getMessage())
				.convertBodyTo(String.class)
				.to("https4:InvokeValidacionCliente").id("Invoke_WS1")
				.convertBodyTo(String.class)
				.unmarshal().json(JsonLibrary.Jackson, JSONObject.class)
				.to(ErrorsEnum.LOGRESPONSE.getMessage())
				.end();

	}
}