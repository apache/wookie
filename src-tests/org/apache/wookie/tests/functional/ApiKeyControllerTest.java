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

package org.apache.wookie.tests.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Functional tests for API key management
 */
public class ApiKeyControllerTest extends AbstractControllerTest {

  private static final String APIKEY_SERVICE_LOCATION_VALID = TEST_SERVER_LOCATION+"keys";

  @Test
  public void getEntriesUnauthorized(){
    try {
      HttpClient client = new HttpClient();
      GetMethod get = new GetMethod(APIKEY_SERVICE_LOCATION_VALID);
      client.executeMethod(get);
      int code = get.getStatusCode();
      assertEquals(401, code);
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("get failed");
    }
  }

  @Test
  public void getKeys(){
    try {
      HttpClient client = new HttpClient();
      GetMethod get = new GetMethod(APIKEY_SERVICE_LOCATION_VALID);
      setAuthenticationCredentials(client);
      client.executeMethod(get);
      int code = get.getStatusCode();
      assertEquals(200, code);
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("get failed");
    }
  }

  @Test
  public void addKey(){
    try {
      HttpClient client = new HttpClient();
      PostMethod post = new PostMethod(APIKEY_SERVICE_LOCATION_VALID);
      setAuthenticationCredentials(client);
      post.setParameter("apikey", "TEST_KEY");
      post.setParameter("email", "test@incubator.apache.org");
      client.executeMethod(post);
      int code = post.getStatusCode();
      assertEquals(201, code);
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("post failed");
    } 
    try {
      HttpClient client = new HttpClient();
      GetMethod get = new GetMethod(APIKEY_SERVICE_LOCATION_VALID);
      setAuthenticationCredentials(client);
      client.executeMethod(get);
      int code = get.getStatusCode();
      assertEquals(200, code);
      assertTrue(get.getResponseBodyAsString().contains("TEST_KEY"));
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("get failed");
    }
  }

  @Test
  public void removeKey(){
    String id = null;
    try {
      HttpClient client = new HttpClient();
      GetMethod get = new GetMethod(APIKEY_SERVICE_LOCATION_VALID);
      setAuthenticationCredentials(client);
      client.executeMethod(get);
      int code = get.getStatusCode();
      assertEquals(200, code);
      assertTrue(get.getResponseBodyAsString().contains("TEST_KEY"));
      // Get the ID
      Document doc = new SAXBuilder().build(get.getResponseBodyAsStream());
      for (Object key: doc.getRootElement().getChildren()){
        Element keyElement = (Element)key;
        if (keyElement.getAttributeValue("value").equals("TEST_KEY")){
          id = keyElement.getAttributeValue("id");
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("get failed");
    }
    try {
      HttpClient client = new HttpClient();
      DeleteMethod del = new DeleteMethod(APIKEY_SERVICE_LOCATION_VALID+"/"+id);
      setAuthenticationCredentials(client);
      client.executeMethod(del);
      int code = del.getStatusCode();
      assertEquals(200, code);
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("delete failed");
    }
    try {
      HttpClient client = new HttpClient();
      GetMethod get = new GetMethod(APIKEY_SERVICE_LOCATION_VALID);
      get.setRequestHeader("Content-type", "application/json");
      setAuthenticationCredentials(client);
      client.executeMethod(get);
      int code = get.getStatusCode();
      assertEquals(200, code);
      System.out.println(get.getResponseBodyAsString());
      assertFalse(get.getResponseBodyAsString().contains("TEST_KEY"));
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("get failed");
    }
  }

  @Test
  public void removeNonExistantEntry(){
    try {
      HttpClient client = new HttpClient();
      DeleteMethod del = new DeleteMethod(APIKEY_SERVICE_LOCATION_VALID+"/99999999");
      setAuthenticationCredentials(client);
      client.executeMethod(del);
      int code = del.getStatusCode();
      assertEquals(404, code);
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("delete failed");
    } 
  }

  @Test
  public void addEntryNoEmailOrValue(){
    try {
      HttpClient client = new HttpClient();
      PostMethod post = new PostMethod(APIKEY_SERVICE_LOCATION_VALID);
      setAuthenticationCredentials(client);
      client.executeMethod(post);
      int code = post.getStatusCode();
      assertEquals(400, code);
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("post failed");
    } 
  }

  @Test
  public void addDuplicateEntry(){
    try {
      HttpClient client = new HttpClient();
      PostMethod post = new PostMethod(APIKEY_SERVICE_LOCATION_VALID);
      setAuthenticationCredentials(client);
      post.setParameter("apikey", "DUPLICATION_TEST");
      post.setParameter("email", "test@127.0.0.1");
      client.executeMethod(post);
      int code = post.getStatusCode();
      assertEquals(201, code);
      client.executeMethod(post);
      code = post.getStatusCode();
      assertEquals(409, code);
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("post failed");
    }
  }

  @Test
  @Ignore
  public void migrateAPIKey(){
    
    String keyId = null;
    // Create a new key
    try {
      HttpClient client = new HttpClient();
      PostMethod post = new PostMethod(APIKEY_SERVICE_LOCATION_VALID);
      setAuthenticationCredentials(client);
      post.setParameter("apikey", "MIGRATION_TEST_KEY_1");
      post.setParameter("email", "test@incubator.apache.org");
      client.executeMethod(post);
      int code = post.getStatusCode();
      assertEquals(201, code);

      GetMethod get = new GetMethod(APIKEY_SERVICE_LOCATION_VALID);
      setAuthenticationCredentials(client);
      client.executeMethod(get);

      // Get the ID
      Document doc = new SAXBuilder().build(get.getResponseBodyAsStream());
      for (Object key: doc.getRootElement().getChildren()){
        Element keyElement = (Element)key;
        if (keyElement.getAttributeValue("value").equals("MIGRATION_TEST_KEY_1")){
          keyId = keyElement.getAttributeValue("id");
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("get failed");
    }

    // Create a widget instance
    String instance_id_key =  null;
    try {
      HttpClient client = new HttpClient();
      PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
      post.setQueryString("api_key=MIGRATION_TEST_KEY_1&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=migration_test");
      client.executeMethod(post);
      int code = post.getStatusCode();
      String response = post.getResponseBodyAsString();
      instance_id_key = post.getResponseBodyAsString().substring(response.indexOf("<identifier>")+12, response.indexOf("</identifier>"));
      assertEquals(201,code);
      post.releaseConnection();
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("post failed");
    }

    // Set participant
    try {
      HttpClient client = new HttpClient();
      PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
      post.setQueryString("api_key=MIGRATION_TEST_KEY_1&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=migration_test&participant_id=1&participant_display_name=bob&participant_thumbnail_url=http://www.test.org");
      client.executeMethod(post);
      int code = post.getStatusCode();
      assertEquals(201,code);
      post.releaseConnection();
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("post failed");
    }

    // Migrate key
    try {
      HttpClient client = new HttpClient();
      PutMethod put = new PutMethod(APIKEY_SERVICE_LOCATION_VALID+"/"+keyId);
      put.setQueryString("apikey=MIGRATION_TEST_KEY_2&email=test@127.0.0.1");
      setAuthenticationCredentials(client);
      client.executeMethod(put);
      int code = put.getStatusCode();
      assertEquals(200,code);
      put.releaseConnection();
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("post failed");
    }

    // Get instance again using the new key - should be 200 not 201
    try {
      HttpClient client = new HttpClient();
      PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
      post.setQueryString("api_key=MIGRATION_TEST_KEY_2&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=migration_test");
      client.executeMethod(post);
      int code = post.getStatusCode();
      assertEquals(200,code);
      post.releaseConnection();
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("post failed");
    }

    // Get participant
    try {
      HttpClient client = new HttpClient();
      GetMethod get = new GetMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
      get.setQueryString("api_key=MIGRATION_TEST_KEY_2&id_key="+instance_id_key);
      client.executeMethod(get);
      int code = get.getStatusCode();
      assertEquals(200,code);
      String response = get.getResponseBodyAsString();
      assertTrue(response.contains("<participant id=\"1\" display_name=\"bob\" thumbnail_url=\"http://www.test.org\" />"));
      get.releaseConnection();
  }
  catch (Exception e) {
    e.printStackTrace();
    fail("get failed");
  }


  }
}
