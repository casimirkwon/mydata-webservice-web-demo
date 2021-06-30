package kr.co.koscom.mydataservicewebdemo.model;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class IntgAuthSignRequest {
	
	@ApiModelProperty(value = "정보제공자 기관코드(org_code)", example = "MOC9999999")
	private String orgCode;
	private UCPIDRequestInfo ucpidRequestInfo;
	private ConsentInfo consentInfo;
	
	
	@Data
	public static class UCPIDRequestInfo {
		@ApiModelProperty(value = "본인확인 이용약관", example = "본인확인 이용약관:본인확인 요청에 동의합니다.")
		private String userAgreement;
		
		private UserAgreeInfo userAgreeInfo;
		
		@ApiModelProperty(value = "마이데이터 서비스 도메인 정보", example = "www.mydata.co.kr")
		private String ispUrlInfo;
		
		@ApiModelProperty(value = "마이데이터 서버가 생성한 Nonce", example = "MTIzNDU2Nzg5MDEyMzQ1Ng==")
		private String ucpidNonce;
	}
	
	@Data
	public static class ConsentInfo {
		private Consent consent;
		
		@ApiModelProperty(value = "마이데이터 서버가 생성한 Nonce", example = "QUJDREVGR0hJSktMTU5PUA==")
		private String consentNonce;
		
	}
	
	@Data
	public static class Consent {
		
		@ApiModelProperty(value = "정보제공자 기관코드(org_code)", example = "MOC9999999")
		private String sndOrgCode;
		
		@ApiModelProperty(value = "마이데이터사업자(정보수신자) 기관코드(org_code)", example = "MOC1234567")
		private String rcvOrgCode;
		
		@ApiModelProperty(value = "정기적 전송 여부(true 또는 false)", example = "true")
		private boolean isScheduled;
		
		private Cycle cycle;
		
		@ApiModelProperty(value = "전송요구의 종료시점", example = "20221231235959")
		private String endDate;
		
		@ApiModelProperty(value = "전송을 요구하는 목적", example = "통합계좌 마이데이터 서비스 제공")
		private String purpose;
		
		@ApiModelProperty(value = "전송을 요구하는 개인신용정보의 보유기간", example = "20221231235959")
		private String holdingPeriod;
		
		private List<TargetInfo> targetInfo;
	}
	
	@Data
	public static class Cycle {
		@ApiModelProperty(value = "정기적 전송 주기 단위", example = "1/w")
		private String fndCycle;
		
		@ApiModelProperty(value = "추가 정보의 정기적 전송 주기 단위", example = "1/d")
		private String addCycle;
	}
	
	
	@Data
	public static class TargetInfo {
		@ApiModelProperty(value = "전송요구 정보에 해당하는 Scope", example = "invest.list invest.account")
		private String scope;
		
		private List<Asset> assetList;
	}

	@Data
	public static class Asset {
		@ApiModelProperty(value = "전송요구 대상 계좌(상품) 식별자", example = "all_asset")
		private String asset;
	}
	
	@Data
	public static class UserAgreeInfo {
		@ApiModelProperty(value = "요청하고자 하는 본인확인정보 여부 - 실명", example = "true")
		private boolean realName;
		@ApiModelProperty(value = "요청하고자 하는 본인확인정보 여부 - 성별", example = "true")
		private boolean gender;
		@ApiModelProperty(value = "요청하고자 하는 본인확인정보 여부 - 국적", example = "true")
		private boolean nationalInfo;
		@ApiModelProperty(value = "요청하고자 하는 본인확인정보 여부 - 생년월일", example = "true")
		private boolean birthDate;
		@ApiModelProperty(value = "요청하고자 하는 본인확인정보 여부 - CI 정보", example = "true")
		private boolean ci;
	}
}
