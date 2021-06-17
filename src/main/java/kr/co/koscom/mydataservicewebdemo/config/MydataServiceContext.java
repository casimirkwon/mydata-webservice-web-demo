package kr.co.koscom.mydataservicewebdemo.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
@Configuration
@ConfigurationProperties(prefix="mydata")
public class MydataServiceContext {
//	private MydataServiceConfig service;
//	
//	private Map<String, DataProviderConfig> dataProviders;
//	
	private String id;
}