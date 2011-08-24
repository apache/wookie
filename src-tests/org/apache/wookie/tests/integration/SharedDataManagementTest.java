/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.tests.integration;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.wookie.tests.functional.AbstractControllerTest;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for verifying boundaries between shared data spaces are managed correctly; e.g. see WOOKIE-206
 */
public class SharedDataManagementTest extends AbstractControllerTest{
  
  private static final String APIKEY_SERVICE_LOCATION_VALID = TEST_SERVER_LOCATION  + "keys";
  
  
  public static final String API_KEY_ONE = "sharedDataManagementTestOne";
  public static final String API_KEY_TWO = "sharedDataManagementTestTwo";
  public static String API_KEY_ONE_ID;
  public static String API_KEY_TWO_ID;
  
  @Before
  public void setup() throws HttpException, IOException, JDOMException{
    
    //
    // Create two new API keys
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(APIKEY_SERVICE_LOCATION_VALID);
    setAuthenticationCredentials(client);
    post.setParameter("apikey", API_KEY_ONE);
    post.setParameter("email", "test@incubator.apache.org");
    client.executeMethod(post);
    post.setParameter("apikey", API_KEY_TWO);
    post.setParameter("email", "test@incubator.apache.org");
    client.executeMethod(post);
    
    //
    // Get the IDs of the keys we created so we can delete them later
    //
    GetMethod get = new GetMethod(APIKEY_SERVICE_LOCATION_VALID);
    setAuthenticationCredentials(client);
    client.executeMethod(get);
    Document doc = new SAXBuilder().build(get.getResponseBodyAsStream());
    for (Object key : doc.getRootElement().getChildren()) {
      Element keyElement = (Element) key;
      if (keyElement.getAttributeValue("value").equals(API_KEY_ONE)) {
        API_KEY_ONE_ID = keyElement.getAttributeValue("id");
      } 
      if (keyElement.getAttributeValue("value").equals(API_KEY_TWO)) {
        API_KEY_TWO_ID = keyElement.getAttributeValue("id");
      } 
      
    }  
  }
  
  @Test
  public void testSharedDataManagement() throws HttpException, IOException{
    
    HttpClient client = new HttpClient();
    int code;

    //
    // Create a widget instance using API KEY ONE
    //
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_ONE + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=shareddatamanagementtest");
    client.executeMethod(post);
    post.releaseConnection();
    
    //
    // Create another widget instance using API KEY ONE
    //
    post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_ONE + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test_second_user&shareddatakey=shareddatamanagementtest");
    client.executeMethod(post);
    post.releaseConnection();
    
    //
    // Create a widget instance using API KEY TWO
    //
    post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_TWO + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=shareddatamanagementtest");
    client.executeMethod(post);
    post.releaseConnection();
    
    //
    // Set shared data property in first widget instance
    //
    post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_ONE
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&is_public=true&shareddatakey=shareddatamanagementtest&propertyname=cat&propertyvalue=garfield");
    client.executeMethod(post);
    post.releaseConnection();
    
    //
    // Verify it does exist in the first and second widget instances that share the same
    // API key and shared data key even though different user id.
    //
    GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_ONE + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=shareddatamanagementtest&propertyname=cat");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    String resp = get.getResponseBodyAsString();
    assertEquals("garfield", resp);
    
    get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_ONE + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test_second_user&shareddatakey=shareddatamanagementtest&propertyname=cat");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    resp = get.getResponseBodyAsString();
    assertEquals("garfield", resp);
    
    //
    // Verify it doesn't exist in the third widget instance, even though it has the same
    // shared data key, it has a different API key
    //
    get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_TWO + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=shareddatamanagementtest&propertyname=cat");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(404, code);
   
  }
  
  @After
  public void cleanup() throws HttpException, IOException{
    
    //
    // Remove the API keys
    //
    HttpClient client = new HttpClient();
    DeleteMethod del = new DeleteMethod(APIKEY_SERVICE_LOCATION_VALID + "/"  + API_KEY_ONE_ID);
    setAuthenticationCredentials(client);
    client.executeMethod(del);
    del = new DeleteMethod(APIKEY_SERVICE_LOCATION_VALID + "/"  + API_KEY_TWO_ID);
    setAuthenticationCredentials(client);
    client.executeMethod(del);
    
  }

}
