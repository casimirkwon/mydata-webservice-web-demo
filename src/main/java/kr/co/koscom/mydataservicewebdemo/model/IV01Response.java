package kr.co.koscom.mydataservicewebdemo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
public class IV01Response extends CommonMydataResponse {
	private String searchTimeStamp;
	private String regDate;
	private String nextPage;
	private String accountCnt;
	private List<IV01ResponseAccount> accountList;
	
	@Data
	@ApiModel
	@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
	static class IV01ResponseAccount {
		private String accountNum;
		private String isConsent;
		private String accountName;
		private String accountType;
		private String accountStatus;
		
	}
}
