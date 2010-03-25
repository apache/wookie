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
package org.apache.wookie.tests.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.jdom.DataConversionException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

public class WidgetAccessRequestPolicyControllerTest extends AbstractControllerTest {

	protected static final String TEST_WARP_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"warp";

	private static String id; // policy id

	@BeforeClass
	public static void setup(){
		try {
			HttpClient client = new HttpClient();
			setAuthenticationCredentials(client);
			PostMethod post = new PostMethod(TEST_WARP_SERVICE_URL_VALID);
			post.addParameter("widgetId", "1");
			post.addParameter("subdomains", "true");
			post.addParameter("origin", "http://www.9128.org");
			post.setDoAuthentication(true);
			client.executeMethod(post);
			int code = post.getStatusCode();
			assertEquals(201,code);
			post.releaseConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("post failed");
		}
		// Now lets GET it to make sure it was added OK
		Element[] policies = getPolicies();
		for (Element policy:policies){
			if (policy.getAttribute("origin").getValue().equals("http://www.9128.org")){
				id = policy.getAttributeValue("id");
			}
		}
	}

	@Test
	public void addPolicy(){
		// To test adding policies works, we just need to check that the pre-test created one OK
		assertTrue(id != null);
	}

	@Test
	public void grantPolicy(){	
		try {
			HttpClient client = new HttpClient();
			setAuthenticationCredentials(client);
			PutMethod put = new PutMethod(TEST_WARP_SERVICE_URL_VALID+"/"+id+"?granted=true");
			put.setDoAuthentication(true);
			client.executeMethod(put);
			int code = put.getStatusCode();
			assertEquals(200,code);
			put.releaseConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("put failed");
		}
		// Now lets GET it to make sure it was modded OK
		Element[] policies = getPolicies();
		for (Element policy:policies){
			if (policy.getAttribute("origin").getValue().equals("http://www.9128.org")){
				try {
					assertTrue(policy.getAttribute("granted").getBooleanValue());
				} catch (DataConversionException e) {
					e.printStackTrace();
					fail("bad return value for granted");
				}
			}
		}
	}

	@Test
	public void revokePolicy(){
		try {
			HttpClient client = new HttpClient();
			setAuthenticationCredentials(client);
			PutMethod put = new PutMethod(TEST_WARP_SERVICE_URL_VALID+"/"+id+"?granted=false");
			put.setDoAuthentication(true);
			client.executeMethod(put);
			int code = put.getStatusCode();
			assertEquals(200,code);
			put.releaseConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("put failed");
		}
		// Now lets GET it to make sure it was modded OK
		Element[] policies = getPolicies();
		for (Element policy:policies){
			if (policy.getAttribute("origin").getValue().equals("http://www.9128.org")){
				try {
					assertFalse(policy.getAttribute("granted").getBooleanValue());
				} catch (DataConversionException e) {
					e.printStackTrace();
					fail("bad return value for granted");
				}
			}
		}
	}

	@Test
	public void deletePolicy(){
		try {
			HttpClient client = new HttpClient();
			setAuthenticationCredentials(client);
			DeleteMethod del = new DeleteMethod(TEST_WARP_SERVICE_URL_VALID+"/"+id);
			del.setDoAuthentication(true);
			client.executeMethod(del);
			int code = del.getStatusCode();
			assertEquals(200,code);
			del.releaseConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("put failed");
		}
		// Now lets make sure it was deleted OK
		Element[] policies = getPolicies();
		for (Element policy:policies){
			if (policy.getAttribute("id").getValue().equals(id)){
				fail("Policy was not deleted");
			}
		}
	}
	
	@Test
	public void testGrantNonExistingPolicy(){
		try {
			HttpClient client = new HttpClient();
			setAuthenticationCredentials(client);
			PutMethod put = new PutMethod(TEST_WARP_SERVICE_URL_VALID+"/9999?granted=true");
			put.setDoAuthentication(true);
			client.executeMethod(put);
			int code = put.getStatusCode();
			assertEquals(404,code);
			put.releaseConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("put failed");
		}
	}
	
	@Test
	public void testGetWithoutAuthentication(){
		try {
			HttpClient client = new HttpClient();
			GetMethod get = new GetMethod(TEST_WARP_SERVICE_URL_VALID);
			get.addRequestHeader("content-type", "text/xml");
			client.executeMethod(get);
			int code = get.getStatusCode();
			assertEquals(401,code);
			get.releaseConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("get failed");
		}
	}

	//// Helpers
	
	private static Element[] getPolicies(){
		try {
			HttpClient client = new HttpClient();
			setAuthenticationCredentials(client);
			GetMethod get = new GetMethod(TEST_WARP_SERVICE_URL_VALID);
			get.setDoAuthentication(true);
			get.addRequestHeader("content-type", "text/xml");
			client.executeMethod(get);
			int code = get.getStatusCode();
			assertEquals(200,code);
			InputStream stream = get.getResponseBodyAsStream();
			Element[] response = getPolicies(stream);
			get.releaseConnection();
			return response;
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("get failed");
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static Element[] getPolicies(InputStream response){
		try {
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(response);
			List policies = doc.getRootElement().getChildren("policy");
			if (policies != null){
				return (Element[])policies.toArray(new Element[policies.size()]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Bad XML returned by server");
		} 
		return null;
	}

}
