package kr.co.koscom.mydataservicewebdemo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import kr.co.koscom.mydataservicewebdemo.config.DataProviderConfig;
import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;
import kr.co.koscom.mydataservicewebdemo.io.MtlsRestClient;
import kr.co.koscom.mydataservicewebdemo.model.EF01Request;
import kr.co.koscom.mydataservicewebdemo.model.EF01Response;
import kr.co.koscom.mydataservicewebdemo.model.IV01Request;
import kr.co.koscom.mydataservicewebdemo.model.IV01Response;
import kr.co.koscom.mydataservicewebdemo.model.IV02Request;
import kr.co.koscom.mydataservicewebdemo.model.IV02Response;
import kr.co.koscom.mydataservicewebdemo.model.MydataException;

@RestController
public class MydataAPIController {
	
	@Autowired
	MydataServiceContext context;
	
	@Autowired
	MtlsRestClient restClient;
	
	@Autowired
	ObjectMapper objectMapper;

	@ApiOperation(value = "Prepaid Accounts List")
    @GetMapping(value = "/v1/efin/prepaid", produces = MediaType.APPLICATION_JSON_VALUE)
    public EF01Response v1_efin_prepaid(@Valid @ModelAttribute EF01Request request, HttpServletRequest servletRequest,
    		HttpServletResponse servletResponse) {
    	String requestPath = servletRequest.getRequestURI();
    	
    	DataProviderConfig dataProvider = context.getDataProviders().get(request.getOrgCode());
    	
    	if(dataProvider == null)
    		throw new MydataException(String.format("Cannot find a proper endpoint with a given org_code '%s'", request.getOrgCode()));
    	
    	String endpoint = dataProvider.getEndpoint();
    	endpoint += requestPath;
    	
		try {
	        ResponseEntity<JsonNode> response = restClient.requestAsGet(endpoint, request);
	        servletResponse.setStatus(response.getStatusCodeValue());
	        
	        if(response.getStatusCode() == HttpStatus.OK) {
		        EF01Response ret = objectMapper.readValue(response.getBody().toString(), EF01Response.class);
				
				return ret;
	        } else
	        	throw new MydataException(String.format("http status is not 200 : received response { %s }", response.getBody().toPrettyString()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new MydataException("error while reading response");
		}
    }
    
	@ApiOperation(value = "Invest Accounts List")
    @GetMapping(value = "/v1/invest/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
    public IV01Response v1_invest_accounts(
    		@RequestHeader(value = "Authorization") @ApiParam(hidden = true) String token,
    		@Valid @ModelAttribute IV01Request request, 
    		HttpServletRequest servletRequest,
    		HttpServletResponse servletResponse) {
    	String requestPath = servletRequest.getRequestURI();
    	//String token = servletRequest.getHeader("Authorization");
    	
    	DataProviderConfig dataProvider = context.getDataProviders().get(request.getOrgCode());
    	
    	if(dataProvider == null)
    		throw new MydataException(String.format("Cannot find a proper endpoint with a given org_code '%s'", request.getOrgCode()));
    	
    	String endpoint = dataProvider.getEndpoint();
    	endpoint += requestPath;
    	
		try {
	        ResponseEntity<JsonNode> response = restClient.requestAsGet(endpoint, request, token);
	        servletResponse.setStatus(response.getStatusCodeValue());
	        
	        if(response.getStatusCode() == HttpStatus.OK) {
		        IV01Response ret = objectMapper.readValue(response.getBody().toString(), IV01Response.class);
				
				return ret;
	        } else
	        	throw new MydataException(String.format("http status is not 200 : received response { %s }", response.getBody().toPrettyString()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new MydataException("error while reading response");
		}
    }
    
    
	@ApiOperation(value = "Invest Accounts Basic")
    @GetMapping(value = "/v1/invest/accounts/basic", produces = MediaType.APPLICATION_JSON_VALUE)
    public IV02Response v1_invest_accounts_basic(
    		@RequestHeader(value = "Authorization") @ApiParam(hidden = true) String token,
    		@Valid @ModelAttribute IV02Request request, 
    		HttpServletRequest servletRequest,
    		HttpServletResponse servletResponse) {
    	String requestPath = servletRequest.getRequestURI();
    	//String token = servletRequest.getHeader("Authorization");
    	
    	DataProviderConfig dataProvider = context.getDataProviders().get(request.getOrgCode());
    	
    	if(dataProvider == null)
    		throw new MydataException(String.format("Cannot find a proper endpoint with a given org_code '%s'", request.getOrgCode()));
    	
    	String endpoint = dataProvider.getEndpoint();
    	endpoint += requestPath;
    	
		try {
	        ResponseEntity<JsonNode> response = restClient.requestAsGet(endpoint, request, token);
	        servletResponse.setStatus(response.getStatusCodeValue());
	        
	        if(response.getStatusCode() == HttpStatus.OK) {
		        IV02Response ret = objectMapper.readValue(response.getBody().toString(), IV02Response.class);
				
				return ret;
	        } else
	        	throw new MydataException(String.format("http status is not 200 : received response { %s }", response.getBody().toPrettyString()));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new MydataException("error while reading response");
		}
    }
    
    
}
