/**
 * 
 */
package org.tencompetence.widgetservice.util.opensocial;

import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
	 * VIEWER = who the current user is (ie. Instance.getUserId)
	 * OWNER = who the page belongs to (assumed to be Instance.getUserId until we change plugin API)
	 * APP_ID = what gadget this is (i.e. Widget.guid)
	 * MOD_ID = which instance of it  (i.e. Instance.id/idkey)
	 * 
	 * Alas, MOD_ID doesn't really map onto instance.IdKey (incompatible types), so we just have to use instance.id
	 */
	
	/**
	 * Utility method for producing a plain text token for OpenSocial applications
	 * @param instance the widget instance to generate a token for
	 * @return the plain text token for the widget instance
	 * @throws Exception
	 */
	public static String createPlainToken(WidgetInstance instance) throws Exception{
		
		if (instance == null) throw new Exception("Instance used to create token cannot be null");
		// check we have the required information:
		if (instance.getUserId().equals(null) || instance.getWidget() == null || instance.getIdKey() == null) {
			throw new Exception("Instance cannot be used to create token - invalid content");
		}

		
		// Order of fields is:
		// owner, viewer, app_id, domain, app_url, mod_id, container
		String[] fields = {instance.getUserId(), instance.getUserId(), instance.getWidget().getGuid(), DOMAIN_ID, instance.getWidget().getUrl(), "0", String.valueOf(instance.getIdKey())};
		for (int i = 0; i < fields.length; i++) {
			// escape each field individually, for metachars in URL
			fields[i] = URLEncoder.encode(fields[i], "UTF-8");
		}		
		String token = StringUtils.join(fields, ":");
		token = URLEncoder.encode(token, "UTF-8");
		
		return token;		
	}
	
	/**
	 * Utility method for producing an encrypted token for OpenSocial applications
	 * TODO Fix this method, its broken
	 * @param instance the widget instance to generate a token for
	 * @return the encrypted token for the widget instance
	 * @throws Exception
	 */
	public static String createEncryptedToken(WidgetInstance instance, String key) throws Exception{
		
		if (instance == null) throw new Exception("Instance used to create token cannot be null");
		// check we have the required information:
		if (instance.getUserId().equals(null) || instance.getWidget() == null || instance.getIdKey() == null) {
			throw new Exception("Instance cannot be used to create token - invalid content");
		}
		
		BasicBlobCrypter crypter = new BasicBlobCrypter(key.getBytes());
		BlobCrypterSecurityToken token = new BlobCrypterSecurityToken(crypter, CONTAINER_ID, DOMAIN_ID);
		token.setAppUrl(instance.getWidget().getUrl());
		// The ModuleId only takes a long
		token.setModuleId(instance.getIdKey().hashCode());
		token.setOwnerId(instance.getUserId());
		token.setViewerId(instance.getUserId());
		String encryptedToken = null;
		encryptedToken = token.encrypt();
		encryptedToken = URLEncoder.encode(encryptedToken, "UTF-8");
		return encryptedToken;
	}

}
