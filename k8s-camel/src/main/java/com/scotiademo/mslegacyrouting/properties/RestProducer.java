
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
@ConfigurationProperties(prefix = "ms.configuration.rest.producer")
public class RestProducer {

	private String pathDemo;

}
