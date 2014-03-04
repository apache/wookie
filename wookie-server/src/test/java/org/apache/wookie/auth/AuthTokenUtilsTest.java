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

import static org.junit.Assert.*;

import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.auth.AuthTokenUtils;
import org.apache.wookie.exceptions.ResourceDuplicationException;
import org.apache.wookie.exceptions.ResourceNotFoundException;
import org.apache.wookie.server.security.ApiKey;
import org.apache.wookie.server.security.ApiKeys;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests creating, parsing and validating auth tokens
 */
public class AuthTokenUtilsTest{

	@BeforeClass
	public static void setup() throws ResourceDuplicationException{
		ApiKeys.getInstance().addKey("ENC_TEST", "me@nowhere.com");
	}

	@Test
	public void encrypt() throws Exception{
		ApiKey apiKey = new ApiKey();
		apiKey.setValue("ENC_TEST");
		apiKey.setSecret("me@nowhere.com");

		AuthToken authToken = AuthToken.STANDARD_LIFESPAN_TOKEN();
		authToken.setApiKey(apiKey);
		authToken.setContextId("shared");
		authToken.setViewerId("user1");
		authToken.setWidgetId("http://apache.org/widgets/test");
		authToken.setLang("en");

		String token = AuthTokenUtils.encryptAuthToken(authToken);
		
		AuthToken decryptedToken = AuthTokenUtils.decryptAuthToken(token);

		assertEquals("shared", decryptedToken.getContextId());
		assertEquals("user1", decryptedToken.getViewerId());
		assertEquals("http://apache.org/widgets/test", decryptedToken.getWidgetId());
		assertEquals("ENC_TEST", decryptedToken.getApiKey());
		assertFalse(decryptedToken.isExpired());
	}
	
	@Test
	public void validate() throws Exception{
		ApiKey apiKey = new ApiKey();
		apiKey.setValue("ENC_TEST");
		apiKey.setSecret("me@nowhere.com");

		AuthToken authToken = AuthToken.STANDARD_LIFESPAN_TOKEN();
		authToken.setApiKey(apiKey);
		authToken.setContextId("shared");
		authToken.setViewerId("user1");
		authToken.setWidgetId("http://apache.org/widgets/test");
		authToken.setLang("en");

		String token = AuthTokenUtils.encryptAuthToken(authToken);
		
		AuthToken decryptedToken = AuthTokenUtils.validateAuthToken(token);

		assertEquals("shared", decryptedToken.getContextId());
		assertEquals("user1", decryptedToken.getViewerId());
		assertEquals("http://apache.org/widgets/test", decryptedToken.getWidgetId());
		assertEquals("ENC_TEST", decryptedToken.getApiKey());
		assertFalse(decryptedToken.isExpired());
	}
	
	@Test
	public void encryptExpiry() throws Exception{
		ApiKey apiKey = new ApiKey();
		apiKey.setValue("ENC_TEST");
		apiKey.setSecret("me@nowhere.com");

		AuthToken authToken = AuthToken.STANDARD_LIFESPAN_TOKEN();
		authToken.setApiKey(apiKey);
		authToken.setContextId("shared");
		authToken.setViewerId("user1");
		authToken.setWidgetId("http://apache.org/widgets/test");
		authToken.setLang("en");
		// Set expiry to ten minutes ago
		authToken.setExpiresAt(System.currentTimeMillis() / 1000 - 600);

		String token = AuthTokenUtils.encryptAuthToken(authToken);
		AuthToken decryptedToken = AuthTokenUtils.decryptAuthToken(token);

		assertEquals("shared", decryptedToken.getContextId());
		assertEquals("user1", decryptedToken.getViewerId());
		assertEquals("http://apache.org/widgets/test", decryptedToken.getWidgetId());
		assertEquals("ENC_TEST", decryptedToken.getApiKey());
		assertTrue(decryptedToken.isExpired());
	}
	
	
	/**
	 * Use validation method instead of decrypt with an expired token
	 * @throws Exception
	 */
	@Test
	public void encryptExpiryWithValidation() throws Exception{
		ApiKey apiKey = new ApiKey();
		apiKey.setValue("ENC_TEST");
		apiKey.setSecret("me@nowhere.com");

		AuthToken authToken = AuthToken.STANDARD_LIFESPAN_TOKEN();
		authToken.setApiKey(apiKey);
		authToken.setContextId("shared");
		authToken.setViewerId("user1");
		authToken.setWidgetId("http://apache.org/widgets/test");
		authToken.setLang("en");
		// Set expiry to ten minutes ago
		authToken.setExpiresAt(System.currentTimeMillis() / 1000 - 600);

		String token = AuthTokenUtils.encryptAuthToken(authToken);
		
		AuthToken decryptedToken = null;
		int errorCode = -1;
		try {
			decryptedToken = AuthTokenUtils.validateAuthToken(token);
		} catch (InvalidAuthTokenException e) {
			errorCode = e.getReason();
		}
		

		assertEquals(InvalidAuthTokenException.EXPIRED, errorCode);
		assertNull(decryptedToken);
	}
	
	@Test
	public void encryptExpirySkew() throws Exception{
		ApiKey apiKey = new ApiKey();
		apiKey.setValue("ENC_TEST");
		apiKey.setSecret("me@nowhere.com");

		AuthToken authToken = AuthToken.STANDARD_LIFESPAN_TOKEN();
		authToken.setApiKey(apiKey);
		authToken.setContextId("shared");
		authToken.setViewerId("user1");
		authToken.setWidgetId("http://apache.org/widgets/test");
		authToken.setLang("en");
		// Set expiry to two minutes ago; this will still be valid
		// using the default clock skew of 3 minutes.
		authToken.setExpiresAt(System.currentTimeMillis() / 1000 - 120);

		String token = AuthTokenUtils.encryptAuthToken(authToken);
		
		AuthToken decryptedToken = AuthTokenUtils.decryptAuthToken(token);

		assertEquals("shared", decryptedToken.getContextId());
		assertEquals("user1", decryptedToken.getViewerId());
		assertEquals("http://apache.org/widgets/test", decryptedToken.getWidgetId());
		assertEquals("ENC_TEST", decryptedToken.getApiKey());
		assertFalse(decryptedToken.isExpired());
	}
	
	@Test
	public void encryptBadApiKey(){
		ApiKey apiKey = new ApiKey();
		apiKey.setValue("ENC_TEST_BAD");
		apiKey.setSecret("me@nowhere.com");

		AuthToken authToken = AuthToken.STANDARD_LIFESPAN_TOKEN();
		authToken.setApiKey(apiKey);
		authToken.setContextId("shared");
		authToken.setViewerId("user1");
		authToken.setWidgetId("http://apache.org/widgets/test");
		authToken.setLang("en");

		String token = "";
		try {
			token = AuthTokenUtils.encryptAuthToken(authToken);
		} catch (Exception e) {
			fail();
		}

		int errorCode = -1;
		try {
			AuthTokenUtils.decryptAuthToken(token);
		} catch (InvalidAuthTokenException e) {
			errorCode = e.getReason();
		}
		
		assertEquals(InvalidAuthTokenException.INVALID_CONTENT, errorCode);
	}
	
	@Test
	public void singleUseToken() throws Exception{
		ApiKey apiKey = new ApiKey();
		apiKey.setValue("ENC_TEST");
		apiKey.setSecret("me@nowhere.com");
		AuthToken authToken1 = AuthToken.STANDARD_LIFESPAN_TOKEN();
		authToken1.setApiKey(apiKey);
		authToken1.setContextId("test");
		authToken1.setViewerId("viewer");
		authToken1.setWidgetId("http://apache.org/widgets/test");
		authToken1.setLang("en");
		
		AuthToken authToken2 = AuthToken.SINGLE_USE_TOKEN(authToken1);
		
		// The two token core content are identical...
		assertTrue(authToken1.toString().equals(authToken2.toString()));
		
		// but token 2 is single use
		assertTrue(authToken2.isSingleUse());
		
	}
	
	// Make sure that we always get a new nonce, and therefore a new encrypted token string
	@Test
	public void copyOfToken() throws Exception{
		ApiKey apiKey = new ApiKey();
		apiKey.setValue("ENC_TEST");
		apiKey.setSecret("me@nowhere.com");
		AuthToken authToken1 = AuthToken.STANDARD_LIFESPAN_TOKEN();
		authToken1.setApiKey(apiKey);
		authToken1.setContextId("test");
		authToken1.setViewerId("viewer");
		authToken1.setWidgetId("http://apache.org/widgets/test");
		authToken1.setLang("en");
		
		AuthToken authToken2 = AuthToken.STANDARD_LIFESPAN_TOKEN(authToken1);
		
		// The two token core content are identical...
		assertTrue(authToken1.toString().equals(authToken2.toString()));
		
		// ... but MUST have a different nonce...
		assertFalse(authToken1.getNonce().equals(authToken2.getNonce()));
		
		// ... and MUST results in a different string
		String token1 = AuthTokenUtils.encryptAuthToken(authToken1);
		String token2 = AuthTokenUtils.encryptAuthToken(authToken2);
		assertFalse(token1.equals(token2));
	}
	
	@Test(expected=Exception.class)
	public void encryptNullApiKey() throws Exception{
		AuthToken authToken = AuthToken.STANDARD_LIFESPAN_TOKEN();
		authToken.setApiKey(null);
		authToken.setContextId("shared");
		authToken.setViewerId("user1");
		authToken.setWidgetId("http://apache.org/widgets/test");
		String token = AuthTokenUtils.encryptAuthToken(authToken);
		AuthTokenUtils.decryptAuthToken(token);
	}
	
	@Test
	public void noEncrypt(){
		String token = "not a real token";
		int errorCode = -1;
		try {
			AuthTokenUtils.decryptAuthToken(token);
		} catch (InvalidAuthTokenException e) {
			errorCode = e.getReason();
		}
		assertEquals(InvalidAuthTokenException.INVALID_ENCRYPTION, errorCode);
	}

	@AfterClass
	public static void tearDown() throws ResourceNotFoundException{
		ApiKeys.getInstance().removeKey("ENC_TEST");
	}


}
