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
import org.junit.Test;

/**
 * @author scott
 *
 */
public class WidgetsControllerTest extends AbstractControllerTest {
	
	@Test
	public void getAllWidgets(){
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID);
	        get.setQueryString("all=true");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        assertTrue(response.contains("<widget id=\"1\" identifier=\"http://www.tencompetence.org/widgets/default/notsupported\" version=\"v1.0\">"));
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
	}
	
	@Test
	public void getSpecificWidget(){
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/1");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        assertTrue(response.contains("<widget id=\"1\" identifier=\"http://www.tencompetence.org/widgets/default/notsupported\" version=\"v1.0\">"));
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
	}
	
	@Test
	public void getSpecificWidget_nonexisting(){
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/99");
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
	public void getWidgetType(){
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/unsupported");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        assertTrue(response.contains("<widget id=\"1\" identifier=\"http://www.tencompetence.org/widgets/default/notsupported\" version=\"v1.0\">"));
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
	}
	
	// We expect a valid response, just no actual widgets
	@Test
	public void getWidgetType_noneexistant(){
	    try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/nosuchtype");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        assertFalse(response.contains("<widget "));
	        get.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("get failed");
	    }
	}
	
}
