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

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.auth.AuthTokenUtils;
import org.apache.wookie.auth.InvalidAuthTokenException;
import org.apache.wookie.beans.IOAuthToken;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.services.OAuthService;
import org.apache.wookie.services.WidgetMetadataService;
import org.apache.wookie.w3c.IParam;
import org.apache.wookie.feature.IFeature;
import org.apache.wookie.helpers.WidgetRuntimeHelper;

public class oAuthClient implements IFeature {
	private static String widgetContextFolder = null;
	
	public String getName() {
		return "http://oauth.net/2";
	}
	
	public String[] scripts() {
		return new String[] {
				WidgetRuntimeHelper.getWebContextPath() + "/dwr/interface/OAuthConnector.js", 
				WidgetRuntimeHelper.getWebContextPath() + "/shared/js/oauth.js"};
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
	
	public oAuthClient() {
		if (oAuthClient.widgetContextFolder != null) return;
		try {
	        File localPropsFile = new File(System.getProperty("user.dir") + File.separator + "local.widgetserver.properties");
	        PropertiesConfiguration localConfiguration = new PropertiesConfiguration(localPropsFile);
	        CompositeConfiguration configuration = new CompositeConfiguration();
	        configuration.addConfiguration(localConfiguration);
	        configuration.addConfiguration(new PropertiesConfiguration("widgetserver.properties"));
	        String widgetFolder = configuration.getString("widget.widgetfolder");
	        if (widgetFolder != null) {
	        	widgetFolder = widgetFolder.replace(File.separatorChar, '/');
	        	if (!widgetFolder.endsWith("/")) widgetFolder += "/";
	        }
	        oAuthClient.widgetContextFolder = widgetFolder;
		} catch (ConfigurationException ex) {

		}		
	}
	
	public String queryToken(String idKey) {
		if(idKey == null) return "invalid";
		
		try {
			AuthToken authToken = AuthTokenUtils.decryptAuthToken(idKey);
			IOAuthToken oauthToken = OAuthService.Factory.getInstance().getOAuthToken(authToken.toString());
			if (oauthToken != null) {
				if (!oauthToken.isExpires())
					return oauthToken.getAccessToken() ;
			}
		} catch (InvalidAuthTokenException e) {
			return "invalid";
		}
		
		return "invalid";
	}
	
	public void invalidateToken(String idKey) {
		if(idKey == null) return;
		try {
			AuthToken authToken = AuthTokenUtils.decryptAuthToken(idKey);
			IOAuthToken oauthToken = OAuthService.Factory.getInstance().getOAuthToken(authToken.toString());
			if (oauthToken != null) {
				OAuthService.Factory.getInstance().deleteOAuthToken(authToken.toString());
			}
		} catch (InvalidAuthTokenException e) {
			return;
		}
	}

	public String updateToken(String idKey_tokenBunch) {
		Map<String,String> params = parseParams(idKey_tokenBunch);		
		String idKey = params.get("id_key");
		
		AuthToken authToken;
		try {
			authToken = AuthTokenUtils.decryptAuthToken(idKey);
			Map<String, String> oAuthParams = queryXMLParams(idKey);
			if (oAuthParams == null) {
				return "invalid";			
			}
			
			IOAuthToken oauthToken = OAuthService.Factory.getInstance().getOAuthToken(authToken.toString());
			if (oauthToken == null){
				oauthToken = OAuthService.Factory.getInstance().createOAuthToken(authToken.toString());			
			}
			try {
				oauthToken.setAccessToken(params.get("access_token"));
				oauthToken.setExpires(System.currentTimeMillis() + 1000 * Integer.parseInt(params.get("expires_in")));
				oauthToken.setClientId(oAuthParams.get("clientId"));
				oauthToken.setAuthzUrl(oAuthParams.get("authzServer"));
				OAuthService.Factory.getInstance().updateOAuthToken(authToken.toString(), oauthToken);
				return oauthToken.getAccessToken();
			} catch (Exception ex) {
				return "invalid";
			}
		} catch (InvalidAuthTokenException e) {
			return "invalid";
		}

	}
	
	public Map<String, String> queryXMLParams(String idKey) {
		try {
			AuthToken authToken = AuthTokenUtils.decryptAuthToken(idKey);
			IWidget widget = WidgetMetadataService.Factory.getInstance().getWidget(authToken.getWidgetId());

			Collection<org.apache.wookie.w3c.IFeature> widgetFeatures = widget.getFeatures();
			org.apache.wookie.w3c.IFeature oAuthFeature = null;
			for (org.apache.wookie.w3c.IFeature aFeature : widgetFeatures) {
				if (getName().equals(aFeature.getName())) {
					oAuthFeature = aFeature;
					break;
				}
			}
			if (oAuthFeature == null) return null;
			
			Collection<IParam> oAuthParams = oAuthFeature.getParameters();
			Map<String, String> oAuthParamMap = new HashMap<String, String>();
			for (IParam aParam : oAuthParams) {
				oAuthParamMap.put(aParam.getName(), aParam.getValue());
			}
			return oAuthParamMap;
			
		} catch (InvalidAuthTokenException e) {
			return null;
		}
	}
	
	public  Map<String, String> queryOAuthParams(Map<String, String> info) {
		if (info.get("id_key") == null || info.get("url") == null) return null;
		Map<String, String>oAuthParamMap = queryXMLParams(info.get("id_key"));
		if (oAuthParamMap == null) return null;
		String url = info.get("url");
		if (widgetContextFolder == null) return oAuthParamMap;
		int iPos = url.indexOf(widgetContextFolder, url.indexOf(WidgetRuntimeHelper.getWebContextPath()));
		if (iPos < 0) return oAuthParamMap;
		url = url.substring(0, iPos);
		if (!oAuthParamMap.containsKey("profile")) 
			oAuthParamMap.put("profile", "implicit");
		try {
			url = URLEncoder.encode(url, "UTF8");
			
			if ("implicit".equals(oAuthParamMap.get("profile"))) 
				url += "%2Ffeatures%2Foauth%2Fimplicit";
			else if ("authorization code".equals(oAuthParamMap.get("profile")))
				url += "%2Ffeatures%2Foauth%2Fauthz-code";
			else 
				url += "%2Ffeatures%2Foauth%2Fother";
		} catch (UnsupportedEncodingException e) {
			if ("implicit".equals(oAuthParamMap.get("profile")))
				url += "/features/oauth/implicit";
			else if ("authorization code".equals(oAuthParamMap.get("profile")))
				url += "/features/oauth/authz-code";
			else 
				url += "/features/oauth/other";
		}
		oAuthParamMap.put("redirectUri", url);
		
		if (!oAuthParamMap.containsKey("persist"))
			oAuthParamMap.put("persist", "true");
		if (!oAuthParamMap.containsKey("popupWidth"))
			oAuthParamMap.put("popupWidth", "400px");
		if (!oAuthParamMap.containsKey("popupHeight"))
			oAuthParamMap.put("popupHeight", "500px");		
		return oAuthParamMap;
	}
	
	private Map<String, String> parseParams(String paramString) {
		StringTokenizer st = new StringTokenizer(paramString, "&");
		Map<String, String> result = new HashMap<String, String>();
		while (st.hasMoreTokens()) { 
			String paramPair = st.nextToken();
			int iPos = paramPair.indexOf('=');
			if (iPos > 0) {
				result.put(paramPair.substring(0, iPos), paramPair.substring(iPos + 1));
			}
		}
		return result;
	}	
}


