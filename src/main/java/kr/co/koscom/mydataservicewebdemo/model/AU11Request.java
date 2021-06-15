package kr.co.koscom.mydataservicewebdemo.model;

import lombok.Data;

@Data
public class AU11Request {
	
	private String txId;
	
	private String orgCode;
	
	private String grantType;
	
	private String clientId;
	
	private String clientSecret;
	
	private String caCode;
	
	private String consentType;
	
	private String consentLen;
	
	private String consent;
	
	private String username;
	
	private String passwordLen;
	
	private String password;
	
	private String authType;
	
	private String signedPersonInfoReqLen;
	
	private String signedPersonInfoReq;
	
	private String consentNonce;
	
	private String ucpidNonce;
}
