package kr.co.koscom.mydataservicewebdemo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import kr.co.koscom.mydataservicewebdemo.config.DataProviderConfig;
import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;
import kr.co.koscom.mydataservicewebdemo.io.MtlsRestClient;
import kr.co.koscom.mydataservicewebdemo.model.AU11Request;
import kr.co.koscom.mydataservicewebdemo.model.AU11Response;
import kr.co.koscom.mydataservicewebdemo.model.MydataException;

@RestController
public class IntgAuthController {
	private static Logger logger = LoggerFactory.getLogger(IntgAuthController.class);

	@Autowired
	private MydataServiceContext context;
	
	@Autowired
	private MtlsRestClient restClient;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@ApiOperation(value = "AU11 - oauth 2.0 token API")
	@PostMapping(value = "/oauth/2.0/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public AU11Response token(HttpServletRequest servletRequest,
			HttpServletResponse servletResponse,
			AU11Request request) {
		
//		Map<String, String> map = 
//				objectMapper.convertValue(request, new TypeReference<Map<String, String>>() {});
//		
//		MydataSignVerifyWrapper wrapper = MydataSignVerifyWrapper.getInstance(context.getDataProvidersCpCodeMap());
//		
//		try {
//			wrapper.verifySign(map);
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new MydataException("error while verifying signature or processing ucpid request");
//		}
		
		logger.info(request.toString());
    	String requestPath = servletRequest.getRequestURI();
    	DataProviderConfig dataProvider = context.getDataProviders().get(request.getOrgCode());
    	
    	if(dataProvider == null)
    		throw new MydataException(String.format("Cannot find a proper endpoint with a given org_code '%s'", request.getOrgCode()));
    	
    	String endpoint = dataProvider.getEndpoint();
    	endpoint += requestPath;
    	
    	
		try {
	        ResponseEntity<JsonNode> response = restClient.requestAsPostFormUrlEncoded(endpoint, request);
	        servletResponse.setStatus(response.getStatusCodeValue());
	        
	        if(response.getStatusCode() == HttpStatus.OK) {
	        	AU11Response ret = objectMapper.readValue(response.getBody().toString(), AU11Response.class);
				
				return ret;
	        } else
	        	throw new MydataException(String.format("http status is not 200 : received response { %s }", response.getBody().toPrettyString()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new MydataException("error while reading response");
		}
	}
}
