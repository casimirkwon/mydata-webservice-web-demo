package kr.co.koscom.mydataservicewebdemo.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix="mydata")
public class MydataServiceContext {
	private MydataServiceConfig service;
	
	private Map<String, DataProviderConfig> dataProviders;
	
	private String id;

	public MydataServiceConfig getService() {
		return service;
	}

	public void setService(MydataServiceConfig service) {
		this.service = service;
	}

	public Map<String, DataProviderConfig> getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(Map<String, DataProviderConfig> dataProviders) {
		this.dataProviders = dataProviders;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "MydataServiceContext [service=" + service + ", dataProviders=" + dataProviders + ", id=" + id + "]";
	}

	
	
}