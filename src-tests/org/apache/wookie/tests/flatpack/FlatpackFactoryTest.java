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

package org.apache.wookie.tests.flatpack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.feature.Features;
import org.apache.wookie.flatpack.FlatpackFactory;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Tests the FlatpackFactory functionalty.
 */
public class FlatpackFactoryTest {
	
	static File download;
	static File output;
	static File flatpack;
	
	/**
	 * Create some temporary folders
	 */
	@BeforeClass
	public static void setup() throws IOException{
		download = File.createTempFile("wookie-download", "wgt");
		output = File.createTempFile("wookie-output", "tmp");
		flatpack = File.createTempFile("wookie-flatpack", "");
		Features.loadFeatures(new File("features"), "/wookie/features");
	}
	
	/**
	 * Setup temporary folders before each test
	 */
	@Before
	public void setupEach(){
		if (download.exists()) download.delete();
		if (output.exists()) output.delete();
		if (flatpack.exists()) flatpack.delete();
		output.mkdir();
		flatpack.mkdir();
	}
	
	/**
	 * Delete temporary folders after tests have been completed
	 */
	@AfterClass
	public static void tearDown(){
		download.delete();
		output.delete();
		flatpack.delete();
	}
	
	/**
	 * Tests that the factory throws an exception if no widget instance
	 * is specified
	 * @throws Exception
	 */
	@Test(expected=Exception.class)
	public void testNoInstance() throws Exception{
		FlatpackFactory flatfac = new FlatpackFactory(null);
		flatfac.setInputWidget(null); // this is the original .wgt
		@SuppressWarnings("unused")
    File file = flatfac.pack(); // Get the new .wgt file	
	}

	/**
	 * Test creating a flatpack for an instance of a widget using the factory defaults
	 * @throws Exception
	 */
	@Test
	public void createBasicFlatpackUsingDefaults() throws Exception{
	  //
		// upload a new widget to test with
	  //
		W3CWidgetFactory fac = getFactory();
		File testWidget = new File("build/widgets/bubbles.wgt");
		fac.parse(testWidget);
		download = fac.getUnzippedWidgetDirectory(); //download is where we unzipped the widget
		
		//
		// Create an instance of it
		//
		IWidgetInstance instance = new WidgetInstanceMock();
		
		//
		// Flatpack it
		//
		FlatpackFactory flatfac = new FlatpackFactory(instance);
		flatfac.setInputWidget(testWidget); // this is the original .wgt
		File file = flatfac.pack(); // Get the new .wgt file
	
		//
		// Test it works!
		//
		System.out.println(file.getAbsolutePath());
		W3CWidget fpWidget = fac.parse(file);
		assertNotNull(fpWidget);
		
	}
	
	/**
	 * Test creating a flatpack for an instance of a widget
	 * @throws Exception
	 */
	@Test
	public void createBasicFlatpack() throws Exception{
	  //
		// upload a new widget to test with
	  //
		W3CWidgetFactory fac = getFactory();
		File testWidget = new File("build/widgets/bubbles.wgt");
		fac.parse(testWidget);
		download = fac.getUnzippedWidgetDirectory(); //download is where we unzipped the widget
		
		//
		// Create an instance of it
		//
		IWidgetInstance instance = new WidgetInstanceMock();
		
		//
		// Flatpack it
		//
		FlatpackFactory flatfac = new FlatpackFactory(instance);
		flatfac.setParser(fac);
		flatfac.setInputWidget(testWidget); // this is the original .wgt
		flatfac.setFlatpackFolder(flatpack); // flatpack is our new temp folder. This would probably be "/flatpack" or similar in deployment
		File file = flatfac.pack(); // Get the new .wgt file
	
		//
		// Test it works!
		//
		System.out.println(file.getAbsolutePath());
		W3CWidget fpWidget = fac.parse(file);
		assertNotNull(fpWidget);
		
	}
	
	 /**
   * Test creating a flatpack for an instance of a widget using the factory defaults
   * when the widget also uses Features
   * @throws Exception
   */
  @Test
  public void createFeatureFlatpackUsingDefaults() throws Exception{
    
    //
    // upload a new widget to test with
    //
    W3CWidgetFactory fac = getFactory();
    fac.setFeatures(Features.getFeatureNames());
    File testWidget = new File("build/widgets/freeder.wgt");
    fac.parse(testWidget);
    
    //
    //download is where we unzipped the widget
    //
    download = fac.getUnzippedWidgetDirectory(); 
    
    //
    // Create an instance of it
    //
    IWidgetInstance instance = new WidgetInstanceMock();
    
    //
    // Flatpack it
    //
    FlatpackFactory flatfac = new FlatpackFactory(instance);
    flatfac.setInputWidget(testWidget); // this is the original .wgt
    File file = flatfac.pack(); // Get the new .wgt file
  
    //
    // Test it works!
    //
    System.out.println("createFeatureFlatpackUsingDefaults: "+file.getAbsolutePath());
    W3CWidget fpWidget = fac.parse(file);
    assertNotNull(fpWidget);
    
    //
    // The JQM feature should have been removed from config.xml
    //
    assertEquals(0, fpWidget.getFeatures().size());
    
  }
	
	/**
	 * Test that we add preference defaults from an instance
	 * @throws Exception
	 */
	@Test
	public void createFlatpackWithPreferences() throws Exception{
		
	  //
		// upload a new widget to test with
	  //
		W3CWidgetFactory fac = getFactory();
		File testWidget = new File("build/widgets/bubbles.wgt");
		fac.parse(testWidget);
		download = fac.getUnzippedWidgetDirectory(); //download is where we unzipped the widget
		
		//
		// Create an instance of it
		//
		IWidgetInstance instance = new WidgetInstanceMock();
		ArrayList<IPreference> prefs = new ArrayList<IPreference>();
		IPreference pref = new PreferenceMock();
		pref.setDkey("hiScore");
		pref.setDvalue("1000");
		pref.setReadOnly(false);
		prefs.add(pref);
		instance.setPreferences(prefs);
		
		//
		// Flatpack it
		//
		FlatpackFactory flatfac = new FlatpackFactory(instance);
		flatfac.setParser(fac);
		flatfac.setInputWidget(testWidget); // this is the original .wgt
		flatfac.setFlatpackFolder(flatpack); // flatpack is our new temp folder. This would probably be "/flatpack" or similar in deployment
		File file = flatfac.pack(); // Get the new .wgt file
	
		//
		// Test it works!
		//
		System.out.println(file.getAbsolutePath());
		W3CWidget fpWidget = fac.parse(file);
		assertEquals("hiScore", fpWidget.getPrefences().get(0).getName());
		assertEquals("1000", fpWidget.getPrefences().get(0).getValue());
	}
	
	
	/**
	 * Test that we add preference defaults from an instance, but don't duplicate
	 * existing preferences.
	 * 
	 * @throws Exception
	 */
	@Test
	public void createFlatpackWithPreferences2() throws Exception{
		
	  //
		// upload a new widget to test with
	  //
		W3CWidgetFactory fac = getFactory();
		fac.setFeatures(Features.getFeatureNames());
		File testWidget = new File("build/widgets/simplechat.wgt");
		fac.parse(testWidget);
		download = fac.getUnzippedWidgetDirectory(); //download is where we unzipped the widget
		
		//
		// Create an instance of it
		//
		IWidgetInstance instance = new WidgetInstanceMock();
		ArrayList<IPreference> prefs = new ArrayList<IPreference>();
		IPreference pref = new PreferenceMock();
		pref.setDkey("moderator");
		pref.setDvalue("true");
		pref.setReadOnly(false);
		prefs.add(pref);
		instance.setPreferences(prefs);
		
		//
		// Flatpack it
		//
		FlatpackFactory flatfac = new FlatpackFactory(instance);
		flatfac.setParser(fac);
		flatfac.setInputWidget(testWidget); // this is the original .wgt
		flatfac.setFlatpackFolder(flatpack); // flatpack is our new temp folder. This would probably be "/flatpack" or similar in deployment
		File file = flatfac.pack(); // Get the new .wgt file
	
		//
		// Test it works!
		//
		System.out.println(file.getAbsolutePath());
		W3CWidget fpWidget = fac.parse(file);
		assertEquals("moderator", fpWidget.getPrefences().get(0).getName());
		assertEquals("true", fpWidget.getPrefences().get(0).getValue());
		assertEquals(1,fpWidget.getPrefences().size());
	}

	/**
	 * Construct a standard W3CWidgetFactory for testing
	 */
	private W3CWidgetFactory getFactory() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setLocalPath("/widgets");
		fac.setEncodings(new String[]{"UTF-8", "ISO-8859-1","Windows-1252"});
		fac.setOutputDirectory(output.getAbsolutePath());
		return fac;
	}
}
