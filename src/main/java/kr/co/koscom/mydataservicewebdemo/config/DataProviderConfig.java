package kr.co.koscom.mydataservicewebdemo.config;

public class DataProviderConfig {
	
	private String host;

	private String endpoint;
	
	private String orgCode;
	
	private String mtlsSerialNumber;
	
	private String cpCode;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getMtlsSerialNumber() {
		return mtlsSerialNumber;
	}

	public void setMtlsSerialNumber(String mtlsSerialNumber) {
		this.mtlsSerialNumber = mtlsSerialNumber;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	@Override
	public String toString() {
		return "DataProviderConfig [host=" + host + ", endpoint=" + endpoint + ", orgCode=" + orgCode
				+ ", mtlsSerialNumber=" + mtlsSerialNumber + ", cpCode=" + cpCode + "]";
	}
	
}
