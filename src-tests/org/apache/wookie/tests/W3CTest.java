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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.manifestmodel.IManifestModel;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.util.WidgetManifestUtils;
import org.jdom.JDOMException;
import org.junit.Before;


/**
 * @author Scott Wilson
 * @author Paul Sharples
 * @version $Id
 *
 */
public class W3CTest extends TestCase implements IW3CXMLConfiguration {

	private static final String WRONG_XML_FILE = "src-tests/testdata/wrong/config.xml";
	private static final String BASIC_MANIFEST_FILE = "src-tests/testdata/basic_manifest/config.xml";
	private static final String MANIFEST_WITH_PREFERENCES_FILE = "src-tests/testdata/prefs_manifest/config.xml";
	private static final String BAD_NAMESPACE_FILE = "src-tests/testdata/bad_ns/config.xml";
	private static final String FEATURES_MANIFEST_FILE = "src-tests/testdata/features/config.xml";
	private static String WRONG_XML;
	private static String BASIC_MANIFEST;
	private static String MANIFEST_WITH_PREFERENCES;
	private static String BAD_NAMESPACE_MANIFEST;
	private static String FEATURES_MANIFEST;


	@Before public void setUp() {  

		try {
			WRONG_XML = readFile(new File(WRONG_XML_FILE));
			BASIC_MANIFEST = readFile(new File(BASIC_MANIFEST_FILE));
			MANIFEST_WITH_PREFERENCES = readFile(new File(MANIFEST_WITH_PREFERENCES_FILE));
			BAD_NAMESPACE_MANIFEST = readFile(new File(BAD_NAMESPACE_FILE));
			FEATURES_MANIFEST = readFile(new File(FEATURES_MANIFEST_FILE));
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void testWrongXML(){
		try {
			@SuppressWarnings("unused")
			IManifestModel model = WidgetManifestUtils.dealWithManifest(WRONG_XML,null);
			// This should throw a BadManifestException	
			//assertNull(model);
		} 
		catch (JDOMException ex) {
			fail("couldn't read XML");
		} 
		catch (IOException ex) {
			fail("couldn't load XML");
		} 
		catch (BadManifestException ex){			
			System.out.println("testWrongXML():" + ex.getMessage());
		}
	}

	public void testParseManifestBadNS(){    	
		try {
			@SuppressWarnings("unused")
			IManifestModel model = WidgetManifestUtils.dealWithManifest(BAD_NAMESPACE_MANIFEST,null);
			// This should throw a BadManifestException			
		} 
		catch (BadManifestException ex) {    		
			System.out.println("testParseManifestBadNS():"+ex.getMessage());
		}
		catch (JDOMException ex) {
			fail("couldn't read XML");    		
		} 
		catch (IOException ex) {
			fail("couldn't load XML");
		}
	}

	public void testParseManifest(){
		try {
			IManifestModel model = WidgetManifestUtils.dealWithManifest(BASIC_MANIFEST,null);
			assertNotNull(model);
			assertEquals("http://www.getwookie.org/widgets/WP3/natter", model.getIdentifier());
			assertEquals("Natter", model.getFirstName());
			assertEquals(255, model.getWidth());
			assertEquals(383, model.getHeight());
			assertEquals("Icon.png", model.getFirstIconPath());
			assertEquals("Scott Wilson", model.getAuthor());
			assertEquals("1.0", model.getVersion());	
			assertEquals("basic chat widget", model.getFirstDescription());
			assertEquals("application", model.getViewModes());	
		} 
		catch (JDOMException ex) {
			fail("couldn't read XML");
		} 
		catch (IOException ex) {
			fail("couldn't load XML");
		} 
		catch (Exception ex){
			fail("didn't parse manifest correctly");
		}
	}

	public void testFeaturesExample(){		
		try {
			IManifestModel model = WidgetManifestUtils.dealWithManifest(FEATURES_MANIFEST, null);
			assertNotNull(model);
			assertEquals("http://www.getwookie.org/example", model.getIdentifier());
			assertEquals("Example Test Widget", model.getFirstName());			
			assertEquals(2, model.getNames().size());
			assertEquals("es", model.getDescriptions().get(1).getLanguage());
			
			// now do the features
			assertEquals(4, model.getFeatures().size());
			assertEquals("http://www.getwookie.org/testfeature1", model.getFeatures().get(0).getName());
			assertEquals(true, model.getFeatures().get(0).isRequired());
			assertEquals(4, model.getFeatures().get(0).getParams().size());
			assertEquals("p1", model.getFeatures().get(0).getParams().get(0).getName());
			assertEquals("v1", model.getFeatures().get(0).getParams().get(0).getValue());
			assertEquals("p2", model.getFeatures().get(0).getParams().get(1).getName());
			assertEquals("v2", model.getFeatures().get(0).getParams().get(1).getValue());
			assertEquals("p3", model.getFeatures().get(0).getParams().get(2).getName());
			assertEquals("v3", model.getFeatures().get(0).getParams().get(2).getValue());
			assertEquals("", model.getFeatures().get(0).getParams().get(3).getName());
			assertEquals("", model.getFeatures().get(0).getParams().get(3).getValue());
			
			assertEquals("http://www.getwookie.org/testfeature2", model.getFeatures().get(1).getName());
			assertEquals(true, model.getFeatures().get(1).isRequired());
			assertEquals(1, model.getFeatures().get(1).getParams().size());
			assertEquals("p5", model.getFeatures().get(1).getParams().get(0).getName());
			assertEquals("v5", model.getFeatures().get(1).getParams().get(0).getValue());
			
			assertEquals("http://www.getwookie.org/testfeature3", model.getFeatures().get(2).getName());
			assertEquals(false, model.getFeatures().get(2).isRequired());
			assertEquals(0, model.getFeatures().get(2).getParams().size());
			
			assertEquals("http://www.getwookie.org/usefeature/polling", model.getFeatures().get(3).getName());
		} 
		catch (JDOMException ex) {
			fail("couldn't read XML");
		} 
		catch (IOException ex) {
			fail("couldn't load XML");
		} 
		catch (BadManifestException ex) {
			fail("Problem with the manifest:" + ex.getMessage());
		} 
		catch (Exception ex){			
			fail("didn't parse manifest correctly");
		}
	}

	public void testPrefsManifest(){
		try {
			IManifestModel model = WidgetManifestUtils.dealWithManifest(MANIFEST_WITH_PREFERENCES,null);
			assertNotNull(model);
			// should be 3 prefs
			assertEquals(3, model.getPrefences().size());

			assertEquals("pref1", model.getPrefences().get(0).getName());
			assertEquals("1", model.getPrefences().get(0).getValue());				
			assertEquals(true, model.getPrefences().get(0).isReadOnly());

			assertEquals("pref2", model.getPrefences().get(1).getName());
			assertEquals("2", model.getPrefences().get(1).getValue());
			assertEquals(false, model.getPrefences().get(1).isReadOnly());

			assertEquals("pref3", model.getPrefences().get(2).getName());
			assertEquals("", model.getPrefences().get(2).getValue());
			assertEquals(false, model.getPrefences().get(2).isReadOnly());
			assertEquals(3, model.getPrefences().size());

		} 
		catch (JDOMException ex) {
			fail("couldn't read XML");
		} 
		catch (IOException ex) {
			fail("couldn't load XML");
		} 
		catch (Exception ex){
			fail("didn't parse manifest correctly");
		}
	}

	private String readFile(File file) throws Exception{
		StringBuffer sb = new StringBuffer(1024);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		reader.close();

		return sb.toString();	
	}

}
