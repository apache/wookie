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

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for the Properties REST API
 */
public class PropertiesControllerTest extends AbstractControllerTest {

  /**
   * Create a new instance for use in testing
   * 
   * @throws HttpException
   * @throws IOException
   */
  @BeforeClass
  public static void setup() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=propstest");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    post.releaseConnection();
  }
  
  @AfterClass
  public static void tearDown() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    DeleteMethod delete;
    delete = new DeleteMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    delete.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=propstest");
    client.executeMethod(delete);
    delete.releaseConnection();
  }

  /**
   * Test that we can set preferences using post parameters
   * 
   * @throws Exception
   */
  @Test
  public void setPreferenceUsingPostParameters() throws Exception {
    //
    // Set a property ("testpost=pass") using POST
    //
    String url = TEST_PROPERTIES_SERVICE_URL_VALID;
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(url);
    post.addParameter("api_key", API_KEY_VALID);
    post.addParameter("widgetid", WIDGET_ID_VALID);
    post.addParameter("userid", "test");
    post.addParameter("is_public", "false");
    post.addParameter("shareddatakey", "propstest");
    post.addParameter("propertyname", "testpost");
    post.addParameter("propertyvalue", "pass");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    post.releaseConnection();

    //
    // Read back property using GET
    //
    client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=propstest&propertyname=testpost");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    String resp = get.getResponseBodyAsString();
    assertEquals("pass", resp);
    get.releaseConnection();

  }

  /**
   * Tests that we can set a property using querystring parameters on a POST
   * method
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void setPreference() throws HttpException, IOException {
    //
    // Set a preference using POST with querystring parameters
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&is_public=false&shareddatakey=propstest&propertyname=pass&propertyvalue=pass");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    post.releaseConnection();

    //
    // Read it back using GET
    //
    client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=propstest&propertyname=pass");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    String resp = get.getResponseBodyAsString();
    assertEquals("pass", resp);
    get.releaseConnection();

  }

  /**
   * Test we can set shared data values using querystring parameters
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void setSharedData() throws HttpException, IOException {

    //
    // Set some shared data with a POST and querystring
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&is_public=true&shareddatakey=propstest&propertyname=cat&propertyvalue=garfield");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    post.releaseConnection();

    //
    // Read back the value using GET
    //
    client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=propstest&propertyname=cat");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    String resp = get.getResponseBodyAsString();
    assertEquals("garfield", resp);
    get.releaseConnection();

  }

  /**
   * Test we can update an existing property
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void updateProperty() throws HttpException, IOException {

    //
    // Set cat=felix using POST
    //
    HttpClient client = new HttpClient();
    PutMethod put = new PutMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    put.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&is_public=true&shareddatakey=propstest&propertyname=cat&propertyvalue=felix");
    client.executeMethod(put);
    int code = put.getStatusCode();
    assertEquals(200, code);
    put.releaseConnection();

    //
    // Read back the value using GET
    //
    client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=propstest&propertyname=cat");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    String resp = get.getResponseBodyAsString();
    assertEquals("felix", resp);
    get.releaseConnection();

  }

  /**
   * Test removing a property
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void removeProperty() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    DeleteMethod delete = new DeleteMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    delete
        .setQueryString("api_key="
            + API_KEY_VALID
            + "&widgetid="
            + WIDGET_ID_VALID
            + "&userid=test&is_public=true&shareddatakey=propstest&propertyname=cat");
    client.executeMethod(delete);
    int code = delete.getStatusCode();
    assertEquals(200, code);
    delete.releaseConnection();
  }

  /**
   * Test removing a non-existing property.
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void removePropertyNonExisting() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    DeleteMethod delete = new DeleteMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    delete
        .setQueryString("api_key="
            + API_KEY_VALID
            + "&widgetid="
            + WIDGET_ID_VALID
            + "&userid=test&is_public=true&shareddatakey=propstest&propertyname=bogus");
    client.executeMethod(delete);
    int code = delete.getStatusCode();
    assertEquals(404, code);
    delete.releaseConnection();
  }

  /**
   * Try to set a property without specifying the property name
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void setPropertyNoName() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&is_public=true&shareddatakey=propstest&propertyvalue=garfield");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(400, code);
    post.releaseConnection();
  }

  /**
   * Try to set a property with an empty (zero length) property name
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void setPropertyEmptyName() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&is_public=true&shareddatakey=propstest&propertyname=&propertyvalue=garfield");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(400, code);
    post.releaseConnection();
  }

  /**
   * Try to set a property with a whitespace-only name
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void setPropertyWhitespaceName() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&is_public=true&shareddatakey=propstest&propertyname=%20%20&propertyvalue=garfield");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(400, code);
    post.releaseConnection();
  }

  /**
   * Try to set a property without a valid API key
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getPropertyInvalidAPIKey() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_INVALID + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=propstest&propertyname=cat");
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(403, code);
    get.releaseConnection();
  }

  /**
   * Try to get the value of a non-existant property
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getPropertyInvalidName() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=propstest&propertyname=madeupname");
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(404, code);
    get.releaseConnection();
  }

}
