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

package org.apache.wookie.tests.integration;

import static org.junit.Assert.*;

import org.apache.wookie.beans.Widget;
import org.apache.wookie.tests.helpers.MockHttpServletRequest;
import org.apache.wookie.util.gadgets.GadgetUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * Tests for Gadget utility methods
 * 
 * @author scott
 * 
 */
public class GadgetUtilsTest {

	private static String TEST_METADATA_VALID;
	private static String TEST_METADATA_URL_EMPTY;
	private static String TEST_METADATA_URL_NULL;
	private static String TEST_METADATA_SET_HEIGHT_WIDTH;
	private static String TEST_METADATA_INVALID;
	private static String TEST_METADATA_NO_GADGETS;

	private static String TEST_SERVICE_URL_VALID;
	private static String TEST_SERVICE_URL_INVALID;
	private static String TEST_GADGET_URL_VALID;
	private static String TEST_GADGET_URL_INVALID;
	private static String TEST_GADGET_URL_MALFORMED;

	private static String SHINDIG;

	@BeforeClass
	public static void setUp() {
		TEST_METADATA_VALID = "{\"gadgets\":[{\"showInDirectory\":false,\"width\":0,\"title\":\"hello world example\",\"singleton\":false,\"categories\":[\"\",\"\"],\"views\":{\"default\":{\"preferredWidth\":0,\"preferredHeight\":0,\"type\":\"html\",\"quirks\":true}},\"screenshot\":\"\",\"links\":{},\"thumbnail\":\"\",\"authorLink\":\"\",\"height\":0,\"scaling\":false,\"moduleId\":1,\"features\":[],\"showStats\":false,\"authorPhoto\":\"\",\"scrolling\":false,\"url\":\"http://www.google.com/ig/modules/hello.xml\",\"titleUrl\":\"\",\"iframeUrl\":\"/gadgets/ifr?container=default&mid=1&v=db18c863f15d5d1e758a91f2a44881b4&lang=en&country=US&view=default&url=http%3A%2F%2Fwww.google.com%2Fig%2Fmodules%2Fhello.xml\",\"userPrefs\":{}}]}";
		TEST_METADATA_URL_EMPTY = "{\"gadgets\":[{\"showInDirectory\":false,\"width\":0,\"title\":\"hello world example\",\"singleton\":false,\"categories\":[\"\",\"\"],\"views\":{\"default\":{\"preferredWidth\":0,\"preferredHeight\":0,\"type\":\"html\",\"quirks\":true}},\"screenshot\":\"\",\"links\":{},\"thumbnail\":\"\",\"authorLink\":\"\",\"height\":0,\"scaling\":false,\"moduleId\":1,\"features\":[],\"showStats\":false,\"authorPhoto\":\"\",\"scrolling\":false,\"url\":\" \",\"titleUrl\":\"\",\"iframeUrl\":\"/gadgets/ifr?container=default&mid=1&v=db18c863f15d5d1e758a91f2a44881b4&lang=en&country=US&view=default&url=http%3A%2F%2Fwww.google.com%2Fig%2Fmodules%2Fhello.xml\",\"userPrefs\":{}}]}";
		TEST_METADATA_URL_NULL = "{\"gadgets\":[{\"showInDirectory\":false,\"width\":0,\"title\":\"hello world example\",\"singleton\":false,\"categories\":[\"\",\"\"],\"views\":{\"default\":{\"preferredWidth\":0,\"preferredHeight\":0,\"type\":\"html\",\"quirks\":true}},\"screenshot\":\"\",\"links\":{},\"thumbnail\":\"\",\"authorLink\":\"\",\"height\":0,\"scaling\":false,\"moduleId\":1,\"features\":[],\"showStats\":false,\"authorPhoto\":\"\",\"scrolling\":false,\"titleUrl\":\"\",\"iframeUrl\":\"/gadgets/ifr?container=default&mid=1&v=db18c863f15d5d1e758a91f2a44881b4&lang=en&country=US&view=default&url=http%3A%2F%2Fwww.google.com%2Fig%2Fmodules%2Fhello.xml\",\"userPrefs\":{}}]}";
		TEST_METADATA_SET_HEIGHT_WIDTH = "{\"gadgets\":[{\"showInDirectory\":false,\"width\":50,\"title\":\"hello world example\",\"singleton\":false,\"categories\":[\"\",\"\"],\"views\":{\"default\":{\"preferredWidth\":0,\"preferredHeight\":0,\"type\":\"html\",\"quirks\":true}},\"screenshot\":\"\",\"links\":{},\"thumbnail\":\"\",\"authorLink\":\"\",\"height\":50,\"scaling\":false,\"moduleId\":1,\"features\":[],\"showStats\":false,\"authorPhoto\":\"\",\"scrolling\":false,\"url\":\"http://www.google.com/ig/modules/hello.xml\",\"titleUrl\":\"\",\"iframeUrl\":\"/gadgets/ifr?container=default&mid=1&v=db18c863f15d5d1e758a91f2a44881b4&lang=en&country=US&view=default&url=http%3A%2F%2Fwww.google.com%2Fig%2Fmodules%2Fhello.xml\",\"userPrefs\":{}}]}";
		TEST_METADATA_INVALID = "{\"gadgets\"[{\"showInDirectory\":false,\"width\":0,\"title\":\"hello world example\",\"singleton\":false,\"categories\":[\"\",\"\"],\"views\":{\"default\":{\"preferredWidth\":0,\"preferredHeight\":0,\"type\":\"html\",\"quirks\":true}},\"screenshot\":\"\",\"links\":{},\"thumbnail\":\"\",\"authorLink\":\"\",\"height\":0,\"scaling\":false,\"moduleId\":1,\"features\":[],\"showStats\":false,\"authorPhoto\":\"\",\"scrolling\":false,\"url\":\"http://www.google.com/ig/modules/hello.xml\",\"titleUrl\":\"\",\"iframeUrl\":\"/gadgets/ifr?container=default&mid=1&v=db18c863f15d5d1e758a91f2a44881b4&lang=en&country=US&view=default&url=http%3A%2F%2Fwww.google.com%2Fig%2Fmodules%2Fhello.xml\",\"userPrefs\":{}}]}";
		TEST_METADATA_NO_GADGETS = "{\"gadgets\":[]}";

		TEST_SERVICE_URL_VALID = "http://localhost:8080/wookie/gadgets/metadata";
		TEST_SERVICE_URL_INVALID = "http://localhost:8080/shindig/gadgets/madeupname";
		TEST_GADGET_URL_VALID = "http://www.google.com/ig/modules/hello.xml";
		TEST_GADGET_URL_INVALID = "http://localhost:8080/gadgets/madeupname";
		TEST_GADGET_URL_MALFORMED = "ttp://www.google.com/ig/modules/hello.xml";

		SHINDIG = GadgetUtils.SHINDIG_SERVICE;
	}

	@Test
	public void createWidget() {

		MockHttpServletRequest request;
		request = new MockHttpServletRequest();
		request.setScheme("HTTP");
		request.setServerName("localhost");
		request.setServerPort(8080);
		request.setParameter("url", TEST_GADGET_URL_VALID);

		try {
			Widget widget = GadgetUtils.createWidget(request);
			assertNotNull(widget);
		} catch (Exception e) {
			fail("Couldn't create widget from valid request");
		}
	}

	@Test(expected = Exception.class)
	public void createWidgetFromInvalidUrl() throws Exception {
		MockHttpServletRequest request;
		request = new MockHttpServletRequest();
		request.setScheme("HTTP");
		request.setServerName("localhost");
		request.setServerPort(8080);
		request.setParameter("url", TEST_GADGET_URL_INVALID);
		@SuppressWarnings("unused")
		Widget widget = GadgetUtils.createWidget(request);
		// Uh-oh! No exception was thrown so we
		// better make this test fail!
		fail("should've thrown an exception!");
	}

	@Test(expected = Exception.class)
	public void createWidgetFromInvalidServiceUrlPort() throws Exception {
		MockHttpServletRequest request;
		request = new MockHttpServletRequest();
		request.setScheme("HTTP");
		request.setServerName("localhost");
		request.setServerPort(8888);
		request.setParameter("url", TEST_GADGET_URL_VALID);
		@SuppressWarnings("unused")
		Widget widget = GadgetUtils.createWidget(request);
		// Uh-oh! No exception was thrown so we
		// better make this test fail!
		fail("should've thrown an exception!");
	}

	@Test
	public void getMetadata() {
		String metadata = null;
		JSONObject json = null;
		JSONArray gadgets = null;

		try {
			metadata = GadgetUtils.getMetadata(TEST_SERVICE_URL_VALID,
					TEST_GADGET_URL_VALID);
		} catch (Exception e) {
			fail("failed to connect to service");
		}

		try {
			json = new JSONObject(metadata);
		} catch (Exception e) {
			fail("failed to return valid JSON from service");
		}

		try {
			assertTrue(json.has("gadgets"));
			gadgets = json.getJSONArray("gadgets");
		} catch (Exception e) {
			fail("JSON returned with no gadgets");
		}

		try {
			assertFalse(gadgets.getJSONObject(0).has("errors"));
		} catch (Exception e) {
			fail("JSON retured containing errors");
		}
	}

	@Test
	public void getMetadataWithInvalidGadget() {
		String metadata = null;
		try {
			metadata = GadgetUtils.getMetadata(TEST_SERVICE_URL_VALID,
					TEST_GADGET_URL_INVALID);
		} catch (Exception e) {
			fail("failed to connect to service");
		}
		try {
			JSONObject object = new JSONObject(metadata);
			assertTrue(object.has("gadgets"));
			JSONArray gadgets = object.getJSONArray("gadgets");
			assertTrue(gadgets.getJSONObject(0).has("errors"));
		} catch (JSONException e) {
			fail("failed to return valid JSON from service, or return did not contain error codes");
		}
	}

	@Test(expected = Exception.class)
	public void getMetadataWithInvalidService() throws Exception {
		@SuppressWarnings("unused")
		String metadata = GadgetUtils.getMetadata(TEST_SERVICE_URL_INVALID,
				TEST_GADGET_URL_VALID);
		// Uh-oh! No exception was thrown so we
		// better make this test fail!
		fail("should've thrown an exception!");
	}

	@Test(expected = Exception.class)
	public void getMetadataWithMalformedGadgetUrl() throws Exception {
		@SuppressWarnings("unused")
		String metadata = GadgetUtils.getMetadata(TEST_SERVICE_URL_VALID,
				TEST_GADGET_URL_MALFORMED);
		// Uh-oh! No exception was thrown so we
		// better make this test fail!
		fail("should've thrown an exception!");
	}

	@Test
	public void getWidgetWithValidMetadata() {
		@SuppressWarnings("unused")
		Widget widget = null;
		try {
			assertNotNull(GadgetUtils.getWidget(TEST_METADATA_VALID, SHINDIG));
		} catch (Exception e) {
			fail("failed to Create widget from basic valid metadata");
		}
	}

	@Test(expected = Exception.class)
	public void getWidgetWithErrorMetadata() throws Exception {
		@SuppressWarnings("unused")
		Widget widget = GadgetUtils.getWidget(GadgetUtils.getMetadata(
				TEST_SERVICE_URL_VALID, TEST_GADGET_URL_INVALID), SHINDIG);
		// Uh-oh! No exception was thrown so we
		// better make this test fail!
		fail("should've thrown an exception!");
	}

	@Test
	public void getWidgetWithMetadataNoResults() {
		@SuppressWarnings("unused")
		Widget widget = null;
		try {
			assertNull(GadgetUtils.getWidget(TEST_METADATA_NO_GADGETS, SHINDIG));
		} catch (Exception e) {
			fail("failed to return null from basic valid metadata with no results");
		}
	}

	@Test
	public void getWidgetWithValidMetadataAndDefaults() {
		Widget widget = null;
		try {
			widget = GadgetUtils.getWidget(TEST_METADATA_VALID, SHINDIG);
			assertEquals(widget.getHeight().intValue(), 200);
			assertEquals(widget.getWidth().intValue(), 320);
		} catch (Exception e) {
			fail("Create widget from basic valid metadata - defaults not used");
		}
	}

	@Test
	public void getWidgetWithValidMetadataAndOverrides() {
		Widget widget = null;
		try {
			widget = GadgetUtils.getWidget(TEST_METADATA_SET_HEIGHT_WIDTH,
					SHINDIG);
			assertEquals(widget.getHeight().intValue(), 50);
			assertEquals(widget.getWidth().intValue(), 50);
		} catch (Exception e) {
			fail("Create widget from basic valid metadata - overrides not used");
		}
	}

	@Test(expected = Exception.class)
	public void getWidgetWithEmptyURL() throws Exception {
		@SuppressWarnings("unused")
		Widget widget = GadgetUtils.getWidget(TEST_METADATA_URL_EMPTY, SHINDIG);
		// Uh-oh! No exception was thrown so we
		// better make this test fail!
		fail("should've thrown an exception!");
	}

	@Test(expected = Exception.class)
	public void getWidgetWithMissingURL() throws Exception {
		@SuppressWarnings("unused")
		Widget widget = GadgetUtils.getWidget(TEST_METADATA_URL_NULL, SHINDIG);
		// Uh-oh! No exception was thrown so we
		// better make this test fail!
		fail("should've thrown an exception!");
	}

	@Test(expected = Exception.class)
	public void getWidgetWithInvalidJSON() throws Exception {
		@SuppressWarnings("unused")
		Widget widget = GadgetUtils.getWidget(TEST_METADATA_INVALID, SHINDIG);
		// Uh-oh! No exception was thrown so we
		// better make this test fail!
		fail("should've thrown an exception!");
	}

}
