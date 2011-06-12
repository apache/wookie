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
	
	private static final String TEST_FLATPACK_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"flatpack";
	private static final String TEST_EXPORT_SERVICE_URL_VALID = TEST_SERVER_LOCATION+"export";
	private static String test_id_key = "";
	
	@BeforeClass
	public static void setup() throws HttpException, IOException{
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=FPtest&shareddatakey=test");
        client.executeMethod(post);
        test_id_key = post.getResponseBodyAsString().substring(post.getResponseBodyAsString().indexOf("<identifier>")+12,post.getResponseBodyAsString().indexOf("</identifier>"));
        post.releaseConnection();
	}
	
	// Test that you can't get a directory listing of exported widgets
	@Test
	public void sniff(){
		try {
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_EXPORT_SERVICE_URL_VALID);
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        if (code != 404){
	        	String html = get.getResponseBodyAsString();
	        	System.out.println(html);
	        	assertEquals(html.length(), 0);
	        }
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
	
	 @Test
	  public void getPackUsingResourceId(){
	      try {
	          HttpClient client = new HttpClient();
	          PostMethod post = new PostMethod(TEST_FLATPACK_SERVICE_URL_VALID+"/"+test_id_key);
	          post.setQueryString("api_key="+API_KEY_VALID);
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
