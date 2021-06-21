package kr.co.koscom.mydataservicewebdemo.model;

import lombok.Data;

@Data
public class IntgAuthSignRequest {

	private String orgCode;
	private UCPIDRequestInfo ucpidRequestInfo;
	private ConsentInfo consentInfo;
	
	
	@Data
	public static class UCPIDRequestInfo {
		private String userAgreement;
		
		private UserAgreeInfo userAgreeInfo;
		
		private String ispUrlInfo;
		
		private String ucpidNonce;
	}
	
	@Data
	public static class ConsentInfo {
		private String consent;
		
		private String consentNonce;
		
	}
	
	@Data
	public static class UserAgreeInfo {
		private boolean realName;
		private boolean gender;
		private boolean nationalInfo;
		private boolean birthDate;
		private boolean ci;
	}
}
