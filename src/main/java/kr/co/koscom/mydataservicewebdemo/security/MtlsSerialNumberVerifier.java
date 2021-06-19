package kr.co.koscom.mydataservicewebdemo.security;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;

import org.apache.http.conn.util.DnsUtils;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.koscom.mydataservicewebdemo.config.DataProviderConfig;

public class MtlsSerialNumberVerifier implements HostnameVerifier {
	
	private static Logger logger = LoggerFactory.getLogger(MtlsSerialNumberVerifier.class);
	
	private HostnameVerifier delegated;
	private Map<String, DataProviderConfig> providers;

	public MtlsSerialNumberVerifier(HostnameVerifier instance, Map<String, DataProviderConfig> providers) {
		this.delegated = instance;
		this.providers = providers;
	}

	@Override
	public boolean verify(String hostname, SSLSession session) {
		if (delegated.verify(hostname, session)) {
			// verify serial number
			try {
				final Certificate[] certs = session.getPeerCertificates();
				final X509Certificate x509 = (X509Certificate) certs[0];

				final String serialNumber = extractSerialNumberFromSubject(x509);
				if (serialNumber == null) {
					throw new SSLException("Certificate subject for <" + hostname + "> doesn't contain "
							+ "a serialnumber");
				}
				
				return matchSerialNumber(providers, hostname, serialNumber);
			} catch (SSLException e) {
				e.printStackTrace();
			}
		} 
		
		return false;
	}

	static boolean matchSerialNumber(Map<String, DataProviderConfig> providers, String hostname, String serialNumber) {
		for (DataProviderConfig provider : providers.values()) {
			if (hostname.equalsIgnoreCase(provider.getHost())
					&& DnsUtils.normalize(serialNumber).equals(DnsUtils.normalize(provider.getMtlsSerialNumber())))
				return true;
		}

		return false;
	}

	static String extractSerialNumberFromSubject(final X509Certificate cert) throws SSLException {
		
		try {
			RDN [] serialNumbers = new JcaX509CertificateHolder(cert).getSubject().getRDNs(BCStyle.SERIALNUMBER);
			if (serialNumbers == null || serialNumbers[0] == null) {
				return null;
			}
			logger.info("server cert's serialnumber : " + serialNumbers[0].getFirst().getValue().toString());
			return serialNumbers[0].getFirst().getValue().toString();
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
			throw new SSLException("invalid certifiate");
		}
		
	}

}