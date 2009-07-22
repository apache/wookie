/**
 * 
 */
package org.apache.wookie.tests.functional;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.junit.Before;


/**
 * Controller tests. Run this after the Ant script completes (give it a sec for Tomcat to restart)
 * @author scott
 *
 */
public class ParticipantsControllerTest extends AbstractControllerTest {	
	
	@Before public void setUp() throws Exception {
		super.setUp();
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

	public void testAddParticipant(){
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
	}
	
	public void testAddParticipant_AlreadyExists(){
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
	
	public void testAddParticipant_InvalidAPIkey(){
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
	
	public void testAddParticipant_InvalidParticipant(){
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
	
	public void testAddParticipant_InvalidWidget(){
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
	
	public void testDeleteParticipant(){
		fail("test not implemented");
	}
	public void testDeleteParticipant_InvalidAPIKey(){
		fail("test not implemented");
	}
	public void testDeleteParticipant_InvalidParticipant(){
		fail("test not implemented");
	}
	public void testDeleteParticipant_InvalidInstance(){
		fail("test not implemented");
	}

}
