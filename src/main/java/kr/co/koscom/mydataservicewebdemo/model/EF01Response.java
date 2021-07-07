package kr.co.koscom.mydataservicewebdemo.model;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import kr.co.koscom.mydataservicewebdemo.model.IV01Response.IV01ResponseAccount;
import lombok.Data;

@Data
@ApiModel
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
public class EF01Response {
	private String searchTimeStamp;
	private String regDate;
	private String nextPage;
	private String fobCnt;
	private List<EF01ResponseFob> fobList;
	
	@Data
	@ApiModel
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	static class EF01ResponseFob {
		private String fobId;
		private String fobName;
		private String isConsent;
		private String regDate;
		private String limitAmt;

		private String isCharge;

		private String currencyCode;

		private String accountCnt;

		private List<EF01ResponseAccount> accountList; 
		
	}
	
	@Data
	@ApiModel
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	static class EF01ResponseAccount {
		private String accountId;
		
	}
}
