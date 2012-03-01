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
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for policies API
 */
public class PoliciesControllerTest extends AbstractControllerTest {
  
  @BeforeClass
  public static void setup() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    PostMethod post = new PostMethod(TEST_POLICIES_SERVICE_URL_VALID);
    StringRequestEntity entity = new StringRequestEntity("http://policies.test.scope http://policies.test.origin ALLOW", "text/plain", "UTF-8");
    post.setRequestEntity(entity);
    client.executeMethod(post);
  }
  
  @AfterClass
  public static void tearDown() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    DeleteMethod delete = new DeleteMethod(TEST_POLICIES_SERVICE_URL_VALID+"/http%3A%2F%2Fpolicies.test.scope%20http%3A%2F%2Fpolicies.test2.origin%20ALLOW");
    client.executeMethod(delete);
    assertEquals(200, delete.getStatusCode()); 
  }
  
  @Test
  public void getPolicies() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    
    GetMethod get = new GetMethod(TEST_POLICIES_SERVICE_URL_VALID);
    get.setRequestHeader("accepts", "text/xml");
    
    client.executeMethod(get);
    try {
      Element policies = processPolicies(get.getResponseBodyAsStream());
      assertEquals("policies", policies.getName());
    } catch (JDOMException e) {
      fail("Response did not contain any policies");
    }
  }
  
  @Test
  public void getPoliciesUnauthorized() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    
    GetMethod get = new GetMethod(TEST_POLICIES_SERVICE_URL_VALID);
    get.setRequestHeader("accepts", "text/xml");
    
    client.executeMethod(get);
    assertEquals(401, get.getStatusCode());
  }
  
  @Test
  public void getPoliciesWithScope() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    
    //
    // We should have a policy for the Weather widget by default, so lets test with that
    //
    GetMethod get = new GetMethod(TEST_POLICIES_SERVICE_URL_VALID+"/http://policies.test.scope");
    get.setRequestHeader("accepts", "text/xml");
    
    client.executeMethod(get);
    try {
      Element policies = processPolicies(get.getResponseBodyAsStream());
      assertEquals("policies", policies.getName());
      assertEquals(1, policies.getChildren("policy").size());
    } catch (JDOMException e) {
      fail("Response did not contain any policies");
    }
  }
  
  @Test
  public void getPoliciesWithNonexistingScope() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    
    GetMethod get = new GetMethod(TEST_POLICIES_SERVICE_URL_VALID+"/nosuchscope");
    get.setRequestHeader("accepts", "text/xml");
    
    client.executeMethod(get);
    try {
      Element policies = processPolicies(get.getResponseBodyAsStream());
      assertEquals("policies", policies.getName());
      assertEquals(0, policies.getChildren("policy").size());
    } catch (JDOMException e) {
      fail("Response did not contain any policies");
    }
  }
  
  @Test
  public void createPolicyUnauthorized() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_POLICIES_SERVICE_URL_VALID);
    StringRequestEntity entity = new StringRequestEntity("http://dodgyplace.org * ALLOW", "text/plain", "UTF-8");
    post.setRequestEntity(entity);
    client.executeMethod(post);
    assertEquals(401,post.getStatusCode());
  }
  
  @Test
  public void createPolicyInvalidDirective() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    PostMethod post = new PostMethod(TEST_POLICIES_SERVICE_URL_VALID);
    StringRequestEntity entity = new StringRequestEntity("http://dodgyplace.org * MAYBE", "text/plain", "UTF-8");
    post.setRequestEntity(entity);
    client.executeMethod(post);
    assertEquals(400,post.getStatusCode());    
  }
  
  @Test
  public void createPolicyInvalidScope() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    PostMethod post = new PostMethod(TEST_POLICIES_SERVICE_URL_VALID);
    StringRequestEntity entity = new StringRequestEntity("FAIL * DENY", "text/plain", "UTF-8");
    post.setRequestEntity(entity);
    client.executeMethod(post);
    assertEquals(400,post.getStatusCode());    
  }
  
  @Test
  public void createPolicyInvalidOrigin() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    PostMethod post = new PostMethod(TEST_POLICIES_SERVICE_URL_VALID);
    StringRequestEntity entity = new StringRequestEntity("http://dodgyplace.org FAIL DENY", "text/plain", "UTF-8");
    post.setRequestEntity(entity);
    client.executeMethod(post);
    assertEquals(400,post.getStatusCode());    
  }
  
  @Test
  public void createPolicy() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    PostMethod post = new PostMethod(TEST_POLICIES_SERVICE_URL_VALID);
    StringRequestEntity entity = new StringRequestEntity("http://policies.test.scope http://policies.test2.origin ALLOW", "text/plain", "UTF-8");
    post.setRequestEntity(entity);
    client.executeMethod(post);
    assertEquals(201,post.getStatusCode());
  }
  
  @Test
  public void deletePolicyUnauthorized() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    DeleteMethod delete = new DeleteMethod(TEST_POLICIES_SERVICE_URL_VALID+"/http%3A%2F%2Fpolicies.test.scope%20http%3A%2F%2Fpolicies.test.origin%20ALLOW");
    client.executeMethod(delete);
    assertEquals(401, delete.getStatusCode());   
  }
  
  @Test
  public void deletePolicyNonexistant() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    DeleteMethod delete = new DeleteMethod(TEST_POLICIES_SERVICE_URL_VALID+"/http%3A%2F%2Fno.such.scope%20http%3A%2F%2Fpolicies.test.origin%20ALLOW");
    client.executeMethod(delete);
    assertEquals(200, delete.getStatusCode());
  }
  
  @Test
  public void deletePolicy() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    DeleteMethod delete = new DeleteMethod(TEST_POLICIES_SERVICE_URL_VALID+"/http%3A%2F%2Fpolicies.test.scope%20http%3A%2F%2Fpolicies.test.origin%20ALLOW");
    client.executeMethod(delete);
    assertEquals(200, delete.getStatusCode());
  }
  
  public Element processPolicies(InputStream in) throws JDOMException, IOException{
    SAXBuilder builder = new SAXBuilder();
    Document doc = builder.build(in);
    Element policies = doc.getRootElement();
    return policies;
  }
  

}
