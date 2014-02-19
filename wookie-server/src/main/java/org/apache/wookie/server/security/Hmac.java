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
package org.apache.wookie.server.security;

import java.security.SignatureException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;

/**
 * Utilities for creating and validating HMAC authentication for REST API calls
 * 
 * We are using a HMAC scheme very similar to that used by Amazon Web Services.
 * The canonical form for each request is computed from the HTTP verb, host, URI
 * and an alpha-sorted array of parameters, including a timestamp and a nonce. 
 * 
 * This canonical string is then signed with the shared secret for the application 
 * using HMAC-SHA256. 
 * 
 * The signature is placed in the Authorization header of the request, preceded by 
 * the public key of the requesting application (the "API key" used in previous APIs)
 * 
 */
public class Hmac {
	
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
	 * The amount of clock skew we allow for requests - if the timestamp on a 
	 * request is older than this, we reject it
	 */
	private static final long CLOCK_SKEW_ALLOWANCE = 180000L; // allow three minutes for clock skew
	
	/**
	 * Gets the public key associated with the request
	 * @param request
	 * @return the public key or null if there is no key
	 */
	public static String getPublicKey(HttpServletRequest request){
		String header = request.getHeader("Authorization");
		if (header == null || header.trim().length() == 0 || header.split(" ").length != 2) return null;
		return header.split(" ")[0];
	}
	
	/**
	 * Gets the signature associated with the request
	 * @param request
	 * @return the signture or null if there is no key
	 */
	public static String getSignature(HttpServletRequest request){
		String header = request.getHeader("Authorization");
		if (header == null || header.trim().length() == 0 || header.split(" ").length != 2) return null;
		return header.split(" ")[1];
	}
	
	/**
	 * Validates a signed request
	 */
	public static boolean isValidSignedRequest(HttpServletRequest request){
		
		//
		// Get the header
		//
		String auth = request.getHeader("Authorization");

		//
		// If no auth header, not valid.
		//
		if (auth == null) return false;
		
		//
		// Split the header into the api key and signature
		// If either part is missing, or there are additional
		// parts, the request is not valid.
		//
		String apiKey = getPublicKey(request);
		String signature = getSignature(request);
		if (apiKey == null || signature == null) return false;

		//
		// Validate the api public key exists
		//
		if (!ApiKeys.getInstance().validate(apiKey)) return false;

		//
		// Get the API key secret
		//
		String secret = ApiKeys.getInstance().getApiKey(apiKey).getSecret();

		//
		// Check the timestamp. If no timestamp is
		// provided, the request is not valid
		//
		String timestamp = request.getParameter("timestamp");
		if (timestamp == null) return false;

		//
		// Parse the timestamp. If its not a valid
		// datetime, the request is not valid
		//
		Date timestampDate = null;
		
		//
		// Check for Z instead of UTC
		//
		if (timestamp.endsWith("Z")){
			timestamp = timestamp.replace("Z", "+0000");
		}
		
		try {
			timestampDate = dateFormat.parse(timestamp);
		} catch (ParseException e1) {
			return false;
		}
		//
		// Compute the window of validity for the timestamp,
		// equivalent to now minus an allowance for clock
		// skew
		//
		long now = System.currentTimeMillis();
		long window = now - CLOCK_SKEW_ALLOWANCE;
		
		//
		// Check that the timestamp provided falls within the
		// allowed range. This is to prevent replay attacks
		//
		if ((timestampDate.getTime()) < window){
			return false;
		}

		//
		// Get the nonce used. If there is no nonce, the
		// request is not valid
		//
		String nonce = request.getParameter("nonce");
		if (nonce == null || nonce.trim().length() == 0) return false;
		
		//
		// Check the nonce hasn't been reused lately
		//
		if (!NonceCache.getInstance().isValid(nonce)) return false;

		//
		// Get the canonical request string to validate
		//
		String canonicalRequest = toCanonicalForm(request);

		//
		// Calculate the signature that should have been used
		//
		String correctSignature = null;
		try {
			correctSignature = getHmac(canonicalRequest, secret);
		} catch (SignatureException e) {
			return false;
		}
		
		//
		// Compare the signature hashes. If they match
		// then the request is valid
		//
		if (correctSignature.equals(signature)){
			return true;
		} else {
			return false;
		}
		
	}
	
	/**
	 * Converts a Java Date object to an ISO-formatted date string
	 * @param date
	 * @return the ISO string
	 */
	public static String getFormattedDate(Date date){
		return dateFormat.format(date);	
	}

	/**
	 * Converts a HTTP request into its canonical form used for signing
	 */
	private static String toCanonicalForm(HttpServletRequest request){		
		return getCanonicalRequest(request.getMethod(), request.getHeader("Host"), request.getRequestURI(), getCanonicalParameters(request.getParameterMap()));
	}	
	
	/**
	 * Gets a canonical request to sign
	 * @param verb  the HTTP verb, e.g. POST
	 * @param host  the host, e.g. wookie.apache.org
	 * @param uri   the URI, e.g. /wookie
	 * @param query the canonical parameters for the request - see getCanonicalParameters
	 * @return the canonical string representation of the request
	 */
	public static String getCanonicalRequest(String verb, String host, String uri, String query){
		String canonical = "";
		verb = verb.toUpperCase();
		host = host.toLowerCase();
		uri = uri.toLowerCase();
		query = query.toLowerCase();
		canonical = verb + "\n" + host + "\n" + uri + "\n" + query;
		return canonical;
	}
	
	/**
	 * Sorts parameters and returns a querystring in canonical form usable for signing
	 * @param parameterMap
	 * @return a String with all parameters sorted and represented correctly
	 */
	public static String getCanonicalParameters(@SuppressWarnings("rawtypes") Map parameterMap){
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
	 * Computes a HMAC signature.
	 * @param data The data to be signed.
	 * @param key The signing key.
	 * @return The HMAC signature.
	 * @throws java.security.SignatureException when signature generation fails
	 */
	public static String getHmac(String data, String key)
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
			hmac64 = Base64.encodeBase64String(hmac);

		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}
		return hmac64;
	}
	
}
