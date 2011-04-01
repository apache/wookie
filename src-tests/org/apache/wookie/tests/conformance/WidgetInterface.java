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
	public void taza(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/author_attrexists/author_attrexists.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/preferences_attrreadonly/preferences_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/shortName_attrreadonly/shortName_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/version_attrreadonly/version_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/width_attrreadonly/width_attrreadonly.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/author_attrtype/author_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/authorEmail_attrtype/authorEmail_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/authorHref_attrtype/authorHref_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/description_attrtype/description_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/height_attrtype/height_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/id_attrtype/id_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/name_attrtype/name_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/preferences_attrtype/preferences_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/shortName_attrtype/shortName_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/version_attrtype/version_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-za/width_attrtype/width_attrtype.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ad/ad.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ae/ae.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/af/af.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ag/ag.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ah/ah.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ai/ai.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/aj/aj.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ac/ak/ak.wgt");
	}
	
	@Test
	public void taah(){
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ah/ab/ab.wgt");		
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/ta-ah/ax/ax.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/001/i18nlro01.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/002/i18nlro02.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/003/i18nlro03.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/004/i18nlro04.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/006/i18nlro06.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/007/i18nlro07.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/008/i18nlro08.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/010/i18nlro10.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/011/i18nlro11.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/012/i18nlro12.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/014/i18nlro14.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/015/i18nlro15.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/016/i18nlro16.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/017/i18nlro17.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/019/i18nlro19.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/020/i18nlro20.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/021/i18nlro21.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/022/i18nlro22.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/036/i18nlro36.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/037/i18nlro37.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/041/i18nlro41.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/042/i18nlro42.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-lro/044/i18nlro44.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/001/i18nrlo01.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/002/i18nrlo02.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/003/i18nrlo03.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/004/i18nrlo04.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/010/i18nrlo10.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/011/i18nrlo11.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/012/i18nrlo12.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/014/i18nrlo14.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/015/i18nrlo15.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/016/i18nrlo16.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/017/i18nrlo17.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/019/i18nrlo19.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/020/i18nrlo20.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/021/i18nrlo21.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/022/i18nrlo22.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/036/i18nrlo36.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/037/i18nrlo37.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/041/i18nrlo41.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/042/i18nrlo42.wgt");
		doTest("http://dev.w3.org/2006/waf/widgets-api/test-suite/test-cases/i18n-rlo/044/i18nrlo44.wgt");
		// Two more lots to go here
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
