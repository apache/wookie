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
import org.junit.Test;

/**
 * Test cases for W3C Widgets 1.0: The Widget Interface
 * See test case definitions at http://dev.w3.org/2006/waf/widgets-api/test-suite/
 * 
 * Currently this just outputs HTML that you need to paste into a text file and view
 * in your browser to "eyeball" the results. But at least its a start
 */
public class WidgetInterface extends AbstractControllerTest{
	
	private static String html = "";
	
	@BeforeClass
	public static void setup(){
		html = "<html><body>";
	}
	
	@AfterClass
	public static void finish(){
		html += "</body></html>";
		System.out.println(html);
	}

	@Test
	public void az(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-az/aa/aa.wgt");
	}
	@Test
	public void za(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/za/za.wgt");
	}
	@Test
	public void ab(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ah/ab/ab.wgt");
	}
	@Test
	public void ax(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ah/ax/ax.wgt");
	}
	@Test
	public void ad(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ad/ad.wgt");
	}
	@Test
	public void ae(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ae/ae.wgt");
	}
	@Test
	public void af(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/af/af.wgt");
	}
	@Test
	public void ag(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ag/ag.wgt");
	}
	@Test
	public void ah(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ah/ah.wgt");
	}
	@Test
	public void ai(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ai/ai.wgt");
	}
	@Test
	public void aj(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/aj/aj.wgt");
	}
	@Test
	public void ak(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ak/ak.wgt");
	}
	@Test
	public void ao(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-pa/ao/ao.wgt");
	}
	@Test
	public void ap(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-pb/ap/ap.wgt");
	}
	@Test
	public void ar(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-al/ar/ar.wgt");
	}
	@Test
	public void as(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-al/as/as.wgt");
	}
	@Test
	public void at(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ae/at/at.wgt");
	}
	@Test
	public void an(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-bb/an/an.wgt");
	}
	@Test
	public void aq(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-bb/aq/aq.wgt");
	}
	
	@Test
	public void am(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ax/am/am.wgt");
	}	
	@Test
	public void al(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ab/al/al.wgt");
	}
	
	@Test
	public void au(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-aa/au/au.wgt");
	}	

	
	//// Utility methods
	private void doTest(String widget){
		String url;
		try {
			url = getWidgetUrl(widget);
			html += "<iframe src=\""+url+"\"></iframe>";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String getWidgetUrl(String widgetfname) throws IOException{
		WidgetUploader.uploadWidget(widgetfname);
		Element widget = WidgetUploader.getLastWidget();
		String response = instantiateWidget(widget);
		return getStartFile(response);
	}
	
	/// Reused from PackagingAndConfiguration - consider refactoring these
	private String instantiateWidget(Element widget){
		String response = null;
		String widgetUri = widget.getAttributeValue("identifier");
		// instantiate widget and parse results
		try {
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
			post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+widgetUri+"&userid=test&shareddatakey=test");
			client.executeMethod(post);
			response = IOUtils.toString(post.getResponseBodyAsStream());
			post.releaseConnection();
		}
		catch (Exception e) {
			//e.printStackTrace();
			fail("failed to instantiate widget");
		}
		return response;		
	}

	private String getStartFile(String response){
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
