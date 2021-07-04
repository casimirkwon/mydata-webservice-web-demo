package kr.co.koscom.mydataservicewebdemo.model;

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
public class IV02Response extends CommonMydataResponse {
	private String searchTimestamp;
	
	private String issueDate;
	
	private String isTaxBenefits;
	
	private String withholdingAmt;
	
	private String creditLoanAmt;
	
	private String mortgageAmt;
	
	private String currentCode;
	
	
}
