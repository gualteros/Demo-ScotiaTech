
package com.scotiademo.mslegacyrouting.routes;

import org.apache.camel.Exchange;

import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.LoggingLevel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;

import com.scotiademo.mslegacyrouting.model.ResponseError;
import com.scotiademo.mslegacyrouting.properties.RestConsumer;
import com.scotiademo.mslegacyrouting.configurator.ConfigurationRoute;
import com.scotiademo.mslegacyrouting.util.ErrorsEnum;

import net.minidev.json.JSONObject;

@Component
public class RestConsumerRoute extends ConfigurationRoute {

	@Autowired
	private RestConsumer restConfig;

	@Override
	public void configure() throws Exception {

		onException(JsonProcessingException.class).handled(true)
				.log(LoggingLevel.ERROR, "Error General en la ruta  ${routeId}")
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTIONCLASS.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTIONCLASS_NAME.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_STACKTRACE.getMessage())
				.setHeader("error", simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON.toString()))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant("400"))
				.bean(ErrorsEnum.BEAN_TRANSFORMATION.getMessage(), ErrorsEnum.BEAN_CREATE_RSERROR.getMessage())
				.wireTap(ErrorsEnum.DEFAULT_LOG_ERROR_ROUTE.getMessage())
				.end();

		onException(Exception.class).handled(true)
				.log(LoggingLevel.ERROR, "Error General en la ruta  ${routeId}")
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTIONCLASS.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_EXCEPTIONCLASS_NAME.getMessage())
				.log(LoggingLevel.ERROR, ErrorsEnum.ERROR_STACKTRACE.getMessage())
				.setHeader(Exchange.CONTENT_TYPE, simple(MediaType.APPLICATION_JSON.toString()))
				.setHeader(Exchange.HTTP_RESPONSE_CODE, constant("500"))
				.setHeader(ErrorsEnum.ERROR_HEADER.getMessage(), simple(ErrorsEnum.ERROR_EXCEPTION.getMessage()))
				.bean(ErrorsEnum.BEAN_TRANSFORMATION.getMessage(), ErrorsEnum.BEAN_CREATE_RSERROR.getMessage())
				.wireTap(ErrorsEnum.DEFAULT_LOG_ERROR_ROUTE.getMessage())
				.end();

		super.configure();
		restConfiguration()
				.component("servlet")
				.bindingMode(RestBindingMode.json)
				.enableCORS(true)
				.apiContextPath(restConfig.getApiPath())
				.apiProperty("api.title", restConfig.getApiTitle())
				.apiProperty("api.version", restConfig.getApiVersion())
				.apiProperty("base.path", restConfig.getApiBasePath())
				.apiProperty("cors", "true");

		rest(restConfig.getServiceName())
				// need to specify the POJO types the binding is using (otherwise json binding
				// defaults to Map based)
				.get()
				.type(JSONObject.class)
				.outType(JSONObject.class)
				.description("Descripción correspondiente a la operación")
				.consumes(ErrorsEnum.APPLICATION_JSON.getMessage()).produces(ErrorsEnum.APPLICATION_JSON.getMessage())
				.id("mslegacyrouting_Consumer")

				.responseMessage().code(HttpStatus.OK.value())
				.message("Respuesta Extosa")
				.responseModel(ResponseError.class)
				.endResponseMessage()

				.responseMessage().code(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.message("Internal server error")
				.responseModel(ResponseError.class)
				.endResponseMessage()

				.responseMessage().code(HttpStatus.BAD_REQUEST.value())
				.message("Bad Request")
				.responseModel(ResponseError.class)
				.endResponseMessage()

				.to("direct:transformationRoute");

		// need to specify the POJO types the binding is using (otherwise json binding
		// defaults to Map based)

	}
}
