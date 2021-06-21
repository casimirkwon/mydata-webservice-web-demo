package kr.co.koscom.mydataservicewebdemo.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationCodeGrant;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.TokenEndpoint;
import springfox.documentation.service.TokenRequestEndpoint;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

@Configuration
@Import({springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class})
public class SpringFoxConfig {
	
	@Value("${mydata.service.clientId}")
	String clientId;
	@Value("${mydata.service.clientSecret}")
	String clientSecret;
	
	String oAuthServerUri = "";
	
    @Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
          .apiInfo(getApiInfo())
          .select()
          .apis(RequestHandlerSelectors.basePackage("kr.co.koscom.mydataservicewebdemo.controller"))              
          .paths(PathSelectors.any())                          
          .build()
          .securitySchemes(Collections.singletonList(oauth()));
    }

	private ApiInfo getApiInfo() {
		return new ApiInfoBuilder()
                .title("Mydata efin API Tester App")
                .build();
	}
	
	
	@Bean
	List<GrantType> grantTypes() {
		List<GrantType> grantTypes = new ArrayList<>();
		TokenRequestEndpoint tokenRequestEndpoint = new TokenRequestEndpoint(oAuthServerUri+"/oauth/authorize", clientId, clientSecret );
        TokenEndpoint tokenEndpoint = new TokenEndpoint(oAuthServerUri+"/oauth/token", "token");
        grantTypes.add(new AuthorizationCodeGrant(tokenRequestEndpoint, tokenEndpoint));
        return grantTypes;
	}
	
	@Bean
    SecurityScheme oauth() {
        return new OAuthBuilder()
                .name("OAuth2")
                .scopes(scopes())
                .grantTypes(grantTypes())
                .build();
    }
	
	private List<AuthorizationScope> scopes() {
		List<AuthorizationScope> list = new ArrayList();
		list.add(new AuthorizationScope("invest.list","(금융투자업권) 자산목록"));
		list.add(new AuthorizationScope("invest.account","(금융투자업권) 계좌정보"));
		list.add(new AuthorizationScope("invest.irp","(금융투자업권) 개인형IRP정보"));
		list.add(new AuthorizationScope("efin.list","(전자금융업권) 자산목록"));
		list.add(new AuthorizationScope("efin.prepaid","(전자금융업권) 선불전자지급수단정보"));
		list.add(new AuthorizationScope("efin.paid","(전자금융업권) 결제정보"));
		return list;
    }	

	@Bean
	public SecurityConfiguration security() {
		return SecurityConfigurationBuilder.builder()
		        .clientId(clientId)
		        .clientSecret(clientSecret)
		        .scopeSeparator(" ")
		        .build();
	}
	
}
