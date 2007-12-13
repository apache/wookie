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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

/**
 * A class using HttpClient to handle HTTP requests & manipulate the responses
 *
 * @author Paul Sharples
 * @version $Id: ProxyClient.java,v 1.4 2007-12-13 21:22:04 ps3com Exp $
 */
public class ProxyClient {
	
	static Logger _logger = Logger.getLogger(ProxyClient.class.getName());
	
	public String ctype;

	public ProxyClient() {}
		
	
		public String get(String url) throws Exception {
			System.out.println("GET from " + url); //$NON-NLS-1$
			return fetchData(new GetMethod(url));
		}
	    
	    public String post(String uri, String xmlData) throws Exception {
			// Prepare HTTP post
			PostMethod post = new PostMethod(uri);
			System.out.println("POST to " + uri); //$NON-NLS-1$
			System.out.println(xmlData);

			return sendXmlData(xmlData, post);
		}
	    
	    private String sendXmlData(String xmlData, EntityEnclosingMethod method) throws Exception {
			// Tell the method to automatically handle authentication.
			method.setDoAuthentication(true);
				try {
					method.setRequestEntity(new StringRequestEntity(xmlData, "text/xml", "UTF8"));//$NON-NLS-1$  //$NON-NLS-2$
				}
				catch (UnsupportedEncodingException e) {
					throw new Exception(e);
				}
			return executeMethod(method);
		}
		
	    
	    private String executeMethod(HttpMethod method) throws Exception {
			// Execute the method.
			try {				
				HttpClient client = new HttpClient();
				// Add user language to http request in order to notify server of user's language
				Locale locale = Locale.getDefault();
				method.setRequestHeader("Accept-Language", locale.getLanguage()); //$NON-NLS-1$ 
				
				int statusCode = client.executeMethod(method);

				if (statusCode == HttpStatus.SC_OK || statusCode == HttpStatus.SC_CREATED) {
					// for now we are only expecting Strings
					return method.getResponseBodyAsString();

				}
				else if (statusCode == HttpStatus.SC_UNAUTHORIZED)
					throw new Exception("ERROR_INVALID_PASSWORD"); 						
				else if (statusCode == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED)
					throw new Exception("ERROR_PROXY"); 									
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
	    
		private String fetchData(HttpMethodBase method) throws Exception {
			try {
				// Create an instance of HttpClient.
				HttpClient client = new HttpClient();
				// Provide custom retry handler is necessary
				/*
				 //TODO: the line below was causing an error under jboss (not tomcat)
				method.getParams()
						.setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));
						*/
				// Tell the method to automatically handle authentication.
				method.setDoAuthentication(true);
				// Execute the method.
				int statusCode = client.executeMethod(method);
				if (statusCode == HttpStatus.SC_OK) {
					Header hType = method.getResponseHeader("Content-type");
					ctype = hType.getValue();
					return method.getResponseBodyAsString();
				} 
				else if (statusCode == HttpStatus.SC_UNAUTHORIZED){	
					throw new Exception("Passed credentials are not correct"); //$NON-NLS-1$
				}
				else if (statusCode == HttpStatus.SC_PROXY_AUTHENTICATION_REQUIRED){
					throw new Exception("Proxy authentication required. This can be set in the preferences."); //$NON-NLS-1$
				}
				else{
					throw new Exception(							
							"Method failed: " + method.getStatusLine() + ' ' + method.getURI() + ' ' + method.getStatusText()); //$NON-NLS-1$
				}
			} 
			catch (HttpException e) {
				throw new Exception(e);
			} 
			catch (IOException e) {
				throw new Exception(e);
			} 
			finally {				
				// Release the connection.
				method.releaseConnection();
			}
		}
}
