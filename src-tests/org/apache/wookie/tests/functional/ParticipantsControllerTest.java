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

import static org.junit.Assert.*;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Controller tests. Run this after the Ant script completes (give it a sec for Tomcat to restart)
 * @author scott
 *
 */
public class ParticipantsControllerTest extends AbstractControllerTest {	
	
	@BeforeClass
	public static void setUp() throws Exception {

		// Create an instance
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstest");
	        client.executeMethod(post);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }
	}

	@Test
	public void addParticipant(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstest&participant_id=1&participant_display_name=bob&participant_thumbnail_url=http://www.test.org");
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
	        GetMethod get = new GetMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        get.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstest");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        System.out.println(response);
	        assertTrue(response.contains("<participant id=\"1\" display_name=\"bob\" thumbnail_url=\"http://www.test.org\" />"));
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
	}
	
	@Test
	public void addParticipant_AlreadyExists(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstest&participant_id=1&participant_display_name=bob&participants_thumbnail_url=http://www.test.org");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(200,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }
	}
	
	@Test
	public void addParticipant_InvalidAPIkey(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_INVALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstest");
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
	public void addParticipant_InvalidParticipant(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstest&participant_id=&participant_display_name=bob&participants_thumbnail_url=http://www.test.org");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(400,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }
	}
	
	@Test
	public void addParticipant_InvalidWidget(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_INVALID+"&userid=test&shareddatakey=participantstest&participant_id=1&participant_display_name=bob&participants_thumbnail_url=http://www.test.org");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(400,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }
	}
	
	@Test
	public void deleteParticipant(){
	    try {
	        HttpClient client = new HttpClient();
	        DeleteMethod post = new DeleteMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstest&participant_id=1");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(200,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("delete failed");
	    }
	    // Now lets GET it to make sure it was deleted
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        get.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstest");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        System.out.println(response);
	        assertFalse(response.contains("<participant id=\"1\" display_name=\"bob\" thumbnail_url=\"http://www.test.org\" />"));
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
	}
	
	@Test
	public void deleteParticipant_InvalidAPIKey(){
	    try {
	        HttpClient client = new HttpClient();
	        DeleteMethod post = new DeleteMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_INVALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstest");
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
	public void deleteParticipant_InvalidParticipant(){
	    try {
	        HttpClient client = new HttpClient();
	        DeleteMethod post = new DeleteMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstest&participant_id=99");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(404,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }	
	}
	
	@Test
	public void deleteParticipant_InvalidInstance(){
	    try {
	        HttpClient client = new HttpClient();
	        DeleteMethod post = new DeleteMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=participantstestinvalidkey&participant_id=1");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(400,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }	
	}

}
