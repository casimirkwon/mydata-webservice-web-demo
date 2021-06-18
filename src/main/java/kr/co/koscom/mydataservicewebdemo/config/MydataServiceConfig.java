package kr.co.koscom.mydataservicewebdemo.config;

public class MydataServiceConfig {
	private String id;
	
	private String name;
	
	private String orgCode;
	
	private String clientId;
	
	private String clientSecret;
	
	private String keyStorePath;
	
	private String keyStorePassword;
	
	private String keyStoreAlias;
	
	private String trustStorePath;
	
	private String trustStorePassword;
	
	private String callbackUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getKeyStorePath() {
		return keyStorePath;
	}

	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	public String getKeyStoreAlias() {
		return keyStoreAlias;
	}

	public void setKeyStoreAlias(String keyStoreAlias) {
		this.keyStoreAlias = keyStoreAlias;
	}

	public String getTrustStorePath() {
		return trustStorePath;
	}

	public void setTrustStorePath(String trustStorePath) {
		this.trustStorePath = trustStorePath;
	}

	public String getTrustStorePassword() {
		return trustStorePassword;
	}

	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	@Override
	public String toString() {
		return "MydataServiceConfig [id=" + id + ", name=" + name + ", orgCode=" + orgCode + ", clientId=" + clientId
				+ ", clientSecret=" + clientSecret + ", keyStorePath=" + keyStorePath + ", keyStorePassword="
				+ keyStorePassword + ", keyStoreAlias=" + keyStoreAlias + ", trustStorePath=" + trustStorePath
				+ ", trustStorePassword=" + trustStorePassword + ", callbackUrl=" + callbackUrl + "]";
	}

}
