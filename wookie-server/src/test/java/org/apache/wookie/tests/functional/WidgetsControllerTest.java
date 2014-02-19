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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.io.FileUtils;
import org.apache.wookie.tests.helpers.Request;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.W3CWidget;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
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
	private static String WIDGET_ID_UPLOAD_POLICIES_TEST = "http://uploadtest/policies";
	private static String WIDGET_ID_UPLOAD_TEST_2 = "http://uploadtest_2";
	private static String WIDGET_ID_WEATHER = "http://www.getwookie.org/widgets/weather";

	@AfterClass
	public static void tearDown() throws HttpException, IOException{
		HttpClient client = new HttpClient();
		setAuthenticationCredentials(client);
		DeleteMethod delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_ACCESS_TEST));
		client.executeMethod(delete);
		delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST));
		client.executeMethod(delete);
		delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST_2));
		client.executeMethod(delete);
		delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_POLICIES_TEST));
		client.executeMethod(delete);
	}

	/*
	 * Utility method for creating a temp directory
	 * @return a new temp directory
	 * @throws IOException
	 */
	public static File createTempDirectory() throws IOException {
		final File temp;

		temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

		if (!(temp.delete())) {
			throw new IOException("Could not delete temp file: "
					+ temp.getAbsolutePath());
		}

		if (!(temp.mkdir())) {
			throw new IOException("Could not create temp directory: "
					+ temp.getAbsolutePath());
		}

		return (temp);
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
		assertEquals(403,code);
		post.releaseConnection();	  
	}

	@Test
	public void importWidget() throws HttpException, IOException{

		Request post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID);

		//
		// Use upload test widget
		//
		File file = new File("src/test/resources/upload-test.wgt");
		assertTrue(file.exists());

		//
		// Add test wgt file to POST
		//
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get 201 (Created)
		//
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(201,code);

		//
		// Cleanup
		//
		Request delete = new Request("DELETE", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST));
		delete.execute(true, false);
	}

	/**
	 * Test that we can import widgets using POST that contain
	 * access requests. See WOOKIE-379.
	 * 
	 * @throws HttpException
	 * @throws IOException
	 * @throws JDOMException 
	 */
	@Test
	public void importWidgetWithAccessPolicies() throws HttpException, IOException, JDOMException{

		Request post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID);

		//
		// Use upload test widget
		//
		File file = new File("src/test/resources/upload-policies-test.wgt");
		assertTrue(file.exists());

		//
		// Add test wgt file to POST
		//
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get 201 (Created)
		//
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(201,code);

		//
		// Check the policy was created
		//
		Request get = new Request("GET", TEST_POLICIES_SERVICE_URL_VALID + "/" + WIDGET_ID_UPLOAD_POLICIES_TEST);
		get.setAccepts("text/xml");
		get.execute(true, false);

		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(get.getResponseBodyAsStream());
		Element policies = doc.getRootElement();
		assertEquals(1, policies.getChildren("policy").size());
		Element policy = policies.getChild("policy");
		assertEquals(WIDGET_ID_UPLOAD_POLICIES_TEST, policy.getAttributeValue("scope"));
		assertEquals("*", policy.getAttributeValue("origin"));
		assertEquals("ALLOW", policy.getAttributeValue("directive"));

		//
		// Cleanup
		//
		Request delete = new Request("DELETE", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_POLICIES_TEST));
		delete.execute(true, false);
	}


	@Test
	public void importWidgetWithDefaultIcon() throws HttpException, IOException, JDOMException{

		Request post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID);

		//
		// Use upload test widget
		//
		File file = new File("src/test/resources/upload-test-2.wgt");
		assertTrue(file.exists());

		//
		// Add test wgt file to POST
		//
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get 201 (Created)
		//
		post.execute(true, false);   
		int code = post.getStatusCode();
		assertEquals(201,code);
		//
		// Lets take a look at the metadata...
		//
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(post.getResponseBodyAsStream());
		Element iconElement = doc.getRootElement().getChild("icon", doc.getRootElement().getNamespace());
		assertNotNull(iconElement);
		assertEquals("http://localhost:8080/wookie/deploy/uploadtest_2/icon.png", iconElement.getAttributeValue("src")); 
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

		Request post = new Request("POST",TEST_WIDGETS_SERVICE_URL_VALID);

		//
		// We'll use a copy of the unsupported widget widget for testing
		//
		File file = new File("src/test/resources/not_a_widget.zip");
		assertTrue(file.exists());

		//
		// Add test wgt file to POST
		//
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get a 400
		//
		post.execute(true, false);  
		int code = post.getStatusCode();
		assertEquals(400,code);   
	}

	/**
	 * Test to ensure that, given two widgets with identical filenames, the second uploaded file
	 * does not overwrite the first when it comes to downloading the widget. See WOOKIE-402
	 * 
	 * @throws BadWidgetZipFileException
	 * @throws BadManifestException
	 * @throws Exception
	 */
	@Test
	public void importWidgetSameFilename() throws BadWidgetZipFileException, BadManifestException, Exception{

		Request post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID);

		//
		// Use upload test widget
		//
		File uploadFile = new File("src/test/resources/upload-test.wgt");
		assertTrue(uploadFile.exists());

		//
		// Add test wgt file to POST
		//
		Part[] parts = { new FilePart(uploadFile.getName(), uploadFile) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get 201 (Created)
		//
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(201,code);

		//
		// Now, upload a new widget, with the same filename
		// We'll use the WARP widget
		//
		uploadFile = new File("src/test/resources/upload-policies-test.wgt");
		assertTrue(uploadFile.exists());
		File tempFolder = createTempDirectory();
		File newFile  = new File(tempFolder+File.separator+"upload-test.wgt");
		FileUtils.copyFile(uploadFile, newFile);  
		assertTrue(newFile.exists());

		//
		// Add test wgt file to POST
		//
		post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID);
		Part[] newParts = { new FilePart(newFile.getName(), newFile) };
		post.setRequestEntity(new MultipartRequestEntity(newParts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get 201 (Created)
		//
		post.execute(true, false);
		code = post.getStatusCode();
		assertEquals(201,code);

		//
		// Now lets try to download the first test widget; this should not
		// have been overwritten by the second one we uploaded, even though we
		// gave it the same file name
		//
		Request get = new Request("GET", TEST_WIDGETS_SERVICE_URL_VALID+"/"+"http://uploadtest");
		get.setAccepts("application/widget");
		get.execute(false, false);
		code = get.getStatusCode();
		assertEquals(200,code);

		InputStream inputStream = get.getResponseBodyAsStream();
		tempFolder = createTempDirectory();
		File downloadedFile = new File(tempFolder + File.separator + "downloadedTestWidget.zip");
		OutputStream out = new FileOutputStream(downloadedFile);

		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		inputStream.close();
		out.flush();
		out.close();

		//
		// check the downloaded file
		//
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setStartPageProcessor(null);
		File outputDir = new File("src/test" + File.separatorChar + "resources" + File.separatorChar + "widgets");
		outputDir.mkdirs();
		fac.setOutputDirectory("src/test" + File.separatorChar + "resources" + File.separatorChar + "widgets");
		W3CWidget widget = fac.parse(downloadedFile);

		//
		// Cleanup
		//
		FileUtils.deleteQuietly(downloadedFile);
		FileUtils.deleteQuietly(tempFolder);
		FileUtils.deleteQuietly(newFile);
		Request delete = new Request("DELETE", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST));
		delete.execute(true, false);
		delete = new Request("DELETE", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_POLICIES_TEST));
		delete.execute(true, false);

		assertEquals("http://uploadtest", widget.getIdentifier());


	}

	@Test
	public void deleteWidgetUnauthorized() throws HttpException, IOException{
		HttpClient client = new HttpClient();
		DeleteMethod delete = new DeleteMethod(TEST_WIDGETS_SERVICE_URL_VALID + "/" + WIDGET_ID_WEATHER);
		client.executeMethod(delete);
		assertEquals(403, delete.getStatusCode());

		//
		// Check it wasn't deleted
		//
		GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID + "/" + WIDGET_ID_WEATHER);
		client.executeMethod(get);
		assertEquals(200, get.getStatusCode());
	}

	@Test
	public void deleteWidgetNonexisting() throws HttpException, IOException{
		Request delete = new Request("DELETE", TEST_WIDGETS_SERVICE_URL_VALID + "/9999");
		delete.execute(true, false);
		assertEquals(404, delete.getStatusCode());
	}

	@Test
	public void deleteWidget() throws HttpException, IOException{

		//
		// Upload widget we'll test deleting next
		//
		File file = new File("src/test/resources/delete-test.wgt");
		assertTrue(file.exists());
		Request post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID);
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(201,code);

		//
		// Delete the widget
		//
		Request delete = new Request("DELETE", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_DELETE_TEST));
		delete.execute(true, false);
		assertEquals(200, delete.getStatusCode());

		//
		// Check it was deleted
		//
		Request get = new Request("GET", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_DELETE_TEST)); 
		get.execute(false, false);
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

		//
		// Use upload test widget
		//
		File file = new File("src/test/resources/upload-test.wgt");
		assertTrue(file.exists());

		//
		// Add test wgt file to POST
		//
		Request post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST)); 
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get 201 (Created)
		//
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(201,code);

		//
		// Now lets try updating
		//
		post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST)); 

		//
		// Use upload test widget
		//
		file = new File("src/test/resources/upload-test.wgt");
		assertTrue(file.exists());

		//
		// Add test wgt file to POST
		//
		Part[] newParts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(newParts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get 200 (Updated)
		//
		post.execute(true, false);
		code = post.getStatusCode();
		assertEquals(200,code);

	}

	@Test
	public void updateWidgetByPut() throws HttpException, IOException{

		Request put = new Request("PUT", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST));

		//
		// Use upload test widget
		//
		File file = new File("src/test/resources/upload-test.wgt");
		assertTrue(file.exists());

		//
		// Add test wgt file to PUT
		//
		Part[] parts = { new FilePart(file.getName(), file) };
		put.setRequestEntity(new MultipartRequestEntity(parts, put.getClient()
				.getParams()));

		//
		// PUT the file to /widgets and check we get 200 (Updated)
		//
		put.execute(true, false);  
		int code = put.getStatusCode();
		assertEquals(200,code);

	}	

	@Test
	public void updateWidgetUnauthorized() throws HttpException, IOException{

		Request post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_UPLOAD_TEST));

		//
		// Use upload test widget
		//
		File file = new File("src/test/resources/upload-test.wgt");
		assertTrue(file.exists());

		//
		// Add test wgt file to POST
		//
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get 200 (Updated)
		//
		post.execute(false, false);
		int code = post.getStatusCode();
		assertEquals(403,code);
	}

	@Test
	public void updateWidgetNotFound() throws HttpException, IOException{

		Request post = new Request("PUT", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_INVALID));

		//
		// Use upload test widget
		//
		File file = new File("src/test/resources/upload-test.wgt");
		assertTrue(file.exists());

		//
		// Add test wgt file to POST
		//
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get 200 (Updated)
		//
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(404,code);
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

			Request post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID);

			//
			// Add the access test widget. This just has a single access request
			// for the origin "http://accesstest.incubator.apache.org"
			//
			File file = new File("src/test/resources/access-test.wgt");
			assertTrue(file.exists());

			//
			// Add test wgt file to POST
			//
			Part[] parts = { new FilePart(file.getName(), file) };
			post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
					.getParams()));

			//
			// POST the file to /widgets 
			//
			post.execute(true, false);
		} 

		//
		// Check that we only have one copy of the access request, not two
		//
		Request get = new Request("GET", TEST_POLICIES_SERVICE_URL_VALID + encodeString("/" + WIDGET_ID_ACCESS_TEST));
		get.setAccepts("text/xml");
		get.execute(true, false);
		assertEquals(1,PoliciesControllerTest.processPolicies(get.getResponseBodyAsStream()).getChildren("policy").size());

	}

	@Test
	public void workingWithUnidentifiedWidgets() throws HttpException, IOException, JDOMException{

		Request post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID);

		//
		// Add the access test widget. This just has a single access request
		// for the origin "http://accesstest.incubator.apache.org"
		//
		File file = new File("src/test/resources/upload-test-noid.wgt");
		assertTrue(file.exists());

		//
		// Add test wgt file to POST
		//
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets 
		//
		post.execute(true, false); 
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(post.getResponseBodyAsStream());
		String id = doc.getRootElement().getAttributeValue("id");

		//
		// Now we'll update it
		//
		Request put = new Request("PUT", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + id));

		//
		// Add test wgt file to PUT
		//
		Part[] updateparts = { new FilePart(file.getName(), file) };
		put.setRequestEntity(new MultipartRequestEntity(updateparts, put.getClient()
				.getParams()));

		//
		// PUT the file to /widgets and check we get 200 (Updated)
		//
		put.execute(true, false);
		int code = put.getStatusCode();
		assertEquals(200,code);

		//
		// DELETE the widget
		//
		Request delete = new Request("DELETE", TEST_WIDGETS_SERVICE_URL_VALID + encodeString("/" + id));
		delete.execute(true, false);
	}

	/**
	 * Download a widget using the Accept type of ("Accept","application/widget")
	 * Once downloaded make sure that the widget package is correct - run it through the parser
	 * @throws Exception
	 */
	@Test
	public void getActualWidget() throws Exception{
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(TEST_WIDGETS_SERVICE_URL_VALID+"/"+WIDGET_ID_WEATHER);
		get.setRequestHeader("Accept","application/widget");
		client.executeMethod(get);
		int code = get.getStatusCode();
		assertEquals(200,code);

		InputStream inputStream = get.getResponseBodyAsStream();
		File downloadedFile = new File("src"+ File.separatorChar + "test" + File.separatorChar + "resources" + File.separatorChar + "downloadedWeatherWidget.zip");
		OutputStream out = new FileOutputStream(downloadedFile);

		int read = 0;
		byte[] bytes = new byte[1024];

		while ((read = inputStream.read(bytes)) != -1) {
			out.write(bytes, 0, read);
		}
		inputStream.close();
		out.flush();
		out.close();
		get.releaseConnection();

		// check the downloaded file
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setStartPageProcessor(null);
		File outputDir = new File("src"+File.separatorChar+"test" + File.separatorChar + "resources" + File.separatorChar + "widgets");
		outputDir.mkdirs();
		fac.setOutputDirectory("src" + File.separatorChar + "test" + File.separatorChar + "resources" + File.separatorChar + "widgets");
		W3CWidget widget = fac.parse(downloadedFile);
		File file = fac.getUnzippedWidgetDirectory();
		assertEquals("src"  + File.separatorChar + "test" + File.separatorChar + "resources"
				+ File.separatorChar + "widgets" + File.separatorChar
				+ "www.getwookie.org" + File.separatorChar + "widgets"
				+ File.separatorChar + "weather", file.getPath());
		assertTrue(file.isDirectory());
		assertEquals(WIDGET_ID_WEATHER, widget.getIdentifier());

		//clean up
		FileUtils.deleteDirectory(outputDir);
		FileUtils.deleteQuietly(downloadedFile);
	}

}
