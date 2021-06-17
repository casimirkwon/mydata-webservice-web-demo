package kr.co.koscom.mydataservicewebdemo.io;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import kr.co.koscom.mydataservicewebdemo.config.MydataServiceConfig;

public class MtlsRestClient {
	
	private MydataServiceConfig context;

	public MtlsRestClient(MydataServiceConfig context) {
		this.context = context;
		
	}
	
	public Object request(String url, Object data) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		SSLContext sslContext = null;
		try {
			sslContext = SSLContextBuilder.create()
					.loadKeyMaterial((File)null, null, null)
					.loadTrustMaterial((File)null, null)
					.build();
		} catch (KeyManagementException | UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException
				| CertificateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		CloseableHttpClient httpClient = HttpClientBuilder.create()
				.setSSLContext(sslContext)
				.build();

		return null;
	}
}
