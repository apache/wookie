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

package org.apache.wookie.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

/**
 * A class using HttpClient to handle HTTP requests & manipulate the responses
 */
public class ProxyClient {

	static Logger fLogger = Logger.getLogger(ProxyClient.class.getName());

	private String fContentType = "text/plain";	
	private String fProxyUsername = null;
	private String fProxyPassword = null;
	private String fBase64Auth = null;
	private NameValuePair[] parameters = null;

	private boolean fUseProxyAuthentication = false;

	@SuppressWarnings("unchecked")
	public ProxyClient(HttpServletRequest request){
		String proxyUserName = request.getParameter("username");
		String proxyPassword = request.getParameter("password");
		String base64Auth = request.getHeader("Authorization");  
		if(proxyUserName != null && proxyPassword != null )	
			this.setProxyAuthConfig(proxyUserName, proxyPassword);
		if(base64Auth != null)
			this.setBase64AuthConfig(base64Auth);
		filterParameters(request.getParameterMap());
	}

	private void setProxyAuthConfig(String username, String password){
		fUseProxyAuthentication = true;
		fProxyUsername = username;
		fProxyPassword = password;
	}

	private void setBase64AuthConfig(String base64Auth){
		fUseProxyAuthentication = true;
		fBase64Auth = base64Auth;
	}

	public String getCType(){
		return fContentType;
	}

	public String get(String url, Configuration properties) throws Exception {
		fLogger.debug("GET from " + url); //$NON-NLS-1$
		GetMethod method = new GetMethod(url);
		method.setDoAuthentication(true);
		return executeMethod(method, properties);
	}

	public String post(String uri, String xmlData, Configuration properties) throws Exception {
		fLogger.debug("POST to " + uri); //$NON-NLS-1$
		PostMethod method = new PostMethod(uri);
		method.setDoAuthentication(true);
		method.setRequestEntity(new StringRequestEntity(xmlData, "text/xml", "UTF8"));//$NON-NLS-1$  //$NON-NLS-2$
		method.addParameters(this.parameters);
		return executeMethod(method, properties);
	}

	/**
	 * Processes the parameters passed through to the request,
	 * removing the parameters used by the proxy itself
	 * @return
	 */
	private void filterParameters(Map<Object,Object> umap){
		Map<Object, Object> map = new HashMap<Object, Object>(umap);
		map.remove("instanceid_key");
		map.remove("url");
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Object key:map.keySet().toArray()){
			for (String value:(String[])map.get(key)){
				NameValuePair param = new NameValuePair();
				param.setName((String)key);
				param.setValue(value);
				params.add(param);				
			}
		}
		parameters = params.toArray(new NameValuePair[params.size()]);
	}

	private String executeMethod(HttpMethod method, Configuration properties) throws Exception, AuthenticationException {
		// Execute the method.
		try {		
			HttpClient client = new HttpClient();
			// set the clients proxy values if needed
			ConnectionsPrefsManager.setProxySettings(client, properties);

			if(fUseProxyAuthentication){
				if (fBase64Auth != null) {
					method.setRequestHeader("Authorization", fBase64Auth);
				}
				else {
					List<String> authPrefs =  new ArrayList<String>(2);
					authPrefs.add(AuthPolicy.DIGEST );
					authPrefs.add(AuthPolicy.BASIC);
					client.getParams().setParameter (AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
					// send the basic authentication response even before the server gives an unauthorized response
					client.getParams().setAuthenticationPreemptive(true);
					// Pass our credentials to HttpClient
					client.getState().setCredentials(
							new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
							new UsernamePasswordCredentials(fProxyUsername, fProxyPassword));
				}
			}

			// Add user language to http request in order to notify server of user's language
			Locale locale = Locale.getDefault();

			method.setRequestHeader("Accept-Language", locale.getLanguage()); //$NON-NLS-1$ 

			int statusCode = client.executeMethod(method);

			if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
				Header hType = method.getResponseHeader("Content-type");					
				if (hType != null) fContentType = hType.getValue();
				// for now we are only expecting Strings					
				//return method.getResponseBodyAsString();
				return readFully(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
			}
			else if (statusCode == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED || statusCode == HttpStatus.SC_UNAUTHORIZED)
				throw new AuthenticationException();																			
			else {
				throw new Exception("Method failed: " + method.getStatusLine() + ' ' + method.getURI() + ' ' + method.getStatusText() + method.getResponseBodyAsString()); //$NON-NLS-1$
			}
		} 
		catch (IOException e) {
			throw e;
		} 
		finally {
			// Release the connection.
			method.releaseConnection();
		}
	}

	/**
	 * This is supposed to be the correct way to read the response instead of using getResponseBody() - which gives a runtime warning;
	 * 
	 * See - http://mail-archives.apache.org/mod_mbox/jakarta-httpclient-user/200411.mbox/%3c1101558111.4070.22.camel@localhost.localdomain%3e
	 * @param input
	 * @return
	 * @throws IOException
	 */
	private String readFully(Reader input) throws IOException {
		BufferedReader bufferedReader = input instanceof BufferedReader ? (BufferedReader) input
				: new BufferedReader(input);
		StringBuffer result = new StringBuffer();
		char[] buffer = new char[4 * 1024];
		int charsRead;
		while ((charsRead = bufferedReader.read(buffer)) != -1) {
			result.append(buffer, 0, charsRead);
		}
		return result.toString();
	}
}
