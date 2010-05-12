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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.exceptions.InvalidContentTypeException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test cases for W3C WARP conformance
 *
 */
public class Access {
	
	private static final String prefix = "http://dev.w3.org/2006/waf/widgets-access/test-suite/test-cases/";
	
	 static File download;
	 static File output;
	
	@BeforeClass
	public static void setup() throws IOException{
		download = File.createTempFile("wookie-download", "wgt");
		output = File.createTempFile("wookie-output", "tmp");
	}
	
	@AfterClass
	public static void tearDown(){
		download.delete();
		output.delete();
	}
	
	private W3CWidget downloadWidget(String url) throws InvalidContentTypeException, BadWidgetZipFileException, BadManifestException, Exception{
		return downloadWidget(url, true);
	}
	
	private W3CWidget downloadWidget(String url, boolean ignoreContentType) throws InvalidContentTypeException, BadWidgetZipFileException, BadManifestException, Exception{
		W3CWidgetFactory fac = new W3CWidgetFactory();
		fac.setLocalPath("http:localhost/widgets");
		fac.setFeatures(new String[]{"feature:a9bb79c1"});
		fac.setEncodings(new String[]{"UTF-8", "ISO-8859-1","Windows-1252"});
		if (download.exists()) download.delete();
		if (output.exists()) output.delete();
		output.mkdir();
		fac.setOutputDirectory(output.getAbsolutePath());
		return fac.parse(new URL(url),ignoreContentType);
	}
	
	public W3CWidget processWidgetNoErrors(String url){
		try {
			return downloadWidget(url);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
			return null;
		}
	}

	@Test
	public void ea(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-5/000/ea.wgt");
		assertEquals(1, widget.getAccessList().size());		
		assertEquals("http://w3.org:80", widget.getAccessList().get(0).getOrigin());
		assertEquals(false, widget.getAccessList().get(0).hasSubDomains());
	}
	
	@Test
	public void eb(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-5/001/eb.wgt");
		assertEquals(1, widget.getAccessList().size());		
		assertEquals("http://w3.org:80", widget.getAccessList().get(0).getOrigin());
		assertEquals(false, widget.getAccessList().get(0).hasSubDomains());
	}
	@Test
	public void ec(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-5/002/ec.wgt");
		assertEquals(1, widget.getAccessList().size());		
		assertEquals("http://w3.org:80", widget.getAccessList().get(0).getOrigin());
		assertEquals(false, widget.getAccessList().get(0).hasSubDomains());
	}
	@Test
	public void ed(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-5/003/ed.wgt");
		assertEquals(0, widget.getAccessList().size());		
	}
	
	@Test
	public void fa(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-6/000/fa.wgt");
		assertEquals(0, widget.getAccessList().size());
	}
	
	@Test
	public void ga(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-7/000/ga.wgt");
		assertEquals(1, widget.getAccessList().size());
		assertEquals("*", widget.getAccessList().get(0).getOrigin());
		assertEquals(false, widget.getAccessList().get(0).hasSubDomains());
	}	
	
	@Test
	public void gb(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-7/001/gb.wgt");
		assertEquals(1, widget.getAccessList().size());
		assertEquals("*", widget.getAccessList().get(0).getOrigin());
		assertEquals(false, widget.getAccessList().get(0).hasSubDomains());
	}	
	
	@Test
	public void gc(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-7/002/gc.wgt");
		assertEquals(2, widget.getAccessList().size());
		assertEquals("*", widget.getAccessList().get(0).getOrigin());
		assertEquals("http://w3.org:80", widget.getAccessList().get(1).getOrigin());
		assertEquals(false, widget.getAccessList().get(1).hasSubDomains());
	}
	
	@Test
	public void ha(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-8/000/ha.wgt");
		assertEquals(0, widget.getAccessList().size());
	}	

	@Test
	public void hb(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-8/001/hb.wgt");
		assertEquals(1, widget.getAccessList().size());
		assertEquals("pass://pass", widget.getAccessList().get(0).getOrigin());
		assertEquals(false, widget.getAccessList().get(0).hasSubDomains());
	}
	
	@Test
	public void hc(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-8/002/hc.wgt");
		assertEquals(1, widget.getAccessList().size());
		assertEquals("pass://pass", widget.getAccessList().get(0).getOrigin());
		assertEquals(false, widget.getAccessList().get(0).hasSubDomains());
	}
	
	@Test
	public void hd(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-8/003/hd.wgt");
		assertEquals(0, widget.getAccessList().size());
	}
	
	@Test
	public void he(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-8/004/he.wgt");
		assertEquals(0, widget.getAccessList().size());
	}
	
	@Test
	public void hf(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-8/005/hf.wgt");
		assertEquals(0, widget.getAccessList().size());
	}
	
	@Test
	public void hg(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-8/006/hg.wgt");
		assertEquals(1, widget.getAccessList().size());
		assertEquals("http://w3.org:80", widget.getAccessList().get(0).getOrigin());
		assertEquals(false, widget.getAccessList().get(0).hasSubDomains());
	}
	
	@Test
	public void hh(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-8/007/hh.wgt");
		assertEquals(0, widget.getAccessList().size());
	}
	
	@Test
	public void hi(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-8/008/hi.wgt");
		assertEquals(0, widget.getAccessList().size());
	}
	
	@Test
	public void hj(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-8/009/hj.wgt");
		assertEquals(0, widget.getAccessList().size());
	}
	
	@Test
	public void ia(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-9/000/ia.wgt");
		assertEquals(1, widget.getAccessList().size());
		assertEquals("http://w3.org:80", widget.getAccessList().get(0).getOrigin());
		assertEquals(true, widget.getAccessList().get(0).hasSubDomains());
	}
	
	@Test
	public void ib(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-9/001/ib.wgt");
		assertEquals(1, widget.getAccessList().size());
		assertEquals("http://w3.org:80", widget.getAccessList().get(0).getOrigin());
		assertEquals(false, widget.getAccessList().get(0).hasSubDomains());
	}
	
	@Test
	public void ic(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-9/002/ic.wgt");
		assertEquals(0, widget.getAccessList().size());
	}
	
	@Test
	public void ja(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-10/000/ja.wgt");
		assertEquals(1, widget.getAccessList().size());
		assertEquals("http://w3.org:8080", widget.getAccessList().get(0).getOrigin());
		assertEquals(true, widget.getAccessList().get(0).hasSubDomains());
	}
	
	@Test
	public void jb(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-10/001/jb.wgt");
		assertEquals(1, widget.getAccessList().size());
		assertEquals("https://w3.org:443", widget.getAccessList().get(0).getOrigin());
		assertEquals(true, widget.getAccessList().get(0).hasSubDomains());
	}
	
	@Test
	public void ma(){
		W3CWidget widget = processWidgetNoErrors(prefix+"ta-13/000/ma.wgt");
		assertEquals(2, widget.getAccessList().size());
		assertEquals("http://w3.org:80", widget.getAccessList().get(0).getOrigin());
		assertEquals(true, widget.getAccessList().get(0).hasSubDomains());
		assertEquals("pass://pass", widget.getAccessList().get(1).getOrigin());
		assertEquals(false, widget.getAccessList().get(1).hasSubDomains());
	}

}
