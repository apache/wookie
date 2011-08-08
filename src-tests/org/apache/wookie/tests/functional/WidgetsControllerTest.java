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

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Test;

/**
 * Test cases for the Widget REST API
 */
public class WidgetsControllerTest extends AbstractControllerTest {
	
  /**
   * Test GET all widgets
   * @throws IOException 
   * @throws HttpException 
   */
	@Test
	public void getAllWidgets() throws HttpException, IOException{
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID);
	        get.setQueryString("all=true");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        assertTrue(response.contains("<widget id=\"1\" identifier=\"http://notsupported\""));
	        get.releaseConnection();
	}
	
	/**
	 * Test we can GET a widget using its internal ID as a resource path
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@Test
	public void getSpecificWidget() throws HttpException, IOException{
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/1");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        assertTrue(response.contains("<widget id=\"1\" identifier=\"http://notsupported\""));
	        get.releaseConnection();
	}
	
	/**
	 * Test that a request for a non-existing widget ID gets a 404
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@Test
	public void getSpecificWidget_nonexisting() throws HttpException, IOException{
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/9999");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(404,code);
	        get.releaseConnection();
	}
	
	/**
	 * Tests we get GET widgets by service type
	 * NOTE that the Services functionality is deprecated and may be removed in a future release
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@Test
	public void getWidgetType() throws HttpException, IOException{
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/unsupported");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        assertTrue(response.contains("<widget id=\"1\" identifier=\"http://notsupported\""));
	        get.releaseConnection();
	}

	 /**
   * Tests we get GET widgets by service type when it is empty; we expect a valid response, just no actual widgets
   * NOTE that the Services functionality is deprecated and may be removed in a future release
   * @throws IOException 
   * @throws HttpException 
   */
	@Test
	public void getWidgetType_empty() throws HttpException, IOException{
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/games");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        assertFalse(response.contains("<widget "));
	        get.releaseConnection();
	}
	
	/**
	 * Tests a GET which matches neither an existing widget id or existing service type gives us a 404
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@Test
	public void getWidgetType_noneexistant() throws HttpException, IOException{
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/nosuchtype");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(404,code);
	        get.releaseConnection();
	}
	
}
