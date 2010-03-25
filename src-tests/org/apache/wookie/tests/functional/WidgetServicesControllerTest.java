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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.junit.Test;

public class WidgetServicesControllerTest extends AbstractControllerTest {

	@Test
	public void addService(){
		 try {
		        HttpClient client = new HttpClient();
		        setAuthenticationCredentials(client);
		        PostMethod post = new PostMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test");
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
		    try {
		        HttpClient client = new HttpClient();
		        GetMethod get = new GetMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test");
		        client.executeMethod(get);
		        int code = get.getStatusCode();
		        assertEquals(200,code);
		        String response = get.getResponseBodyAsString();
		        assertTrue(response.contains("<service name=\"test\">"));
		        get.releaseConnection();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    	fail("get failed");
		    }
	}
	
	@Test
	public void modifyService(){
		 try {
		        HttpClient client = new HttpClient();
		        setAuthenticationCredentials(client);
		        PutMethod put = new PutMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test");
		        put.setQueryString("name=test2");
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
		    // Now lets GET it to make sure it was added OK
		    try {
		        HttpClient client = new HttpClient();
		        GetMethod get = new GetMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test2");
		        client.executeMethod(get);
		        int code = get.getStatusCode();
		        assertEquals(200,code);
		        String response = get.getResponseBodyAsString();
		        assertTrue(response.contains("<service name=\"test2\">"));
		        get.releaseConnection();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    	fail("get failed");
		    }
		
	}
	

	@Test
	public void deleteService(){
		 try {
		        HttpClient client = new HttpClient();
		        setAuthenticationCredentials(client);
		        DeleteMethod del = new DeleteMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test2");
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
		    // Now lets GET it to make sure it was deleted OK
		    try {
		        HttpClient client = new HttpClient();
		        GetMethod get = new GetMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test2");
		        client.executeMethod(get);
		        int code = get.getStatusCode();
		        assertEquals(404,code);
		        get.releaseConnection();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    	fail("get failed");
		    }
	}
	
	@Test
	public void addServiceNoAuth(){
		 try {
		        HttpClient client = new HttpClient();
		        PostMethod post = new PostMethod(TEST_SERVICES_SERVICE_URL_VALID+"/noauth");
		        client.executeMethod(post);
		        int code = post.getStatusCode();
		        assertEquals(401,code);
		        post.releaseConnection();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    	fail("post failed");
		    }
	}
	
	@Test
	public void modifyServiceNoAuth(){
		 try {
		        HttpClient client = new HttpClient();
		        PutMethod put = new PutMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test");
		        put.setQueryString("name=test2");
		        client.executeMethod(put);
		        int code = put.getStatusCode();
		        assertEquals(401,code);
		        put.releaseConnection();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    	fail("put failed");
		    }
	}
	
	@Test
	public void deleteServiceNoAuth(){
		 try {
		        HttpClient client = new HttpClient();
		        DeleteMethod del = new DeleteMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test2");
		        del.setQueryString("name=test2");
		        client.executeMethod(del);
		        int code = del.getStatusCode();
		        assertEquals(401,code);
		        del.releaseConnection();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    	fail("put failed");
		    }
	}
	
	@Test
	public void addServiceAlreadyExists(){
		 try {
		        HttpClient client = new HttpClient();
		        setAuthenticationCredentials(client);
		        PostMethod post = new PostMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test_add2");
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
			try {
			        HttpClient client = new HttpClient();
			        setAuthenticationCredentials(client);
			        PostMethod post = new PostMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test_add2");
			        post.setDoAuthentication(true);
			        client.executeMethod(post);
			        int code = post.getStatusCode();
			        assertEquals(409,code);
			        post.releaseConnection();
			    }
			    catch (Exception e) {
			    	e.printStackTrace();
			    	fail("post failed");
			    }
			    // clean up
				 try {
				        HttpClient client = new HttpClient();
				        setAuthenticationCredentials(client);
				        DeleteMethod del = new DeleteMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test_add2");
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
	}
	
	@Test
	public void modifyServiceNonExists(){
		 try {
		        HttpClient client = new HttpClient();
		        setAuthenticationCredentials(client);
		        PutMethod put = new PutMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test999");
		        put.setQueryString("name=test2");
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
	public void deleteServiceNonExists(){
		 try {
		        HttpClient client = new HttpClient();
		        setAuthenticationCredentials(client);
		        DeleteMethod del = new DeleteMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test999");
		        del.setDoAuthentication(true);
		        client.executeMethod(del);
		        int code = del.getStatusCode();
		        assertEquals(404,code);
		        del.releaseConnection();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    	fail("put failed");
		    }
	}
	
	@Test
	public void modifyServiceNoName(){
		 try {
		        HttpClient client = new HttpClient();
		        setAuthenticationCredentials(client);
		        PutMethod put = new PutMethod(TEST_SERVICES_SERVICE_URL_VALID+"/test999");
		        put.setDoAuthentication(true);
		        client.executeMethod(put);
		        int code = put.getStatusCode();
		        assertEquals(400,code);
		        put.releaseConnection();
		    }
		    catch (Exception e) {
		    	e.printStackTrace();
		    	fail("put failed");
		    }	
	}

}
