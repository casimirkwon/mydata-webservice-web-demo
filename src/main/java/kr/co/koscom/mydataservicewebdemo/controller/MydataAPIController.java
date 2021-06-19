package kr.co.koscom.mydataservicewebdemo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;
import kr.co.koscom.mydataservicewebdemo.io.MtlsRestClient;
import kr.co.koscom.mydataservicewebdemo.model.EF01Request;

@RestController
public class MydataAPIController {
	
	@Autowired
	MydataServiceContext context;
	
	@Autowired
	MtlsRestClient restClient;

    @RequestMapping(value = "/v1/efin/prepaid", method = RequestMethod.GET)
    public String v1_efin_prepaid(HttpServletRequest servletRequest,
    		HttpServletResponse servletResponse,
    		@RequestParam(value = "orgCode") String orgCode,
    		@RequestParam(value = "searchTimestamp") String searchTimestamp,
    		@RequestParam(value = "limit") String limit) {
    	String requestPath = servletRequest.getRequestURI();
    	
    	EF01Request request = new EF01Request();
    	request.setOrgCode(orgCode);
    	request.setSearchTimestamp(searchTimestamp);
    	request.setLimit(limit);
    	
    	String endpoint = context.getDataProviders().get(orgCode).getEndpoint();
    	
    	if(endpoint == null)
    		return "error";
    	
    	endpoint += requestPath;
    	
        ResponseEntity<JsonNode> response = restClient.requestAsGet(endpoint, request);
        servletResponse.setStatus(response.getStatusCodeValue());
        
        return response.getBody().toPrettyString();
    }
    
    
}
