package kr.co.koscom.mydataservicewebdemo.model;

import lombok.Data;

@Data
public class AU11Response {
	
	private String txid;
	
	private String tokenType;
	
	private String accessToken;
	
	private String expiresIn;
	
	private String refreshToken;
	
	private String refreshTokenExiresIn;
	
	private String scope;

}
