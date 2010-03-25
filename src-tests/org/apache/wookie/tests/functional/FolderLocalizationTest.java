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

import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 * Tests folder-based localization using the localetest widget
 * @author scott
 *
 */
public class FolderLocalizationTest extends AbstractControllerTest {
	
	private static final String LOCALIZED_WIDGET = "http://www.getwookie.org/widgets/localetest";
	private static String WIDGET_START_URL_ROOT;
	
	private static HttpClient client;
	private static GetMethod get;
	
	@BeforeClass
	public static void setup(){
		// Set up the client and enable cookies
        client = new HttpClient();
        client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        get = new GetMethod();
        
        // Create a widget instance localized to Yorkshire English
        try {
	        PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
	        post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+LOCALIZED_WIDGET+"&userid=foldertest1&shareddatakey=foldertest1&locale=en-gb-yorks");
	        client.executeMethod(post);
	      
	        WIDGET_START_URL_ROOT = getStartFile(post.getResponseBodyAsString());
	        // We have to load the start file in order to start the session
	        getResource(WIDGET_START_URL_ROOT);
	        // take off the resource bit
	        String path = WIDGET_START_URL_ROOT.substring(WIDGET_START_URL_ROOT.indexOf("locales"));
	        WIDGET_START_URL_ROOT= StringUtils.remove(WIDGET_START_URL_ROOT, path);
	        post.releaseConnection();
	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    }			
	}
	
	/**
	 * Gets the start file url for an instance
	 * @param response
	 * @return
	 */
	private static String getStartFile(String response){
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
	 * @param url
	 * @return
	 */
	private static String getResource(String url){
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
	
	@Test
	// The root resource is overridden by the EN-GB-YORKS localized resource
	public void getLocalizedResourceFromRootPath(){
		String resource = "index.htm";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT+"locales/en-gb-yorks/"+resource, getResource(url));
	}
	
	@Test
	// The root resource is correct, so should not be redirected
	public void getRootResourceFromRootResource(){
		String resource = "Images/test.png";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT+resource, getResource(url));	
	}
	
	@Test
	// The FR resource is replaced by the Root resource as there is no EN resource
	public void getRootResourceFromIncorrectLocalizedPath(){
		String resource = "locales/fr/Images/test.png";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT+"Images/test.png", getResource(url));
	}
	
	@Test
	// The EN resource is replaced by the Root resource as there is no EN resource
	public void getRootResourceFromLocalizedPath(){
		String resource = "locales/en/Images/test.png";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT+"Images/test.png", getResource(url));
	}
	
	@Test
	// The FR resource is replaced by the EN-GB-YORKS resource
	public void getLocalizedResourceFromIncorrectLocalizedPath(){
		String resource = "locales/fr/index.htm";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT+"locales/en-gb-yorks/index.htm", getResource(url));
	}
	
	@Test
	// The EN-GB-YORKS resource is not available and is replaced by the EN resource
	public void getLocalizedResourceGracefulDegradation(){
		String resource = "locales/en-gb-yorks/test.txt";
		String url = WIDGET_START_URL_ROOT + resource;
		assertEquals(WIDGET_START_URL_ROOT+"locales/en/test.txt", getResource(url));
	}

}
