package kr.co.koscom.mydataservicewebdemo.model;

import java.util.List;

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
		private Consent consent;
		
		private String consentNonce;
		
	}
	
	@Data
	public static class Consent {
		private String sndOrgCode;
		
		private String rcvOrgCode;
		
		private boolean isScheduled;
		
		private Cycle cycle;
		
		private String endDate;
		
		private String purpose;
		
		private String holdingPeriod;
		
		private List<TargetInfo> targetInfo;
	}
	
	@Data
	public static class Cycle {
		private String fndCycle;
		
		private String addCycle;
	}
	
	
	@Data
	public static class TargetInfo {
		private String scope;
		
		private List<Asset> assetList;
	}

	@Data
	public static class Asset {
		private String asset;
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
