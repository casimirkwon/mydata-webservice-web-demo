package kr.co.koscom.mydataservicewebdemo.model;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IV02Request {
	@NotBlank
	private String orgCode;
	
	@NotBlank
	private String accountNum;
	
	@NotBlank
	private String searchTimestamp;
	
}
