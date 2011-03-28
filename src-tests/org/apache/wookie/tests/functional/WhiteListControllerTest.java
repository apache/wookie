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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

public class WhiteListControllerTest extends AbstractControllerTest {
	
	private static final String WHITELIST_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"whitelist";

	@Test
	public void getEntriesUnauthorized(){
		try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(WHITELIST_SERVICE_URL_VALID);
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(401, code);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
	}
	
	@Test
	public void getEntries(){
		try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(WHITELIST_SERVICE_URL_VALID);
			setAuthenticationCredentials(client);
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200, code);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
	}
	
	@Test
	public void addEntry(){
		try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(WHITELIST_SERVICE_URL_VALID);
			setAuthenticationCredentials(client);
	        post.setParameter("url", "http://test.apache.org");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(201, code);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }	
		try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(WHITELIST_SERVICE_URL_VALID);
			setAuthenticationCredentials(client);
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200, code);
	        assertTrue(get.getResponseBodyAsString().contains("http://test.apache.org"));
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
	}
	
	@Test
	public void removeEntry(){
		String id = null;
		try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(WHITELIST_SERVICE_URL_VALID);
			setAuthenticationCredentials(client);
	        get.setRequestHeader("Content-type", "application/json");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200, code);
	        assertTrue(get.getResponseBodyAsString().contains("http://test.apache.org"));
	        // Now find the ID and delete it
	        JSONObject obj = new JSONObject(get.getResponseBodyAsString());
	        JSONArray entries = obj.getJSONArray("entries");
	        for (int i=0;i<entries.length();i++){
	        	JSONObject entry = entries.getJSONObject(i);
	        	if (entry.getString("url").equals("http://test.apache.org")) id = entry.getString("id");
	        }
	        }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
		try {
	        HttpClient client = new HttpClient();
	        DeleteMethod del = new DeleteMethod(WHITELIST_SERVICE_URL_VALID+"/"+id);
			setAuthenticationCredentials(client);
	        client.executeMethod(del);
	        int code = del.getStatusCode();
	        assertEquals(200, code);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("delete failed");
	    }
		try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(WHITELIST_SERVICE_URL_VALID);
	        get.setRequestHeader("Content-type", "application/json");
			setAuthenticationCredentials(client);
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200, code);
	        System.out.println(get.getResponseBodyAsString());
	        assertFalse(get.getResponseBodyAsString().contains("http://test.apache.org"));
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
	}
	
	@Test
	public void removeNonExistantEntry(){
		try {
	        HttpClient client = new HttpClient();
	        DeleteMethod del = new DeleteMethod(WHITELIST_SERVICE_URL_VALID+"/99999999");
			setAuthenticationCredentials(client);
	        client.executeMethod(del);
	        int code = del.getStatusCode();
	        assertEquals(404, code);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("delete failed");
	    }	
	}
	
	@Test
	public void addEntryNoUrl(){
		try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(WHITELIST_SERVICE_URL_VALID);
			setAuthenticationCredentials(client);
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(400, code);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }	
	}
	
	@Test
	public void addEntryInvalidUrl(){
		try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(WHITELIST_SERVICE_URL_VALID);
	        post.setParameter("url", "I AM NOT A URL");
			setAuthenticationCredentials(client);
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(400, code);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }	
	}
	
	@Test
	public void addDuplicateEntry(){
		try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(WHITELIST_SERVICE_URL_VALID);
			setAuthenticationCredentials(client);
	        post.setParameter("url", "http://test.apache.org");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(201, code);
	        client.executeMethod(post);
	        code = post.getStatusCode();
	        assertEquals(409, code);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }
	}

}
