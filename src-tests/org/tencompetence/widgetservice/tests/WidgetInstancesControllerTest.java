/**
 * 
 */
package org.tencompetence.widgetservice.tests;

import junit.framework.TestCase;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;


/**
 * Controller tests. Run this after the Ant script completes (give it a sec for Tomcat to restart)
 * @author scott
 *
 */
public class WidgetInstancesControllerTest extends TestCase {
	
	private static final String TEST_SERVICE_URL_VALID = "http://localhost:8080/wookie/widgetinstances";
	private static final String TEST_PROPERTIES_SERVICE_URL_VALID = "http://localhost:8080/wookie/properties";
	
	private static final String API_KEY_VALID = "test";
	private static final String API_KEY_INVALID = "rubbish";
	private static final String WIDGET_ID_VALID = "http://www.getwookie.org/widgets/natter";
	
	public void testGetInstanceInvalidAPIkey(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_INVALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=test");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(403,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }		
	}
	
	public void testCreateInstanceById(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=test");
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

	public void testGetExistingInstanceById(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=test");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(200,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("post failed");
	    }		
	}
	
	public void testStop(){
		fail("test not written");
	}
	
	public void testResume(){
		fail("test not written");
	}
	
	public void testCloneSharedData(){
		// Create an instance
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=clonetestsrc");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(201,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("create instance failed");
	    }
		
		// Set some shared data
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&is_public=true&shareddatakey=clonetestsrc&propertyname=cat&propertyvalue=garfield");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(201,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("set shared data failed");
	    }	    
		
		// Clone it
	    try {
	        HttpClient client = new HttpClient();
	        PutMethod post = new PutMethod(TEST_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=clonetestsrc&requestid=clone&cloneshareddatakey=clonetestsync");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(200, code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("clone shared data failed");
	    }	      
		
		// Check it
		// Create an instance for the clone
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=clonetestsync");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(201,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("create instance failed");
	    }
	    // Get the data for the clone
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod post = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=clonetestsync&propertyname=cat");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(200, code);
	        String resp = post.getResponseBodyAsString();
	        assertEquals("garfield",resp);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("create instance failed");
	    }		
	}

}
