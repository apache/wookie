/**
 * 
 */
package org.tencompetence.widgetservice.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import junit.framework.TestCase;

import org.jdom.JDOMException;
import org.junit.Before;
import org.tencompetence.widgetservice.beans.PreferenceDefault;
import org.tencompetence.widgetservice.util.ManifestHelper;


/**
 * @author scott
 *
 */
public class W3CTest extends TestCase{
	
	private static final String WRONG_XML_FILE = "src-tests/testdata/wrong.xml";
	private static final String BASIC_MANIFEST_FILE = "src-tests/testdata/basic_manifest.xml";
	private static final String MANIFEST_WITH_PREFERENCES_FILE = "src-tests/testdata/prefs_manifest.xml";
	private static final String BAD_NAMESPACE_FILE = "src-tests/testdata/bad_ns.xml";
	private static String WRONG_XML;
	private static String BASIC_MANIFEST;
	private static String MANIFEST_WITH_PREFERENCES;
	private static String BAD_NAMESPACE_MANIFEST;
	
	
    @Before public void setUp() {  
    	
    	try {
    		WRONG_XML = readFile(new File(WRONG_XML_FILE));
			BASIC_MANIFEST = readFile(new File(BASIC_MANIFEST_FILE));
			MANIFEST_WITH_PREFERENCES = readFile(new File(MANIFEST_WITH_PREFERENCES_FILE));
			BAD_NAMESPACE_MANIFEST = readFile(new File(BAD_NAMESPACE_FILE));
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void testWrongXML(){
    	try {
			Hashtable<String, String> table = ManifestHelper.dealWithManifest(WRONG_XML,null);
			assertNull(table);
		} catch (JDOMException e) {
			fail("couldn't read XML");
		} catch (IOException e) {
			fail("couldn't load XML");
		} catch (Exception e){
			e.printStackTrace();
			fail("didn't parse manifest correctly");
		}
    }
    
    public void testParseManifestBadNS(){
    	try {
			@SuppressWarnings("unused")
			Hashtable<String, String> table = ManifestHelper.dealWithManifest(BAD_NAMESPACE_MANIFEST,null);
            // Uh-oh! No exception was thrown so we 
            // better make this test fail!
            fail("should've thrown an exception!");
		} catch (Exception e) {
            // this is exactly what we were expecting so 
            // let's just ignore it and let the test pass
		}
    }
	
	public void testParseManifest(){
		try {
			Hashtable<String, String> table = ManifestHelper.dealWithManifest(BASIC_MANIFEST,null);
			assertNotNull(table);
			assertEquals(table.get("id"), "http://www.getwookie.org/widgets/WP3/natter");
			assertEquals(table.get("name"), "Natter");
			assertEquals(table.get("width"), "255");
			assertEquals(table.get("height"), "383");
			assertEquals(table.get("icon_src_1"), "Icon.png");
			assertEquals(table.get("author"), "Scott Wilson");
			assertEquals(table.get("version"), "1.0");	
			assertEquals(table.get("description"), "basic chat widget");
			assertEquals(table.get("viewmode"), "default");	
		} catch (JDOMException e) {
			fail("couldn't read XML");
		} catch (IOException e) {
			fail("couldn't load XML");
		} catch (Exception e){
			fail("didn't parse manifest correctly");
		}
	}
	
	public void testPreferences(){
		try {
			ArrayList<PreferenceDefault> prefsList = (ArrayList<PreferenceDefault>) ManifestHelper.getPreferenceDefaults(MANIFEST_WITH_PREFERENCES);
			PreferenceDefault[] prefs = (PreferenceDefault[]) prefsList.toArray(new PreferenceDefault[prefsList.size()]);
			assertNotNull(prefs);
			assertEquals(3,prefs.length);
			assertEquals("pref1", prefs[0].getPreference());
			assertEquals("1", prefs[0].getValue());
			assertEquals("pref2", prefs[1].getPreference());
			assertEquals("2", prefs[1].getValue());	
			assertEquals("pref3", prefs[2].getPreference());
			assertNull(prefs[2].getValue());		
		} catch (Exception e) {
			fail("Didn't parse preferences correctly");
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
