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

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.wookie.w3c.*;
import org.apache.wookie.w3c.util.FormattingUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * i18n Test Cases requiring a manual check. Use this class to generate the "eyeball file" to use for checking
 * conformance manually.
 */
public class i18nManual extends ConformanceTest{
	
	static String output;
	
	@BeforeClass
	public static void setupManualOutput(){
		output = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/> </head><body><table>"; 
	}

	// ta-VerEfVGeTc
	// This can only be tested when displayed
	
	@Test
	public void i18nlro42(){
		W3CWidget widget = getWidget("042","lro");
		addResult("i18nlro42",FormattingUtils.getFormattedWidgetVersion(widget), "םפללחק");
	}
	@Test
	public void i18nltr42(){
		W3CWidget widget = getWidget("042","ltr");
		addResult("i18nltr42",FormattingUtils.getFormattedWidgetVersion(widget), "The arrow should point right -->");
	}
	@Test
	public void i18nrlo42(){
		W3CWidget widget = getWidget("042","rlo");
		addResult("i18nrlo42",FormattingUtils.getFormattedWidgetVersion(widget), "PASSED");
	}
	@Test
	public void i18nrtl42(){
		W3CWidget widget = getWidget("042","rtl");
		addResult("i18nrtl42",FormattingUtils.getFormattedWidgetVersion(widget), "The arrow should point right -->");
	}
	
	// ta-AYLMhryBnD
	@Test
	public void nameLro(){
		W3CWidget widget;
		IName name;

		widget = getWidget("001","lro");
		name = widget.getNames().get(0);
		addResult("i18nlro01", FormattingUtils.getFormattedWidgetName(name), "םפללחק");
		widget = getWidget("002","lro");
		name = widget.getNames().get(0);
		addResult("i18nlro02", FormattingUtils.getFormattedWidgetShortName(name), "םפללחק");	
		widget = getWidget("006","lro");
		name = widget.getNames().get(0);
		addResult("i18nlro06", FormattingUtils.getFormattedWidgetName(name), "םפללחק");
		widget = getWidget("010","lro");
		name = widget.getNames().get(0);
		addResult("i18nlro10", FormattingUtils.getFormattedWidgetName(name), "םפללחק");
		widget = getWidget("014","lro");
		name = widget.getNames().get(0);
		addResult("i18nlro14", FormattingUtils.getFormattedWidgetName(name), "םפללחק");
		widget = getWidget("015","lro");
		name = widget.getNames().get(0);
		addResult("i18nlro15", FormattingUtils.getFormattedWidgetShortName(name), "םפללחק");
		widget = getWidget("019","lro");
		name = widget.getNames().get(0);
		addResult("i18nlro019", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
		widget = getWidget("020","lro");
		name = widget.getNames().get(0);
		addResult("i18nlro020", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
		widget = getWidget("021","lro");
		name = widget.getNames().get(0);
		addResult("i18nlro021", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
		widget = getWidget("022","lro");
		name = widget.getNames().get(0);
		addResult("i18nlro022", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
	}

	@Test
	public void nameLtr(){
			String dir ="ltr";
			W3CWidget widget;
			IName name;

			widget = getWidget("001",dir);
			name = widget.getNames().get(0);
			addResult("i18n"+dir+"01", FormattingUtils.getFormattedWidgetName(name), "The arrow should point right -->");
			widget = getWidget("002",dir);
			name = widget.getNames().get(0);
			addResult("i18n"+dir+"02", FormattingUtils.getFormattedWidgetShortName(name), "The arrow should point right -->");	
			widget = getWidget("006",dir);
			name = widget.getNames().get(0);
			addResult("i18n"+dir+"06", FormattingUtils.getFormattedWidgetName(name), "The arrow should point right -->");
			widget = getWidget("010",dir);
			name = widget.getNames().get(0);
			addResult("i18n"+dir+"10", FormattingUtils.getFormattedWidgetName(name), "The arrow should point right -->");
			widget = getWidget("014",dir);
			name = widget.getNames().get(0);
			addResult("i18n"+dir+"14", FormattingUtils.getFormattedWidgetName(name), "The arrow should point right -->");
			widget = getWidget("015",dir);
			name = widget.getNames().get(0);
			addResult("i18n"+dir+"15", FormattingUtils.getFormattedWidgetShortName(name), "The arrow should point right -->");
			widget = getWidget("019",dir);
			name = widget.getNames().get(0);
			addResult("i18n"+dir+"019", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
			widget = getWidget("020",dir);
			name = widget.getNames().get(0);
			addResult("i18n"+dir+"020", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
			widget = getWidget("021",dir);
			name = widget.getNames().get(0);
			addResult("i18n"+dir+"021", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
			widget = getWidget("022",dir);
			name = widget.getNames().get(0);
			addResult("i18n"+dir+"022", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
	}
	
	@Test
	public void nameRlo(){
		String dir ="rlo";
		W3CWidget widget;
		IName name;

		widget = getWidget("001",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"01", FormattingUtils.getFormattedWidgetName(name), "PASSED");
		widget = getWidget("002",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"02", FormattingUtils.getFormattedWidgetShortName(name), "PASSED");	
		widget = getWidget("006",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"06", FormattingUtils.getFormattedWidgetName(name), "PASSED");
		widget = getWidget("010",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"10", FormattingUtils.getFormattedWidgetName(name), "PASSED");
		widget = getWidget("014",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"14", FormattingUtils.getFormattedWidgetName(name), "PASSED");
		widget = getWidget("015",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"15", FormattingUtils.getFormattedWidgetShortName(name), "PASSED");
		widget = getWidget("019",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"019", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
		widget = getWidget("020",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"020", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
		widget = getWidget("021",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"021", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
		widget = getWidget("022",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"022", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");	}
	
	@Test
	public void nameRtl(){
		String dir ="rtl";
		W3CWidget widget;
		IName name;

		widget = getWidget("001",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"01", FormattingUtils.getFormattedWidgetName(name), "The arrow should point right -->");
		widget = getWidget("002",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"02", FormattingUtils.getFormattedWidgetShortName(name), "The arrow should point right -->");	
		widget = getWidget("006",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"06", FormattingUtils.getFormattedWidgetName(name), "The arrow should point right -->");
		widget = getWidget("010",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"10", FormattingUtils.getFormattedWidgetName(name), "The arrow should point right -->");
		widget = getWidget("014",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"14", FormattingUtils.getFormattedWidgetName(name), "The arrow should point right -->");
		widget = getWidget("015",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"15", FormattingUtils.getFormattedWidgetShortName(name), "The arrow should point right -->");
		widget = getWidget("019",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"019", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
		widget = getWidget("020",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"020", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
		widget = getWidget("021",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"021", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
		widget = getWidget("022",dir);
		name = widget.getNames().get(0);
		addResult("i18n"+dir+"022", FormattingUtils.getFormattedWidgetName(name), "< PASSED -->");
	}
	
	// ta-VdCEyDVSA
	// descriptions
	@Test
	public void descriptions(){

		W3CWidget widget;
		IDescription description;
		String dir;
		String test;
		
		dir ="lro";
		test = "003";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "םפללחק");
		
		test="007";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "םפללחק");

		test="011";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "םפללחק");

		test="016";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "םפללחק");


		dir ="ltr";
		test = "003";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "The arrow should point right -->");
		
		test="007";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "The arrow should point right -->");

		test="011";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "The arrow should point right -->");

		test="016";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "The arrow should point right -->");

		
		dir ="rlo";
		test = "003";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "PASSED");
		
		test="007";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "PASSED");

		test="011";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "PASSED");

		test="016";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "PASSED");

		
		dir ="rtl";
		test = "003";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "The arrow should point right -->");
		
		test="007";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "The arrow should point right -->");

		test="011";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "The arrow should point right -->");

		test="016";
		widget = getWidget(test,dir);
		description = widget.getDescriptions().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetDescription(description), "The arrow should point right -->");

	}
	
	// ta-YUMJAPVEgI
	// licenses
	@Test
	public void licenses(){

		W3CWidget widget;
		ILicense license;
		String dir;
		String test;
		
		dir="lro";
		test="005";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "םפללחק");
		test="009";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "םפללחק");
		test="013";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "םפללחק");
		test="018";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "םפללחק");

		dir="ltr";
		test="005";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "The arrow should point right -->");
		test="009";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "The arrow should point right -->");
		test="013";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "The arrow should point right -->");
		test="018";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "The arrow should point right -->");
		
		dir="rlo";
		test="005";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "PASSED");
		test="009";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "PASSED");
		test="013";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "PASSED");
		test="018";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "PASSED");
		
		dir="rtl";
		test="005";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "The arrow should point right -->");
		test="009";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "The arrow should point right -->");
		test="013";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "The arrow should point right -->");
		test="018";
		widget = getWidget(test,dir);
		license = widget.getLicenses().get(0);
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetLicense(license), "The arrow should point right -->");
	}
		
	
	// ta-argMozRiC
	// author
	@Test
	public void author(){
		W3CWidget widget;
		String dir;
		String test;
		IAuthor author;
		
		dir="lro";
		test="004";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "םפללחק");
		test="008";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "םפללחק");
		test="012";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "םפללחק");
		test="017";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "םפללחק");
		test="036";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, author.getHref(), "http://widget.example.org/");
		test="037";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test,author.getEmail(), "םפללחק");
	
		dir="ltr";
		test="004";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "The arrow should point right -->");
		test="008";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "The arrow should point right -->");
		test="012";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "The arrow should point right -->");
		test="017";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "The arrow should point right -->");
		test="036";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, author.getHref(), "http://widget.example.org/");
		test="037";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, author.getEmail(), "The arrow should point right -->");
		
		dir="rlo";
		test="004";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "PASSED");
		test="008";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "PASSED");
		test="012";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "PASSED");
		test="017";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "PASSED");
		test="036";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, author.getHref(), "http://widget.example.org/");
		test="037";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, author.getEmail(), "PASSED");
		
		dir="rtl";
		test="004";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "The arrow should point right -->");
		test="008";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "The arrow should point right -->");
		test="012";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "The arrow should point right -->");
		test="017";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, FormattingUtils.getFormattedWidgetAuthor(author), "The arrow should point right -->");
		test="036";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test, author.getHref(), "http://widget.example.org/");
		test="037";
		widget = getWidget(test,dir);
		author = widget.getAuthor();
		addResult("i18n"+dir+test,  author.getEmail(), "The arrow should point right -->");
				
	}
	
	// Results output
	
	public void addResult(String test, String expected, String actual){
		output += "<tr>";
		output += "<td>"+test+"</td>";
		output += "<td>"+expected+"</td>";
		output += "<td>"+actual+"</td>";
		output += "</tr>";

	}
	
	@AfterClass
	public static void close(){
		output += "</table></body></html>";
		System.out.println(output);
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
	protected String getLocalIconPath(W3CWidget widget, int index){
		return getLocalUrl(widget.getIcons().get(index).getSrc());
	}
	
	protected String getLocalUrl(String src){
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
