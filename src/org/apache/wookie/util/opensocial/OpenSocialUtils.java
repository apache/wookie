/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.wookie.util.opensocial;

import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shindig.auth.BlobCrypterSecurityToken;
import org.apache.shindig.common.crypto.BasicBlobCrypter;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.IStartFile;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.w3c.util.LocalizationUtils;

/**
 * Utilities for supporting OpenSocial integration, such as creation of security tokens that
 * convey OpenSocial container context.
 * @author scott
 *
 */
public class OpenSocialUtils {
	
	static Logger _logger = Logger.getLogger(OpenSocialUtils.class.getName());
	
	private static final String DOMAIN_ID = "default"; //$NON-NLS-1$
	private static final String CONTAINER_ID = "default"; //$NON-NLS-1$
	
	
	/**
	 * A quick explanation of the fields in the SecurityToken
	 * 
	 * VIEWER = who the current user is (ie. Instance.getUserId)
	 * OWNER = who the page belongs to (assumed to be Instance.getUserId until we change plugin API)
	 * APP_ID/APP URL = what gadget this is (i.e. Widget URI)
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
	public static String createPlainToken(IWidgetInstance instance, Messages localizedMessages) throws Exception{
		
		if (instance == null) throw new Exception(localizedMessages.getString("OpenSocialUtils.0")); //$NON-NLS-1$
		// check we have the required information:
		if (instance.getWidget() == null || instance.getIdKey() == null) {
			throw new Exception(localizedMessages.getString("OpenSocialUtils.1")); //$NON-NLS-1$
		}

		// Order of fields is:
		// owner, viewer, app_id, domain, app_url, mod_id, container
		// NOTE that we're hacking this now to push the id_key through the container value as Shindig won't let us use Strings for mod_id, only Longs
		// TODO replace hack with a real solution
		String[] fields = {getOwnerId(instance), getUserId(instance), instance.getWidget().getGuid(), DOMAIN_ID, getUrl(instance), "0", String.valueOf(instance.getIdKey())}; //$NON-NLS-1$
		for (int i = 0; i < fields.length; i++) {
			// escape each field individually, for metachars in URL
			fields[i] = URLEncoder.encode(fields[i], "UTF-8"); //$NON-NLS-1$
		}		
		String token = StringUtils.join(fields, ":"); //$NON-NLS-1$
		token = URLEncoder.encode(token, "UTF-8"); //$NON-NLS-1$
		
		return token;		
	}
	
	/**
	 * Utility method for producing an encrypted token for OpenSocial applications
	 * TODO FIXME this method is broken, or at least a pain to test in real deployment
	 * @param instance the widget instance to generate a token for
	 * @return the encrypted token for the widget instance
	 * @throws Exception
	 */
	public static String createEncryptedToken(IWidgetInstance instance, String key, Messages localizedMessages) throws Exception{
		
		if (instance == null) throw new Exception(localizedMessages.getString("OpenSocialUtils.0")); //$NON-NLS-1$
		// check we have the required information:
		if (instance.getWidget() == null || instance.getIdKey() == null) {
			throw new Exception(localizedMessages.getString("OpenSocialUtils.1")); //$NON-NLS-1$
		}
		
		BasicBlobCrypter crypter = new BasicBlobCrypter(key.getBytes());
		BlobCrypterSecurityToken token = new BlobCrypterSecurityToken(crypter, CONTAINER_ID, DOMAIN_ID);
		token.setAppUrl(getUrl(instance));
		// The ModuleId only takes a long, so we just have to hash the idKey for now. We could use the instance id,
		// but this would involve updating the instance object in a two-step create, which is more fiddly and expensive.
		token.setModuleId(instance.getIdKey().hashCode());
		token.setOwnerId(getOwnerId(instance));
		token.setViewerId(getUserId(instance));
		// The URL used in the active request
		token.setActiveUrl(getUrl(instance));
		
		String encryptedToken = token.encrypt();
		
		encryptedToken = URLEncoder.encode(encryptedToken, "UTF-8"); //$NON-NLS-1$
		
		return encryptedToken;
	}
	
	private static String getOwnerId(IWidgetInstance instance){
		//TODO FIXME
		return getUserId(instance);
	}
	
	private static String getUserId(IWidgetInstance instance){
		String userid = "@anon"; //$NON-NLS-1$
		if (instance.getUserId()!=null) if(!instance.getUserId().equals("")) userid = instance.getUserId(); //$NON-NLS-1$
		return userid;
	}
	
	private static String getUrl(IWidgetInstance instance){
    	IStartFile[] startFiles = instance.getWidget().getStartFiles().toArray(new IStartFile[instance.getWidget().getStartFiles().size()]);
		IStartFile start = (IStartFile) LocalizationUtils.getLocalizedElement(startFiles, new String[]{"en"}, instance.getWidget().getDefaultLocale());
		return start.getUrl();
	}

}
