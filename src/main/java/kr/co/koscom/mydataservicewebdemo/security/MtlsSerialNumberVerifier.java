package kr.co.koscom.mydataservicewebdemo.security;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.naming.InvalidNameException;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.security.auth.x500.X500Principal;

import org.apache.http.conn.util.DnsUtils;

import kr.co.koscom.mydataservicewebdemo.config.DataProviderConfig;

public class MtlsSerialNumberVerifier implements HostnameVerifier {
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

				final X500Principal subjectPrincipal = x509.getSubjectX500Principal();
				final String serialNumber = extransSerialNumberFromSubject(subjectPrincipal.getName(X500Principal.RFC2253));
				if (serialNumber == null) {
					throw new SSLException("Certificate subject for <" + hostname + "> doesn't contain "
							+ "a serialnumber");
				}
				
				return matchSerialNumber(providers, hostname, serialNumber);

			} catch (SSLException e) {
				// TODO Auto-generated catch block
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

	static String extransSerialNumberFromSubject(final String subjectPrincipal) throws SSLException {
		if (subjectPrincipal == null) {
			return null;
		}
		try {
			final LdapName subjectDN = new LdapName(subjectPrincipal);
			final List<Rdn> rdns = subjectDN.getRdns();
			for (int i = rdns.size() - 1; i >= 0; i--) {
				final Rdn rds = rdns.get(i);
				final Attributes attributes = rds.toAttributes();
				final Attribute serialnumber = attributes.get("serailnumber");
				if (serialnumber != null) {
					try {
						final Object value = serialnumber.get();
						if (value != null) {
							return value.toString();
						}
					} catch (final NoSuchElementException ignore) {
						// ignore exception
					} catch (final NamingException ignore) {
						// ignore exception
					}
				}
			}
			return null;
		} catch (final InvalidNameException e) {
			throw new SSLException(subjectPrincipal + " is not a valid X500 distinguished name");
		}
	}

}