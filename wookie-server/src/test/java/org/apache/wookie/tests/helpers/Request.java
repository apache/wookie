/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wookie.tests.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.wookie.server.security.Hmac;
import org.apache.wookie.w3c.util.RandomGUID;

/**
 * Wrapper for functional tests
 * Use it to create new API calls that can also carry
 * a valid HMAC header
 */
public class Request {
	
    protected static final int TEST_SERVER_PORT = 8080;
    protected static final String TEST_SERVER_HOST = "localhost";	
	
	private String method;
	private String url;
	private String host;
	private String uri;
	private HashMap<String, String> params;
	private int statusCode;
	private String response;
	private InputStream stream;
	private String accepts;
	
	private String key;
	private String secret;
	
	/**
	 * Construct a new request
	 * @param method the HTTP method, e.g. POST
	 * @param url the URL to request
	 */
	public Request(String method, String url){
		this.method = method;
		this.key = "TEST";
		this.secret = "test@127.0.0.1";
		this.url = url;
		params = new HashMap<String, String>();
	}
	
	/**
	 * Sets the content type to accept for the response, e.g application/json, text/xml
	 * @param type the content type
	 */
	public void setAccepts(String type){
		this.accepts = type;
	}
	
	/**
	 * Add a parameter to the request; this is turned into a querystring or params depending
	 * on the method used
	 * @param name
	 * @param value
	 */
	public void addParameter(String name, String value){
		this.params.put(name, value);
	}
	
	/**
	 * Execute the request.   
	 * @param sign if true, add a valid HMAC signature
	 * @param auth if true, add HTTP BASIC credentials
	 * @throws HttpException
	 * @throws IOException
	 */
	public void execute(boolean sign, boolean auth) throws HttpException, IOException{
		if (method.equals("POST")){
			HttpMethod post = new PostMethod(this.url);
			execute(post,sign, auth);
		}
		if (method.equals("PUT")){
			HttpMethod put = new PutMethod(this.url);
			execute(put,sign, auth);
		}
		if (method.equals("DELETE")){
			HttpMethod delete = new DeleteMethod(this.url);
			execute(delete,sign, auth);
		}
		if (method.equals("GET")){
			HttpMethod get = new GetMethod(this.url);
			execute(get, sign, auth);
		}
	}
	
	/*
	 * Executes the request
	 */
	private void execute(HttpMethod method, boolean sign, boolean auth) throws HttpException, IOException{
	    HttpClient client = new HttpClient();
	    
	    if (auth){
	    	Credentials defaultcreds = new UsernamePasswordCredentials("java", "java");
	    	client.getState().setCredentials(new AuthScope(TEST_SERVER_HOST, TEST_SERVER_PORT, AuthScope.ANY_REALM), defaultcreds);
	    }
        
		this.host = method.getURI().getHost() + ":" +method.getURI().getPort();
		this.uri = method.getURI().getPath();
		if (sign) method.setRequestHeader("Authorization", getAuthz());
		if (accepts != null) method.setRequestHeader("Accept", accepts);

		if (method instanceof PostMethod){
			for (String name: this.params.keySet()){
				  ((PostMethod)method).setParameter(name, this.params.get(name));	
			}
		} else {
			method.setQueryString(getParamsAsQueryString());
		}
		client.executeMethod(method);
		this.statusCode = method.getStatusCode();
		this.response = method.getResponseBodyAsString();
		this.stream = method.getResponseBodyAsStream();
	}
	
	public String getResponseBodyAsString(){
		return this.response;
	}
	
	/**
	 * Creates a HMAC signature for the request
	 * @return
	 */
	private String getAuthz(){
		addParameter("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		addParameter("nonce", new RandomGUID().toString());
	
		String query = Hmac.getCanonicalParameters(params);

		String reqString = Hmac.getCanonicalRequest(method, host, uri, query);
				
		try {
			String signature = key + " " + Hmac.getHmac(reqString, secret);
			return signature;
		} catch (SignatureException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Returns the parameters as a querystring
	 */
	private String getParamsAsQueryString(){
		String query = "";
		for (String name: this.params.keySet()){
			if (query.length() > 1) query += '&';
			try {
				String value = this.params.get(name);
				if (name.equals("timestamp")) value = URLEncoder.encode(value, "UTF-8");
				query += name + "=" + value;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return query;
	}
	
	/**
	 * Gets the status code returned after executing the request
	 * @return
	 */
	public int getStatusCode(){
		return statusCode;
	}
	
	public String getResponse(){
		return response;
	}
	
	/**
	 * Get the response as a stream
	 * @return the response stream
	 */
	public InputStream getResponseStream(){
		return stream;
	}
	
	

}
