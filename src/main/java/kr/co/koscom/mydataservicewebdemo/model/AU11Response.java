package kr.co.koscom.mydataservicewebdemo.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AU11Response {
	
	private String txId;
	
	private String tokenType;
	
	private String accessToken;
	
	private String expiresIn;
	
	private String refreshToken;
	
	private String refreshTokenExpiresIn;
	
	private String scope;

}
