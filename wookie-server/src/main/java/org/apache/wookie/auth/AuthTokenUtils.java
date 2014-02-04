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

package org.apache.wookie.auth;

import java.util.HashMap;
import java.util.Map;

import org.apache.wookie.server.security.ApiKey;
import org.apache.wookie.server.security.ApiKeys;

/**
 * Utilities for token handling, including encrypting an AuthToken object into an opaque string,
 * and decrypting a string token to retrieve an AuthToken object. 
 */
public class AuthTokenUtils {

	private static final String WIDGET_ID_NAME = "widgetId";
	private static final String VIEWER_ID_NAME = "viewerId";
	private static final String CONTEXT_ID_NAME = "contextId";
	private static final String API_KEY_HASH_NAME = "apiKey";
	private static final String EXPIRES_NAME = "expires";
	private static final String LANG_NAME = "lang";	

	/**
	 * Validate a token, and return an AuthToken object if its valid
	 * @param token
	 * @return a valid AuthToken object
	 * @throws InvalidAuthTokenException if the token is not valid
	 */
	public static AuthToken validateAuthToken(String token) throws InvalidAuthTokenException{
		AuthToken authToken = createAuthToken(extractParametersFromToken(token));
		if (authToken.isExpired()){
			throw new InvalidAuthTokenException(InvalidAuthTokenException.EXPIRED);
		} else {
			return authToken;
		}
	}
	
	/**
	 * Get a widget instance for the given token. Attempt to decrypt the
	 * token
	 * @param token
	 * @return an AuthToken, or null if the token is not valid
	 * @throws Exception 
	 */
	public static AuthToken decryptAuthToken(String token) throws InvalidAuthTokenException{
		return createAuthToken(extractParametersFromToken(token));
	}

	/*
	 * Decrypts a token and extracts the contents as a Map of Strings.
	 * @param token the token to decrpyt
	 * @return a Map of key-value pairs extracted from the token
	 * @throws Exception
	 */
	private static Map<String, String> extractParametersFromToken(String token) throws InvalidAuthTokenException{
		try {
			AuthTokenCrypter crypter = new AuthTokenCrypter();
			return crypter.unwrap(token);
		} catch (Exception e) {
			throw new InvalidAuthTokenException(InvalidAuthTokenException.INVALID_ENCRYPTION);
		}
	}

	/*
	 * Create an AuthToken from the given set of parameters
	 * @param parameters the parameters to use to construct the AuthToken
	 * @return a new AuthToken
	 * @throws Exception 
	 */
	private static AuthToken createAuthToken(Map<String, String> parameters) throws InvalidAuthTokenException{
		AuthToken authToken = new AuthToken();
		authToken.setWidgetId(parameters.get(WIDGET_ID_NAME));
		authToken.setContextId(parameters.get(CONTEXT_ID_NAME));
		authToken.setViewerId(parameters.get(VIEWER_ID_NAME));
		authToken.setLang(parameters.get(LANG_NAME));
		if (parameters.get(EXPIRES_NAME) != null){
			authToken.setExpiresAt(Long.valueOf(parameters.get(EXPIRES_NAME)));
		} else {
			authToken.setExpires();
		}
		ApiKey apiKey = getApiKeyFromHash(Integer.valueOf(parameters.get(API_KEY_HASH_NAME)));
		if (apiKey == null) throw new InvalidAuthTokenException(InvalidAuthTokenException.INVALID_CONTENT);
		authToken.setApiKey(apiKey);
		return authToken;
	}

	/*
	 * Check that the hash supplied matches an API key, and
	 * return the matching key
	 * @param hashcode
	 * @return the API key corresponding to the hashcode
	 */
	private static ApiKey getApiKeyFromHash(int hashcode){
		for (ApiKey apiKey: ApiKeys.getInstance().getKeys()){
			if (apiKey.getValue().hashCode() == hashcode) return apiKey;
		}
		return null;
	}

	/**
	 * Encrypts a token
	 * @param authToken
	 * @return the encrypted token
	 * @throws Exception 
	 */
	public static String encryptAuthToken(AuthToken authToken) throws Exception{
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(WIDGET_ID_NAME, authToken.getWidgetId());
		parameters.put(CONTEXT_ID_NAME, authToken.getContextId());
		parameters.put(VIEWER_ID_NAME, authToken.getViewerId());
		parameters.put(API_KEY_HASH_NAME, String.valueOf(authToken.getApiKey().hashCode()));
		parameters.put(LANG_NAME, authToken.getLang());
		if (authToken.getExpiresAt() != null){
			parameters.put(EXPIRES_NAME, String.valueOf(authToken.getExpiresAt()));
		}
		return encrypt(parameters);		
	}

	/*
	 * Encrypts a token with the given key
	 * @param parameters the set of parameters to encrypt
	 * @return the encrypted token
	 * @throws Exception
	 */
	private static String encrypt(Map<String, String> parameters) throws Exception{
		AuthTokenCrypter crypter = new AuthTokenCrypter();
		return crypter.wrap(parameters);
	}

}
