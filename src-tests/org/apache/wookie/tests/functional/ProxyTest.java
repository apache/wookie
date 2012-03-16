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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.wookie.tests.helpers.NanoHTTPD;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;


/**
 * Test cases for the server-side web proxy
 */
public class ProxyTest extends AbstractControllerTest {

  private static String instance_id_key;
  private static String other_instance_id_key;
  private static final String OTHER_WIDGET_ID = "http://www.getwookie.org/widgets/natter";
  private static final String PROXY_URL = TEST_SERVER_LOCATION+"proxy";

  /**
   * The valid site URL is our Apache incubator page. This is added by default to the global site whitelist
   */
  private static final String VALID_SITE_URL = "http://incubator.apache.org/wookie/";

  /**
   * Returns XML for checking we get the correct content-type
   */
  private static final String VALID_SITE_XML_URL = TEST_SERVER_LOCATION+"widgets?all=true";

  /**
   * An invalid URL
   */
  private static final String INVALID_SITE_URL = "DFASFAFEQ3FQ32145235123452";

  /**
   * A site not allowed in any policies
   */
  private static final String BLOCKED_SITE_URL = "http://very.bad.place";

  /**
   * The policy allowed site URL is one that the Weather widget is granted acccess to using WARP in the default install
   */
  private static final String POLICY_ALLOWED_SITE_URL = "http://newsrss.bbc.co.uk/weather/forecast/9/Next3DaysRSS.xml";
  private static final String POLICY_DISALLOWED_SITE_URL = "http://news.bbc.co.uk";
  private static final String PROTECTED_SITE_URL = "http://localhost:8300";

  private static ProxiedSecureServer secureServer;
  
  /**
   * Create two instances of a widget for testing
   * @throws IOException 
   * @throws HttpException 
   */
  @BeforeClass
  public static void setup() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=proxytest");
    client.executeMethod(post);
    String response = post.getResponseBodyAsString();
    instance_id_key = response.substring(response.indexOf("<identifier>")+12, response.indexOf("</identifier>"));
    post.releaseConnection();

    client = new HttpClient();
    post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
    post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+OTHER_WIDGET_ID+"&userid=test&shareddatakey=proxytest");
    client.executeMethod(post);
    response = post.getResponseBodyAsString();
    other_instance_id_key = response.substring(response.indexOf("<identifier>")+12, response.indexOf("</identifier>"));
    post.releaseConnection();

    //
    // Create a secure test server
    //
    secureServer = new ProxiedSecureServer(8300);
    
    //
    // Create a policy allowing it
    //
    setAuthenticationCredentials(client);
    post = new PostMethod(TEST_POLICIES_SERVICE_URL_VALID);
    StringRequestEntity entity = new StringRequestEntity("* http://localhost:8300 ALLOW", "text/plain", "UTF-8");
    post.setRequestEntity(entity);
    client.executeMethod(post);
    post.releaseConnection();

  }
  
  @AfterClass
  public static void tearDown() throws HttpException, IOException{
    //
    // Shut down the test server
    //
    secureServer.stop();
    
    //
    // Remove policy
    //
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    DeleteMethod delete = new DeleteMethod(TEST_POLICIES_SERVICE_URL_VALID + "/" + encodeString("* http://localhost:8300 ALLOW"));
    client.executeMethod(delete);
    assertEquals(200, delete.getStatusCode());
  }
  
  

  /**
   * Check we can access a site allowed under the global whitelist using our widget instance's id key
   */
  @Test
  public void getValidSite(){
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+VALID_SITE_URL;
    assertEquals(200,send(url,"GET"));
    assertEquals(405,send(url,"POST")); // This URL doesn't support POST
  }

  /**
   * Check we can access a site allowed under a widget-specific policy using an instance's id key
   */
  @Test
  public void getAllowedSite(){
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+POLICY_ALLOWED_SITE_URL;
    assertEquals(200,send(url,"GET"));
  }

  /**
   * Check that we can't access a site which is not in the whitelist or WARP
   */
  @Test
  public void getDisallowedSite(){
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+POLICY_DISALLOWED_SITE_URL;
    assertEquals(403,send(url,"GET"));
    assertEquals(403,send(url,"POST")); // This URL doesn't support POST
  }

  /**
   * Test trying to get to a site allowed for a different widget, but not this one
   */
  @Test
  public void getAllowedSiteWrongInstance(){
    String url = PROXY_URL+"?instanceid_key="+other_instance_id_key+"&url="+POLICY_ALLOWED_SITE_URL;
    assertEquals(403,send(url,"GET"));
    assertEquals(403,send(url,"POST"));
  }

  /**
   * Test we can get the content from a site with the correct content-type (in this case XML)
   * @throws Exception
   */
  @Test
  public void getValidSiteAndValidXMLContentType() throws Exception{
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+VALID_SITE_XML_URL;
    HttpClient client = new HttpClient();
    HttpMethod req = new GetMethod(url);
    client.executeMethod(req);
    int code = req.getStatusCode();
    req.releaseConnection();
    assertEquals(200,code);
    assertTrue(req.getResponseHeader("Content-Type").toString().contains("text/xml"));
  }

  /**
   * Test we can get the content from a site with the correct content-type (in this case HTML)
   * @throws Exception
   */
  @Test
  public void getValidSiteAndValidHTMLContentType() throws Exception{
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+VALID_SITE_URL;
    HttpClient client = new HttpClient();
    HttpMethod req = new GetMethod(url);
    client.executeMethod(req);
    int code = req.getStatusCode();
    req.releaseConnection();
    assertEquals(200,code);
    assertTrue(req.getResponseHeader("Content-Type").toString().contains("text/html"));
  }


  /**
   * Test that we can't access a site with an invalid instance ID key
   */
  @Test
  public void getValidSiteInvalidIdKey(){
    String url = PROXY_URL+"?instanceid_key=TEST&url="+VALID_SITE_URL;
    assertEquals(403,send(url,"GET"));
    assertEquals(403,send(url,"POST"));
  }

  /**
   * Test that we can't access a site no instance id key
   */
  @Test
  public void getValidSiteNoIdKey(){
    String url = PROXY_URL+"?url="+VALID_SITE_URL;
    assertEquals(403,send(url,"GET"));
    assertEquals(403,send(url,"POST"));
  }

  /**
   * Test we can't get access to a site using an invalid URL
   */
  @Test
  public void getInvalidSite(){
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+INVALID_SITE_URL;
    assertEquals(400,send(url,"GET"));
    assertEquals(400,send(url,"POST"));
  }

  /**
   * Test we can't get access to a site that isn't in either the global whitelist of WARP
   */
  @Test
  public void getBlockedSite(){
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+BLOCKED_SITE_URL;
    assertEquals(403,send(url,"GET"));
  }

  /**
   * Tests that we can get valid returns when using querystring parameters in a POST request
   * @throws Exception
   */
  @Test
  public void postWithOnlyQueryStringParameters() throws Exception{
    HttpClient client = new HttpClient();
    PostMethod req;
    req = new PostMethod(PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+PROTECTED_SITE_URL+"&username=java&password=java");
    client.executeMethod(req);
    int code = req.getStatusCode();
    req.releaseConnection();
    assertEquals(200,code);
  }

  /**
   * Ignored - this was a case for handling double-encoding, but which is no longer valid
   */
  @Ignore // Not a valid use case
  @Test
  public void getWithEncodedParameters(){
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+VALID_SITE_URL+"%3Fx=1%26y=2";
    assertEquals(200,send(url,"GET"));
  }

  /**
   * Test the proxy can handle a site URL that contains unencoded parameters including a second "?" character
   */
  @Test
  public void getWithUnencodedParameters(){
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+VALID_SITE_URL+"?x=1&y=2";
    assertEquals(200,send(url,"GET"));
  }

  /**
   * Test the proxy can handle a site URL that contains unencoded parameters appended in the usual form generated
   * by JQuery
   */
  @Test
  public void getWithJQueryEncodedParameters(){
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+VALID_SITE_URL+"&x=y";
    assertEquals(200,send(url,"GET"));
    assertEquals(405,send(url,"POST")); // This URL doesn't support POST
  }

  /**
   * Test that the proxy behaves as expected when a POST request contains a mix of querystring parameters and post parameters
   * @throws Exception
   */
  @Test
  public void postWithMixedQueryAndParameters() throws Exception{
    HttpClient client = new HttpClient();
    PostMethod req;
    req = new PostMethod(PROXY_URL+"?instanceid_key="+instance_id_key);
    req.addParameter("url", PROTECTED_SITE_URL);
    req.addParameter("username", "java");
    req.addParameter("password", "java");
    client.executeMethod(req);
    int code = req.getStatusCode();
    req.releaseConnection();
    assertEquals(200,code);
  }

  /**
   * This tests accessing a site passing through POST parameters
   * @throws Exception
   */
  @Test
  public void postWithPassingParameters() throws Exception{
    String url = PROXY_URL;
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(url);
    post.addParameter("instanceid_key", instance_id_key);
    post.addParameter("url", TEST_PROPERTIES_SERVICE_URL_VALID);
    post.addParameter("api_key", API_KEY_VALID);
    post.addParameter("widgetid", WIDGET_ID_VALID);
    post.addParameter("userid", "test");
    post.addParameter("is_public", "false");
    post.addParameter("shareddatakey","proxytest");
    post.addParameter("propertyname", "pass");
    post.addParameter("propertyvalue","pass");
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(201,code);
    post.releaseConnection();

    client = new HttpClient();
    GetMethod get = new GetMethod(TEST_PROPERTIES_SERVICE_URL_VALID);
    get.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+WIDGET_ID_VALID+"&userid=test&shareddatakey=proxytest&propertyname=pass");
    client.executeMethod(get);
    code = get.getStatusCode();
    assertEquals(200, code);
    String resp = get.getResponseBodyAsString();
    assertEquals("pass",resp);
    get.releaseConnection();
  }

  /**
   * Tests that the proxy behaves correctly when using POST parameters only
   * @throws Exception
   */
  @Test
  public void postWithOnlyParameters() throws Exception{
    HttpClient client = new HttpClient();
    PostMethod req;
    req = new PostMethod(PROXY_URL);
    req.addParameter("url", PROTECTED_SITE_URL);
    req.addParameter("instanceid_key", instance_id_key);
    req.addParameter("username", "java");
    req.addParameter("password", "java");
    client.executeMethod(req);
    int code = req.getStatusCode();
    req.releaseConnection();
    assertEquals(200,code);
  }

  /**
   * Test that the proxy passes through the correct status code when trying to access 
   * an allowed resource without authn credentials required at the final target site
   * @throws Exception
   */
  @Test
  public void getProtectedSiteWithoutAuth() throws Exception{
    HttpClient client = new HttpClient();
    HttpMethod req;
    req = new GetMethod(PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+PROTECTED_SITE_URL);
    client.executeMethod(req);
    int code = req.getStatusCode();
    req.releaseConnection();
    assertEquals(401,code);
  }

  /**
   * Test that the proxy passes along credentials to the proxied site and is granted access
   * @throws Exception
   */
  @Test
  @Ignore // we have disabled this functionality
  public void getProtectedSiteWithBasicAuth() throws Exception{
    HttpClient client = new HttpClient();
    List<String> authPrefs =  new ArrayList<String>(2);
    authPrefs.add(AuthPolicy.DIGEST );
    authPrefs.add(AuthPolicy.BASIC);
    client.getParams().setParameter (AuthPolicy.AUTH_SCHEME_PRIORITY, authPrefs);
    //
    // send the basic authentication response even before the server gives an unauthorized response
    //
    client.getParams().setAuthenticationPreemptive(true);
    client.getState().setCredentials(
        new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM),
        new UsernamePasswordCredentials("java", "java"));		
    HttpMethod req;
    req = new GetMethod(PROXY_URL+"?instanceid_key="+instance_id_key+"&url="+PROTECTED_SITE_URL);
    client.executeMethod(req);
    int code = req.getStatusCode();
    req.releaseConnection();
    assertEquals(200,code);
  }

  /**
   * Test that proxy responses contain headers from the originating site. See WOOKIE-251
   * @throws IOException
   */
  @Test
  @Ignore // we have disabled this functionality
  public void getWithCustomResponseHeaders() throws IOException{

    //
    // Create a custom server
    //
    ProxiedServer server = new ProxiedServer(9000);
    String testUrl = "http://localhost:9000";

    //
    // Check the server works
    //
    HttpClient client = new HttpClient();
    HttpMethod req = new GetMethod(testUrl);

    client.executeMethod(req);
    int code = req.getStatusCode();

    String header = req.getResponseHeader("CUSTOM-WOOKIE").getValue();
    assertEquals(200,code);
    assertEquals("hhrooar", header);
    req.releaseConnection();

    //
    // Make a proxed request, and check we get the custom header back
    //
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url=http://localhost:9000";
    req = new GetMethod(url);
    client.executeMethod(req);
    code = req.getStatusCode();
    
    header = req.getResponseHeader("CUSTOM-WOOKIE").getValue();

    req.releaseConnection();
    assertEquals(200,code);
    assertEquals("hhrooar", header);

    server.stop();
  }

  /**
   * Test that proxy responses contain headers from the originating site. See WOOKIE-251
   * @throws IOException
   */
  @Test
  @Ignore // we have disabled this functionality
  public void getWithCookies() throws IOException{

    //
    // Create a custom server
    //
    ProxiedServer server = new ProxiedServer(9000);
    String testUrl = "http://localhost:9000";

    //
    // Check the server works
    //
    HttpClient client = new HttpClient();
    HttpMethod req = new GetMethod(testUrl);

    client.executeMethod(req);
    int code = req.getStatusCode();
    String header = req.getResponseHeader("Set-Cookie").getValue();
    assertEquals(200,code);
    assertEquals("test=wookie", header);
    req.releaseConnection();

    //
    // Make a proxed request, and check we get the custom header back
    //
    String url = PROXY_URL+"?instanceid_key="+instance_id_key+"&url=http://localhost:9000";
    req = new GetMethod(url);
    client.executeMethod(req);
    code = req.getStatusCode();
    header = req.getResponseHeader("Set-Cookie").getValue();

    req.releaseConnection();
    assertEquals(200,code);
    assertEquals("test=wookie", header);

    //
    // Make another proxied request, this time send the cookie
    //
    String cookieurl = PROXY_URL+"?instanceid_key="+instance_id_key+"&url=http://localhost:9000";
    req = new GetMethod(cookieurl);
    req.addRequestHeader("Cookie", "test=wookie");
    client.executeMethod(req);
    code = req.getStatusCode();
    header = req.getResponseHeader("GOT-COOKIE").getValue();

    req.releaseConnection();
    assertEquals(200,code);
    assertEquals("true", header);

    server.stop();
  }

  
  /**
   * A subclass of NanoHTTPD for testing access to servers using HTTP Basic Auth
   */
  static class ProxiedSecureServer extends NanoHTTPD{

    /* (non-Javadoc)
     * @see org.apache.wookie.tests.helpers.NanoHTTPD#serve(java.lang.String, java.lang.String, java.util.Properties, java.util.Properties, java.util.Properties)
     */
    @Override
    public Response serve(String uri, String method, Properties header,
        Properties parms, Properties files) {
        
        Response response = new Response();
        String auth = header.getProperty("authorization");
        if (auth != null && auth.equals("Basic amF2YTpqYXZh")){
          response.status = "200";          
        } else {
          response.status = "401";
        }
        return response;
    }

    /**
     * @param port
     * @throws IOException
     */
    public ProxiedSecureServer(int port) throws IOException {
      super(port);
    }
    
    
  }

  /**
   * A subclass of NanoHTTPD for testing 
   */
  class ProxiedServer extends NanoHTTPD{

    /**
     * @param port
     * @throws IOException
     */
    public ProxiedServer(int port) throws IOException {
      super(port);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.tests.helpers.NanoHTTPD#serve(java.lang.String, java.lang.String, java.util.Properties, java.util.Properties, java.util.Properties)
     */
    @Override
    public Response serve(String uri, String method, Properties header,
        Properties parms, Properties files) {
      Response response = new Response();
      response.addHeader("CUSTOM-WOOKIE", "hhrooar");
      response.addHeader("Set-Cookie","test=wookie");
      String cookie = header.getProperty("cookie");
      if (cookie != null && cookie.contains("test=wookie")){
        response.addHeader("GOT-COOKIE", "true");
      }
      response.status = "200 OK";
      return response;
    }
  }

  /**
   * Utility method for sending requests
   * @param url
   * @param method
   * @return
   */
  private int send(String url, String method){
    try {
      HttpClient client = new HttpClient();
      HttpMethod req;
      if (method.equals("POST")){
        req = new PostMethod(url);
      } else {
        req = new GetMethod(url);
      }
      client.executeMethod(req);
      int code = req.getStatusCode();
      req.releaseConnection();
      return code;
    }
    catch (Exception e) {
      e.printStackTrace();
      fail("post failed");
      return -1;
    }
  }

}
