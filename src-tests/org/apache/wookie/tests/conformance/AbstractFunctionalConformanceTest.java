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
package org.apache.wookie.tests.conformance;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.wookie.tests.functional.AbstractControllerTest;
import org.apache.wookie.tests.helpers.WidgetUploader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * Abstract superclass for conformance tests including utility functions
 * for working with widgets and outputting results
 */
public abstract class AbstractFunctionalConformanceTest extends
AbstractControllerTest {

	protected static String html = "";

	@BeforeClass
	public static void setup(){
		html = "<html><body>";
	}

	@AfterClass
	public static void finish(){
		html += "</body></html>";
		System.out.println(html);
	}

	//// Utility methods
	protected static void doTest(String widget){
		String url;
		try {
			url = getWidgetUrl(widget);
			html += "<iframe src=\""+url+"\"></iframe>";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected static void outputInstance(String widgetId){
		String url;
		String response = instantiateWidget(widgetId);
		url = getStartFile(response);
		html += "<iframe src=\""+url+"\"></iframe>";
	}

	protected static String getWidgetUrl(String widgetfname) throws IOException{
		WidgetUploader.uploadWidget(widgetfname);
		Element widget = WidgetUploader.getLastWidget();
		String response = instantiateWidget(widget);
		return getStartFile(response);
	}

	protected static String instantiateWidget(Element widget){
		return instantiateWidget(widget.getAttributeValue("identifier"));	
	}

	protected static String instantiateWidget(String identifier){
		String response = null;
		// instantiate widget and parse results
		try {
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
			post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+identifier+"&userid=test&shareddatakey=test");
			client.executeMethod(post);
			response = IOUtils.toString(post.getResponseBodyAsStream());
			post.releaseConnection();
		}
		catch (Exception e) {
			fail("failed to instantiate widget");
		}
		return response;		
	}

	protected static String getStartFile(String response){
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

}
