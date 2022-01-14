package com.scotiademo.mslegacyrouting;

import static org.junit.Assert.assertTrue;

import java.io.File;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.apache.camel.test.spring.MockEndpointsAndSkip;
import org.apache.camel.test.spring.UseAdviceWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.junit.Ignore;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.scotiademo.mslegacyrouting.IntegrationUnitTest;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest(classes = IntegrationUnitTest.class)
@UseAdviceWith
@SpringBootApplication
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class IntegrationUnitTest {

	@Autowired
	private ProducerTemplate template;

	@Autowired
	private CamelContext context;
	ObjectMapper mapper = new ObjectMapper();

	@EndpointInject(uri = "mock:direct:restProducerRoute")
	MockEndpoint mockProducerEndpoint;

	private final static String RESPONSE_SUCCESS_MESSAGE = "src/test/resources/responseMessages/responseSuccessWS.txt";
	private final static String REQUEST_SUCCESS_MESSAGE = "src/test/resources/messageIn/request_success.txt";

	private final static String RESPONSE_SUCCESS_MESSAGE_REDENCION = "src/test/resources/responseMessages/responseSuccessRedencion.txt";
	private final static String REQUEST_SUCCESS_MESSAGE_REDENCION = "src/test/resources/messageIn/request_successRedencion.txt";

	private final static String RESPONSE_SUCCESS_MESSAGE_RECLAMACION = "src/test/resources/responseMessages/responseSuccessReclamacion.txt";
	private final static String REQUEST_SUCCESS_MESSAGE_RECLAMACION = "src/test/resources/messageIn/request_successReclamacion.txt";

	private final static String RESPONSE_SUCCESS_MESSAGE_ACUMULACION = "src/test/resources/responseMessages/responseSuccessAcumulacion.txt";
	private final static String REQUEST_SUCCESS_MESSAGE_ACUMULACION = "src/test/resources/messageIn/request_successAcumulacion.txt";

	@Before
	public void routeConfiguration() throws Exception {

	}

	// @Ignore
	@Test
	public void successRequestTransformationTest() throws Exception {

		final RouteDefinition routeConsumerWS = context.getRouteDefinition("rest_producer");
		routeConsumerWS.adviceWith(context, new AdviceWithRouteBuilder() {
			@Override
			public void configure() throws Exception {
				weaveById("Invoke_WS1").replace().process((e) -> {

					File responseMessage = new File(RESPONSE_SUCCESS_MESSAGE);
					String expectedMessage = context.getTypeConverter().convertTo(String.class, responseMessage);
					e.getIn().setBody(expectedMessage);
					e.getIn().setHeader("CamelHttpResponseCode", 200);
				});

			}
		});

		context.start();
		assertTrue(context.getStatus().isStarted());

		File expectedSuccessFile = new File(REQUEST_SUCCESS_MESSAGE);
		template.sendBody("direct:transformationRoute", ExchangePattern.InOut, expectedSuccessFile);

		mockProducerEndpoint.expectedMinimumMessageCount(0);
		mockProducerEndpoint.assertIsSatisfied();

	}

}
