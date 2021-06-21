package kr.co.koscom.mydataservicewebdemo.controller;


import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import SK.Utils.VarUtils;
import kr.co.koscom.mio.CertificateAndKey;
import kr.co.koscom.mio.MydataSignClientWrapper;
import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;
import kr.co.koscom.mydataservicewebdemo.io.MtlsRestClient;
import kr.co.koscom.mydataservicewebdemo.model.IntgAuthSignRequest;
import kr.co.koscom.mydataservicewebdemo.model.IntgAuthSignResponse;
import kr.co.koscom.mydataservicewebdemo.model.MydataException;

@RestController
public class MydataClientSignController {

	@Autowired
	MydataServiceContext context;
	
	@Autowired
	MtlsRestClient restClient;


    @RequestMapping(value = "/certlist", method = RequestMethod.GET)
    public String certlist(HttpServletRequest servletRequest,
    		HttpServletResponse servletResponse) {
    	
    	MydataSignClientWrapper wrapper = MydataSignClientWrapper.getInstance();
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    	
    	try {
			List<CertificateAndKey> certList = wrapper.getCertList();
			
			List<Map<String,Object>> ret = new ArrayList<>();
			
			for(int index = 0 ; index < certList.size(); index++) {
				CertificateAndKey item = certList.get(index);
				Map<String, Object> map = new HashMap<String, Object>();
				
				X509Certificate cert = (X509Certificate) item.getCert();
				map.put("cert_id", index);
				map.put("cert_cn", cert.getSubjectDN().toString());
				map.put("cert_encoded", VarUtils.base64_url_encoding(cert.getEncoded()));
				
				ret.add(map);
			}
			
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ret);

		} catch (Exception e) {
			e.printStackTrace();
			
			throw new MydataException("error");
		}
    }
    
    @RequestMapping(value = "/signrequest", method = RequestMethod.POST)
    public String signrequest(HttpServletRequest servletRequest,
    		HttpServletResponse servletResponse,
    		@RequestBody List<IntgAuthSignRequest> request,
    		@RequestParam(value = "certId") int certId) {
    	
    	MydataSignClientWrapper wrapper = MydataSignClientWrapper.getInstance();
    	
    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE);
    	
       /*
        
[
    {
        "orgCode": "1",
        "ucpidRequestInfo": {
            "userAgreement": "사용자 본인확인 동의 내역 블라블라...",
            "userAgreeInfo": {
            "realName": true,
            "gender": true,
            "nationalInfo": true,
            "birthDate": true,
            "ci": true
            },
            "ispUrlInfo": "www.mydata.co.kr",
            "ucpidNonce": "WGOcYBte6JHLi-B_KfJmMg"
        },
        "consentInfo": {
            "consent": "계좌 정보...",
            "consentNonce": "djVJqSSmujAS"
        }
    },
    {
        "orgCode": "2",
        "ucpidRequestInfo": {
            "userAgreement": "1",
            "userAgreeInfo": {
            "realName": true,
            "gender": true,
            "nationalInfo": true,
            "birthDate": true,
            "ci": true
            },
            "ispUrlInfo": "isp.url.info",
            "ucpidNonce": "WGOcYBte6JHLi-B_KfJmMg"
        },
        "consentInfo": {
            "consent": "카드 정보...",
            "consentNonce": "djVJqSSmujAS"
        }
    }
]

        */
        
    	try {

	    	return wrapper.makeSign(objectMapper.writeValueAsString(request), certId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MydataException("error");
		}
    }
}
