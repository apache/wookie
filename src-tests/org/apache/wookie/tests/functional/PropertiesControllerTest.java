package org.apache.wookie.tests.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.junit.BeforeClass;
import org.junit.Test;

public class PropertiesControllerTest extends AbstractControllerTest {
	
	@BeforeClass
	public static void setup(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=propstest");
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
	
	@Test
	public void setPreference(){
		// Set some shared data
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&is_public=false&shareddatakey=propstest&propertyname=pass&propertyvalue=pass");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(201,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("set shared data failed");
	    }	 
	    
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        get.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=propstest&propertyname=pass");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200, code);
	        String resp = get.getResponseBodyAsString();
	        assertEquals("pass",resp);
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("get preference failed");
	    }		
	}
	
	@Test
	public void setSharedData(){
		// Set some shared data
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&is_public=true&shareddatakey=propstest&propertyname=cat&propertyvalue=garfield");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(201,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("set shared data failed");
	    }	 
	    
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        get.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=propstest&propertyname=cat");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200, code);
	        String resp = get.getResponseBodyAsString();
	        assertEquals("garfield",resp);
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("get property failed");
	    }	
	}
	
	@Test
	public void updateProperty(){
	    try {
	        HttpClient client = new HttpClient();
	        PutMethod put = new PutMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        put.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&is_public=true&shareddatakey=propstest&propertyname=cat&propertyvalue=felix");
	        client.executeMethod(put);
	        int code = put.getStatusCode();
	        assertEquals(200,code);
	        put.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("set shared data failed");
	    }	 
	    
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        get.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=propstest&propertyname=cat");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200, code);
	        String resp = get.getResponseBodyAsString();
	        assertEquals("felix",resp);
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("get property failed");
	    }	
		
	}
	
	@Test
	public void removeProperty(){
	    try {
	        HttpClient client = new HttpClient();
	        DeleteMethod delete = new DeleteMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        delete.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&is_public=true&shareddatakey=propstest&propertyname=cat");
	        client.executeMethod(delete);
	        int code = delete.getStatusCode();
	        assertEquals(200,code);
	        delete.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("delete failed");
	    }			
	}
	
	@Test
	public void removePropertyNonExisting(){
	    try {
	        HttpClient client = new HttpClient();
	        DeleteMethod delete = new DeleteMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        delete.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&is_public=true&shareddatakey=propstest&propertyname=bogus");
	        client.executeMethod(delete);
	        int code = delete.getStatusCode();
	        assertEquals(404,code);
	        delete.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("delete failed");
	    }			
	}
	
	@Test
	public void setPropertyNoName(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&is_public=true&shareddatakey=propstest&propertyvalue=garfield");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(400,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("set shared data failed");
	    }	
	}
	
	@Test
	public void setPropertyEmptyName(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&is_public=true&shareddatakey=propstest&propertyname=&propertyvalue=garfield");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(400,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("set shared data failed");
	    }	
	}
	
	@Test
	public void setPropertyWhitespaceName(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&is_public=true&shareddatakey=propstest&propertyname=%20%20&propertyvalue=garfield");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(400,code);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("set shared data failed");
	    }	
	}
	
	@Test
	public void getPropertyInvalidAPIKey(){
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        get.setQueryString("api_key="+API_KEY_INVALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=propstest&propertyname=cat");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(401, code);
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("get property failed");
	    }	
	}
	
	@Test
	public void getPropertyInvalidName(){
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
	        get.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=propstest&propertyname=madeupname");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(404, code);
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	fail("get property failed");
	    }	
	}

}
