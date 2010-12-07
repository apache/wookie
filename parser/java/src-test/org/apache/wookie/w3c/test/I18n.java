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
package org.apache.wookie.w3c.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.wookie.w3c.IContentEntity;
import org.apache.wookie.w3c.IFeatureEntity;
import org.apache.wookie.w3c.W3CWidget;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests for i18n conformance.
 * 
 * Note that these are very basic tests that check things like appropriate attribute handling by the parser; they 
 * do not test whether the user experience is appropriate, which has to be done manually
 *
 */
public class I18n extends ConformanceTest{
	
	// ta-klLDaEgJeU
	// Can't really test this as we don't bother storing the xml:lang
	// attribute of the widget as its never actually used for anything
	
	// ta-RawAIWHoMs
	@Test
	public void id(){
		String pass_value="http://widget.example.org/";
		String id="041";
		String[] tests = {"lro","rlo","rtl","ltr"};
		for (String dir: tests){
			W3CWidget widget = getWidget(id,dir);
			assertEquals(pass_value, widget.getIdentifier());
		}
	}
	
	// ta-VerEfVGeTc
	// This can only be tested when displayed
	
	// ta-BxjoiWHaMr
	@Test
	public void height(){
		int pass_value=123;
		String id="040";
		String[] tests = {"lro","rlo","rtl","ltr"};
		for (String dir: tests){
			W3CWidget widget = getWidget(id,dir);
			assertEquals(pass_value, widget.getHeight().intValue());
		}
	}
	@Test
	public void width(){
		int pass_value=123;
		String id="039";
		String[] tests = {"lro","rlo","rtl","ltr"};
		for (String dir: tests){
			W3CWidget widget = getWidget(id,dir);
			assertEquals(pass_value, widget.getWidth().intValue());
		}
	}
	
	// ta-viewmodes
	public void viewmode(){
		String pass_value="windowed floating";
		String id="043";
		String[] tests = {"lro","rlo","rtl","ltr"};
		for (String dir: tests){
			W3CWidget widget = getWidget(id,dir);
			assertEquals(pass_value, widget.getIdentifier());
		}
	}
	
	
	// ta-AYLMhryBnD
	// Note that this set of tests only proves that the parser correctly stores the information, not that it is correctly displayed. This
	// needs to be passed by the User Agent (e.g. the Wookie Server) by checking how its rendered.
	@Test
	public void lroName(){
		runNameTests("lro");
	}
	
	@Test
	public void ltrName(){
		runNameTests("ltr");
	}
	
	@Test
	public void rloName(){
		runNameTests("rlo");
	}

	@Test
	public void rtlName(){
		runNameTests("rtl");
	}
	
	public void runNameTests(String dir){
		// TODO note that 002 tests can only be done manually
		String[] tests = {"001","010","014","015"};
		for (String id: tests){
			W3CWidget widget = getWidget(id,dir);
			assertEquals(dir,widget.getNames().get(0).getDir());
		}
		// this has inline dir tags. we have to check that we get them correctly and haven't filtered out the span tag.
		W3CWidget widget = getWidget("006", dir);
		assertTrue(widget.getNames().get(0).getName().contains("dir=\""+dir+"\""));
	}
	
	//18 {19,20,21,22}
	// These look like they need manual testing
	
	
	//20
	// Note that this set of tests only proves that the parser correctly stores the information, not that it is correctly displayed. This
	// needs to be passed by the User Agent (e.g. the Wookie Server) by checking how its rendered.
	@Test
	public void lroDescription(){
		runDescriptionTests("lro");
	}
	
	@Test
	public void ltrDescription(){
		runDescriptionTests("ltr");
	}
	
	@Test
	public void rloDescription(){
		runDescriptionTests("rlo");
	}

	@Test
	public void rtlDescription(){
		runDescriptionTests("rtl");
	}
	
	public void runDescriptionTests(String dir){
		String[] tests = {"003","011","016"};
		for (String id: tests){
			W3CWidget widget = getWidget(id,dir);
			assertEquals(dir,widget.getDescriptions().get(0).getDir());
		}
		// this has inline dir tags. we have to check that we get them correctly and haven't filtered out the span tag.
		W3CWidget widget = getWidget("007", dir);
		assertTrue(widget.getDescriptions().get(0).getDescription().contains("dir=\""+dir+"\""));
	}
	
	// ta-VdCEyDVSA
	// TODO implement these tests
	
	// ta-YUMJAPVEgI
	
	@Test
	public void lroLicense(){
		runLicenseTests("lro");
	}
	@Test
	public void rloLicense(){
		runLicenseTests("rlo");
	}
	@Test
	public void rtlLicense(){
		runLicenseTests("rtl");
	}
	@Test
	public void ltrLicense(){
		runLicenseTests("ltr");
	}
	
	public void runLicenseTests(String dir){
		// test dir attributes correctly set
		String[] tests = {"005","018"};
		for (String id: tests){
			W3CWidget widget = getWidget(id,dir);
			assertEquals(dir,widget.getLicensesList().get(0).getDir());
		}
		W3CWidget widget;
		// this has inline dir tags. we have to check that we get them correctly and haven't filtered out the span tag.
		widget = getWidget("009",dir);
		assertTrue(widget.getLicensesList().get(0).getLicenseText().contains("dir=\""+dir));
		widget = getWidget("013",dir);
		assertTrue(widget.getLicensesList().get(0).getDir().equals(dir));

		// this has mixed inline dir tags. we have to check that we get them correctly and haven't filtered out the span tag.
		widget = getWidget("038",dir);
		assertEquals("http://widget.example.org/", widget.getLicensesList().get(0).getHref());
	}
	
	// ta-roCaKRxZhS
	@Test
	public void icon(){
		String pass_value = "test.png"; 
		W3CWidget widget = getWidget("023","lro");
		assertEquals(pass_value,getLocalIconPath(widget, 0));
		widget = getWidget("023","rlo");
		assertEquals(pass_value,getLocalIconPath(widget, 0));
		widget = getWidget("023","rtl");
		assertEquals(pass_value,getLocalIconPath(widget, 0));
		widget = getWidget("023","ltr");
		assertEquals(pass_value,getLocalIconPath(widget, 0));
	}
	
	// ta-argMozRiC
	
	@Test
	public void rloAuthor(){
		runAuthorTests("rlo");
	}
	@Test
	public void lroAuthor(){
		runAuthorTests("lro");
	}
	@Test
	public void ltrAuthor(){
		runAuthorTests("ltr");
	}
	@Test
	public void rtlAuthor(){
		runAuthorTests("rtl");
	}
	
	@Test
	public void authorEmail(){
		//TODO these must also be tested manually
		W3CWidget widget;
		
		widget = getWidget("037","lro");
		assertEquals("םפללחק",widget.getAuthor().getEmail());
		
		widget = getWidget("037","ltr");
		assertEquals("The arrow should point right -->",widget.getAuthor().getEmail());		
		
		widget = getWidget("037","rtl");
		assertEquals("The arrow should point right -->",widget.getAuthor().getEmail());
		
		widget = getWidget("037","rlo");
		assertEquals("PASSED",widget.getAuthor().getEmail());
	}
	
	public void runAuthorTests(String dir){
		// test dir attributes correctly set
		String[] tests = {"004","017","012"};
		for (String id: tests){
			W3CWidget widget = getWidget(id,dir);
			assertEquals(dir,widget.getAuthor().getDir());
		}
		W3CWidget widget;
		// this has inline dir tags. we have to check that we get them correctly and haven't filtered out the span tag.
		widget = getWidget("012",dir);
		assertTrue(widget.getAuthor().getAuthorName().contains("dir=\""));
		widget = getWidget("008",dir);
		assertTrue(widget.getAuthor().getAuthorName().contains("dir=\""+dir));
		
		// ensure hrefs aren't checked
		widget = getWidget("036",dir);
		assertEquals("http://widget.example.org/", widget.getAuthor().getHref());
	}
	
	// ta-DwhJBIJRQN Preferences TODO
	
	// ta-LQcjNKBLUZ
	@Test
	public void srctest(){
		String pass_value="pass.htm";
		String id="026";
		String[] tests = {"lro","rlo","rtl","ltr"};
		for (String dir: tests){
			W3CWidget widget = getWidget(id,dir);
			IContentEntity start = (IContentEntity)widget.getContentList().get(0);
			assertEquals(pass_value, getLocalUrl(start.getSrc()));
		}	
	}
	
	// ta-dPOgiLQKNK 
	@Test
	public void encoding(){
		String id="028";
		
		W3CWidget widget;
		IContentEntity start;
		
		widget = getWidget(id, "lro");
		start = (IContentEntity)widget.getContentList().get(0);
		assertEquals("UTF-8", start.getCharSet());
		
		widget = getWidget(id, "rlo");
		start = (IContentEntity)widget.getContentList().get(0);
		assertEquals("ISO-8859-1", start.getCharSet());
		
		widget = getWidget(id, "rtl");
		start = (IContentEntity)widget.getContentList().get(0);
		assertEquals("UTF-8", start.getCharSet());
		
		widget = getWidget(id, "ltr");
		start = (IContentEntity)widget.getContentList().get(0);
		assertEquals("UTF-8", start.getCharSet());
		
	}
	
	
	// ta-paIabGIIMC
	@Test
	public void type(){
		String pass_value="text/html";
		String id="027";
		String[] tests = {"lro","rlo","rtl","ltr"};
		for (String dir: tests){
			W3CWidget widget = getWidget(id,dir);
			IContentEntity start = (IContentEntity)widget.getContentList().get(0);
			assertEquals(pass_value, start.getType());
		}	
	}
	
	
	// ta-rZdcMBExBX
	@Test
	public void feature29(){
		String pass_value="feature:a9bb79c1";
		String id="029";
		String[] tests = {"lro","rlo","rtl","ltr"};
		for (String dir: tests){
			W3CWidget widget = getWidget(id,dir);
			IFeatureEntity feature = widget.getFeatures().get(0);
			assertEquals(pass_value, feature.getName());
		}	
	}
	@Test
	@Ignore // test in error
	public void feature30(){
		boolean pass_value=false;
		String id="030";
		String[] tests = {"lro","rlo","rtl","ltr"};
		for (String dir: tests){
			W3CWidget widget = getWidget(id,dir);
			IFeatureEntity feature = widget.getFeatures().get(0);
			assertEquals(pass_value, feature.isRequired());
		}	
	}
	
	// ta-CEGwkNQcWo 
	@Test
	public void paramTests(){
		W3CWidget widget;
		String dir;
		
		widget = getWidget("031","lro");
		assertEquals("םפללחק", widget.getFeatures().get(0).getParams().get(0).getName());
		widget = getWidget("032","lro");
		assertEquals("םפללחק", widget.getFeatures().get(0).getParams().get(0).getValue());
		widget = getWidget("031","ltr");
		assertEquals("The arrow should point right -->", widget.getFeatures().get(0).getParams().get(0).getName());
		widget = getWidget("032","ltr");
		assertEquals("The arrow should point right -->", widget.getFeatures().get(0).getParams().get(0).getValue());	
		widget = getWidget("031","rtl");
		assertEquals("The arrow should point right -->", widget.getFeatures().get(0).getParams().get(0).getName());
		widget = getWidget("032","rtl");
		assertEquals("The arrow should point right -->", widget.getFeatures().get(0).getParams().get(0).getValue());	
		widget = getWidget("031","rlo");
		assertEquals("PASSED", widget.getFeatures().get(0).getParams().get(0).getName());
		widget = getWidget("032","rlo");
		assertEquals("PASSED", widget.getFeatures().get(0).getParams().get(0).getValue());	
	}
	
	// Utilities
	
	public W3CWidget getWidget(String id, String dir){
		String url = "http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/i18n-XXX/YYY/i18nXXXYY.wgt";
		url = url.replace("XXX", dir);
		url = url.replace("YYY", id);
		url = url.replace("YY", id.substring(1));
		// Uncomment this to output url to console, just to check which widget tests we've tried
		// System.out.println(url);
		W3CWidget widget = processWidgetNoErrors(url);
		return widget;
	}
	private String getLocalIconPath(W3CWidget widget, int index){
		return getLocalUrl(widget.getIconsList().get(index).getSrc());
	}
	
	private String getLocalUrl(String src){
		// Localized files are easy
		if (src.contains("locales")){
			return src.substring(src.indexOf("locales"));
		}
		// Bit of a hack
		if (src.contains("icons")){
			return src.substring(src.indexOf("icons"));
		}
		URL url;
		try {
			url = new URL(src);
		} catch (MalformedURLException e) {
				System.out.println("start file URL was invalid");
				return null;
		}
		String[] parts = url.getPath().split("/");
		src = parts[parts.length-1];
		return src;
	}
	
}
