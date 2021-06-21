package kr.co.koscom.mydataservicewebdemo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import io.swagger.annotations.ApiOperation;
import kr.co.koscom.mydataservicewebdemo.config.DataProviderConfig;
import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;
import kr.co.koscom.mydataservicewebdemo.io.MtlsRestClient;
import kr.co.koscom.mydataservicewebdemo.model.EF01Request;
import kr.co.koscom.mydataservicewebdemo.model.EF01Response;
import kr.co.koscom.mydataservicewebdemo.model.MydataException;

@RestController
public class MydataAPIController {
	
	@Autowired
	MydataServiceContext context;
	
	@Autowired
	MtlsRestClient restClient;

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
    	
        ResponseEntity<JsonNode> response = restClient.requestAsGet(endpoint, request);
        servletResponse.setStatus(response.getStatusCodeValue());
        
        //FIXME test code
        return new EF01Response();
    }
    
    
}
