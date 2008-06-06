/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.widgetservice.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 * A class using HttpClient to handle HTTP requests & manipulate the responses
 *
 * @author Paul Sharples
 * @version $Id: ProxyClient.java,v 1.5 2008-06-06 10:48:11 ps3com Exp $
 */
public class ProxyClient {
	
	static Logger fLogger = Logger.getLogger(ProxyClient.class.getName());
	
	private String fContentType;	
	private String fProxyUsername = null;
	private String fProxyPassword = null;
	private boolean fUseProxyAuthentication = false;

	public ProxyClient(){}
	
	public void setProxyAuthConfig(String username, String password){
		fUseProxyAuthentication = true;
		fProxyUsername = username;
		fProxyPassword = password;
	}
		
	public String getCType(){
		return fContentType;
	}
	
	public void setCType(String cType){
		fContentType = cType;
	}
	
		public String get(String url, Configuration properties) throws Exception {
			System.out.println("GET from " + url); //$NON-NLS-1$
			return fetchData(new GetMethod(url), properties);
		}
	    
	    public String post(String uri, String xmlData, Configuration properties) throws Exception {
			// Prepare HTTP post
			PostMethod post = new PostMethod(uri);
			System.out.println("POST to " + uri); //$NON-NLS-1$
			System.out.println(xmlData);

			return sendXmlData(xmlData, post, properties);
		}
	    
	    private String sendXmlData(String xmlData, EntityEnclosingMethod method, Configuration properties) throws Exception {
			// Tell the method to automatically handle authentication.
			method.setDoAuthentication(true);
				try {
					method.setRequestEntity(new StringRequestEntity(xmlData, "text/xml", "UTF8"));//$NON-NLS-1$  //$NON-NLS-2$
				}
				catch (UnsupportedEncodingException e) {
					throw new Exception(e);
				}
			return executeMethod(method, properties);
		}
		
	    
	    private String executeMethod(HttpMethod method, Configuration properties) throws Exception {
			// Execute the method.
			try {		
				HttpClient client = new HttpClient();
				// set the clients proxy values if needed
				ConnectionsPrefsManager.setProxySettings(client, properties);
				
				if(fUseProxyAuthentication){
					List authPrefs =  new ArrayList(2);
					  authPrefs.add(AuthPolicy.DIGEST );
					  authPrefs.add(AuthPolicy.BASIC);
					  client.getParams().setParameter (AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
					// send the basic authentication response even before the server gives an unauthorized response
					client.getParams().setAuthenticationPreemptive(true);
					// Pass our credentials to HttpClient
					client.getState().setCredentials(
							new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
							// this needs to be tenc username & password!
							new UsernamePasswordCredentials(fProxyUsername, fProxyPassword));
				
				}
				
				
				// Add user language to http request in order to notify server of user's language
				Locale locale = Locale.getDefault();
				;
				method.setRequestHeader("Accept-Language", locale.getLanguage()); //$NON-NLS-1$ 
				
				int statusCode = client.executeMethod(method);

				if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
					
					Header hType = method.getResponseHeader("Content-type");
					fLogger.error("7");
					fContentType = hType.getValue();
					// for now we are only expecting Strings
					fLogger.error("proxy::"+method.getResponseBodyAsString());
					return method.getResponseBodyAsString();
					//return readFully(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
				}
				else if (statusCode == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED)
					throw new Exception("ERROR_PROXY"); 
				else if (statusCode == HttpStatus.SC_UNAUTHORIZED)
					throw new Exception("ERROR_INVALID_PASSWORD"); 																			
				else {
					throw new Exception("Method failed: " + method.getStatusLine() + ' ' + method.getURI() + ' ' + method.getStatusText() + method.getResponseBodyAsString()); //$NON-NLS-1$
				}
			} 
			catch (IOException e) {
				throw new Exception("ERROR_CONNECT", e);
			} 
			finally {
				// Release the connection.
				method.releaseConnection();
			}
		}
	    
	    private String fetchData(HttpMethodBase method, Configuration properties) throws Exception {			
	    	// Provide custom retry handler is necessary
	    	//TODO: the line below was causing an error under jboss (not tomcat)
	    	//method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));

	    	// Tell the method to automatically handle authentication.
	    	method.setDoAuthentication(true);

	    	return executeMethod(method, properties);

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
