/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */
package org.apache.wookie.feature.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.wookie.Messages;
import org.apache.wookie.beans.IOAuthToken;
import org.apache.wookie.beans.IParam;
import org.apache.wookie.beans.IStartFile;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.feature.IFeature;
import org.apache.wookie.server.LocaleHandler;
import org.directwebremoting.WebContextFactory;

public class oAuthClient implements IFeature {

	public String getName() {
		return "http://oauth.net/2";
	}
	
	public String[] scripts() {
		return new String[] {"/wookie/dwr/interface/OAuthConnector.js", "/wookie/shared/js/oauth.js"};
	}

	public String[] stylesheets() {
		return null;
	}

	public boolean flattenOnExport() {
		return false;
	}

	public String getFolder() {
		return null;
	}
	
	public String authenticate(String idKey_RedirectUri) {
		int iPos = idKey_RedirectUri.indexOf('#');
		String idKey = idKey_RedirectUri.substring(0, iPos);
		String redirectUri = idKey_RedirectUri.substring(iPos + 1);
		if(idKey == null) return "invalid";		
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(idKey);
		if(widgetInstance==null) return "invalid";
		
		Collection<IStartFile> startFiles = widgetInstance.getWidget().getStartFiles();
		String startFileUrl = null;
		for(IStartFile startFile : startFiles) {
			iPos = redirectUri.indexOf(startFile.getUrl());
			if (iPos > -1) {
				startFileUrl = startFile.getUrl();
				break;
			}
		}
		if (startFileUrl != null) {
			redirectUri = redirectUri.substring(0, iPos + startFileUrl.length()) + "?idkey=" + idKey;
		}
		
		try {
			redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		
		Collection<org.apache.wookie.beans.IFeature> widgetFeatures = widgetInstance.getWidget().getFeatures();
		org.apache.wookie.beans.IFeature oAuthFeature = null;
		for (org.apache.wookie.beans.IFeature aFeature : widgetFeatures) {
			if (getName().equals(aFeature.getFeatureName())) {
				oAuthFeature = aFeature;
				break;
			}
		}
		
		if (oAuthFeature == null) {
			return "";
		}
		
		Collection<IParam> oAuthParams = oAuthFeature.getParameters();
		String clientId = idKey;
		String authzServer = null;
		String scope = ""; 
		for (IParam aParam : oAuthParams) {
			String paramName = aParam.getParameterName().toLowerCase();
			String paramValue = aParam.getParameterValue();
			if ("authzserver".equals(paramName)) {
				authzServer = paramValue;
			} else if ("clientid".equals(paramName)) {
				if (!"auto".equalsIgnoreCase(paramValue)) {
					clientId = paramValue;
				}
			} else if ("scope".equals(aParam.getParameterName())) {
				scope = paramValue;
			} else if ("redirecturi".equals(paramName)) {
				if (paramValue.length() != 0 && !"auto".equalsIgnoreCase(paramValue)) {
					redirectUri = paramValue;
				}
			}
		}
		
		IOAuthToken oauthToken = persistenceManager.findOAuthToken(widgetInstance);
		if (oauthToken != null) {
			persistenceManager.delete(oauthToken);
		}
		
		String url = authzServer + "?client_id=" + clientId + "&response_type=token&redirect_uri=" + redirectUri; 
		
		if (scope.length() > 0) {
			url = url + "&scope=" + scope;
		}
		
		return url;
	}
	
	public String queryToken(String idKey) {
		if(idKey == null) return "invalid";
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(idKey);
		if(widgetInstance==null) return "invalid";
		
		IOAuthToken oauthToken = persistenceManager.findOAuthToken(widgetInstance);
		if (oauthToken != null) {
			if (!oauthToken.isExpires())
				return oauthToken.getAccessToken() ;
		}
		return "invalid";
	}
	
	public void invalidateToken(String idKey) {
		if(idKey == null) return;
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(idKey);
		if(widgetInstance==null) return;
		
		IOAuthToken oauthToken = persistenceManager.findOAuthToken(widgetInstance);
		if (oauthToken != null) {
			persistenceManager.delete(oauthToken);
		}
	}
	
	public String getClientId(String idKey) {
		if(idKey == null) return "invalid";
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(idKey);
		if(widgetInstance==null) return "invalid";
		IOAuthToken oauthToken = persistenceManager.findOAuthToken(widgetInstance);
		if (oauthToken != null) {
			return oauthToken.getClientId();
		} else {
			return "invalid";
		}
	}
	
	public String updateToken(String idKey_tokenBunch) {
		int iPos = idKey_tokenBunch.indexOf('#');
		String idKey = idKey_tokenBunch.substring(0, iPos);
		String tokenBunch = idKey_tokenBunch.substring(iPos + 1);
		
		Map<String,String> oAuthTokenBunch = new HashMap<String, String>();
		iPos = 0;
		int iEqual, iOffset = 0;
		String fragment = tokenBunch;
		do {
			iPos = tokenBunch.indexOf('&', iOffset);
			if (iPos < 0) {
				iPos = tokenBunch.length();
			}
			
			fragment = tokenBunch.substring(iOffset, iPos);
			iOffset = iOffset + iPos + 1;
			iEqual = fragment.indexOf('=');
			if (iEqual < 0) continue;
			oAuthTokenBunch.put(fragment.substring(0, iEqual), fragment.substring(iEqual + 1));
		} while (iOffset < tokenBunch.length());
		
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(idKey);
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);		

		if(widgetInstance==null) {
			return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$
		}

		Map<String, String> oAuthParams = queryOAuthParams(idKey);
		if (oAuthParams == null) {
			return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$			
		}
		
		IOAuthToken oauthToken = persistenceManager.findOAuthToken(widgetInstance);
		if (oauthToken == null) oauthToken = persistenceManager.newInstance(IOAuthToken.class);
		oauthToken.setAccessToken(oAuthTokenBunch.get("access_token"));
		oauthToken.setExpires(System.currentTimeMillis() + 1000 * Integer.parseInt(oAuthTokenBunch.get("expires_in")));
		oauthToken.setClientId(oAuthParams.get("clientId"));
		oauthToken.setAuthzUrl(oAuthParams.get("authzServer"));
		oauthToken.setWidgetInstance(widgetInstance);
		persistenceManager.save(oauthToken);
		return oauthToken.getAccessToken();
	}
	
	private Map<String, String> queryOAuthParams(String idKey) {
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(idKey);
		if(widgetInstance==null) return null;
		
		Collection<org.apache.wookie.beans.IFeature> widgetFeatures = widgetInstance.getWidget().getFeatures();
		org.apache.wookie.beans.IFeature oAuthFeature = null;
		for (org.apache.wookie.beans.IFeature aFeature : widgetFeatures) {
			if (getName().equals(aFeature.getFeatureName())) {
				oAuthFeature = aFeature;
				break;
			}
		}
		if (oAuthFeature == null) return null;
		
		Collection<IParam> oAuthParams = oAuthFeature.getParameters();
		Map<String, String> oAuthParamMap = new HashMap<String, String>();
		for (IParam aParam : oAuthParams) {
			oAuthParamMap.put(aParam.getParameterName(), aParam.getParameterValue());
		}
		return oAuthParamMap;
	}
}