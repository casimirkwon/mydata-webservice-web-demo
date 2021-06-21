package kr.co.koscom.mydataservicewebdemo.model;

import java.util.List;

import lombok.Data;

@Data
public class IntgAuthSignResponse {

	private String caOrg;
	
	private List<SignedData> signedDataList;
	
	@Data
	public static class SignedData {
		private String orgCode;
		
		private String signedPersonInfoReq;
		
		private String signedConsent;
	}
}
