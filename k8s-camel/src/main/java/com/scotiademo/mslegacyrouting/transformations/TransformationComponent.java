
package com.scotiademo.mslegacyrouting.transformations;

import com.google.gson.Gson;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.scotiademo.mslegacyrouting.model.Error;
import com.scotiademo.mslegacyrouting.model.ErrorEstructura;

@Component("transformationComponent")
public class TransformationComponent {

    private static final Logger logger = LoggerFactory.getLogger("ms-rest-scotiademo-OC");

    public void createRsError(Exchange exchange) {
        Error errormsg = new Error();
        errormsg.setCode(exchange.getIn().getHeader("CamelHttpResponseCode").toString());
        errormsg.setLogID("-");
        errormsg.setMessage(exchange.getIn().getHeader("error").toString());

        ErrorEstructura error = new ErrorEstructura();
        error.setError(errormsg);

        exchange.getOut().setBody(error);

        logger.info(".:: Error controlado ::.");
    }

}
