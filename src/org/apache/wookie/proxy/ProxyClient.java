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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * A class using HttpClient to handle HTTP requests & manipulate the responses
 */
public class ProxyClient {

	static Logger fLogger = Logger.getLogger(ProxyClient.class.getName());

	private String fProxyUsername = null;
	private String fProxyPassword = null;
	private String fBase64Auth = null;
	private NameValuePair[] parameters = null;
	private boolean fUseProxyAuthentication = false;

	@SuppressWarnings("unchecked")
	public ProxyClient(HttpServletRequest request){
	  //
	  // If the request includes authn parameters, create proxy auth headers
	  //
		String proxyUserName = request.getParameter("username");
		String proxyPassword = request.getParameter("password");
		String base64Auth = request.getHeader("Authorization");  
		if(proxyUserName != null && proxyPassword != null )	
			this.setProxyAuthConfig(proxyUserName, proxyPassword);
		if(base64Auth != null)
			this.setBase64AuthConfig(base64Auth);
		//
		// Filter out instructions to the proxy server from the original request parameters
		//
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
	
	/**
	 * Process a proxied GET request
	 * @param url the URL to GET
	 * @param request the original request object
	 * @param properties the servlet configuration
	 * @return a ResponseObject from the remote site
	 * @throws Exception
	 */
	public ResponseObject get(String url, HttpServletRequest request, Configuration properties) throws Exception {
    fLogger.debug("GET from " + url); //$NON-NLS-1$
    GetMethod method = new GetMethod(url);
    method.setDoAuthentication(true);
    return executeMethod(method, request, properties);	  
	}
	
	/**
	 * Process a proxied POST request
	 * @param url the URL to POST
	 * @param xmlData the body of the request
	 * @param request the original request object
	 * @param properties the servlet configuration
	 * @return a ResponseObject from the remote site
	 * @throws Exception
	 */
	 public ResponseObject post(String url , HttpServletRequest request, Configuration properties) throws Exception {
	    fLogger.debug("POST to " + url); //$NON-NLS-1$
	    PostMethod method = new PostMethod(url);
	    method.setDoAuthentication(true);
	    
	    if(this.parameters.length > 0) {
	      method.addParameters(this.parameters);
	    } else {
	      method.setRequestEntity(new InputStreamRequestEntity(request.getInputStream()));
	    }
	    
	    return executeMethod(method, request, properties); 
	  }

	/**
	 * Processes the parameters passed through to the request,
	 * removing the parameters used by the proxy itself
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
	
	/**
	 * Execute the request and return the components of the response
	 * @param method the method to use, e.g. POST or GET
	 * @param request the original request
	 * @param properties the configuration of the servlet
	 * @return a ResponseObject containing both the response body and all headers returned
	 * @throws Exception
	 */
	private ResponseObject executeMethod(HttpMethod method, HttpServletRequest request, Configuration properties) throws Exception{
	  ResponseObject responseObject = new ResponseObject();

	  try {   
	    HttpClient client = new HttpClient();
	    
	    //
	    // set the clients proxy values if needed
	    //
	    ConnectionsPrefsManager.setProxySettings(client, properties);
	    
	    //
	    // Add in original request headers
	    //
	    @SuppressWarnings("unchecked")
      Enumeration<String> headers = request.getHeaderNames();
	    while(headers.hasMoreElements()){
	      String header = (String) headers.nextElement();
	      
	      //
	      // We can't use content-length headers in case we altered the original body when filtering out
	      // the proxy parameters, so exclude them when adding the headers to the request
	      //
	      if(!header.equalsIgnoreCase("Content-Length")){
	          method.addRequestHeader(header, request.getHeader(header));
	      }
	    }

	    //
	    // Include authentication if required
	    //
	    if(fUseProxyAuthentication){
	      if (fBase64Auth != null) {
	        method.setRequestHeader("Authorization", fBase64Auth);
	      }
	      else {
	        List<String> authPrefs =  new ArrayList<String>(2);
	        authPrefs.add(AuthPolicy.DIGEST );
	        authPrefs.add(AuthPolicy.BASIC);
	        client.getParams().setParameter (AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
	        
	        //
	        // send the basic authentication response even before the server gives an unauthorized response
	        //
	        client.getParams().setAuthenticationPreemptive(true);
	        
	        //
	        // Pass our credentials to HttpClient
	        //
	        client.getState().setCredentials(
	            new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
	            new UsernamePasswordCredentials(fProxyUsername, fProxyPassword));
	      }
	    }

	    //
	    // Excecute request and return response
	    //
	    int statusCode = client.executeMethod(method);
	    responseObject.status = statusCode;
	    responseObject.body = IOUtils.toByteArray(method.getResponseBodyAsStream());
	    responseObject.headers = method.getResponseHeaders();
	    return responseObject;
	  } 
	  catch (IOException e) {
	    throw e;
	  } 
	  finally {
	    
	    //
	    // Release the connection.
	    //
	    method.releaseConnection();
	  }
	}
	
	/**
	 * Inner class used to pass response parts back to the servlet
	 */
	class ResponseObject{
	  public int status;
	  public byte[] body;
	  public Header[] headers;
	  
	}
}
