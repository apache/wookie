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

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.wookie.tests.helpers.Request;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for the Properties REST API
 */
public class PropertiesControllerTest extends AbstractControllerTest {

	/**
	 * Create a new instance for use in testing
	 * 
	 * @throws HttpException
	 * @throws IOException
	 */
	@BeforeClass
	public static void setup() throws HttpException, IOException {
		Request post = new Request("POST", TEST_INSTANCES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "propstest");
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(200, code);
	}

	@AfterClass
	public static void tearDown() throws HttpException, IOException{
	}

	/**
	 * Test that we can set preferences using POST
	 * 
	 * @throws Exception
	 */
	@Test
	public void setPreferenceUsingPost() throws Exception {
		//
		// Set a property ("testpost=pass") using POST
		//
		Request post = new Request("POST", TEST_PROPERTIES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "propstest");

		JSONObject pref = new JSONObject();
		pref.put("name", "testpost");
		pref.put("value", "pass");
		pref.put("readOnly", false);
		JSONObject json = new JSONObject();
		JSONArray prefs = new JSONArray();
		prefs.put(pref);
		json.put("preferences", prefs);		
		StringRequestEntity entity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
		post.setRequestEntity(entity);
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(201, code);

		//
		// Read back property using GET
		//
		Request get = new Request("GET", TEST_PROPERTIES_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "propstest");
		get.addParameter("propertyname", "testpost");
		get.execute(true, false);
		code = get.getStatusCode();
		assertEquals(200, code);
		String resp = get.getResponseBodyAsString();
		assertEquals("pass", resp);
	}


	/**
	 * Test that we can set multiple preferences at the same time by POSTing JSON
	 * 
	 * @throws Exception
	 */
	@Test
	public void setMultiplePreferencesUsingPost() throws Exception {
		//
		// Set a property ("testpost=pass") using POST
		//
		Request post = new Request("POST", TEST_PROPERTIES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "propstest");

		JSONObject pref1 = new JSONObject();
		pref1.put("name", "testpost1");
		pref1.put("value", "pass1");
		pref1.put("readOnly", false);
		
		JSONObject pref2 = new JSONObject();
		pref2.put("name", "testpost2");
		pref2.put("value", "pass2");
		pref2.put("readOnly", false);
		
		JSONObject json = new JSONObject();
		JSONArray prefs = new JSONArray();
		prefs.put(pref1);
		prefs.put(pref2);
		json.put("preferences", prefs);		
		StringRequestEntity entity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
		post.setRequestEntity(entity);
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(201, code);

		//
		// Read back each property using GET
		//
		Request get = new Request("GET", TEST_PROPERTIES_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "propstest");
		get.addParameter("propertyname", "testpost1");
		get.execute(true, false);
		code = get.getStatusCode();
		assertEquals(200, code);
		String resp = get.getResponseBodyAsString();
		assertEquals("pass1", resp);
		
		get = new Request("GET", TEST_PROPERTIES_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "propstest");
		get.addParameter("propertyname", "testpost2");
		get.execute(true, false);
		code = get.getStatusCode();
		assertEquals(200, code);
		resp = get.getResponseBodyAsString();
		assertEquals("pass2", resp);
	}

	
	/**
	 * Test we can set shared data values using querystring parameters
	 * 
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException 
	 */
	@Test
	public void setSharedData() throws HttpException, IOException, JSONException {

		//
		// Set some shared data with POST
		//
		Request post = new Request("POST", TEST_PROPERTIES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "propstest");

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
		int code = post.getStatusCode();
		assertEquals(201, code);

		//
		// Read back the value using GET
		//
		Request get = new Request("GET", TEST_PROPERTIES_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "propstest");
		get.addParameter("propertyname", "cat");
		get.execute(true, false);
		code = get.getStatusCode();
		assertEquals(200, code);
		String resp = get.getResponseBodyAsString();
		assertEquals("garfield", resp);

	}

	/**
	 * Test we can update an existing property
	 * 
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException 
	 */
	@Test
	public void updateProperty() throws HttpException, IOException, JSONException {

		//
		// Set cat=felix using POST
		//
		Request put = new Request("PUT", TEST_PROPERTIES_SERVICE_URL_VALID);
		put.addParameter("api_key", API_KEY_VALID);
		put.addParameter("widgetid", WIDGET_ID_VALID);
		put.addParameter("userid", "test");
		put.addParameter("shareddatakey", "propstest");
		
		JSONObject data = new JSONObject();
		data.put("name", "cat");
		data.put("value", "felix");
		JSONObject json = new JSONObject();
		JSONArray set = new JSONArray();
		set.put(data);
		json.put("shareddata", set);		
		StringRequestEntity entity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
		put.setRequestEntity(entity);
		
		put.execute(true, false);
		int code = put.getStatusCode();
		assertEquals(200, code);


		//
		// Read back the value using GET
		//
		Request get = new Request("GET", TEST_PROPERTIES_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "propstest");
		get.addParameter("propertyname", "cat");
		get.execute(true, false);
		code = get.getStatusCode();
		assertEquals(200, code);
		String resp = get.getResponseBodyAsString();
		assertEquals("felix", resp);


	}

	/**
	 * Test removing a property
	 * 
	 * @throws HttpException
	 * @throws IOException
	 */
	@Test
	public void removeProperty() throws HttpException, IOException {


		Request delete = new Request("DELETE", TEST_PROPERTIES_SERVICE_URL_VALID);
		delete.addParameter("api_key", API_KEY_VALID);
		delete.addParameter("widgetid", WIDGET_ID_VALID);
		delete.addParameter("userid", "test");
		delete.addParameter("is_public", "true");
		delete.addParameter("shareddatakey", "propstest");
		delete.addParameter("propertyname", "cat");
		delete.execute(true, false);
		int code = delete.getStatusCode();
		assertEquals(200, code);
	}

	/**
	 * Test removing a non-existing property.
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void removePropertyNonExisting() throws HttpException, IOException {
		Request delete = new Request("DELETE", TEST_PROPERTIES_SERVICE_URL_VALID);
		delete.addParameter("api_key", API_KEY_VALID);
		delete.addParameter("widgetid", WIDGET_ID_VALID);
		delete.addParameter("userid", "test");
		delete.addParameter("shareddatakey", "propstest");
		delete.addParameter("propertyname", "bogus");
		delete.execute(true, false);
		int code = delete.getStatusCode();
		assertEquals(404, code);
	}

	/**
	 * Try to set a property without specifying the property name
	 * 
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException 
	 */
	@Test
	public void setPropertyNoName() throws HttpException, IOException, JSONException {
		Request post = new Request("POST", TEST_PROPERTIES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "propstest");
		
		JSONObject data = new JSONObject();
		data.put("value", "garfield");
		JSONObject json = new JSONObject();
		JSONArray set = new JSONArray();
		set.put(data);
		json.put("shareddata", set);		
		StringRequestEntity entity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
		post.setRequestEntity(entity);
		
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(400, code);

	}

	/**
	 * Try to set a property with an empty (zero length) property name
	 * 
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException 
	 */
	@Test
	public void setPropertyEmptyName() throws HttpException, IOException, JSONException {
		Request post = new Request("POST", TEST_PROPERTIES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "propstest");
		
		JSONObject data = new JSONObject();
		data.put("name", "");
		data.put("value", "garfield");
		JSONObject json = new JSONObject();
		JSONArray set = new JSONArray();
		set.put(data);
		json.put("shareddata", set);		
		StringRequestEntity entity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
		post.setRequestEntity(entity);
		
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(400, code);
	}

	/**
	 * Try to set a property with a whitespace-only name
	 * 
	 * @throws IOException
	 * @throws HttpException
	 * @throws JSONException 
	 */
	@Test
	public void setPropertyWhitespaceName() throws HttpException, IOException, JSONException {
		Request post = new Request("POST", TEST_PROPERTIES_SERVICE_URL_VALID);
		post.addParameter("api_key", API_KEY_VALID);
		post.addParameter("widgetid", WIDGET_ID_VALID);
		post.addParameter("userid", "test");
		post.addParameter("shareddatakey", "propstest");
		
		JSONObject data = new JSONObject();
		data.put("name", " ");
		data.put("value", "garfield");
		JSONObject json = new JSONObject();
		JSONArray set = new JSONArray();
		set.put(data);
		json.put("shareddata", set);		
		StringRequestEntity entity = new StringRequestEntity(json.toString(), "application/json", "UTF-8");
		post.setRequestEntity(entity);
		
		post.execute(true, false);
		int code = post.getStatusCode();
		assertEquals(400, code);
	}

	/**
	 * Try to get a property without a valid API key
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void getPropertyInvalidAPIKey() throws HttpException, IOException {

		Request get = new Request("GET", TEST_PROPERTIES_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_INVALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "propstest");
		get.addParameter("propertyname", "cat");
		get.execute(true, false);
		int code = get.getStatusCode();
		assertEquals(403, code);
	}

	/**
	 * Try to get the value of a non-existant property
	 * 
	 * @throws IOException
	 * @throws HttpException
	 */
	@Test
	public void getPropertyInvalidName() throws HttpException, IOException {

		Request get = new Request("GET", TEST_PROPERTIES_SERVICE_URL_VALID);
		get.addParameter("api_key", API_KEY_VALID);
		get.addParameter("widgetid", WIDGET_ID_VALID);
		get.addParameter("userid", "test");
		get.addParameter("shareddatakey", "propstest");
		get.addParameter("propertyname", "madeupname");
		get.execute(true, false);
		int code = get.getStatusCode();
		assertEquals(404, code);
	}

}
