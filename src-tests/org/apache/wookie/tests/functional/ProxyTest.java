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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.BeforeClass;
import org.junit.Test;


public class ProxyTest extends AbstractControllerTest {

	private static String instance_id_key;
	private static final String PROXY_URL = "http://localhost:8080/wookie/proxy";
	private static final String VALID_SITE_URL = "http://incubator.apache.org/wookie/";
	private static final String INVALID_SITE_URL = "DFASFAFEQ3FQ32145235123452";
	private static final String BLOCKED_SITE_URL = "http://very.bad.place";
	private static final String PROTECTED_SITE_URL = "http://localhost:8080/wookie/admin/";

	/**
	 * Create an instance of a widget
	 */
	@BeforeClass
	public static void setup(){
		try {
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
			post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=proxytest");
			client.executeMethod(post);
			String response = post.getResponseBodyAsString();
			instance_id_key = response.substring(response.indexOf("<identifier>")+12, response.indexOf("</identifier>"));
			post.releaseConnection();
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("post failed");
		}
	}

	@Test
	public void getValidSite(){
		String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+VALID_SITE_URL;
		assertEquals(200,send(url,"GET"));
		assertEquals(500,send(url,"POST")); // This URL doesn't support POST
	}

	@Test
	public void getValidSiteInvalidIdKey(){
		String url = PROXY_URL+"?instanceid_key=TEST&url="+VALID_SITE_URL;
		assertEquals(403,send(url,"GET"));
		assertEquals(403,send(url,"POST"));
	}

	@Test
	public void getValidSiteNoIdKey(){
		String url = PROXY_URL+"?url="+VALID_SITE_URL;
		assertEquals(403,send(url,"GET"));
		assertEquals(403,send(url,"POST"));
	}

	@Test
	public void getInvalidSite(){
		String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+INVALID_SITE_URL;
		assertEquals(400,send(url,"GET"));
		assertEquals(400,send(url,"POST"));
	}

	@Test
	public void getBlockedSite(){
		String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+BLOCKED_SITE_URL;
		assertEquals(403,send(url,"GET"));
	}
	
	@Test
	public void postWithMixedQueryAndParameters() throws Exception{
		HttpClient client = new HttpClient();
		List<String> authPrefs =  new ArrayList<String>(2);
		authPrefs.add(AuthPolicy.DIGEST );
		authPrefs.add(AuthPolicy.BASIC);
		client.getParams().setParameter (AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
		// send the basic authentication response even before the server gives an unauthorized response
		client.getParams().setAuthenticationPreemptive(true);
		client.getState().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
				new UsernamePasswordCredentials("java", "java"));
		PostMethod req;
		req = new PostMethod(PROXY_URL+"?instanceid_key="+instance_id_key);
		req.addParameter("url", PROTECTED_SITE_URL);
		client.executeMethod(req);
		int code = req.getStatusCode();
		req.releaseConnection();
		assertEquals(200,code);
	}

	@Test
	public void postWithOnlyParameters() throws Exception{
		HttpClient client = new HttpClient();
		List<String> authPrefs =  new ArrayList<String>(2);
		authPrefs.add(AuthPolicy.DIGEST );
		authPrefs.add(AuthPolicy.BASIC);
		client.getParams().setParameter (AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
		// send the basic authentication response even before the server gives an unauthorized response
		client.getParams().setAuthenticationPreemptive(true);
		client.getState().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
				new UsernamePasswordCredentials("java", "java"));
		PostMethod req;
		req = new PostMethod(PROXY_URL);
		req.addParameter("url", PROTECTED_SITE_URL);
		req.addParameter("instanceid_key", instance_id_key);
		client.executeMethod(req);
		int code = req.getStatusCode();
		req.releaseConnection();
		assertEquals(200,code);
	}

	@Test
	public void getProtectedSiteWithoutAuth() throws Exception{
		HttpClient client = new HttpClient();
		HttpMethod req;
		req = new GetMethod(PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+PROTECTED_SITE_URL);
		client.executeMethod(req);
		int code = req.getStatusCode();
		req.releaseConnection();
		assertEquals(401,code);
	}
	
	@Test
	public void getProtectedSiteWithBasicAuth() throws Exception{
		HttpClient client = new HttpClient();
		List<String> authPrefs =  new ArrayList<String>(2);
		authPrefs.add(AuthPolicy.DIGEST );
		authPrefs.add(AuthPolicy.BASIC);
		client.getParams().setParameter (AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
		// send the basic authentication response even before the server gives an unauthorized response
		client.getParams().setAuthenticationPreemptive(true);
		client.getState().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
				new UsernamePasswordCredentials("java", "java"));		
		HttpMethod req;
		req = new GetMethod(PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+PROTECTED_SITE_URL);
		client.executeMethod(req);
		int code = req.getStatusCode();
		req.releaseConnection();
		assertEquals(200,code);
	}

	private int send(String url, String method){
		try {
			HttpClient client = new HttpClient();
			HttpMethod req;
			if (method.equals("POST")){
				req = new PostMethod(url);
			} else {
				req = new GetMethod(url);
			}
			client.executeMethod(req);
			int code = req.getStatusCode();
			req.releaseConnection();
			return code;
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("post failed");
			return -1;
		}
	}

}
