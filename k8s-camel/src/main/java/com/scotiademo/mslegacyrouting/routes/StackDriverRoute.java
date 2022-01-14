
package com.scotiademo.mslegacyrouting.routes;

import com.scotiademo.mslegacyrouting.util.ErrorsEnum;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StackDriverRoute extends RouteBuilder {

        private static final Logger LOGGER = LoggerFactory.getLogger("ms-rest-scotiademo");

        @Override
        public void configure() {

                from(ErrorsEnum.TOKEN_FAIL_STACK_DRIVER_HEADERS_ROUTE.getMessage())
                                .routeId("scotiademo_route_extractheaders")
                                .setHeader("stackHeader", simple("ms-rest-scotiademo;${date:now:yyyy-MM-dd HH:mm:ssZ}"))
                                .end();

                from("direct:logRequestMS")
                                .to(ErrorsEnum.TOKEN_FAIL_STACK_DRIVER_HEADERS_ROUTE.getMessage())
                                .log(LoggingLevel.INFO, LOGGER,
                                                "->> ${headers.stackHeader}, Uri: ${headers.CamelHttpUri}, Request MS: ${body}")
                                .end();

                from("direct:logRequestToken")
                                .to(ErrorsEnum.TOKEN_FAIL_STACK_DRIVER_HEADERS_ROUTE.getMessage())
                                .log(LoggingLevel.INFO, LOGGER,
                                                "->> ${headers.stackHeader}, Uri: ${headers.CamelHttpUri} , " +
                                                                "QueryParams: ${headers.CamelHttpQuery}, Request Token: ${body}")
                                .end();

                from("direct:logResponseToken")
                                .to(ErrorsEnum.TOKEN_FAIL_STACK_DRIVER_HEADERS_ROUTE.getMessage())
                                .log(LoggingLevel.INFO, LOGGER, "<<- ${headers.stackHeader}," +
                                                "HttpCode: ${headers.CamelHttpResponseCode}, Response Token: ${body}")
                                .end();

                from("direct:logRequestProducer")
                                .to(ErrorsEnum.TOKEN_FAIL_STACK_DRIVER_HEADERS_ROUTE.getMessage())
                                .log(LoggingLevel.INFO, LOGGER,
                                                "->> ${headers.stackHeader}, Uri: ${headers.CamelHttpUri}, Request Producer: ${body}")
                                .end();

                from("direct:logResponseProducer")
                                .to(ErrorsEnum.TOKEN_FAIL_STACK_DRIVER_HEADERS_ROUTE.getMessage())
                                .log(LoggingLevel.INFO, LOGGER, "<<- ${headers.stackHeader},  " +
                                                "HttpCode: ${headers.CamelHttpResponseCode}, Response Token: ${body}")
                                .end();

                from("direct:logResponseMS")
                                .to(ErrorsEnum.TOKEN_FAIL_STACK_DRIVER_HEADERS_ROUTE.getMessage())
                                .log(LoggingLevel.INFO, LOGGER, "<<- ${headers.stackHeader}, " +
                                                "HttpCode: ${headers.CamelHttpResponseCode}, Response MS: ${body}")
                                .end();

                from("direct:logResponseWithErrorMS")
                                .to("direct:extractStackDriverHeaders")
                                .log(LoggingLevel.ERROR, LOGGER, "<<- ${headers.stackHeader}, " +
                                                "HttpCode: ${headers.CamelHttpResponseCode}, Response MS: ${body}," +
                                                "Route ${routeId}, ExceptionClassName: ${exchangeProperty.CamelExceptionCaught.class.name}"
                                                +
                                                ", ErroBody: ${exception.responseBody}")
                                .end();
        }
}
