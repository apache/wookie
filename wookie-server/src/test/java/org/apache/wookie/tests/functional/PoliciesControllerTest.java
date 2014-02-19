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

package org.apache.wookie.tests.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.wookie.tests.helpers.Request;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for policies API
 */
public class PoliciesControllerTest extends AbstractControllerTest {

	private static String POLICY_TEST_SCOPE = "http://policies.test.scope";
	private static String POLICY_TEST_INVALID_SCOPE = "http://no.such.scope";
	private static String POLICY_TEST_ORIGIN = "http://policies.test.origin";
	private static String POLICY_TEST_ORIGIN_2 = "http://policies.test2.origin";

	private static String POLICY_TEST_VALID = POLICY_TEST_SCOPE + " " + POLICY_TEST_ORIGIN + " " + "ALLOW";
	private static String POLICY_TEST_VALID_2 = POLICY_TEST_SCOPE + " " + POLICY_TEST_ORIGIN_2 + " " + "ALLOW";
	private static String POLICY_TEST_INVALID = POLICY_TEST_INVALID_SCOPE + " " + POLICY_TEST_ORIGIN + " " + "ALLOW";


	@BeforeClass
	public static void setup() throws HttpException, IOException{
		Request post = new Request("POST", TEST_POLICIES_SERVICE_URL_VALID);
		StringRequestEntity entity = new StringRequestEntity(POLICY_TEST_VALID, "text/plain", "UTF-8");
		post.setRequestEntity(entity);
		post.execute(true, false);
		assertEquals(201, post.getStatusCode());
	}

	@AfterClass
	public static void tearDown() throws HttpException, IOException{
		Request delete = new Request("DELETE", TEST_POLICIES_SERVICE_URL_VALID + "/" + encodeString(POLICY_TEST_VALID_2));
		delete.execute(true, false);
		assertEquals(200, delete.getStatusCode());
	}

	@Test
	public void getPolicies() throws HttpException, IOException{    
		Request get = new Request("GET", TEST_POLICIES_SERVICE_URL_VALID);
		get.setAccepts("text/xml");
		get.execute(true, false);
		try {
			Element policies = processPolicies(get.getResponseBodyAsStream());
			assertEquals("policies", policies.getName());
		} catch (JDOMException e) {
			fail("Response did not contain any policies");
		}
	}

	@Test
	public void getPoliciesUnauthorized() throws HttpException, IOException{
		Request get = new Request("GET", TEST_POLICIES_SERVICE_URL_VALID);
		get.setAccepts("text/xml");
		get.execute(false, false);
		assertEquals(403, get.getStatusCode());
	}

	@Test
	public void getPoliciesWithScope() throws HttpException, IOException{
		//
		// We should have a policy setup from above
		//
		Request get = new Request("GET", TEST_POLICIES_SERVICE_URL_VALID + "/" + POLICY_TEST_SCOPE);
		get.setAccepts("text/xml");
		get.execute(true, false);
		try {
			Element policies = processPolicies(get.getResponseBodyAsStream());
			assertEquals("policies", policies.getName());
			assertEquals(1, policies.getChildren("policy").size());
		} catch (JDOMException e) {
			fail("Response did not contain any policies");
		}
	}

	@Test
	public void getPoliciesWithNonexistingScope() throws HttpException, IOException{  
		Request get = new Request("GET", TEST_POLICIES_SERVICE_URL_VALID + "/nosuchscope");
		get.setAccepts("text/xml");
		get.execute(true, false);

		try {
			Element policies = processPolicies(get.getResponseBodyAsStream());
			assertEquals("policies", policies.getName());
			assertEquals(0, policies.getChildren("policy").size());
		} catch (JDOMException e) {
			fail("Response did not contain any policies");
		}
	}

	@Test
	public void createPolicyUnauthorized() throws HttpException, IOException{
		Request post = new Request("POST", TEST_POLICIES_SERVICE_URL_VALID);
		post.setAccepts("text/xml");
		StringRequestEntity entity = new StringRequestEntity("http://dodgyplace.org * ALLOW", "text/plain", "UTF-8");
		post.setRequestEntity(entity);
		post.execute(false, false);
		assertEquals(403,post.getStatusCode());
	}

	@Test
	public void createPolicyInvalidDirective() throws HttpException, IOException{
		Request post = new Request("POST", TEST_POLICIES_SERVICE_URL_VALID);
		post.setAccepts("text/xml");
		StringRequestEntity entity = new StringRequestEntity("http://dodgyplace.org * MAYBE", "text/plain", "UTF-8");
		post.setRequestEntity(entity);
		post.execute(true, false);
		assertEquals(400,post.getStatusCode());    
	}

	@Test
	public void createPolicyInvalidScope() throws HttpException, IOException{
		Request post = new Request("POST", TEST_POLICIES_SERVICE_URL_VALID);
		post.setAccepts("text/xml");
		StringRequestEntity entity = new StringRequestEntity("FAIL * DENY", "text/plain", "UTF-8");
		post.setRequestEntity(entity);
		post.execute(true, false);
		assertEquals(400,post.getStatusCode());
	}

	@Test
	public void createPolicyInvalidOrigin() throws HttpException, IOException{
		Request post = new Request("POST", TEST_POLICIES_SERVICE_URL_VALID);
		post.setAccepts("text/xml");
		StringRequestEntity entity = new StringRequestEntity("http://dodgyplace.org FAIL DENY", "text/plain", "UTF-8");
		post.setRequestEntity(entity);
		post.execute(true, false);
		assertEquals(400,post.getStatusCode());
	}

	@Test
	public void createPolicy() throws HttpException, IOException{
		Request post = new Request("POST", TEST_POLICIES_SERVICE_URL_VALID);
		StringRequestEntity entity = new StringRequestEntity(POLICY_TEST_VALID_2, "text/plain", "UTF-8");
		post.setRequestEntity(entity);
		post.execute(true, false);
		assertEquals(201,post.getStatusCode());
	}

	@Test
	public void deletePolicyUnauthorized() throws HttpException, IOException{
		Request delete = new Request("DELETE", TEST_POLICIES_SERVICE_URL_VALID + "/" + encodeString(POLICY_TEST_VALID));
		delete.execute(false, false);
		assertEquals(403, delete.getStatusCode());   
	}

	@Test
	public void deletePolicyNonexistant() throws HttpException, IOException{
		Request delete = new Request("DELETE", TEST_POLICIES_SERVICE_URL_VALID + "/" + encodeString(POLICY_TEST_INVALID));
		delete.execute(true, false);
		assertEquals(404, delete.getStatusCode());
	}

	@Test
	public void deletePolicy() throws HttpException, IOException{
		Request delete = new Request("DELETE", TEST_POLICIES_SERVICE_URL_VALID + "/" + encodeString(POLICY_TEST_VALID));
		delete.execute(true, false);
		assertEquals(200, delete.getStatusCode());
	}

	public static Element processPolicies(InputStream in) throws JDOMException, IOException{
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(in);
		Element policies = doc.getRootElement();
		return policies;
	}

}
