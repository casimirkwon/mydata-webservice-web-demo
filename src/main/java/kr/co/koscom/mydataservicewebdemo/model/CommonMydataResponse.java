package kr.co.koscom.mydataservicewebdemo.model;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@ApiModel
@AllArgsConstructor
public class CommonMydataResponse {
	String rspCode;
	String rspMsg;
	
	public CommonMydataResponse() {
	}
}
