/**
 * 
 */
package org.tencompetence.widgetservice.tests.functional;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;


/**
 * Controller tests. Run this after the Ant script completes (give it a sec for Tomcat to restart)
 * @author scott
 *
 */
public class WidgetInstancesControllerTest extends AbstractControllerTest {
	
	public void testGetInstanceById(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
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

	public void testGetInstanceById_AlreadyExists(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
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
	
	public void testGetInstance_InvalidAPIkey(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_INVALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=test");
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
	
	public void testGetInstanceById_InvalidWidget(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_INVALID+"&userid=test&shareddatakey=test");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(404,code); // but must return the "default widget"
	        assertTrue(post.getResponseBodyAsString().contains("<title>Unsupported widget widget</title>"));
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
	        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
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
	        PutMethod post = new PutMethod(TEST_INSTANCES_SERVICE_URL_VALID);
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
	        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
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
