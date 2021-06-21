package kr.co.koscom.mydataservicewebdemo.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import io.swagger.annotations.ApiOperation;
import kr.co.koscom.mio.MydataSignVerifyWrapper;
import kr.co.koscom.mydataservicewebdemo.config.DataProviderConfig;
import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;
import kr.co.koscom.mydataservicewebdemo.io.MtlsRestClient;
import kr.co.koscom.mydataservicewebdemo.model.AU11Request;
import kr.co.koscom.mydataservicewebdemo.model.AU11Response;
import kr.co.koscom.mydataservicewebdemo.model.MydataException;

@RestController
public class IntgAuthController {
	
	@Autowired
	private MydataServiceContext context;
	
	@Autowired
	MtlsRestClient restClient;
	
	@ApiOperation(value = "AU11 - oauth 2.0 token API")
	@PostMapping(value = "/oauth/2.0/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
	public AU11Response token(HttpServletRequest servletRequest,
			HttpServletResponse servletResponse,
			AU11Request request) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		
		Map<String, String> map = 
				objectMapper.convertValue(request, new TypeReference<Map<String, String>>() {});
		
		MydataSignVerifyWrapper wrapper = MydataSignVerifyWrapper.getInstance(context.getDataProvidersCpCodeMap());
		
		try {
			wrapper.verifySign(map);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MydataException("error while verifying signature or processing ucpid request");
		}
		
    	String requestPath = servletRequest.getRequestURI();
    	DataProviderConfig dataProvider = context.getDataProviders().get(request.getOrgCode());
    	
    	if(dataProvider == null)
    		throw new MydataException(String.format("Cannot find a proper endpoint with a given org_code '%s'", request.getOrgCode()));
    	
    	String endpoint = dataProvider.getEndpoint();
    	endpoint += requestPath;
    	
        ResponseEntity<JsonNode> response = restClient.requestAsGet(endpoint, request);
        servletResponse.setStatus(response.getStatusCodeValue());
		
        AU11Response ret;
		try {
			ret = objectMapper.readValue(response.getBody().asText(), AU11Response.class);
			
			return ret;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new MydataException("error while reading response");
		}
	}
}
