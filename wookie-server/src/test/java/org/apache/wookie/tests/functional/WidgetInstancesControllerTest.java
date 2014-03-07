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
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.wookie.tests.helpers.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test cases for the Widget Instances REST API
 */
public class WidgetInstancesControllerTest extends AbstractControllerTest {

	private static String TEST_ID_KEY = "";

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
		post.setRequestEntity(new MultipartRequestEntity(parts, post.getClient()
				.getParams()));

		//
		// POST the file to /widgets and check we get 201 (Created)
		//
		post.execute(true, false); 
		int code = post.getStatusCode();
		assertEquals(201,code);
	}

	@AfterClass
	public static void tearDown() throws HttpException, IOException{
		Request delete;  

		delete = new Request("DELETE", TEST_INSTANCES_SERVICE_URL_VALID + "?api_key=" + API_KEY_VALID + "&widgetid="
				+ WIDGET_ID_VALID + "&userid=test&shareddatakey=test");
		delete.execute(true, false);

		delete = new Request("DELETE", TEST_INSTANCES_SERVICE_URL_VALID + "?api_key=" + API_KEY_VALID + "&widgetid="
				+ WIDGET_ID_VALID + "&userid=test&shareddatakey=clonetestsrc");
		delete.execute(true, false);  

		delete = new Request("DELETE", TEST_INSTANCES_SERVICE_URL_VALID + "?api_key=" + API_KEY_VALID + "&widgetid="
				+ WIDGET_ID_VALID + "&userid=test&shareddatakey=clonetestsync");
		delete.execute(true, false);            

		delete = new Request("DELETE", TEST_WIDGETS_SERVICE_URL_VALID + "/" + WIDGET_ID_LOCALIZED);
		delete.execute(true, false);
	}

	/**
	 * Test we can get an instance localized using the locale parameter
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void getLocalizedInstance() throws HttpException, IOException {
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "localetest");
		post.addParameter("shareddatakey", "localetest");
		post.addParameter("locale", "fr");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(200, code);
		assertTrue(post.getResponseBodyAsString().contains("locales/fr/index.htm"));
		assertTrue(post.getResponseBodyAsString().contains(
		"tester les param&#232;tres r&#233;gionaux"));
		TEST_ID_KEY = post.getResponseBodyAsString().substring(
				post.getResponseBodyAsString().indexOf("<identifier>") + 12,
				post.getResponseBodyAsString().indexOf("</identifier>"));
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
		Request get = new Request("GET", TEST_INSTANCES_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		get.addParameter("userid", "localetest");
		get.addParameter("shareddatakey", "localetest");
		get.addParameter("locale", "fr");
		get.execute(true, false);
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
		Request get = new Request("GET", TEST_INSTANCES_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("idkey", TEST_ID_KEY);
		get.execute(true, false);
		int code = get.getStatusCode();
		assertEquals(200, code);
		System.out.println(get.getResponseBodyAsString());
		assertTrue(get.getResponseBodyAsString().contains("locales/fr/index.htm"));
		assertTrue(get.getResponseBodyAsString().contains(
		"tester les param&#232;tres r&#233;gionaux"));
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
		Request get = new Request("GET", TEST_INSTANCES_SERVICE_URL_VALID + "/"
				+ TEST_ID_KEY);
		get.addParameter("api_key", API_KEY_VALID);
		get.execute(true, false);
		int code = get.getStatusCode();
		assertEquals(200, code);
		assertTrue(get.getResponseBodyAsString().contains("locales/fr/index.htm"));
		assertTrue(get.getResponseBodyAsString().contains(
		"tester les param&#232;tres r&#233;gionaux"));
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
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "localetest");
		post.addParameter("shareddatakey", "localetest1b");
		post.addParameter("locale", "fr-1694acad");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(200, code);
		assertTrue(post.getResponseBodyAsString().contains("locales/fr/index.htm"));
		assertFalse(post.getResponseBodyAsString().contains("locale test"));
		assertTrue(post.getResponseBodyAsString().contains(
		"tester les param&#232;tres r&#233;gionaux"));
	}

	/**
	 * Tests that requesting an instance for an unsupported locale returns a
	 * non-localized instance
	 * 
	 * @throws IOException
	 */
	@Test
	public void getNonLocalizedInstance() throws IOException {
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "localetest");
		post.addParameter("shareddatakey", "localetest2");
		post.addParameter("locale", "bu");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(200, code);
		assertFalse(post.getResponseBodyAsString().contains("locales/fr/index.htm"));
		assertFalse(post.getResponseBodyAsString().contains("locales/en/index.htm"));
		assertTrue(post.getResponseBodyAsString().contains("index.htm"));
		assertTrue(post.getResponseBodyAsString().contains("locale test"));
		assertFalse(post.getResponseBodyAsString().contains(
		"tester les param&#232;tres r&#233;gionaux"));
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
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "localetest3");
		post.addParameter("shareddatakey", "localetest4");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(200, code);
		assertTrue(post.getResponseBodyAsString().contains("locales/en/index.htm"));
		assertTrue(post.getResponseBodyAsString().contains("locale test"));
		assertFalse(post.getResponseBodyAsString().contains(
		"tester les param&#232;tres r&#233;gionaux"));
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
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "test");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(200, code);
	}

	/**
	 * Tests that getting an existing instance returns 200 rather than 201
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void getInstanceById_AlreadyExists() throws HttpException, IOException {
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "test");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(200, code);
	}

	/**
	 * Tests that a request for an instance with an invalid API key is rejected
	 * 
	 * @throws HttpException
	 * @throws IOException
	 */
	@Test
	public void getInstance_InvalidAPIkey() throws HttpException, IOException {
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_INVALID);
		post.addParameter("widgetid", WIDGET_ID_LOCALIZED);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "test");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(403, code);
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
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_INVALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "test");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(404, code); // but must return the "default widget"
		assertTrue(post.getResponseBodyAsString().contains(
		"Unsupported widget widget"));
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
	 * @throws JSONException 
	 */
	@Test
	public void cloneSharedData() throws HttpException, IOException, JSONException {
		//
		// Create an instance using POST
		//
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "clonetestsrc");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(200, code);

		//
		// Set some shared data
		//
		post = new Request("POST", TEST_PROPERTIES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "clonetestsrc");
		
		JSONObject data = new JSONObject();
		data.put("name", "cat");
		data.put("value", "garfield");
		JSONObject json = new JSONObject();
		JSONArray set = new JSONArray();
		set.put(data);
		json.put("shareddata", set);		
		StringRequestEntity entity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
		post.setRequestEntity(entity);
		
		post.execute(true, false);
		code = post.getStatusCode();
		assertEquals(201, code);

		//
		// Clone it using PUT
		//
		Request put = new Request("PUT", TEST_INSTANCES_SERVICE_URL_VALID);
		put.addParameter("api_key", API_KEY_VALID);
		put.addParameter("widgetid", WIDGET_ID_VALID);
		put.addParameter("userid", "test");
		put.addParameter("shareddatakey", "clonetestsrc");
		put.addParameter("requestid", "clone");
		put.addParameter("cloneshareddatakey", "clonetestsync");
		put.execute(true, false);
		code = put.getStatusCode();
		assertEquals(200, code);

		//
		// Create an instance for the clone
		//
		post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "clonetestsync");
		post.execute(true, false);
		code = post.getStatusCode();
		assertEquals(200, code);

		//
		// Get the data for the clone and check it is the same set for the original
		//
		Request get = new Request("GET", TEST_PROPERTIES_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "clonetestsync");
		get.addParameter("propertyname", "cat");
		get.execute(true, false);
		code = get.getStatusCode();
		assertEquals(200, code);
		String resp = get.getResponseBodyAsString();
		assertEquals("garfield", resp);
	}

}
