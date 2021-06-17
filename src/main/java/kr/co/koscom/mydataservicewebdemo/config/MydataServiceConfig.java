package kr.co.koscom.mydataservicewebdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix="service")
public class MydataServiceConfig {
	private String id;
	
	private String name;
	
	private String orgCode;
	
	private String clientId;
	
	private String clientSecret;
	
	private String keyStorePath;
	
	private String keyPassword;
	
	private String trustStorePath;
	
	private String callbackUrl;
}
