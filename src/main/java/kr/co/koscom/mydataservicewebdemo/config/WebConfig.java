package kr.co.koscom.mydataservicewebdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.RedirectView;

@Configuration
@RestController
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins("*");
	}
	
	/**
	 * * Home redirection to swagger api documentation * 
	 * @return redirect to swagger documentation
	 */
	@GetMapping(value = "/")
	public RedirectView index() {
		return new RedirectView("swagger-ui/");
	}
}