/**
 * 
 */
package org.tencompetence.widgetservice.util.opensocial;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shindig.auth.BasicSecurityToken;
import org.apache.shindig.auth.BlobCrypterSecurityToken;
import org.apache.shindig.common.crypto.BasicBlobCrypter;
import org.apache.shindig.common.crypto.BlobCrypterException;
import org.tencompetence.widgetservice.beans.WidgetInstance;

/**
 * @author scott
 *
 */
public class OpenSocialUtils {
	
	static Logger _logger = Logger.getLogger(OpenSocialUtils.class.getName());
	
	private static final String DOMAIN_ID = "wookie";
	private static final String CONTAINER_ID = "wookie";
	
	
	/**
	 * A quick explanation of the fields in the SecurityToken
	 * 
	 * VIEWER = who the current user is
	 * OWNER = who the page belongs to
	 * APP_ID = what gadget this is 
	 * MOD_ID = which instance of it 
	 * 
	 * Alas, MOD_ID doesn't really map onto instance.IdKey (incompatible types), so we just have to use instance.id
	 */
	
	/**
	 * Utility method for producing a plain text token for OpenSocial applications
	 * @param instance the widget instance to generate a token for
	 * @return the plain text token for the widget instance
	 * @throws Exception
	 */
	public static String createPlainToken(WidgetInstance instance){
		// Order of fields is:
		// owner, viewer, app_id, domain, app_url, mod_id, container
		String[] fields = {instance.getUserId(), instance.getUserId(), instance.getWidget().getGuid(), DOMAIN_ID, instance.getWidget().getUrl(), "0", String.valueOf(instance.getIdKey())};
		for (int i = 0; i < fields.length; i++) {
			// escape each field individually, for metachars in URL
				try {
					fields[i] = URLEncoder.encode(fields[i], "UTF-8");
				} catch (UnsupportedEncodingException e) {
					_logger.error(e.getMessage());
				}
		}		
		String token = StringUtils.join(fields, ":");
		try {
			token = URLEncoder.encode(token, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			_logger.error(e.getMessage());
		}
		
		return token;		
	}
	
	/**
	 * Utility method for producing an encrypted token for OpenSocial applications
	 * TODO Fix this method, its broken
	 * @param instance the widget instance to generate a token for
	 * @return the encrypted token for the widget instance
	 * @throws Exception
	 */
	public static String createEncryptedToken(WidgetInstance instance, String key){
		BasicBlobCrypter crypter = new BasicBlobCrypter(key.getBytes());
		BlobCrypterSecurityToken token = new BlobCrypterSecurityToken(crypter, CONTAINER_ID, DOMAIN_ID);
		token.setAppUrl(instance.getWidget().getUrl());
		token.setModuleId(instance.getIdKey().hashCode());
		token.setOwnerId(instance.getUserId());
		token.setViewerId(instance.getUserId());
		String encryptedToken = null;
		try {
			encryptedToken = token.encrypt();
		} catch (BlobCrypterException e) {
			_logger.error(e.getMessage());
		}
		return encryptedToken;
	}

}
