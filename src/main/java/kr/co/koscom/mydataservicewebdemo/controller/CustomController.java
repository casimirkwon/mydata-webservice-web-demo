package kr.co.koscom.mydataservicewebdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;

@RestController
public class CustomController {
	
	@Autowired
	MydataServiceContext context;

    @RequestMapping(value = "/custom", method = RequestMethod.GET)
    public String custom() {
    	System.out.println(context);
        return "custom";
    }
}
