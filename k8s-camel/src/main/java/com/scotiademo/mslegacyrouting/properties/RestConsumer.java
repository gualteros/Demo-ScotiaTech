
package com.scotiademo.mslegacyrouting.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "ms.configuration.rest.consumer")
public class RestConsumer {

	private String serviceName;
	private String apiPath;
	private String apiTitle;
	private String apiVersion;
	private String apiBasePath;

}