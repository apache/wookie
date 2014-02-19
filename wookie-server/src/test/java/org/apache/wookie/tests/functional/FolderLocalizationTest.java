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
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.lang.StringUtils;
import org.apache.wookie.tests.helpers.Request;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests folder-based localization using the localetest widget
 */
public class FolderLocalizationTest extends AbstractControllerTest {

	private static String WIDGET_START_URL_ROOT;

	private static HttpClient client;
	private static GetMethod get;

	/**
	 * Setup a widget instance to test with, and create a shared HTTP client with
	 * cookies enabled.
	 * 
	 * @throws HttpException
	 * @throws IOException
	 */
	@BeforeClass
	public static void setup() throws HttpException, IOException {    
		//
		// Setup POST method
		//
		Request post = new Request("POST", TEST_WIDGETS_SERVICE_URL_VALID);

		//
		// Get the locale test widget
		//
		File file = new File("src/test/resources/localetest.wgt");
		assertTrue(file.exists());

		//
		// Add test wgt file to POST
		//
		Part[] parts = { new FilePart(file.getName(), file) };
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient().getParams()));

		//
		// POST the file to /widgets and check we get 201 (Created)
		//
		post.execute(true, false);  
		int code = post.getStatusCode();
		assertEquals(201,code);
		client = post.getClient();

		//
		// Create a widget instance localized to Yorkshire English
		//
		post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID, client);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "foldertest1");
		post.addParameter("shareddatakey", "foldertest1");
		post.addParameter("locale", "en-gb-yorks");

		post.execute(true, false);

		//
		// Get the root URL
		//
		WIDGET_START_URL_ROOT = getStartFile(post.getResponseBodyAsString());

		//
		// We have to load the start file in order to start the session
		//
		getResource(WIDGET_START_URL_ROOT);

		//
		// take off the resource bit
		//
		WIDGET_START_URL_ROOT = getRootUrl(WIDGET_START_URL_ROOT);

	}

	@AfterClass
	public static void tearDown() throws HttpException, IOException{
		Request delete = new Request("DELETE", TEST_WIDGETS_SERVICE_URL_VALID + "/" + WIDGET_ID_LOCALIZED);
		delete.execute(true, false);
	}  

	/**
	 * Gets the start file url for an instance
	 * 
	 * @param response
	 * @return
	 */
	private static String getStartFile(String response) {
		SAXBuilder builder = new SAXBuilder();
		Reader in = new StringReader(response);
		Document doc;
		try {
			doc = builder.build(in);
		} catch (Exception e) {
			return null;
		}
		return doc.getRootElement().getChild("url").getText();
	}

	/**
	 * Gets a resource and returns the final URL after redirection
	 * 
	 * @param url
	 * @return
	 */
	private static String getResource(String url) {
		get = new GetMethod(url);
		get.setFollowRedirects(true);
		try {
			client.executeMethod(get);
			return TEST_SERVER_ORIGIN + get.getPath();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Test that the root resource is overridden by the EN-GB-YORKS localized
	 * resource
	 */
	@Test
	public void getLocalizedResourceFromRootPath() {
		String resource = "index.htm";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT + "locales/en-gb-yorks/" + resource,
				getResource(url));
	}

	/**
	 * The root resource is correct, so should not be redirected
	 */
	@Test
	public void getRootResourceFromRootResource() {
		String resource = "Images/test.png";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT + resource, getResource(url));
	}

	/**
	 * The FR resource is replaced by the Root resource as there is no EN resource
	 */
	@Test
	public void getRootResourceFromIncorrectLocalizedPath() {
		String resource = "locales/fr/Images/test.png";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT + "Images/test.png", getResource(url));
	}

	/**
	 * The EN resource is replaced by the Root resource as there is no EN resource
	 */
	@Test
	public void getRootResourceFromLocalizedPath() {
		String resource = "locales/en/Images/test.png";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT + "Images/test.png", getResource(url));
	}

	/**
	 * The FR resource is replaced by the EN-GB-YORKS resource
	 */
	@Test
	public void getLocalizedResourceFromIncorrectLocalizedPath() {
		String resource = "locales/fr/index.htm";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT + "locales/en-gb-yorks/index.htm",
				getResource(url));
	}

	/**
	 * The EN-GB-YORKS resource is not available and is replaced by the EN
	 * resource
	 */
	@Test
	public void getLocalizedResourceGracefulDegradation() {
		String resource = "locales/en-gb-yorks/test.txt";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT + "locales/en/test.txt",
				getResource(url));
	}

	/**
	 * Request the instance with different locales, and check that the correct
	 * resources are returned in each case (i.e. that the locale of the instance
	 * has changed)
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void updateLocalizedResources() throws HttpException, IOException {
		//
		// Update the widget instance localized to French
		//
		Request post = new Request("POST",TEST_INSTANCES_SERVICE_URL_VALID, client);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "foldertest1");
		post.addParameter("shareddatakey", "foldertest1");
		post.addParameter("locale", "fr");
		post.execute(true, false);
		WIDGET_START_URL_ROOT = getStartFile(post.getResponseBodyAsString());
		assertEquals(200, post.getStatusCode());
		//
		// We have to load the start file in order to start the session
		//
		getResource(WIDGET_START_URL_ROOT);

		//
		// take off the resource bit
		//
		WIDGET_START_URL_ROOT = getRootUrl(WIDGET_START_URL_ROOT);

		String resource = "index.htm";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT + "locales/fr/index.htm",getResource(url));

		//
		// Update the widget instance localized to English
		//
		post = new Request("POST",TEST_INSTANCES_SERVICE_URL_VALID, client);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "foldertest1");
		post.addParameter("shareddatakey", "foldertest1");
		post.addParameter("locale", "en");
		post.execute(true, false);

		WIDGET_START_URL_ROOT = getStartFile(post.getResponseBodyAsString());

		//
		// We have to load the start file in order to start the session
		//
		getResource(WIDGET_START_URL_ROOT);

		//
		// take off the resource bit
		//
		WIDGET_START_URL_ROOT = getRootUrl(WIDGET_START_URL_ROOT);

		resource = "index.htm";
		url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT + "locales/en/index.htm",
				getResource(url));

		//
		// Update the widget instance unlocalized (expecting the default locale here
		// to be "en")
		//
		post = new Request("POST",TEST_INSTANCES_SERVICE_URL_VALID, client);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "foldertest1");
		post.addParameter("shareddatakey", "foldertest1");
		post.execute(true, false);
		assertEquals(200, post.getStatusCode());

		WIDGET_START_URL_ROOT = getStartFile(post.getResponseBodyAsString());

		//
		// We have to load the start file in order to start the session
		//
		getResource(WIDGET_START_URL_ROOT);

		//
		// take off the resource bit
		//
		WIDGET_START_URL_ROOT = getRootUrl(WIDGET_START_URL_ROOT);

		resource = "index.htm";
		url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT + "locales/en/index.htm",
				getResource(url));
	}

	/**
	 * Removes the resource path and query components of a given URL
	 * @param url
	 * @return the "root" url
	 */
	private static String getRootUrl(String url){
		String path = url.substring(url.indexOf("locales"));
		url = StringUtils.remove(url, path);
		return url;
	}

}
