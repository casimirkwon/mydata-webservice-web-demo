package kr.co.koscom.mydataservicewebdemo.io;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.koscom.mydataservicewebdemo.config.MydataServiceContext;
import kr.co.koscom.mydataservicewebdemo.security.MtlsSerialNumberVerifier;

@Component
public class MtlsRestClient {

	@Autowired
	private MydataServiceContext context;
	
	private HttpClient httpClient;

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

	}

	public Object request(String url, Object data) {


		return null;
	}
	
}
