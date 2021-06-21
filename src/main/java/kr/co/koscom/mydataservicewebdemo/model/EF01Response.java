package kr.co.koscom.mydataservicewebdemo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class EF01Response {
	@ApiModelProperty(name = "기관코드", example = "ZWAACP0000")
	@NotBlank
	@Size(min = 10, max=10, message = "입력된 기관코드 '${validatedValue}'는 {max}자리 이어야 합니다.")
	private String orgCode;
	
}
