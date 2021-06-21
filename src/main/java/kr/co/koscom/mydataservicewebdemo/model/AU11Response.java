package kr.co.koscom.mydataservicewebdemo.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AU11Response {
	
	private String txid;
	
	private String tokenType;
	
	private String accessToken;
	
	private String expiresIn;
	
	private String refreshToken;
	
	private String refreshTokenExiresIn;
	
	private String scope;

}
