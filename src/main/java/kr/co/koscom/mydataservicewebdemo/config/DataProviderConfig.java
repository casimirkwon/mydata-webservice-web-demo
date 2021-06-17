package kr.co.koscom.mydataservicewebdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Configuration
@ConfigurationProperties(prefix="data-provider")
public class DataProviderConfig {
	
	private String id;

	private String endpoint;
	
	private String orgCode;
}
