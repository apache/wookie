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
package org.apache.wookie.tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.wookie.util.html.HtmlSerializer;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.junit.BeforeClass;
import org.junit.Test;

public class HtmlSerializerTest {
	
	static HtmlCleaner cleaner;
	static CleanerProperties properties;
	
	@BeforeClass
	public static void setup(){
		cleaner = new  org.htmlcleaner.HtmlCleaner();
		// set cleaner properties	
		properties  = cleaner.getProperties();
		properties.setOmitDoctypeDeclaration(false);
		properties.setOmitXmlDeclaration(true);
		properties.setUseCdataForScriptAndStyle(true);
		properties.setUseEmptyElementTags(false);	
	}
	
	private String parse(String content){
		StringWriter writer = new StringWriter();
		HtmlSerializer ser = new HtmlSerializer(properties);
		try {
			TagNode html = cleaner.clean(content);
			ser.writeXml(html, writer, "UTF-8");
			return writer.getBuffer().toString();
		} catch (IOException e) {
			return null;
		}
	}
	
	// tests the content of event handlers are not escaped
	@Test
	public void eventHandlerAttribute(){
		String out = parse("<body onload=\"$('#projects').dataTable();\">");
		assertEquals("<html><head></head><body onload=\"$('#projects').dataTable();\"></body></html>", out);
	}
	
	// tests that other attributes do have content escaped
	@Test
	public void otherAttr(){
		String out = parse("<body class=\"$('#projects').dataTable();\">");
		assertEquals("<html><head></head><body class=\"$(&apos;#projects&apos;).dataTable();\"></body></html>", out);
	}
	
	// tests that script tags are not encoded
	@Test
	public void scriptTag(){
		String out = parse("<script>$('#projects').dataTable();</script>");
		assertEquals("<html><head></head><body><script>$('#projects').dataTable();</script></body></html>", out);
	}
	
	// tests that non-script tags are encoded
	@Test
	public void sillyTag(){
		String out = parse("<silly>$('#projects').dataTable();</silly>");
		assertEquals("<html><head></head><body><silly>$(&apos;#projects&apos;).dataTable();</silly></body></html>", out);
	}
	
	// TODO tests for inline CSS

}
