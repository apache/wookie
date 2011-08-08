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
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test cases for the Widget Instances REST API
 */
public class WidgetInstancesControllerTest extends AbstractControllerTest {

  private static final String LOCALIZED_WIDGET = "http://www.getwookie.org/widgets/localetest";
  private static String test_id_key = "";

  /**
   * Test we can get an instance localized using the locale parameter
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getLocalizedInstance() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + LOCALIZED_WIDGET
        + "&userid=localetest&shareddatakey=localetest&locale=fr");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    assertTrue(post.getResponseBodyAsString().contains("locales/fr/index.htm"));
    assertTrue(post.getResponseBodyAsString().contains(
        "tester les param&#232;tres r&#233;gionaux"));
    test_id_key = post.getResponseBodyAsString().substring(
        post.getResponseBodyAsString().indexOf("<identifier>") + 12,
        post.getResponseBodyAsString().indexOf("</identifier>"));
    post.releaseConnection();
  }

  /**
   * Test we can get an existing instance using instance parameters - widgetid,
   * apikey, userid, shareddatakey, locale
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void getExistingInstanceByInstanceParams() throws HttpException,
      IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + LOCALIZED_WIDGET
        + "&userid=localetest&shareddatakey=localetest&locale=fr");
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(200, code);
    assertTrue(get.getResponseBodyAsString().contains("locales/fr/index.htm"));
    assertTrue(get.getResponseBodyAsString().contains(
        "tester les param&#232;tres r&#233;gionaux"));
  }

  /**
   * Test we can get an existing instance using just the id_key parameter
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getExistingInstanceByIdKey() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&id_key=" + test_id_key);
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(200, code);
    assertTrue(get.getResponseBodyAsString().contains("locales/en/index.htm"));
    assertTrue(get.getResponseBodyAsString().contains("locale test"));
  }

  /**
   * Test we can get an existing instance using the id_key as a resource path
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getExistingInstanceByIdResource() throws HttpException,
      IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(TEST_INSTANCES_SERVICE_URL_VALID + "/"
        + test_id_key);
    get.setQueryString("api_key=" + API_KEY_VALID);
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(200, code);
    assertTrue(get.getResponseBodyAsString().contains("locales/en/index.htm"));
    assertTrue(get.getResponseBodyAsString().contains("locale test"));
  }

  /**
   * Test that instance localization includes support for fallback locales -
   * e.g. specifying "early modern french" locale returns standard FR start file
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getGracefulLocalizedInstance() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + LOCALIZED_WIDGET
        + "&userid=localetest1b&shareddatakey=localetest1b&locale=fr-1694acad");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    assertTrue(post.getResponseBodyAsString().contains("locales/fr/index.htm"));
    assertFalse(post.getResponseBodyAsString().contains("locale test"));
    assertTrue(post.getResponseBodyAsString().contains(
        "tester les param&#232;tres r&#233;gionaux"));
    post.releaseConnection();
  }

  /**
   * Tests that requesting an instance for an unsupported locale returns a
   * non-localized instance
   * 
   * @throws IOException
   */
  @Test
  public void getNonLocalizedInstance() throws IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + LOCALIZED_WIDGET
        + "&userid=localetest2&shareddatakey=localetest2&locale=bu");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    assertFalse(post.getResponseBodyAsString().contains("locales/fr/index.htm"));
    assertFalse(post.getResponseBodyAsString().contains("locales/en/index.htm"));
    assertTrue(post.getResponseBodyAsString().contains("index.htm"));
    assertTrue(post.getResponseBodyAsString().contains("locale test"));
    assertFalse(post.getResponseBodyAsString().contains(
        "tester les param&#232;tres r&#233;gionaux"));
    post.releaseConnection();
  }

  /**
   * Test that requesting an instance with no locale property returns the
   * instance for the default locale
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getDefaultLocalizedInstance() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + LOCALIZED_WIDGET + "&userid=localetest3&shareddatakey=localetest3");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    assertTrue(post.getResponseBodyAsString().contains("locales/en/index.htm"));
    assertTrue(post.getResponseBodyAsString().contains("locale test"));
    assertFalse(post.getResponseBodyAsString().contains(
        "tester les param&#232;tres r&#233;gionaux"));
    post.releaseConnection();
  }

  /**
   * Tests we can create an instance using instance params and widget id and get
   * a 201 response
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getInstanceById() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=test");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    post.releaseConnection();
  }

  /**
   * Tests that getting an existing instance returns 200 rather than 201
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getInstanceById_AlreadyExists() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=test");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(200, code);
    post.releaseConnection();
  }

  /**
   * Tests that a request for an instance with an invalid API key is rejected
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void getInstance_InvalidAPIkey() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_INVALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=test");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(401, code);
    post.releaseConnection();
  }

  /**
   * Tests that we get a 404 when requesting an instance for a non-installed
   * widget, even though we still get the representation of the
   * "unsupported widget widget"
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void getInstanceById_InvalidWidget() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_INVALID + "&userid=test&shareddatakey=test");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(404, code); // but must return the "default widget"
    assertTrue(post.getResponseBodyAsString().contains(
        "Unsupported widget widget"));
    post.releaseConnection();
  }

  /**
   * Test for stop() extension feature. The feature itself may be removed in
   * future releases.
   */
  @Test
  @Ignore
  public void stop() {
    fail("test not written");
  }

  /**
   * Test for resume() extension feature. The feature itself may be removed in
   * future releases.
   */
  @Test
  @Ignore
  public void resume() {
    fail("test not written");
  }

  /**
   * Tests that we can clone an instance
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void cloneSharedData() throws HttpException, IOException {
    //
    // Create an instance using POST
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=clonetestsrc");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    post.releaseConnection();

    //
    // Set some shared data
    //
    client = new HttpClient();
    post = new PostMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&is_public=true&shareddatakey=clonetestsrc&propertyname=cat&propertyvalue=garfield");
    client.executeMethod(post);
    code = post.getStatusCode();
    assertEquals(201, code);
    post.releaseConnection();

    //
    // Clone it using PUT
    //
    client = new HttpClient();
    PutMethod put = new PutMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    put.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=clonetestsrc&requestid=clone&cloneshareddatakey=clonetestsync");
    client.executeMethod(put);
    code = put.getStatusCode();
    assertEquals(200, code);
    put.releaseConnection();

    //
    // Create an instance for the clone
    //
    client = new HttpClient();
    post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=clonetestsync");
    client.executeMethod(post);
    code = post.getStatusCode();
    assertEquals(201, code);
    post.releaseConnection();

    //
    // Get the data for the clone and check it is the same set for the original
    //
    client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=clonetestsync&propertyname=cat");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    String resp = get.getResponseBodyAsString();
    assertEquals("garfield", resp);
    post.releaseConnection();
  }

}
