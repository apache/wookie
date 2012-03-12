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
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for the Participants REST API
 */
public class ParticipantsControllerTest extends AbstractControllerTest {

  private static String instance_id_key;

  /**
   * Create a widget instance to test
   * 
   * @throws Exception
   */
  @BeforeClass
  public static void setUp() throws Exception {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=participantstest");
    client.executeMethod(post);
    String response = post.getResponseBodyAsString();
    instance_id_key = response.substring(response.indexOf("<identifier>") + 12,
        response.indexOf("</identifier>"));
    post.releaseConnection();
  }

  /**
   * Tests adding and then getting a participant
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void addParticipant() throws HttpException, IOException {

    //
    // Create a new participant
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=participantstest&participant_id=1&participant_display_name=bob&participant_thumbnail_url=http://www.test.org");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);
    post.releaseConnection();

    //
    // Now lets GET it to make sure it was added OK
    //
    client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=participantstest");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    String response = get.getResponseBodyAsString();
    assertTrue(response
        .contains("<participant id=\"1\" display_name=\"bob\" thumbnail_url=\"http://www.test.org\" />"));
    get.releaseConnection();

  }

  /**
   * Tests getting a participant using the widget instance ID key
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void getParticipant_usingIdKey() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&idkey="
        + instance_id_key);
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(200, code);
    String response = get.getResponseBodyAsString();
    assertTrue(response
        .contains("<participant id=\"1\" display_name=\"bob\" thumbnail_url=\"http://www.test.org\" />"));
    get.releaseConnection();
  }

  /**
   * Tests trying to add a duplicate participant
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void addParticipant_AlreadyExists() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=participantstest&participant_id=1&participant_display_name=bob&participants_thumbnail_url=http://www.test.org");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(200, code);
    post.releaseConnection();
  }

  /**
   * Try to add a participant with an invalid API key
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void addParticipant_InvalidAPIkey() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_INVALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=participantstest");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(403, code);
    post.releaseConnection();
  }

  /**
   * Try to add a participant with no participant ID
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void addParticipant_InvalidParticipant() throws HttpException,
      IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=participantstest&participant_id=&participant_display_name=bob&participants_thumbnail_url=http://www.test.org");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(400, code);
    post.releaseConnection();
  }

  /**
   * Try to add a participant using a non-existing widget
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void addParticipant_InvalidWidget() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_INVALID
        + "&userid=test&shareddatakey=participantstest&participant_id=1&participant_display_name=bob&participants_thumbnail_url=http://www.test.org");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(400, code);
    post.releaseConnection();
  }

  /**
   * Delete a participant and check it was deleted
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void deleteParticipant() throws HttpException, IOException {
    //
    // Delete the participant
    //
    HttpClient client = new HttpClient();
    DeleteMethod post = new DeleteMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=participantstest&participant_id=1");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(200, code);
    post.releaseConnection();

    //
    // Now lets GET it to make sure it was deleted
    //

    client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    get.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=participantstest");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    String response = get.getResponseBodyAsString();
    assertFalse(response
        .contains("<participant id=\"1\" display_name=\"bob\" thumbnail_url=\"http://www.test.org\" />"));
    get.releaseConnection();

  }

  /**
   * Try to delete a participant with an invalid API key
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void deleteParticipant_InvalidAPIKey() throws HttpException,
      IOException {
    HttpClient client = new HttpClient();
    DeleteMethod post = new DeleteMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_INVALID + "&widgetid="
        + WIDGET_ID_VALID + "&userid=test&shareddatakey=participantstest");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(403, code);
    post.releaseConnection();
  }

  /**
   * Try to delete a non-existing participant
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void deleteParticipant_InvalidParticipant() throws HttpException,
      IOException {
    HttpClient client = new HttpClient();
    DeleteMethod post = new DeleteMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    post.setQueryString("api_key=" + API_KEY_VALID + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=participantstest&participant_id=99");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(404, code);
    post.releaseConnection();
  }

  /**
   * Try to delete a participant for a non-existing widget instance
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void deleteParticipant_InvalidInstance() throws HttpException,
      IOException {
    HttpClient client = new HttpClient();
    DeleteMethod post = new DeleteMethod(TEST_PARTICIPANTS_SERVICE_URL_VALID);
    post.setQueryString("api_key="
        + API_KEY_VALID
        + "&widgetid="
        + WIDGET_ID_VALID
        + "&userid=test&shareddatakey=participantstestinvalidkey&participant_id=1");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(400, code);
    post.releaseConnection();
  }

}
