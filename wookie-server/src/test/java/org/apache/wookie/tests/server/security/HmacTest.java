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
package org.apache.wookie.tests.server.security;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.SignatureException;
import java.util.Calendar;
import java.util.Date;

import org.apache.wookie.server.security.Hmac;
import org.apache.wookie.tests.helpers.MockHttpServletRequest;
import org.apache.wookie.w3c.util.RandomGUID;
import org.junit.Test;

public class HmacTest {

	@Test
	public void basicSignedRequest() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		request.addParameter("nonce", new RandomGUID().toString());
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+signature);
		assertTrue(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void signedRequestWithMultipleParameters() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "one two".split(" "));
		request.addParameter("nonce", new RandomGUID().toString());
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+signature);
		assertTrue(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void basicSignedRequestWithCasing() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "WOOKIE.APACHE.org");
		request.setRequestURI("/");
		request.setMethod("PoST");
		request.addParameter("TEST", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+signature);
		assertTrue(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void signedRequestWithNoTimestamp() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+signature);
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void signedRestPostRequest() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/http://notsupported");
		request.setMethod("POST");
		request.addParameter("nonce", new RandomGUID().toString());
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", request.getRequestURI(), query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+signature);
		assertTrue(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void signedRequestTwoMinutesOld() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/http://notsupported");
		request.setMethod("POST");
		request.addParameter("nonce", new RandomGUID().toString());
		long time = Calendar.getInstance().getTimeInMillis()-120000L; // set to 2 mins ago
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", request.getRequestURI(), query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+signature);
		assertTrue(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void signedRequestOneHourOld() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		long time = System.currentTimeMillis()-3600000L; // set to one hour ago
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+signature);
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	// Set authz header with signature but no key
	@Test
	public void signedRequestWithNoApiKey() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		long time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", signature);
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	// Set authz header with extra info
	@Test
	public void signedRequestWithExtraInfo() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		long time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+signature+" EXTRASTUFF");
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	// Set authz header with key but no signaure	
	@Test
	public void unsignedRequestWithApiKey() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		long time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		request.addHeader("Authorization", "TEST");
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	// Set authz header with key but no signaure
	@Test
	public void unsignedRequestEmptyHeader() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		long time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		request.addHeader("Authorization", "");
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	// Wrong secret for key used to sign request
	@Test
	public void signedRequestSignedWithBadSecret() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		long time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "wrongkey");
		request.addHeader("Authorization", "TEST "+ signature);
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void signedRequestSignedWithBadSignature() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		long time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+ signature+"9");
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void signedRequestSignedWithBadApiKey() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		long time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "BANANA "+ signature);
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void signedRequestWithBadTimeStamp() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		request.addParameter("timestamp", "99999999999999");
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+ signature);
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void unsignedRequest() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", new RandomGUID().toString());
		long time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void signedRequestWithNoNonce() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		long time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+ signature);
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
	@Test
	public void signedRequestWithReusedNonce() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", "banana");
		long time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+ signature);
		assertTrue(Hmac.isValidSignedRequest(request));
		
		request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("nonce", "banana");
		time = System.currentTimeMillis();
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(time)));
		query = Hmac.getCanonicalParameters(request.getParameterMap());
		reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+ signature);
		assertFalse(Hmac.isValidSignedRequest(request));
	}

	@Test
	public void signedRequestEmptyNonce() throws SignatureException{
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Host", "wookie.apache.org");
		request.setRequestURI("/");
		request.setMethod("POST");
		request.addParameter("test", "test-value");
		request.addParameter("timestamp", Hmac.getFormattedDate(new Date(System.currentTimeMillis())));
		request.addParameter("nonce", "  ");
		String query = Hmac.getCanonicalParameters(request.getParameterMap());
		String reqString = Hmac.getCanonicalRequest("POST", "wookie.apache.org", "/", query);
		String signature = Hmac.getHmac(reqString, "test@127.0.0.1");
		request.addHeader("Authorization", "TEST "+signature);
		assertFalse(Hmac.isValidSignedRequest(request));
	}
	
}
