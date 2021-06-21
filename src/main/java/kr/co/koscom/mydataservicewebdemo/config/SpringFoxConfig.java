package kr.co.koscom.mydataservicewebdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class SpringFoxConfig {                                    
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.OAS_30)  
          .apiInfo(getApiInfo())
          .select()                                  
          .apis(RequestHandlerSelectors.basePackage("kr.co.koscom.mydataservicewebdemo.controller"))              
          .paths(PathSelectors.any())                          
          .build();                                           
    }

	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder()
                .title("Mydata efin API Tester App")
                .build();
	}
}
