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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Test cases for the Whitelist REST API
 */
public class WhiteListControllerTest extends AbstractControllerTest {

  private static final String WHITELIST_SERVICE_URL_VALID = TEST_SERVER_LOCATION
      + "whitelist";

  /**
   * Tests that a request for the whitelist is rejected without admin
   * credentials
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void getEntriesUnauthorized() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(WHITELIST_SERVICE_URL_VALID);
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(401, code);
  }

  /**
   * Tests that valid, authenticated requests are accepted
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void getEntries() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(WHITELIST_SERVICE_URL_VALID);
    setAuthenticationCredentials(client);
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(200, code);
  }

  /**
   * Tests adding a new whitelist entry
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void addEntry() throws HttpException, IOException {

    //
    // Set a new entry using POST
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(WHITELIST_SERVICE_URL_VALID);
    setAuthenticationCredentials(client);
    post.setParameter("url", "http://test.apache.org");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);

    //
    // Read it back using GET
    //
    client = new HttpClient();
    GetMethod get = new GetMethod(WHITELIST_SERVICE_URL_VALID);
    setAuthenticationCredentials(client);
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    assertTrue(get.getResponseBodyAsString().contains("http://test.apache.org"));

  }

  /**
   * Test that we can remove a whitelist entry
   * 
   * @throws HttpException
   * @throws IOException
   * @throws JSONException
   */
  @Test
  public void removeEntry() throws HttpException, IOException, JSONException {
    String id = null;

    //
    // GET the whitelist
    //
    HttpClient client = new HttpClient();
    GetMethod get = new GetMethod(WHITELIST_SERVICE_URL_VALID);
    setAuthenticationCredentials(client);
    get.setRequestHeader("Accept", "application/json");
    client.executeMethod(get);
    int code = get.getStatusCode();
    assertEquals(200, code);
    assertTrue(get.getResponseBodyAsString().contains("http://test.apache.org"));

    //
    // find the ID of the entry we want to delete
    //
    JSONObject obj = new JSONObject(get.getResponseBodyAsString());
    JSONArray entries = obj.getJSONArray("entries");
    for (int i = 0; i < entries.length(); i++) {
      JSONObject entry = entries.getJSONObject(i);
      if (entry.getString("url").equals("http://test.apache.org"))
        id = entry.getString("id");
    }

    //
    // Remove the whitelist entry using DELETE
    //
    client = new HttpClient();
    DeleteMethod del = new DeleteMethod(WHITELIST_SERVICE_URL_VALID + "/" + id);
    setAuthenticationCredentials(client);
    client.executeMethod(del);
    code = del.getStatusCode();
    assertEquals(200, code);

    //
    // GET the whitelist and verify the entry has been removed
    //
    client = new HttpClient();
    get = new GetMethod(WHITELIST_SERVICE_URL_VALID);
    get.setRequestHeader("Content-type", "application/json");
    setAuthenticationCredentials(client);
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    System.out.println(get.getResponseBodyAsString());
    assertFalse(get.getResponseBodyAsString()
        .contains("http://test.apache.org"));

  }

  /**
   * Try to delete a non-existant whitelist entry
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void removeNonExistantEntry() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    DeleteMethod del = new DeleteMethod(WHITELIST_SERVICE_URL_VALID
        + "/99999999");
    setAuthenticationCredentials(client);
    client.executeMethod(del);
    int code = del.getStatusCode();
    assertEquals(404, code);
  }

  /**
   * Try to add a whitelist entry without specifying a URL
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void addEntryNoUrl() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(WHITELIST_SERVICE_URL_VALID);
    setAuthenticationCredentials(client);
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(400, code);
  }

  /**
   * Try to add a whitelist entry with an invalid URL
   * 
   * @throws IOException
   * @throws HttpException
   */
  @Test
  public void addEntryInvalidUrl() throws HttpException, IOException {
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(WHITELIST_SERVICE_URL_VALID);
    post.setParameter("url", "I AM NOT A URL");
    setAuthenticationCredentials(client);
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(400, code);
  }

  /**
   * Try to add a duplicate entry to the whitelist
   * 
   * @throws HttpException
   * @throws IOException
   */
  @Test
  public void addDuplicateEntry() throws HttpException, IOException {

    //
    // POST the entry
    //
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(WHITELIST_SERVICE_URL_VALID);
    setAuthenticationCredentials(client);
    post.setParameter("url", "http://test.apache.org");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201, code);

    //
    // Replay the method
    //
    client.executeMethod(post);
    code = post.getStatusCode();
    assertEquals(409, code);
  }

}
