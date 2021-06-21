package kr.co.koscom.mydataservicewebdemo.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import kr.co.koscom.mydataservicewebdemo.model.CommonMydataResponse;
import kr.co.koscom.mydataservicewebdemo.model.MydataException;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler{
	@ExceptionHandler(MydataException.class)
	protected ResponseEntity<Object> handleConflict(
			MydataException ex, WebRequest request) {
				HttpHeaders headers = new HttpHeaders();
				headers.add("x-api-tran-id", "value_from_request");
		        return handleExceptionInternal(ex, new CommonMydataResponse("50004",ex.getMessage()), 
		        		headers, HttpStatus.INTERNAL_SERVER_ERROR, request);
		    }
}
