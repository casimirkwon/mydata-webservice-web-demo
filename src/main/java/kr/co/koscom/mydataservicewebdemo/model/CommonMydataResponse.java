package kr.co.koscom.mydataservicewebdemo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonMydataResponse {
	String rspCode;
	String rspMsg;
}
