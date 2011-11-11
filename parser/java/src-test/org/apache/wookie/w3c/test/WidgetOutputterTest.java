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
import java.io.FileOutputStream;
import java.net.URL;

import org.apache.wookie.w3c.INameEntity;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.impl.IconEntity;
import org.apache.wookie.w3c.util.WidgetOutputter;
import org.apache.wookie.w3c.util.WidgetPackageUtils;
import org.junit.Test;

/**
 * Testing outputting Widgets
 */
public class WidgetOutputterTest extends ConformanceTest{
	
	@Test
	public void outputString() throws Exception{
		File testWidget = new File("build/widgets/bubbles.wgt");
		W3CWidget widget = load(testWidget);
		WidgetOutputter outputter = new WidgetOutputter();
		outputter.setWidgetFolder("/widgets");
		String manifest = outputter.outputXMLString(widget);
		assertTrue(manifest.contains("id=\"http://www.opera.com/widgets/bubbles\""));
		assertTrue(manifest.contains("<content src=\"index.html\""));
	}
	@Test
	public void outputString2() throws Exception{
		File testWidget = new File("build/widgets/natter.wgt");
		W3CWidget widget = load(testWidget);
		WidgetOutputter outputter = new WidgetOutputter();
		outputter.setWidgetFolder("/widgets");
		String manifest = outputter.outputXMLString(widget);
		assertTrue(manifest.contains("id=\"http://www.getwookie.org/widgets/natter\""));
		assertTrue(manifest.contains("<content src=\"index.htm\""));
		assertTrue(manifest.contains("name=\"moderator\" value=\"false\""));
	}
	@Test
	public void outputString3() throws Exception{
		File testWidget = new File("build/widgets/weather.wgt");
		W3CWidget widget = load(testWidget);
		WidgetOutputter outputter = new WidgetOutputter();
		outputter.setWidgetFolder("/widgets");
		String manifest = outputter.outputXMLString(widget);
		assertTrue(manifest.contains("id=\"http://www.getwookie.org/widgets/weather\""));
		assertTrue(manifest.contains("origin=\"http://newsrss.bbc.co.uk"));
	}
	@Test
	public void outputString4() throws Exception{
		File testWidget = new File("build/widgets/localetest.wgt");
		W3CWidget widget = load(testWidget);
		WidgetOutputter outputter = new WidgetOutputter();
		outputter.setWidgetFolder("/widgets");
		String manifest = outputter.outputXMLString(widget);
		assertTrue(manifest.contains("id=\"http://www.getwookie.org/widgets/localetest\""));
		assertTrue(manifest.contains("xml:lang=\"fr\">tester les paramètres régionaux</name>"));
	}
 @Test
  public void outputString5() throws Exception{
    File testWidget = new File("build/widgets/localetest.wgt");
    W3CWidget widget = load(testWidget);
    widget.getIconsList().add(new IconEntity("icon.png",100,100));
    widget.getNames().get(0).setShort("shortName");
    WidgetOutputter outputter = new WidgetOutputter();
    outputter.setWidgetFolder("/widgets");
    String manifest = outputter.outputXMLString(widget);
    System.out.println(manifest);
    assertTrue(manifest.contains("id=\"http://www.getwookie.org/widgets/localetest\""));
    assertTrue(manifest.contains("xml:lang=\"fr\">tester les paramètres régionaux</name>"));
    assertTrue(manifest.contains("<icon src=\"icon.png\" height=\"100\" width=\"100\" />"));
    assertTrue(manifest.contains("name short=\"shortName\""));
  }
	
	@Test
	public void outputStream() throws Exception{
		File testWidget = new File("build/widgets/geo.wgt");
		W3CWidget widget = load(testWidget);
		WidgetOutputter outputter = new WidgetOutputter();
		outputter.setWidgetFolder("/widgets");
		
		File widgetFolder = new File(output.getPath());
		File configXml = new File(widgetFolder, "config.xml");
		FileOutputStream out = new FileOutputStream(configXml);
		outputter.outputXML(widget, out);
		assertTrue(configXml.exists());
	}
	
	@Test 
	public void loadModifySave() throws Exception{
		File testWidget = new File("build/widgets/bubbles.wgt");
		W3CWidget widget = load(testWidget);
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
	private W3CWidget load(File file) throws BadWidgetZipFileException, BadManifestException, Exception{
		W3CWidgetFactory fac = getFactory();
		W3CWidget widget = fac.parse(file);
		return widget;		
	}
	
	private W3CWidgetFactory getFactory() throws Exception{
		String outputPath = "/widgets";
		// Load the widget
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setLocalPath(outputPath);
		fac.setFeatures(new String[]{"feature:a9bb79c1","http://wave.google.com", "http://incubator.apache.org/wookie/ext"});
		fac.setEncodings(new String[]{"UTF-8", "ISO-8859-1","Windows-1252"});
		if (download.exists()) download.delete();
		if (output.exists()) output.delete();
		output.mkdir();
		fac.setOutputDirectory(output.getAbsolutePath());
		return fac;
	}
}
