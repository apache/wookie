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


import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.BeforeClass;
import org.junit.Test;


public class FlatpackControllerTest extends AbstractControllerTest {
	
	private static final String TEST_FLATPACK_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"export";
	
	@BeforeClass
	public static void setup() throws HttpException, IOException{
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=FPtest&shareddatakey=test");
        client.executeMethod(post);
        int code = post.getStatusCode();
        post.releaseConnection();
	}
	
	@Test
	public void sniff(){
		try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_FLATPACK_SERVICE_URL_VALID);
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(403, code);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }
	}
	
	@Test
	public void getPack(){
	    try {
	        HttpClient client = new HttpClient();
	        PostMethod post = new PostMethod(TEST_FLATPACK_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=FPtest&shareddatakey=test");
	        client.executeMethod(post);
	        int code = post.getStatusCode();
	        assertEquals(200,code);
	        String url = post.getResponseBodyAsString();
	        post.releaseConnection();
	        
	        // Now lets try to download it!
	        GetMethod get = new GetMethod(url);
	        client.executeMethod(get);
	        code = get.getStatusCode();
	        assertEquals(200, code);
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	fail("post failed");
	    }
	}
}
