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

import java.io.File;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.io.FileUtils;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.jdom.JDOMException;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * Test cases for the Widget REST API
 */
public class WidgetsControllerTest extends AbstractControllerTest {
  
  private static String WIDGET_ID_ACCESS_TEST = "http://wookie.apache.org/widgets/access-test";
  private static String WIDGET_ID_DELETE_TEST = "http://deletetest";
  private static String WIDGET_ID_NOT_SUPPORTED = "http://notsupported";
  private static String WIDGET_ID_UPLOAD_TEST = "http://uploadtest";
  
  @AfterClass
  public static void tearDown() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    setAuthenticationCredentials(client);
    DeleteMethod delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_ACCESS_TEST));
    client.executeMethod(delete);
    delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST));
    client.executeMethod(delete);
  }
	
  /**
   * Test GET all widgets
   * @throws IOException 
   * @throws HttpException 
   */
	@Test
	public void getAllWidgets() throws HttpException, IOException{
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID);
	        get.setQueryString("all=true");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        assertTrue(response.contains("<widget id=\"http://notsupported\""));
	        get.releaseConnection();
	}
	
	/**
	 * Test we can GET a widget using its internal ID as a resource path
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@Test
	public void getSpecificWidget() throws HttpException, IOException{
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/1");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(200,code);
	        String response = get.getResponseBodyAsString();
	        assertTrue(response.contains("<widget id=\"http://notsupported\""));
	        get.releaseConnection();
	}
	
	 /**
   * Test we can GET a widget using its URI as a resource path
   * @throws IOException 
   * @throws HttpException 
   */
  @Test
  public void getSpecificWidgetByUri() throws HttpException, IOException{
          HttpClient client = new HttpClient();
          GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_NOT_SUPPORTED));
          client.executeMethod(get);
          int code = get.getStatusCode();
          assertEquals(200,code);
          String response = get.getResponseBodyAsString();
          assertTrue(response.contains("<widget id=\"http://notsupported\""));
          get.releaseConnection();
  }
  /**
   * Test we can GET a widget using its URI as a resource path
   * @throws IOException 
   * @throws HttpException 
   */
  @Test
  public void getSpecificWidgetByUri2() throws HttpException, IOException{
          HttpClient client = new HttpClient();
          GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID + "/" + WIDGET_ID_NOT_SUPPORTED);
          client.executeMethod(get);
          int code = get.getStatusCode();
          assertEquals(200,code);
          String response = get.getResponseBodyAsString();
          assertTrue(response.contains("<widget id=\"http://notsupported\""));
          get.releaseConnection();
  }
	
	/**
	 * Test that a request for a non-existing widget ID gets a 404
	 * @throws IOException 
	 * @throws HttpException 
	 */
	@Test
	public void getSpecificWidget_nonexisting() throws HttpException, IOException{
	        HttpClient client = new HttpClient();
	        GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/9999");
	        client.executeMethod(get);
	        int code = get.getStatusCode();
	        assertEquals(404,code);
	        get.releaseConnection();
	}
	
	@Test
	public void importWidget_unauthorized() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    PostMethod post = new PostMethod(TEST_WIDGETS_SERVICE_URL_VALID);
    client.executeMethod(post);
    int code = post.getStatusCode();
    assertEquals(401,code);
    post.releaseConnection();	  
	}
	
	@Test
	public void importWidget() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    //
    // Use admin credentials
    //
    setAuthenticationCredentials(client);
    
    PostMethod post = new PostMethod(TEST_WIDGETS_SERVICE_URL_VALID);
    
    //
    // Use upload test widget
    //
    File file = new File("src-tests/testdata/upload-test.wgt");
    assertTrue(file.exists());
    
    //
    // Add test wgt file to POST
    //
    Part[] parts = { new FilePart(file.getName(), file) };
    post.setRequestEntity(new MultipartRequestEntity(parts, post
        .getParams()));
    
    //
    // POST the file to /widgets and check we get 201 (Created)
    //
    client.executeMethod(post);   
    int code = post.getStatusCode();
    assertEquals(201,code);
    post.releaseConnection();  	  
	}
	
	@Test
	public void downloadWidgetPackage() throws BadWidgetZipFileException, BadManifestException, Exception{
    HttpClient client = new HttpClient();	
    GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID + "/" + WIDGET_ID_NOT_SUPPORTED);
    get.setRequestHeader("accept", "application/widget");
    get.setFollowRedirects(true);
    client.executeMethod(get);
    
    assertEquals(200, get.getStatusCode());
    
    File file = File.createTempFile("wookie", ".wgt");
    FileUtils.writeByteArrayToFile(file, get.getResponseBody());
    
    System.out.println(get.getStatusCode());
    
    
    File outputFolder = File.createTempFile("temp", Long.toString(System.nanoTime()));
    outputFolder.delete();
    outputFolder.mkdir();
    
    System.out.println(outputFolder.getPath());
    
    W3CWidgetFactory fac = new W3CWidgetFactory();
    fac.setOutputDirectory(outputFolder.getPath());
    W3CWidget widget = fac.parse(file);
    assertEquals("Unsupported widget widget", widget.getLocalName("en"));
	}
	
	@Test
	public void importWrongFileType() throws HttpException, IOException{
	  HttpClient client = new HttpClient();
	  //
	  // Use admin credentials
	  //
	  setAuthenticationCredentials(client);
	  PostMethod post = new PostMethod(TEST_WIDGETS_SERVICE_URL_VALID);

	  //
	  // We'll use a copy of the unsupported widget widget for testing
	  //
	  File file = new File("src-tests/testdata/not_a_widget.zip");
	  assertTrue(file.exists());

	  //
	  // Add test wgt file to POST
	  //
	  Part[] parts = { new FilePart(file.getName(), file) };
	  post.setRequestEntity(new MultipartRequestEntity(parts, post
	      .getParams()));

	  //
	  // POST the file to /widgets and check we get a 400
	  //
	  client.executeMethod(post);   
	  int code = post.getStatusCode();
	  assertEquals(400,code);
	  post.releaseConnection();     
	}
	
	@Test
	public void deleteWidgetUnauthorized() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    DeleteMethod delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + "/1");
	  client.executeMethod(delete);
	  assertEquals(401, delete.getStatusCode());
	  
    //
    // Check it wasn't deleted
    //
    GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID + "/1");
    client.executeMethod(get);
    assertEquals(200, get.getStatusCode());
	}
	
	@Test
	public void deleteWidgetNonexisting() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    //
    // Use admin credentials
    //
    setAuthenticationCredentials(client);
    DeleteMethod delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + "/9999");
    client.executeMethod(delete);
    assertEquals(404, delete.getStatusCode());
	}
	
	@Test
	public void deleteWidget() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    //
    // Use admin credentials
    //
    setAuthenticationCredentials(client);

    //
    // Upload widget we'll test deleting next
    //
    File file = new File("src-tests/testdata/delete-test.wgt");
    assertTrue(file.exists());
    PostMethod post = new PostMethod(TEST_WIDGETS_SERVICE_URL_VALID);
    Part[] parts = { new FilePart(file.getName(), file) };
    post.setRequestEntity(new MultipartRequestEntity(parts, post
        .getParams()));
    client.executeMethod(post);   
    int code = post.getStatusCode();
    assertEquals(201,code);
    post.releaseConnection();   
    
    //
    // Delete the widget
    //
    DeleteMethod delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_DELETE_TEST)); 
    client.executeMethod(delete);
    assertEquals(200, delete.getStatusCode());
    
    //
    // Check it was deleted
    //
    GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_DELETE_TEST)); 
    client.executeMethod(get);
    assertEquals(404, get.getStatusCode());
	}
	
	/**
	 * We allow updates to existing widgets via POST as well as PUT 
	 * (to allow browsers to update using forms)
	 * @throws HttpException
	 * @throws IOException
	 */
	@Test
	public void updateWidgetByPost() throws HttpException, IOException{
	  HttpClient client = new HttpClient();
    //
    // Use admin credentials
    //
    setAuthenticationCredentials(client);
    
    PostMethod post = new PostMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST)); 
    
    //
    // Use upload test widget
    //
    File file = new File("src-tests/testdata/upload-test.wgt");
    assertTrue(file.exists());
    
    //
    // Add test wgt file to POST
    //
    Part[] parts = { new FilePart(file.getName(), file) };
    post.setRequestEntity(new MultipartRequestEntity(parts, post
        .getParams()));
    
    //
    // POST the file to /widgets and check we get 200 (Updated)
    //
    client.executeMethod(post);   
    int code = post.getStatusCode();
    assertEquals(200,code);
    post.releaseConnection();     
	  
	}
	
  @Test
  public void updateWidgetByPut() throws HttpException, IOException{
    HttpClient client = new HttpClient();
    //
    // Use admin credentials
    //
    setAuthenticationCredentials(client);
    
    PutMethod put = new PutMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST));
    
    //
    // Use upload test widget
    //
    File file = new File("src-tests/testdata/upload-test.wgt");
    assertTrue(file.exists());
    
    //
    // Add test wgt file to PUT
    //
    Part[] parts = { new FilePart(file.getName(), file) };
    put.setRequestEntity(new MultipartRequestEntity(parts, put
        .getParams()));
    
    //
    // PUT the file to /widgets and check we get 200 (Updated)
    //
    client.executeMethod(put);   
    int code = put.getStatusCode();
    assertEquals(200,code);
    put.releaseConnection();     
    
  }	
	
	@Test
	public void updateWidgetUnauthorized() throws HttpException, IOException{
	   HttpClient client = new HttpClient();
    
	   PutMethod post = new PutMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST));
	    
	    //
	    // Use upload test widget
	    //
	    File file = new File("src-tests/testdata/upload-test.wgt");
	    assertTrue(file.exists());
	    
	    //
	    // Add test wgt file to POST
	    //
	    Part[] parts = { new FilePart(file.getName(), file) };
	    post.setRequestEntity(new MultipartRequestEntity(parts, post
	        .getParams()));
	    
	    //
	    // POST the file to /widgets and check we get 200 (Updated)
	    //
	    client.executeMethod(post);   
	    int code = post.getStatusCode();
	    assertEquals(401,code);
	    post.releaseConnection();  
	}
	
	@Test
	public void updateWidgetNotFound() throws HttpException, IOException{
	   HttpClient client = new HttpClient();
	    //
	    // Use admin credentials
	    //
	    setAuthenticationCredentials(client);
	    
	    PutMethod post = new PutMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_INVALID));
	   
	    //
	    // Use upload test widget
	    //
	    File file = new File("src-tests/testdata/upload-test.wgt");
	    assertTrue(file.exists());
	    
	    //
	    // Add test wgt file to POST
	    //
	    Part[] parts = { new FilePart(file.getName(), file) };
	    post.setRequestEntity(new MultipartRequestEntity(parts, post
	        .getParams()));
	    
	    //
	    // POST the file to /widgets and check we get 200 (Updated)
	    //
	    client.executeMethod(post);   
	    int code = post.getStatusCode();
	    assertEquals(404,code);
	    post.releaseConnection();  
	  
	}
	
	/**
	 * Check that when we update a widget, we don't duplicate access policies. See WOOKIE-273.
	 * @throws HttpException
	 * @throws IOException
	 * @throws InterruptedException 
	 * @throws JDOMException 
	 */
	@Test
	public void checkForDuplicateAccessRequests() throws HttpException, IOException, InterruptedException, JDOMException{

	    //
	    // Add the test widget, and update it a few times
	    //
	    for (int i=0;i<4;i++){
	      
	      HttpClient client = new HttpClient();
	      //
	      // Use admin credentials
	      //
	      setAuthenticationCredentials(client);
	      
	      PostMethod post = new PostMethod(TEST_WIDGETS_SERVICE_URL_VALID);

	      //
	      // Add the access test widget. This just has a single access request
	      // for the origin "http://accesstest.incubator.apache.org"
	      //
	      File file = new File("src-tests/testdata/access-test.wgt");
	      assertTrue(file.exists());

	      //
	      // Add test wgt file to POST
	      //
	      Part[] parts = { new FilePart(file.getName(), file) };
	      post.setRequestEntity(new MultipartRequestEntity(parts, post
	          .getParams()));

	      //
	      // POST the file to /widgets 
	      //
	      client.executeMethod(post);   
	      post.releaseConnection(); 
	    } 

	    //
	    // Check that we only have one copy of the access request, not two
	    //
      HttpClient client = new HttpClient();
      setAuthenticationCredentials(client);
	    GetMethod get = new GetMethod(TEST_POLICIES_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_ACCESS_TEST));
	    get.setRequestHeader("accepts","text/xml");
	    client.executeMethod(get);
	    assertEquals(1,PoliciesControllerTest.processPolicies(get.getResponseBodyAsStream()).getChildren("policy").size());

	}
	
}
