package kr.co.koscom.mydataservicewebdemo.io;

import java.io.File;
import java.io.IOException;
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
import org.apache.http.ssl.SSLContextBuilder;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;
import kr.co.koscom.mydataservicewebdemo.security.MtlsSerialNumberVerifier;

@Component
public class MtlsRestClient {

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
				sslContext = SSLContextBuilder.create()
						.loadKeyMaterial(new File(context.getService().getKeyStorePath()),
								context.getService().getKeyStorePassword().toCharArray(),
								context.getService().getKeyStorePassword().toCharArray())
						.loadTrustMaterial(new File(context.getService().getTrustStorePath()),
								context.getService().getTrustStorePassword().toCharArray())
						.build();
				sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1.2", "TLSv1.3" }, null,
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
			.additionalMessageConverters(new ObjectToUrlEncodedConverter(getObjectMapper()))
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

		return restTemplate.exchange(
				UriComponentsBuilder.fromHttpUrl(url).queryParams(queryParams).build().toUriString(), HttpMethod.GET,
				request, JsonNode.class);
	}

	public ResponseEntity<JsonNode> requestAsPostJson(String url, Object data, Map<String, String> queryParams) {
		HttpHeaders headers = makeCommonRequestHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Object> request = new HttpEntity<>(data, headers);

		return restTemplate.exchange(url, HttpMethod.POST, request, JsonNode.class, queryParams);
	}

	public ResponseEntity<JsonNode> requestAsPostFormUrlEncoded(String url, Object data,
			Map<String, String> queryParams) {
		HttpHeaders headers = makeCommonRequestHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		HttpEntity<Object> request = new HttpEntity<>(data, headers);

		return restTemplate.exchange(url, HttpMethod.POST, request, JsonNode.class, queryParams);
	}

	private HttpHeaders makeCommonRequestHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("ci", "TEF0101/AsCfLyJMF4bNFu4oWUHopstoUokAi2nsZtM78tch8SeegAHM9P8hJG1vpNe1RcvPRrl3b/MOC9999999");
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
