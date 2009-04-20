/**
 * 
 */
package org.tencompetence.widgetservice.tests;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.tests.helpers.MockHttpServletRequest;
import org.tencompetence.widgetservice.util.gadgets.GadgetUtils;

import junit.framework.TestCase;

/**
 * Tests for Gadget utility methods
 * @author scott
 *
 */
public class GadgetUtilsTest extends TestCase {
	
	private String TEST_METADATA_VALID;
	private String TEST_METADATA_URL_EMPTY;
	private String TEST_METADATA_URL_NULL;
	private String TEST_METADATA_SET_HEIGHT_WIDTH;
	private String TEST_METADATA_INVALID;
	private String TEST_METADATA_NO_GADGETS;
	
	private String TEST_SERVICE_URL_VALID;
	private String TEST_SERVICE_URL_INVALID;
	private String TEST_GADGET_URL_VALID;
	private String TEST_GADGET_URL_INVALID;
	private String TEST_GADGET_URL_MALFORMED;
	
	private String SHINDIG;
	
    @Before public void setUp() {
    	TEST_METADATA_VALID = "{\"gadgets\":[{\"showInDirectory\":false,\"width\":0,\"title\":\"hello world example\",\"singleton\":false,\"categories\":[\"\",\"\"],\"views\":{\"default\":{\"preferredWidth\":0,\"preferredHeight\":0,\"type\":\"html\",\"quirks\":true}},\"screenshot\":\"\",\"links\":{},\"thumbnail\":\"\",\"authorLink\":\"\",\"height\":0,\"scaling\":false,\"moduleId\":1,\"features\":[],\"showStats\":false,\"authorPhoto\":\"\",\"scrolling\":false,\"url\":\"http://www.google.com/ig/modules/hello.xml\",\"titleUrl\":\"\",\"iframeUrl\":\"/gadgets/ifr?container=default&mid=1&v=db18c863f15d5d1e758a91f2a44881b4&lang=en&country=US&view=default&url=http%3A%2F%2Fwww.google.com%2Fig%2Fmodules%2Fhello.xml\",\"userPrefs\":{}}]}";
    	TEST_METADATA_URL_EMPTY = "{\"gadgets\":[{\"showInDirectory\":false,\"width\":0,\"title\":\"hello world example\",\"singleton\":false,\"categories\":[\"\",\"\"],\"views\":{\"default\":{\"preferredWidth\":0,\"preferredHeight\":0,\"type\":\"html\",\"quirks\":true}},\"screenshot\":\"\",\"links\":{},\"thumbnail\":\"\",\"authorLink\":\"\",\"height\":0,\"scaling\":false,\"moduleId\":1,\"features\":[],\"showStats\":false,\"authorPhoto\":\"\",\"scrolling\":false,\"url\":\" \",\"titleUrl\":\"\",\"iframeUrl\":\"/gadgets/ifr?container=default&mid=1&v=db18c863f15d5d1e758a91f2a44881b4&lang=en&country=US&view=default&url=http%3A%2F%2Fwww.google.com%2Fig%2Fmodules%2Fhello.xml\",\"userPrefs\":{}}]}";
    	TEST_METADATA_URL_NULL = "{\"gadgets\":[{\"showInDirectory\":false,\"width\":0,\"title\":\"hello world example\",\"singleton\":false,\"categories\":[\"\",\"\"],\"views\":{\"default\":{\"preferredWidth\":0,\"preferredHeight\":0,\"type\":\"html\",\"quirks\":true}},\"screenshot\":\"\",\"links\":{},\"thumbnail\":\"\",\"authorLink\":\"\",\"height\":0,\"scaling\":false,\"moduleId\":1,\"features\":[],\"showStats\":false,\"authorPhoto\":\"\",\"scrolling\":false,\"titleUrl\":\"\",\"iframeUrl\":\"/gadgets/ifr?container=default&mid=1&v=db18c863f15d5d1e758a91f2a44881b4&lang=en&country=US&view=default&url=http%3A%2F%2Fwww.google.com%2Fig%2Fmodules%2Fhello.xml\",\"userPrefs\":{}}]}";
    	TEST_METADATA_SET_HEIGHT_WIDTH = "{\"gadgets\":[{\"showInDirectory\":false,\"width\":50,\"title\":\"hello world example\",\"singleton\":false,\"categories\":[\"\",\"\"],\"views\":{\"default\":{\"preferredWidth\":0,\"preferredHeight\":0,\"type\":\"html\",\"quirks\":true}},\"screenshot\":\"\",\"links\":{},\"thumbnail\":\"\",\"authorLink\":\"\",\"height\":50,\"scaling\":false,\"moduleId\":1,\"features\":[],\"showStats\":false,\"authorPhoto\":\"\",\"scrolling\":false,\"url\":\"http://www.google.com/ig/modules/hello.xml\",\"titleUrl\":\"\",\"iframeUrl\":\"/gadgets/ifr?container=default&mid=1&v=db18c863f15d5d1e758a91f2a44881b4&lang=en&country=US&view=default&url=http%3A%2F%2Fwww.google.com%2Fig%2Fmodules%2Fhello.xml\",\"userPrefs\":{}}]}";
    	TEST_METADATA_INVALID = "{\"gadgets\"[{\"showInDirectory\":false,\"width\":0,\"title\":\"hello world example\",\"singleton\":false,\"categories\":[\"\",\"\"],\"views\":{\"default\":{\"preferredWidth\":0,\"preferredHeight\":0,\"type\":\"html\",\"quirks\":true}},\"screenshot\":\"\",\"links\":{},\"thumbnail\":\"\",\"authorLink\":\"\",\"height\":0,\"scaling\":false,\"moduleId\":1,\"features\":[],\"showStats\":false,\"authorPhoto\":\"\",\"scrolling\":false,\"url\":\"http://www.google.com/ig/modules/hello.xml\",\"titleUrl\":\"\",\"iframeUrl\":\"/gadgets/ifr?container=default&mid=1&v=db18c863f15d5d1e758a91f2a44881b4&lang=en&country=US&view=default&url=http%3A%2F%2Fwww.google.com%2Fig%2Fmodules%2Fhello.xml\",\"userPrefs\":{}}]}";
    	TEST_METADATA_NO_GADGETS = "{\"gadgets\":[]}";
    	
    	TEST_SERVICE_URL_VALID = "http://localhost:8080/wookie/gadgets/metadata";
    	TEST_SERVICE_URL_INVALID= "http://localhost:8080/shindig/gadgets/madeupname"; 
    	TEST_GADGET_URL_VALID = "http://www.google.com/ig/modules/hello.xml";
    	TEST_GADGET_URL_INVALID= "http://localhost:8080/gadgets/madeupname";
    	TEST_GADGET_URL_MALFORMED = "ttp://www.google.com/ig/modules/hello.xml";
    	
    	SHINDIG = GadgetUtils.SHINDIG_SERVICE;
    }
	
	/**
	 * Test method for {@link org.tencompetence.widgetservice.util.gadgets.GadgetUtils#createWidget(javax.servlet.http.HttpServletRequest)}.
	 */
	public void testCreateWidget() {
		
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

	/**
	 * Test method for {@link org.tencompetence.widgetservice.util.gadgets.GadgetUtils#createWidget(javax.servlet.http.HttpServletRequest)}.
	 */
    @Test(expected=Exception.class)
	public void testCreateWidgetFromInvalidUrl() throws Exception{
		MockHttpServletRequest request;
		request = new MockHttpServletRequest();
		request.setScheme("HTTP");
		request.setServerName("localhost");
		request.setServerPort(8080);
		request.setParameter("url", TEST_GADGET_URL_INVALID);
		
		try {
			Widget widget = GadgetUtils.createWidget(request);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
		} catch (Exception e) {
            // this is exactly what we were expecting so 
            // let's just ignore it and let the test pass
		}
	}

	/**
	 * Test method for {@link org.tencompetence.widgetservice.util.gadgets.GadgetUtils#createWidget(javax.servlet.http.HttpServletRequest)}.
	 */
    @Test(expected=Exception.class)
	public void testCreateWidgetFromInvalidServiceUrlPort() throws Exception{
		MockHttpServletRequest request;
		request = new MockHttpServletRequest();
		request.setScheme("HTTP");
		request.setServerName("localhost");
		request.setServerPort(8888);
		request.setParameter("url", TEST_GADGET_URL_VALID);
		
		try {
			Widget widget = GadgetUtils.createWidget(request);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
		} catch (Exception e) {
            // this is exactly what we were expecting so 
            // let's just ignore it and let the test pass
		}
	}
    
	/**
	 * Test method for {@link org.tencompetence.widgetservice.util.gadgets.GadgetUtils#getMetadata(java.lang.String, java.lang.String)}.
	 */
    @Test
	public void testGetMetadata() {
		String metadata = null;
		try {
			metadata = GadgetUtils.getMetadata(TEST_SERVICE_URL_VALID, TEST_GADGET_URL_VALID);
		} catch (Exception e) {
			fail("failed to connect to service");
		}
		try {
			JSONObject object = new JSONObject(metadata);
		} catch (JSONException e) {
			fail("failed to return valid JSON from service");
		}	
	}
	
	/**
	 * Test method for {@link org.tencompetence.widgetservice.util.gadgets.GadgetUtils#getMetadata(java.lang.String, java.lang.String)}.
	 */
	public void testGetMetadataWithInvalidGadget() {
		String metadata = null;
		try {
			metadata = GadgetUtils.getMetadata(TEST_SERVICE_URL_VALID, TEST_GADGET_URL_INVALID);
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
	
    @Test(expected=Exception.class)
    public void testGetMetadataWithInvalidService()
            throws Exception {
        try {
    		String metadata = GadgetUtils.getMetadata(TEST_SERVICE_URL_INVALID, TEST_GADGET_URL_VALID);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
        } catch (Exception expected) {
            // this is exactly what we were expecting so 
            // let's just ignore it and let the test pass
        }
    }
    
    @Test(expected=Exception.class)
    public void testGetMetadataWithMalformedGadgetUrl()
            throws Exception {
        try {
    		String metadata = GadgetUtils.getMetadata(TEST_SERVICE_URL_VALID, TEST_GADGET_URL_MALFORMED);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
        } catch (Exception expected) {
            // this is exactly what we were expecting so 
            // let's just ignore it and let the test pass
        }
    }

	/**
	 * Test method for {@link org.tencompetence.widgetservice.util.gadgets.GadgetUtils#getWidget(java.lang.String)}.
	 */
	@Test
	public void testGetWidgetWithValidMetadata() {
		Widget widget = null;
		try {
			assertNotNull(GadgetUtils.getWidget(TEST_METADATA_VALID, SHINDIG));
		} catch (Exception e) {
			fail("failed to Create widget from basic valid metadata");
		}
	}
	
	/**
	 * Test method for {@link org.tencompetence.widgetservice.util.gadgets.GadgetUtils#getWidget(java.lang.String)}.
	 */
    @Test(expected=Exception.class)
    public void testGetWidgetWithErrorMetadata()
            throws Exception {
        try {
        	Widget widget = GadgetUtils.getWidget(GadgetUtils.getMetadata(TEST_SERVICE_URL_VALID,TEST_GADGET_URL_INVALID), SHINDIG);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
        } catch (Exception expected) {
            // this is exactly what we were expecting so 
            // let's just ignore it and let the test pass
        }
    }

	/**
	 * Test method for {@link org.tencompetence.widgetservice.util.gadgets.GadgetUtils#getWidget(java.lang.String)}.
	 */
	@Test
	public void testGetWidgetWithMetadataNoResults() {
		Widget widget = null;
		try {
			assertNull(GadgetUtils.getWidget(TEST_METADATA_NO_GADGETS, SHINDIG));
		} catch (Exception e) {
			fail("failed to return null from basic valid metadata with no results");
		}
	}
	
	/**
	 * Test method for {@link org.tencompetence.widgetservice.util.gadgets.GadgetUtils#getWidget(java.lang.String)}.
	 */
	@Test
	public void testGetWidgetWithValidMetadataAndDefaults() {
		Widget widget = null;
		try {
			widget = GadgetUtils.getWidget(TEST_METADATA_VALID, SHINDIG);
			assertEquals(widget.getHeight(),200);
			assertEquals(widget.getWidth(),320);
		} catch (Exception e) {
			fail("Create widget from basic valid metadata - defaults not used");
		}
	}

	/**
	 * Test method for {@link org.tencompetence.widgetservice.util.gadgets.GadgetUtils#getWidget(java.lang.String)}.
	 */
	@Test
	public void testGetWidgetWithValidMetadataAndOverrides() {
		Widget widget = null;
		try {
			widget = GadgetUtils.getWidget(TEST_METADATA_SET_HEIGHT_WIDTH, SHINDIG);
			assertEquals(widget.getHeight(),50);
			assertEquals(widget.getWidth(),50);
		} catch (Exception e) {
			fail("Create widget from basic valid metadata - overrides not used");
		}
	}
	
    @Test(expected=Exception.class)
    public void testGetWidgetWithEmptyURL()
            throws Exception {
        try {
        	Widget widget = GadgetUtils.getWidget(TEST_METADATA_URL_EMPTY, SHINDIG);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
        } catch (Exception expected) {
            // this is exactly what we were expecting so 
            // let's just ignore it and let the test pass
        }
    }

    @Test(expected=Exception.class)
    public void testGetWidgetWithMissingURL()
            throws Exception {
        try {
        	Widget widget = GadgetUtils.getWidget(TEST_METADATA_URL_NULL, SHINDIG);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
        } catch (Exception expected) {
            // this is exactly what we were expecting so 
            // let's just ignore it and let the test pass
        }
    }
    
    @Test(expected=Exception.class)
    public void testGetWidgetWithInvalidJSON()
            throws Exception {
        try {
        	Widget widget = GadgetUtils.getWidget(TEST_METADATA_INVALID, SHINDIG);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
        } catch (Exception expected) {
            // this is exactly what we were expecting so 
            // let's just ignore it and let the test pass
        }
    }

}
