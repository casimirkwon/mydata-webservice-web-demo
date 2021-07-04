package kr.co.koscom.mydataservicewebdemo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IV01Request {
	//@ApiModelProperty(notes = "기관코드", example = "C1AAGU0000")
	@NotBlank
	//@Size(min = 10, max=10, message = "입력된 기관코드 '${validatedValue}'는 {max}자리 이어야 합니다.")
	private String orgCode;
	
	@NotBlank
	private String searchTimestamp;
	
	@NotBlank
	private String limit;
	
	private String nextPage;
}
