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
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class W3CWidgetFactoryTest {
	
	final static File wgt = new File("parser/java/src/test/resources/test.wgt");
	final static String widgetGuid = "http://www.getwookie.org/widgets/test";
	final static File testfolder = new File("parser/java/test");	
	final static File testfolderreadonly = new File("parser/java/testr");	
	
	@BeforeClass
	public static void setup(){
		testfolder.mkdir();
		testfolderreadonly.mkdir();
		testfolderreadonly.setReadOnly();
	}
	
	@AfterClass
	public static void tearDown() throws IOException{
		FileUtils.deleteDirectory(testfolder);
		testfolderreadonly.delete();
	}
	
	@Test
	public void testCreateFactory(){
		try {
			@SuppressWarnings("unused")
			W3CWidgetFactory fac = new W3CWidgetFactory();
		} catch (Exception e) {
			fail();
		}
	}
	
	@Test(expected = NullPointerException.class)
	public void testSetOutputDirectoryNull() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setOutputDirectory(null);
	}
	
	@Test(expected = IOException.class)
	public void testSetOutputDirectoryInvalid() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setOutputDirectory("FAIL");
	}
	
	@Test(expected = IOException.class)
	public void testSetOutputDirectoryReadOnly() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setOutputDirectory(testfolderreadonly.getPath());
	}
	
	@Test(expected = IOException.class)
	public void testSetOutputDirectoryInvalidType() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setOutputDirectory(wgt.getPath());
	}
	
	@Test(expected = Exception.class)
	public void testDontSetOutputDirectory() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();		
		fac.parse(wgt);
	}
	
	@Test(expected = NullPointerException.class)
	public void testSetNullEncodings() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();	
		fac.setEncodings(null);
	}
	
	@Test(expected = Exception.class)
	public void testSetNoEncodings() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();		
		fac.setEncodings(new String[]{});
	}
	
	@Test
	public void testSetFeaturesNull() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();	
		fac.setOutputDirectory(testfolder.getPath());
		fac.setFeatures(null);
		fac.parse(wgt);
	}
	
	@Test(expected = NullPointerException.class)
	public void testSetLocalPathNull() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setLocalPath(null);
	}
	
	@Test
	public void testSetLocalPath() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setLocalPath("/");
		fac.setOutputDirectory(testfolder.getPath());
		fac.parse(wgt);
	}
	
	@Test(expected = NullPointerException.class)
	public void setLocalesNull() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setLocales(null);
		fac.setOutputDirectory(testfolder.getPath());
		fac.parse(wgt);		
	}
	
	@Test
	public void testSetLocales() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setLocales(new String[]{"en"});
		fac.setOutputDirectory(testfolder.getPath());
		fac.parse(wgt);
	}
	
	@Test
	public void testSetStartPageProcessorNull() throws Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setStartPageProcessor(null);
		fac.setOutputDirectory(testfolder.getPath());
		fac.parse(wgt);
	}
	
	 @Test
	  public void testGetUnzipped() throws Exception{
	    W3CWidgetFactory fac = new W3CWidgetFactory();
	    fac.setStartPageProcessor(null);
	    fac.setOutputDirectory(testfolder.getPath());
	    fac.parse(wgt);
	    File file = fac.getUnzippedWidgetDirectory();
	    assertEquals("parser/java/test/www.getwookie.org/widgets/test", file.getPath());
      assertTrue(file.isDirectory());
	  }
}


