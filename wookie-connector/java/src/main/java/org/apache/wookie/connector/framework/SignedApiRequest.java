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
package org.apache.wookie.connector.framework;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.net.HttpURLConnection;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class SignedApiRequest {

	protected static final int TEST_SERVER_PORT = 8080;
	protected static final String TEST_SERVER_HOST = "localhost";	
	
	private static final String BOUNDARY = "*****";
	private static final String LINE_END = "\r\n";
	private static final String TWO_HYPHENS = "--";

	private String method;
	private String url;
	private String host;
	private String uri;
	private HashMap<String, String> params;
	private int statusCode;
	private InputStream stream;
	private File file;
	private String accepts;
	private String entity;
	private String key;
	private String secret;

	/**
	 * Construct a new request
	 * @param method the HTTP method, e.g. POST
	 * @param url the URL to request
	 */
	public SignedApiRequest(String method, String url, String key, String secret){
		this.method = method;
		this.key = "TEST";
		this.secret = "test@127.0.0.1";
		this.url = url;
		params = new HashMap<String, String>();
	}
	
	public static SignedApiRequest GET(String url, String key, String secret){
		return new SignedApiRequest("GET", url, key, secret);
	}
	public static SignedApiRequest POST(String url, String key, String secret){
		return new SignedApiRequest("POST", url, key, secret);
	}
	public static SignedApiRequest PUT(String url, String key, String secret){
		return new SignedApiRequest("PUT", url, key, secret);
	}
	public static SignedApiRequest DELETE(String url, String key, String secret){
		return new SignedApiRequest("DELETE", url, key, secret);
	}
	

	/**
	 * Sets the content type to accept for the response, e.g application/json, text/xml
	 * @param type the content type
	 */
	public void setAccepts(String type){
		this.accepts = type;
	}
	
	public void setFile(File file){
		this.file = file;
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
	public void execute()  throws IOException, WookieConnectorException{

		//
		// Add timestamp and nonce to request
		//
		addParameter("timestamp", dateFormat.format(new Date(System.currentTimeMillis())));
		addParameter("nonce", UUID.randomUUID().toString());

		//
		// Append query string
		//
		this.url += getParamsAsQueryString();
		
		//
		// Double encode spaces in URLs; this is to handle some slightly odd DELETE methods.
		//
		this.url = this.url.replace(" ", "%20");
		
		URL url = new URL(this.url);
		this.host = url.getHost()+ ":" + url.getPort();
		this.uri = url.getPath();
			

		//
		// Create connection
		//
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod(method);

		//
		// Set signature header
		//
		connection.addRequestProperty ("Authorization", getAuthz());

		//
		// Set accepted content type
		//
		if (accepts != null) connection.addRequestProperty("Accept", accepts);
		
		//
		// Set input to true
		//
		connection.setDoInput(true);

		//
		// Write file to data output stream if not null
		//
		if (file != null){
			
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024 * 1024;
			
			FileInputStream fileInputStream = new FileInputStream(this.file);
			
			connection.setDoOutput(true);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
			DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

			dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
			dos.writeBytes("Content-Disposition: form-data; name=\"upload\";"
					+ " filename=\"" + this.file.getAbsolutePath() + "\"" + LINE_END);
			dos.writeBytes(LINE_END);
			
			// create a buffer of maximum size

			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...

			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			//
			// send multipart form data necesssary after file data...
			//
			dos.writeBytes(LINE_END);
			dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END);

			//
			// close streams
			//
			fileInputStream.close();
			dos.flush();
			dos.close();
		}
		
		//
		// Write body content
		//
		if (this.entity != null){
			//this.entity =  URLEncoder.encode( this.entity, "UTF-8");
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-Type", "text/plain");
			connection.setRequestProperty("Content-Length", Integer.toString(this.entity.length()) );
			DataOutputStream stream = new DataOutputStream(connection.getOutputStream());
			stream.write(this.entity.getBytes());
			stream.flush();
			stream.close();
		}

		//
		// Get the result
		//

		this.statusCode = connection.getResponseCode();
		try {
			this.stream = connection.getInputStream();
		} catch (Exception e) {
			e.printStackTrace();
			this.stream = null;
		}
	}

	/**
	 * Get the response as a string
	 * @return
	 * @throws IOException 
	 */
	public String getResponseBodyAsString() throws IOException{
		StringWriter writer = new StringWriter();
		char buff[] = new char[1024];
		BufferedReader reader = new BufferedReader(new InputStreamReader(this.stream, "UTF-8"));
		int n;
		while ((n = reader.read(buff)) != -1 ) {
			writer.write(buff,0,n);
		}
		writer.close();
		return writer.toString();
	}

	/**
	 * Creates a HMAC signature for the request
	 * @return
	 */
	private String getAuthz(){
		String query = getCanonicalParameters(params);
		String reqString =getCanonicalRequest(method, host, uri, query);
		try {
			String signature = key + " " + getHmac(reqString, secret);
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
		String query = "?";
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

	/**
	 * Get the response as a stream
	 * @return the response stream
	 */
	public InputStream getResponseBodyAsStream(){
		return stream;
	}

	/**
	 * Set the request entity (body)
	 * @param entity
	 */
	public void setRequestEntity(String entity) {
		this.entity = entity;
	}
	
	/**
	 * Date formatter for ISO datetime
	 */
	private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"){ 
		private static final long serialVersionUID = 7465240007718011363L;
		public Date parse(String source,ParsePosition pos) {  
	        return super.parse(source.replaceFirst(":(?=[0-9]{2}$)",""),pos);
	    }
	};
	
	/**
	 * The hashing algorithm to use
	 */
	private static final String HMAC_ALGORITHM = "HmacSHA256";
	
	/**
	 * Computes a HMAC signature.
	 * @param data The data to be signed.
	 * @param key The signing key.
	 * @return The HMAC signature.
	 * @throws java.security.SignatureException when signature generation fails
	 */
	private String getHmac(String data, String key)
	throws java.security.SignatureException
	{
		String hmac64;
		try {
			// get an hmac_sha1 key from the raw key bytes
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_ALGORITHM);

			// get an hmac_sha1 Mac instance and initialize with the signing key
			Mac mac = Mac.getInstance(HMAC_ALGORITHM);
			mac.init(signingKey);

			// compute the hmac on input data bytes
			byte[] hmac = mac.doFinal(data.getBytes());

			// base64-encode the hmac
			hmac64 = encodeBase64String(hmac);

		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
		return hmac64;
	}
	
	/**
	 * Sorts parameters and returns a querystring in canonical form usable for signing
	 * @param parameterMap
	 * @return a String with all parameters sorted and represented correctly
	 */
	private String getCanonicalParameters(@SuppressWarnings("rawtypes") Map parameterMap){
		String query = "?";
		ArrayList<String> parameterNames = new ArrayList<String>();
		@SuppressWarnings("rawtypes")
		Iterator it = parameterMap.keySet().iterator();
		while (it.hasNext()) parameterNames.add((String) it.next());
		Collections.sort(parameterNames);
		for (String name:parameterNames){
			if (!query.equals("?")) query += "&";
			Object value = parameterMap.get(name);
			if (value instanceof String[]){
				query += name.toLowerCase() + "=" + ((String[])value)[0].toLowerCase();
			} else {
				query += name.toLowerCase() +"="+((String)value).toLowerCase();			
			}

		}
		return query;
	}
	
	/**
	 * Gets a canonical request to sign
	 * @param verb  the HTTP verb, e.g. POST
	 * @param host  the host, e.g. wookie.apache.org
	 * @param uri   the URI, e.g. /wookie
	 * @param query the canonical parameters for the request - see getCanonicalParameters
	 * @return the canonical string representation of the request
	 */
	private String getCanonicalRequest(String verb, String host, String uri, String query){
		String canonical = "";
		verb = verb.toUpperCase();
		host = host.toLowerCase();
		uri = uri.toLowerCase();
		query = query.toLowerCase();
		canonical = verb + "\n" + host + "\n" + uri + "\n" + query;
		return canonical;
	}
	
	private String encodeBase64String (byte[] inputBytes ) {
		return DatatypeConverter.printBase64Binary(inputBytes);
	}



}


