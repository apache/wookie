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

import java.io.File;
import java.net.URL;

import org.apache.wookie.w3c.INameEntity;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.util.WidgetOutputter;
import org.apache.wookie.w3c.util.WidgetPackageUtils;
import org.junit.Test;

/**
 * Testing outputting Widgets
 */
public class WidgetOutputterTest extends ConformanceTest{
	
	@Test
	public void outputString() throws Exception{
		W3CWidget widget = load("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RRZxvvTFHx/001/b6.wgt");
		WidgetOutputter outputter = new WidgetOutputter();
		outputter.setWidgetFolder("/widgets");
		String manifest = outputter.outputXMLString(widget);
		assertTrue(manifest.contains("id=\"b6:\""));
		assertTrue(manifest.contains("<content src=\"index.html\""));
	}
	
	@Test 
	public void loadModifySave() throws Exception{
		W3CWidget widget = load("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RRZxvvTFHx/001/b6.wgt");
		INameEntity name = widget.getNames().get(0);
		name.setName("Modified Widget");
		WidgetOutputter outputter = new WidgetOutputter();
		outputter.setWidgetFolder("/widgets");
		String manifest = outputter.outputXMLString(widget);
		assertTrue(manifest.contains("Modified Widget"));
		assertTrue(manifest.contains("<content src=\"index.html\""));
	}

	@Test 
	public void loadModifySaveReload() throws Exception{

		// Load the widget
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setLocalPath("/widgets");
		fac.setFeatures(new String[]{"feature:a9bb79c1"});
		fac.setEncodings(new String[]{"UTF-8", "ISO-8859-1","Windows-1252"});
		if (download.exists()) download.delete();
		if (output.exists()) output.delete();
		output.mkdir();
		fac.setOutputDirectory(output.getAbsolutePath());
		W3CWidget widget = fac.parse(new URL("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RRZxvvTFHx/001/b6.wgt"));
		
		INameEntity name = widget.getNames().get(0);
		name.setName("Re-Modified Widget");
		WidgetOutputter outputter = new WidgetOutputter();
		outputter.setWidgetFolder("/widgets");
		
		// Save the config.xml
		File widgetFolder = new File(output.getPath()+"/b6");
		File configXml = new File(widgetFolder, "config.xml");
		outputter.outputXML(widget, configXml);
		
		// Pack up the widget
		File zip = File.createTempFile("wookie-output", ".wgt");
		WidgetPackageUtils.repackZip(widgetFolder, zip);
		
		// Reload the widget but using a new path
		fac.setLocalPath("/zaphod.bee.ble/brox");
		widget = fac.parse(zip);
		assertEquals("Re-Modified Widget", widget.getLocalName("en"));
		// Check the content is now pointing to the new location
		assertEquals("/zaphod.bee.ble/brox/b6/index.html", widget.getContentList().get(0).getSrc());
	}
	
	private W3CWidget load(String url) throws Exception{
		String outputPath = "/widgets";
		
		// Load the widget
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setLocalPath(outputPath);
		fac.setFeatures(new String[]{"feature:a9bb79c1"});
		fac.setEncodings(new String[]{"UTF-8", "ISO-8859-1","Windows-1252"});
		if (download.exists()) download.delete();
		if (output.exists()) output.delete();
		output.mkdir();
		fac.setOutputDirectory(output.getAbsolutePath());
		W3CWidget widget = fac.parse(new URL(url));
		return widget;
	}
}
