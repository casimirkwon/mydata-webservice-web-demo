package kr.co.koscom.mydataservicewebdemo.io;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;
import kr.co.koscom.mydataservicewebdemo.security.MtlsSerialNumberVerifier;

@Component
public class MtlsRestClient {

	@Autowired
	private MydataServiceContext context;
	
	private HttpClient httpClient;
	
	private RestTemplate restTemplate;

	public MtlsRestClient() {
		SSLContext sslContext = null;
		SSLConnectionSocketFactory sslsf = null;
		try {
			sslContext = SSLContextBuilder.create()
					.loadKeyMaterial(new File(context.getService().getKeyStorePath()),
							context.getService().getKeyStorePassword().toCharArray(),
							context.getService().getKeyStorePassword().toCharArray())
					.loadTrustMaterial(new File(context.getService().getTrustStorePath()),
							context.getService().getTrustStorePassword().toCharArray())
					.build();
			sslsf = new SSLConnectionSocketFactory(sslContext, 
					new String[] { "TLSv1.3" }, 
					null,
					new MtlsSerialNumberVerifier(SSLConnectionSocketFactory.getDefaultHostnameVerifier(), context.getDataProviders()) );
		} catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException
				| CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.httpClient = HttpClientBuilder.create()
				.setSSLSocketFactory(sslsf)
				.build();

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(this.httpClient);
		
		this.restTemplate = new RestTemplate(clientHttpRequestFactory);
		this.restTemplate.getMessageConverters().add(new ObjectToUrlEncodedConverter(new ObjectMapper()));
		
	}

	public ResponseEntity<JsonNode> requestAsGet(String url, Map<String, String> queryParams) {
		HttpHeaders headers = new HttpHeaders();
		//headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<>(headers);
		
		return restTemplate.exchange(url, HttpMethod.GET, request, JsonNode.class, queryParams);
	}

	public ResponseEntity<JsonNode> requestAsPostJson(String url, Object data, Map<String, String> queryParams) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<>(data, headers);
		
		return restTemplate.exchange(url, HttpMethod.POST, request, JsonNode.class, queryParams);
	}	
	
	public ResponseEntity<JsonNode> requestAsPostFormUrlEncoded(String url, Object data, Map<String, String> queryParams) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<Object> request = new HttpEntity<>(data, headers);
		
		return restTemplate.exchange(url, HttpMethod.POST, request, JsonNode.class, queryParams);
	}	
}
