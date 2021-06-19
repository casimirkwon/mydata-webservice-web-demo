package kr.co.koscom.mydataservicewebdemo.model;

import lombok.Data;

@Data
public class EF01Request {
	private String orgCode;
	
	private String searchTimestamp;
	
	private String limit;
}