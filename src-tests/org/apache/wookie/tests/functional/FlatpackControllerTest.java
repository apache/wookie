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
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for the flatpack feature. These tests try to pack and export
 * widget instances and then verify that they were exported correctly
 */
public class FlatpackControllerTest extends AbstractControllerTest {

  private static final String TEST_FLATPACK_SERVICE_URL_VALID = TEST_SERVER_LOCATION
      + "flatpack";
  private static final String TEST_EXPORT_SERVICE_URL_VALID = TEST_SERVER_LOCATION
      + "export";
  private static final String TEST_WIDGET_ID_JQM = "http://wookie.apache.org/widgets/freeder";
  private static String test_id_key = "";

  /**
   * Create widget instances to use for testing purposes
   * 
   * @throws HttpException
   * @throws IOException
   */
  @BeforeClass
  public static void setup() throws HttpException, IOException {

    //
    // Create basic widget test instance
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=FPtest&shareddatakey=test");
    client.executeMethod(post);
    test_id_key = post.getResponseBodyAsString().substring(
        post.getResponseBodyAsString().indexOf("<identifier>") + 12,
        post.getResponseBodyAsString().indexOf("</identifier>"));
    post.releaseConnection();

    //
    // Create a test instance of a widget that uses JQuery Mobile as a feature
    //
    post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + TEST_WIDGET_ID_JQM + "&userid=FPtest&shareddatakey=test");
    client.executeMethod(post);
    post.releaseConnection();
  }

  /**
   * Test that you can't get a directory listing of exported widgets
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void sniff() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(TEST_EXPORT_SERVICE_URL_VALID);
    client.executeMethod(get);
    int code = get.getStatusCode();
    if (code != 404) {
      String html = get.getResponseBodyAsString();
      System.out.println(html);
      assertEquals(html.length(), 0);
    }

  }

  /**
   * Tests exporting a simple widget instance
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getPack() throws HttpException, IOException {

    //
    // Create the export package
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_FLATPACK_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=FPtest&shareddatakey=test");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(200, code);
    String url = post.getResponseBodyAsString();
    post.releaseConnection();

    //
    // Now lets try to download it!
    //
    GetMethod get = new GetMethod(url);
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
  }

  /**
   * Create an export using a more RESTful pattern
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getPackUsingResourceId() throws HttpException, IOException {

    //
    // Create the export package
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_FLATPACK_SERVICE_URL_VALID + "/"
        + test_id_key);
    post.setQueryString("api_key=" + API_KEY_VALID);
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(200, code);
    String url = post.getResponseBodyAsString();
    post.releaseConnection();

    //
    // Now lets try to download it!
    //
    GetMethod get = new GetMethod(url);
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
  }

  /**
   * Tests that "flattening" works when exporting a widget instance. This means
   * that features like JQuery Mobile are replaced by directly including their
   * scripts within the exported package, removing the <feature> element from
   * config.xml
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getPackUsingFlattenedFeature() throws HttpException, IOException {

    //
    // Create the export package
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_FLATPACK_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + TEST_WIDGET_ID_JQM + "&userid=FPtest&shareddatakey=test");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(200, code);
    String url = post.getResponseBodyAsString();
    post.releaseConnection();

    //
    // Now lets try to download it!
    //
    GetMethod get = new GetMethod(url);
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);

  }
}
