package kr.co.koscom.mydataservicewebdemo.io;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.PrivateKeyDetails;
import org.apache.http.ssl.PrivateKeyStrategy;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;
import kr.co.koscom.mydataservicewebdemo.model.MydataException;
import kr.co.koscom.mydataservicewebdemo.security.MtlsSerialNumberVerifier;

@Component
public class MtlsRestClient {
	
	private static Logger logger = LoggerFactory.getLogger(MtlsRestClient.class);

	@Autowired
	private MydataServiceContext context;

	private HttpClient httpClient;

	private RestTemplate restTemplate;

	@Value("${io.useMtls:false}")
	private boolean useMtls;
	
	private ObjectMapper objectMapper;

	@PostConstruct
	private void init() {
		if (useMtls) {
			SSLContext sslContext = null;
			SSLConnectionSocketFactory sslsf = null;
			try {
				sslContext = SSLContexts.custom()
						.loadKeyMaterial(new File(context.getService().getKeyStorePath()),
								context.getService().getKeyStorePassword().toCharArray(),
								context.getService().getKeyStorePassword().toCharArray(),
								new PrivateKeyStrategy() {
									@Override
									public String chooseAlias(Map<String, PrivateKeyDetails> aliases, Socket socket) {
										return context.getService().getKeyStoreAlias();
									}
								})
						.loadTrustMaterial(new File(context.getService().getTrustStorePath()),
								context.getService().getTrustStorePassword().toCharArray())
						.build();
				sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1.3" }, null,
						new MtlsSerialNumberVerifier(SSLConnectionSocketFactory.getDefaultHostnameVerifier(),
								context.getDataProviders()));
			} catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException
					| CertificateException | IOException e) {
				e.printStackTrace();
			}

			httpClient = HttpClientBuilder.create().setSSLSocketFactory(sslsf).build();
		} else {
			httpClient = HttpClientBuilder.create().build();
		}

		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		restTemplate = new RestTemplateBuilder(
			rt -> rt.getInterceptors().add(
					(request, body, execution) -> {
							request.getHeaders().addAll(makeCommonRequestHeaders());
							return execution.execute(request, body);				
						}
					))
			.requestFactory(() -> clientHttpRequestFactory)
			//.additionalMessageConverters(new ObjectToUrlEncodedConverter(getObjectMapper()))
			.errorHandler(new ResponseErrorHandler() {
				@Override
				public boolean hasError(ClientHttpResponse response) throws IOException {
					return false;
				}

				@Override
				public void handleError(ClientHttpResponse response) throws IOException {
				}
			})
			.build();
		
		restTemplate.getMessageConverters().add(new ObjectToUrlEncodedConverter(getObjectMapper()));
	}

	public ResponseEntity<JsonNode> requestAsGet(String url, Object data) {
		
		Map<String, String> map = getObjectMapper().convertValue(data, new TypeReference<Map<String, String>>() {
		});

		LinkedMultiValueMap<String, String> linkedMultiValueMap = new LinkedMultiValueMap<>();
		map.entrySet().forEach(e -> linkedMultiValueMap.add(e.getKey(), e.getValue()));

		return requestAsGet(url, linkedMultiValueMap);
	}

	public ResponseEntity<JsonNode> requestAsGet(String url, MultiValueMap<String, String> queryParams) {
		HttpHeaders headers = makeCommonRequestHeaders();
		HttpEntity<Object> request = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(
				UriComponentsBuilder.fromHttpUrl(url).queryParams(queryParams).build().toUriString(), HttpMethod.GET,
				request, String.class);
		
		logger.info("response : " + response.toString());
		try {
			return new ResponseEntity<JsonNode>( objectMapper.readValue(response.getBody(), JsonNode.class), response.getStatusCode());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new MydataException("error in reading response");
		}
	
				
//		return restTemplate.exchange(
//				UriComponentsBuilder.fromHttpUrl(url).queryParams(queryParams).build().toUriString(), HttpMethod.GET,
//				request, JsonNode.class);
	}

	public ResponseEntity<JsonNode> requestAsPostJson(String url, Object data) {
		return requestAsPostJson(url, data, null);
	}
	
	public ResponseEntity<JsonNode> requestAsPostJson(String url, Object data, MultiValueMap<String, String> queryParams) {
		HttpHeaders headers = makeCommonRequestHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<>(data, headers);

		ResponseEntity<Object> response = restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(url).queryParams(queryParams).build().toUriString(), HttpMethod.POST, request, Object.class);
		
		logger.info("response : " + response.toString());
		return new ResponseEntity<JsonNode>((JsonNode) response.getBody(), response.getStatusCode());
		//return restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(url).queryParams(queryParams).build().toUriString(), HttpMethod.POST, request, JsonNode.class);
	}

	public ResponseEntity<JsonNode> requestAsPostFormUrlEncoded(String url, Object data) {
		return requestAsPostFormUrlEncoded(url, data, null);
	}
	
	public ResponseEntity<JsonNode> requestAsPostFormUrlEncoded(String url, Object data,
			MultiValueMap<String, String> queryParams) {
		HttpHeaders headers = makeCommonRequestHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<Object> request = new HttpEntity<>(data, headers);

		return restTemplate.exchange(UriComponentsBuilder.fromHttpUrl(url).queryParams(queryParams).build().toUriString(), HttpMethod.POST, request, JsonNode.class);
	}

	private HttpHeaders makeCommonRequestHeaders() {
		HttpHeaders headers = new HttpHeaders();
		//headers.add("ci", "TEF0101/AsCfLyJMF4bNFu4oWUHopstoUokAi2nsZtM78tch8SeegAHM9P8hJG1vpNe1RcvPRrl3b/MOC9999999");
		headers.add("x-tranId", "1234567890");
		return headers;
	}

	private ObjectMapper getObjectMapper() {
		if(objectMapper != null) {
			return objectMapper;
		}
		objectMapper = new ObjectMapper();
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		return objectMapper;
	}
}
