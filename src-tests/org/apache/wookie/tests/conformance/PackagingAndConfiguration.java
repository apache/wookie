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
package org.apache.wookie.tests.conformance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.wookie.tests.helpers.WidgetUploader;
import org.apache.wookie.w3c.util.WidgetPackageUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Conformance testing for the Packaging and Configuration spec
 * These are functional tests and need to run against a running Wookie server on localhost:8080
 * You need to be online to run these tests as they download test widgets hosted externally.
 * For more information see http://dev.w3.org/2006/waf/widgets/test-suite/index.html
 * 
 * Note that in order to run the Feature tests, a ServerFeature named "feature:a9bb79c1" must be set up in the DB
 */
public class PackagingAndConfiguration extends AbstractFunctionalConformanceTest {
	// 1 files
	@Test
	public void b5(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RRZxvvTFHx/000/b5.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void b6(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RRZxvvTFHx/001/b6.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.html",start);
	}

	// 2 files
	@Test
	public void bg(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-dxzVDWpaWg/000/bg.wgt");
		assertFalse(err == null||err.equals(""));
	}	

	@Test
	public void bh(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-dxzVDWpaWg/001/bh.wgt");
		assertFalse(err == null||err.equals(""));
	}	

	// 3 bad magic number
	@Test
	public void dk(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-qxLSCRCHlN/000/dk.wgt");
		assertFalse(err == null||err.equals(""));
	}	

	// 4 start files
	@Test
	public void dn(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FDGQBROtzW/000/dn.test");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void dm(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FDGQBROtzW/001/dm");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}


	// 5
	@Test
	public void dl(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-uLHyIMvLwz/000/dl.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void doh(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-uLHyIMvLwz/001/split.wgt.001");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void dp(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-uLHyIMvLwz/002/dp.wgt");
		assertFalse(err == null||err.equals(""));
	}
	
	/**
	 * This test has been deprecated by W3C

	// 6 defaults
	@Test 
	public void ds(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-xGYcVTMoAX/000/ds.wgt");
		// To pass, author email must be null, 
		// author href must be null, 
		// author name must be null, 
		assertEquals("", widget.getChild("author").getText());
		// feature list must be empty (null)
		/// Check manually
		System.out.println("Manual test:"+widget.getAttributeValue("id")+": feature list must be empty ");
		// icons must be null, 
		assertEquals(null, widget.getChild("icon"));
		// start file encoding must be UTF-8, 
		assertEquals("UTF-8", getStartFileEncoding(widget));
		// start file content-type must be text/html, 
		assertTrue(getStartFileContentType(widget).startsWith("text/html"));
		// widget config doc must be 'config.xml' at the root of the widget package, 
		// widget description must be null, 
		assertEquals("", widget.getChild("description").getText());
		// widget height must be null, 
		assertEquals("", getWidgetHeight(widget));
		// widget id must be null, 
		/// We have to use generated ids - no nulls allowed
		assertTrue(widget.getAttributeValue("id").contains("generated"));
		// widget license must be null, 
		// widget license file must be null, 
		// widget license href must be null, 
		assertEquals(null, widget.getChild("license"));
		// widget name must be null, 
		/// We put a default of "unknown" when exporting this when the value is "null"
		assertEquals("", getWidgetname(widget));
		// widget preferences must be null, 
		assertEquals(null, widget.getChild("license"));
		// widget short name must be null, 
		/// We put a default of "unknown" when exporting this when the value is "null"
		assertEquals(null, getWidgetShortName(widget));
		// widget version must be null, 
		assertEquals("", widget.getAttributeValue("version"));
		// widget width must be null, 
		assertEquals("", getWidgetHeight(widget));
		// widget window modes must a list with one entry whose value is 'floating', 
		// ???
		// widget start file must be index.htm at the root of the widget, 
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
		// user agent locales must contain at least one item whose value is 'en' 
		// ???
	}
		 */

	// 7 config.xml
	@Test
	public void dq(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-ZjcdAxFMSx/000/dq.wgt");
		assertFalse(err == null||err.equals(""));
	}
	@Test
	public void dw(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-ZjcdAxFMSx/001/dw.wgt");
		assertFalse(err == null||err.equals(""));
	}

	// 8 xml
	@Test
	public void bt(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-klLDaEgJeU/000/bt.wgt");
		assertFalse(err == null||err.equals(""));
	}
	@Test
	public void bu(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-klLDaEgJeU/001/bu.wgt");
		assertFalse(err == null||err.equals(""));
	}

	@Ignore // This test passes in the parser; we don't need to test it here as its not a valid URL
	@Test 
	public void bv(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-klLDaEgJeU/002/bv.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass&.html",start);
	}
	@Test
	public void bw(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-klLDaEgJeU/003/bw.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
	}
	@Test
	public void lt(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-klLDaEgJeU/004/lt.wgt");
		assertTrue(err.contains("Config.xml is not well-formed XML"));
	}
	@Test
	public void amp(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-klLDaEgJeU/005/amp.wgt");
		assertTrue(err.contains("Config.xml is not well-formed XML"));
	}
	
	
	// 9 widget
	@Test
	public void aa(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-ACCJfDGwDQ/000/aa.wgt");
		assertTrue(err.contains("bad namespace"));
	}
	@Test
	public void ab(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-ACCJfDGwDQ/001/ab.wgt");
		assertTrue(err.contains("bad namespace"));
	}
	@Test
	public void ac(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-ACCJfDGwDQ/002/ac.wgt");
		assertTrue(err.contains("bad namespace"));
	}

	// 10 id
	@Test
	public void b1(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RawAIWHoMs/000/b1.wgt");
		assertEquals("pass:", widget.getAttributeValue("id"));
	}
	@Test
	public void rd(){
		// We can't allow null ids, we use generated IDs where they aren't valid
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RawAIWHoMs/001/rd.wgt");
		assertTrue(widget.getAttributeValue("id").contains("generated"));
	}
	
	@Ignore // Note that this test will pass on its own, but not if run in a series including b1, as it will update b1 rather than create a new widget");
	@Test
	public void b2(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RawAIWHoMs/002/b2.wgt");
		assertEquals("pass:", widget.getAttributeValue("id")); 
	}

	// 11 Version

	@Test
	public void cf(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VerEfVGeTc/000/cf.wgt");
		assertEquals("PASS", widget.getAttributeValue("version"));
	}
	@Test
	public void cg(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VerEfVGeTc/001/cg.wgt");
		assertEquals("", widget.getAttributeValue("version"));
	}
	@Test
	public void ch(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VerEfVGeTc/002/ch.wgt");
		assertEquals("PASS", widget.getAttributeValue("version"));
	}


	// 12  Height

	@Test
	public void ax(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/000/ax.wgt");
		assertEquals("123", getWidgetHeight(widget));
	}
	@Test
	public void ay(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/001/ay.wgt");
		assertEquals("0", getWidgetHeight(widget));
	}
	@Test
	public void az(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/002/az.wgt");
		assertEquals("100", getWidgetHeight(widget));
	}
	@Test
	public void a1(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/003/a1.wgt");
		assertEquals("123", getWidgetHeight(widget));
	}
	@Test
	public void a2(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/004/a2.wgt");
		assertEquals("", getWidgetHeight(widget));
	}
	@Test
	public void a3(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/005/a3.wgt");
		assertEquals("", getWidgetHeight(widget));
	}
	@Test
	public void a4(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/006/a4.wgt");
		assertEquals("0", getWidgetHeight(widget));
	}	
	// 13 Width

	@Test
	public void c9(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/000/c9.wgt");
		assertEquals("0", getWidgetWidth(widget));
	}
	@Test
	public void cq(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/001/cq.wgt");
		assertEquals("123", getWidgetWidth(widget));
	}
	@Test
	public void cw(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/002/cw.wgt");
		assertEquals("200", getWidgetWidth(widget));
	}
	@Test
	public void ce(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/003/ce.wgt");
		assertEquals("123", getWidgetWidth(widget));
	}
	@Test
	public void cr(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/004/cr.wgt");
		assertEquals("", getWidgetWidth(widget));
	}
	@Test
	public void ct(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/005/ct.wgt");
		assertEquals("", getWidgetWidth(widget));
	}
	@Test
	public void cy(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/006/cy.wgt");
		assertEquals("0", getWidgetWidth(widget));
	}


	// 14 empty widget config

	@Test
	public void d3(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-MFcsScFEaC/000/d3.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}

	// 15 name

	@Test
	public void bx(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LYLMhryBBT/000/bx.wgt");
		assertEquals("PASS", getWidgetname(widget));
	}

	@Test
	public void by(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LYLMhryBBT/001/by.wgt");
		assertEquals("", getWidgetname(widget));
	}

	@Test
	public void bz(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LYLMhryBBT/002/bz.wgt");
		assertEquals("PASS", getWidgetname(widget));
	}

	// 16 name

	@Test
	public void ao(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/000/ao.wgt");
		assertEquals("PASS", getWidgetname(widget));
	}

	@Test
	public void ap(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/001/ap.wgt");
		assertEquals("P A S S", getWidgetname(widget));
	}

	@Test
	public void aq(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/002/aq.wgt");
		assertEquals("PASS", getWidgetname(widget));
	}

	@Test
	public void ar(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/003/ar.wgt");
		assertEquals("PASS", getWidgetShortName(widget));
	}

	@Test
	public void as(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/004/as.wgt");
		assertEquals("PASS", getWidgetname(widget));
		assertEquals("PASS", getWidgetShortName(widget));
	}

	@Test
	public void at(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/005/at.wgt");
		assertEquals("PASS", getWidgetname(widget));
		assertEquals("PASS", getWidgetShortName(widget));
	}

	@Test
	public void au(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/006/au.wgt");
		assertEquals("", getWidgetShortName(widget));
	}

	@Test
	public void av(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/007/av.wgt");
		assertEquals("", getWidgetname(widget));
	}

	@Test
	public void oa(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/008/oa.wgt");
		assertEquals("PASS", getWidgetname(widget));
	}

	// 17 Description

	@Test
	public void c6(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UEMbyHERkI/000/c6.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}
	@Test
	public void c7(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UEMbyHERkI/001/c7.wgt");
		assertEquals("",  widget.getChild("description").getText());
	}
	@Test
	public void rb(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UEMbyHERkI/002/rb.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}
	@Test
	public void c8(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UEMbyHERkI/003/c8.wgt");
		assertEquals("PASS", widget.getChild("description").getText());
	}

	// 18 Description
	@Test
	public void cp(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/000/cp.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}
	@Test
	public void ca(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/001/ca.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}
	@Test
	public void cs(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/002/cs.wgt");
		assertEquals("",  widget.getChild("description").getText());
	}
	@Test
	public void cd(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/003/cd.wgt");
		assertEquals("P A S S",  widget.getChild("description").getText());
	}
	@Test
	public void x1(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/004/x1.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}
	@Test
	public void x2(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/005/x2.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}

	// 19 License
	@Test
	public void cu(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-vcYJAPVEym/000/cu.wgt");
		assertEquals("PASS",  widget.getChild("license").getText());	
		assertEquals("PASS:",  widget.getChild("license").getAttributeValue("href"));
	}
	@Test
	public void ci(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-vcYJAPVEym/001/ci.wgt");
		assertEquals("",  widget.getChild("license").getText());	
		assertEquals(null, widget.getChild("license").getAttribute("href"));
	}
	@Test
	public void ra(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-vcYJAPVEym/002/ra.wgt");
		assertEquals("PASS",  widget.getChild("license").getText());	
	}
	@Test
	public void co(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-vcYJAPVEym/003/co.wgt");
		assertEquals("PASS",  widget.getChild("license").getText());			
	}
	
	
	// 20 License
	@Test
	public void cj(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-YUMJAPVEgI/000/cj.wgt");
		assertEquals("PASS",  widget.getChild("license").getText());	
	}
	@Test
	public void ck(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-YUMJAPVEgI/001/ck.wgt");
		assertEquals("PASS",  widget.getChild("license").getText());			
	}
	@Test
	public void cl(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-YUMJAPVEgI/002/cl.wgt");
		assertEquals("",  widget.getChild("license").getText());	
	}
	@Test
	public void cz(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-YUMJAPVEgI/003/cz.wgt");
		assertEquals("P A S S",  widget.getChild("license").getText());	
		
	}
	@Test
	public void cx(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-YUMJAPVEgI/004/cx.wgt");
		assertEquals("",  widget.getChild("license").getText());	
		assertEquals("test/pass.html", widget.getChild("license").getAttributeValue("href"));
	}

	// 21 Icon
	@Test
	public void d1(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iipTwNshRg/000/d1.wgt");
		assertEquals("icon.png",getIcon(widget));
	}

	@Test
	public void ga(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iipTwNshRg/001/ga.wgt");
		assertEquals("icon.png",getIcon(widget));
	}
	
	// 22 Icon
	@Test
	public void d2(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-roCaKRxZhS/000/d2.wgt");
		assertEquals("icon.png",getIcon(widget));
	}
	
	//23
	@Test
	public void zz(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iuJHnskSHq/000/zz.wgt");
		assertEquals(null,getIcon(widget));
	}
	@Test
	public void za(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iuJHnskSHq/001/za.wgt");
		assertEquals("pass.png",getIcon(widget));
	}
	
	@Test 
	@Ignore // No longer in test suite
	public void zb(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iuJHnskSHq/002/zb.wgt");
		assertEquals("locales/en/icon.png",getIcon(widget));
		assertEquals(null, widget.getChild("icon").getAttribute("width"));
		assertEquals(null, widget.getChild("icon").getAttribute("height"));
	}
	@Test
	public void zc(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iuJHnskSHq/003/zc.wgt");
		assertEquals("locales/en/custom.png",getIcon(widget));
		assertEquals(null, widget.getChild("icon").getAttribute("width"));
		assertEquals(null, widget.getChild("icon").getAttribute("height"));
	}
	
	//24
	@Test
	public void ix(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/000/ix.wgt");
		assertEquals("123", widget.getChild("icon").getAttributeValue("height"));
	}
	@Test
	public void iy(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/001/iy.wgt");
		assertEquals("0", widget.getChild("icon").getAttributeValue("height"));
	}
	@Test
	public void iz(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/002/iz.wgt");
		assertEquals("100", widget.getChild("icon").getAttributeValue("height"));
	}
	@Test
	public void i1(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/003/i1.wgt");
		assertEquals("123", widget.getChild("icon").getAttributeValue("height"));
	}
	@Test
	public void i2(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/004/i2.wgt");
		assertEquals(null, widget.getChild("icon").getAttribute("height"));
	}
	@Test
	public void i3(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/005/i3.wgt");
		assertEquals(null, widget.getChild("icon").getAttribute("height"));
	}
	@Test
	public void i4(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/006/i4.wgt");
		assertEquals("0", widget.getChild("icon").getAttributeValue("height"));	}
	
	//25
	@Test
	public void iq(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/000/iq.wgt");
		assertEquals("123", widget.getChild("icon").getAttributeValue("width"));	
	}
	@Test
	public void i9(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/001/i9.wgt");
		assertEquals("0", widget.getChild("icon").getAttributeValue("width"));	
	}
	@Test
	public void iw(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/002/iw.wgt");
		assertEquals("100", widget.getChild("icon").getAttributeValue("width"));	
	}
	@Test
	public void ie(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/003/ie.wgt");
		assertEquals("123", widget.getChild("icon").getAttributeValue("width"));	
	}
	@Test
	public void ir(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/004/ir.wgt");
		assertEquals(null, widget.getChild("icon").getAttribute("width"));	
	}
	@Test
	public void it(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/005/it.wgt");
		assertEquals(null, widget.getChild("icon").getAttribute("width"));	
	}
	@Test
	public void ib(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/006/ib.wgt");
		assertEquals("0", widget.getChild("icon").getAttributeValue("width"));	
	}
	
	// 26 Author
	@Test
	public void b7(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-sdwhMozwIc/000/b7.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
	}
	@Test
	public void b8(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-sdwhMozwIc/001/b8.wgt");
		assertEquals("", widget.getChild("author").getText());

	}
	@Test
	public void b9(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-sdwhMozwIc/002/b9.wgt");
		assertEquals("PASS", widget.getChild("author").getText());

	}

	// 27 Author  
	@Test
	public void af(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/000/af.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
	}
	@Test
	public void ag(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/001/ag.wgt");
		assertEquals("P A S S", widget.getChild("author").getText());
	}
	@Test
	public void ah(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/002/ah.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
	}
	@Test
	public void ai(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/003/ai.wgt");
		assertEquals("PASS", widget.getChild("author").getAttributeValue("email"));
	}
	@Test
	public void aj(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/004/aj.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
	}
	@Test
	public void ak(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/005/ak.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
	}
	@Test
	public void al(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/006/al.wgt");
		assertEquals("", widget.getChild("author").getText());
	}
	@Test
	public void am(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/007/am.wgt");
		assertEquals("PASS:PASS", widget.getChild("author").getAttributeValue("href"));	}
	@Test
	public void an(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/008/an.wgt");
		assertEquals(null, widget.getChild("author").getAttribute("href"));	}

	// 28 Preference
	@Test
	public void a5(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/000/a5.wgt");
		assertTrue(widget.getChildren("preference") == null || widget.getChildren("preference").size() == 0);
	}

	@Test
	public void a6(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/001/a6.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); 
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}		
	@Test
	public void a7(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/002/a7.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); 
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}			
	@Test
	public void a8(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/003/a8.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); 
		assertEquals("true", widget.getChild("preference").getAttributeValue("readonly"));
	}	
	@Test
	public void a9(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/004/a9.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); 
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}	
	@Test
	public void ba(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/005/ba.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("a", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("a", widget.getChild("preference").getAttributeValue("value")); 
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}
	@SuppressWarnings("unchecked")
	@Test
	public void bb(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/006/bb.wgt");
		List<Element> preferences = widget.getChildren("preference");
		assertTrue(preferences.size() == 2);
		Element pref1 = preferences.get(0);
		Element pref2 = preferences.get(1);
		
		assertEquals("a", pref1.getAttributeValue("name"));
		assertEquals("a", pref1.getAttributeValue("value")); 
		assertEquals("false", pref1.getAttributeValue("readonly"));
		assertEquals("A", pref2.getAttributeValue("name"));
		assertEquals("b", pref2.getAttributeValue("value")); 
		assertEquals("false", pref2.getAttributeValue("readonly"));
	}
	@SuppressWarnings("unchecked")
	@Test
	public void bc(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/007/bc.wgt");
		List<Element> preferences = widget.getChildren("preference");
		assertTrue(preferences.size() == 1);
		Element pref1 = preferences.get(0);
		
		assertEquals("PASS", pref1.getAttributeValue("name"));
		assertEquals("PASS", pref1.getAttributeValue("value")); 
		assertEquals("false", pref1.getAttributeValue("readonly"));
	}
	@Test
	@Ignore
	public void bd(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/008/bd.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}
	@Test
	@Ignore
	public void be(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/009/be.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}
	@Test
	@Ignore
	public void bf(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/010/bf.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}

	// 29
	@Test
	public void bq(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-hkWmGJgfve/000/bq.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
	}
	@Test
	public void br(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-hkWmGJgfve/001/br.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void bs(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-hkWmGJgfve/002/bs.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
	}

	//30
	@Test
	public void d7(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LTUJGJFCOU/000/d7.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void d8(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LTUJGJFCOU/001/d8.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void gb(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LTUJGJFCOU/002/gb.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	
	//31
	@Test
	public void db(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-pIffQywZin/000/db.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	//32
	@Test
	public void d9(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LQcjNKBLUZ/000/d9.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void d0(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LQcjNKBLUZ/001/d0.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}

	// 33 Start File Text Encoding
	@Test
	public void e4(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-dPOgiLQKNK/000/e4.wgt");
		assertEquals("UTF-8",getStartFileEncoding(widget));
	}
	@Test
	// We don't support non-UTF-8 encodings, so this requires a manual test
	public void e5(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-dPOgiLQKNK/001/e5.wgt");
		//assertEquals("ISO-8859-1",getStartFileEncoding(widget));
		System.out.println("Manual test: charset for start file of "+widget.getAttributeValue("id")+" must be ISO-8859-1");
	}
	@Test
	// We don't support non-UTF-8 encodings, so this requires a manual test
	public void e6(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-dPOgiLQKNK/002/e6.wgt");
		//assertEquals("ISO-8859-1",getStartFileEncoding(widget));
		System.out.println("Manual test: charset for start file of "+widget.getAttributeValue("id")+" must be ISO-8859-1");
	}
	@Test
	public void e7(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-dPOgiLQKNK/003/e7.wgt");
		assertEquals("UTF-8",getStartFileEncoding(widget));
	}
	
	//34
	@Ignore 
	// This test passes in the parser test suite. We can't test it here as "index.php" is not a valid file for us to serve
	// In future we may want to reparse a widget like this and change to .html, but its never come up
	@Test
	public void dc(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-paIabGIIMC/000/dc.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.php",start);
		assertEquals("text/html",getStartFileContentType(widget));
	}
	@Test
	public void dv(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-paIabGIIMC/001/dv.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	
	//35
	@Test
	// We don't support non-UTF-8 encodings, so this requires a manual test
	public void z1(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-aaaaaaaaaa/000/z1.wgt");
		System.out.println("Manual test: charset for start file of "+widget.getAttributeValue("id")+" must be ISO-8859-1");
		
	}
	
	@Test
	// We don't support non-UTF-8 encodings, so this requires a manual test
	public void z2(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-aaaaaaaaaa/001/z2.wgt");
		System.out.println("Manual test: charset for start file of "+widget.getAttributeValue("id")+" must be Windows-1252");
		
	}
	
	//36
	@Test
	public void e1(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-KNiLPOKdgQ/000/e1.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":the feature feature:a9bb79c1 must not have any params associated with it.");
	}
	//37
	@Test
	public void df(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-rZdcMBExBX/000/df.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":To pass, the feature list must remain empty. ");

	}
	@Test
	@Ignore
	public void dr(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-rZdcMBExBX/001/dr.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":the feature list must contain one feature named 'feature:a9bb79c1' whose required value is false.");
	}
	
	@Test
	public void ha(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-rZdcMBExBX/002/ha.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":To pass, the feature list must contain two features. Both are named 'feature:a9bb79c1'. One feature must have a parameter named \"test\" whose value is \"pass1\" The other feature must have a parameter named \"test\" whose value is \"pass2\" (the order in which the features appear in the feature list in not relevant).");
	}

	//38
	@Test
	public void d4(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-paWbGHyVrG/000/d4.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	
	// ta-ignore-unrequired-feature-with-invalid-name
	@Test
	public void gg(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-ignore-unrequired-feature-with-invalid-name/000/gg.wgt");		
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":the user agent must not contain any values in the feature list (i.e., the unknown feature is skipped).");
	}
	
	//39
	@Test
	public void e8(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-vOBaOcWfll/000/e8.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	//40
	@Test
	public void d5(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-luyKMFABLX/000/d5.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":the user agent must not contain any values in the feature list (i.e., the unknown feature is skipped).");
	}
	//41
	@Test
	public void dt(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-EGkPfzCBOz/000/dt.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":the feature list must contain one feature named 'feature:a9bb79c1' with no associated parameters. ");
	}
	@Test
	public void dg(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-EGkPfzCBOz/001/dg.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":the feature list must contain one feature named 'feature:a9bb79c1' with one associated parameter whose name is 'PASS' and whose value is 'PASS'. ");
	}	
	@Test
	public void v9(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-EGkPfzCBOz/002/v9.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":the feature list must contain one feature named 'feature:a9bb79c1' with two associated parameters whose name is 'PASS' and whose value are 'value1' and 'value2'.");
	}
	//42
	@Test
	public void d6(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-xlgUWUVzCY/000/d6.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":feature 'feature:a9bb79c1' must not have any associated parameters.");
	}

	// 43 Feature
	@Test
	public void e2(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-CEGwkNQcWo/000/e2.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":the feature feature:a9bb79c1 must not have any associated params. ");
	}
	@Test
	public void e3(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-CEGwkNQcWo/001/e3.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("id")+":the feature feature:a9bb79c1 must not have any associated params. ");
	}
	
	//44
	@Test
	public void xx(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-bbbbbbbbbb/000/xx.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
	}
	
	//45 Start file and icons
	@Test
	public void aw(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BnWPqNvNVo/000/aw.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
		assertEquals("icon.png",getIcon(widget));
	}

	//46  Start file
	@Test
	public void cc(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/008/cc.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void cv(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/009/cv.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.html",start);
	}
	@Test
	public void b3(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/000/b3.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
		assertTrue(getStartFileContentType(widget).startsWith("text/html"));
	}
	@Test
	public void b4(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/001/b4.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.html",start);
		assertTrue(getStartFileContentType(widget).startsWith("text/html"));
	}
	@Test
	public void b0(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/002/b0.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void c1(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/003/c1.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void c2(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/004/c2.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void c3(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/005/c3.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void c4(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/006/c4.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.html",start);
	}
	@Test
	public void c5(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/007/c5.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.html",start);
	}
	// 47 Icons
	@Test
	public void bj(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/000/bj.wgt");
		assertEquals("icon.png",getIcon(widget));	
	}
	@Test
	public void bk(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/001/bk.wgt");
		assertEquals("locales/en/icon.png",getIcon(widget));	
	}

	@Test
	public void bl(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/002/bl.wgt");
		List icons = widget.getChildren("icon");
		assertTrue(icons.size() == 2);
		String icon1 = getLocalIconPath(widget,((Element)icons.get(0)));
		String icon2 = getLocalIconPath(widget,((Element)icons.get(1))); 
		assertTrue((icon1.equals("locales/en/icon.jpg") && (icon2.equals("icon.png"))) || (icon2.equals("locales/en/icon.jpg") && (icon1.equals("icon.png"))));
	}

	@Test
	public void bm(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/003/bm.wgt");
		List icons = widget.getChildren("icon");
		assertTrue(icons.size() == 2);
		String icon1 = getLocalIconPath(widget,((Element)icons.get(0)));
		String icon2 = getLocalIconPath(widget,((Element)icons.get(1))); 
		assertTrue((icon1.equals("locales/en/icon.jpg") && (icon2.equals("icon.png"))) || (icon2.equals("locales/en/icon.jpg") && (icon1.equals("icon.png"))));

	}

	@Test
	@Ignore
	public void bn(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/004/bn.wgt");
		List icons = widget.getChildren("icon");
		assertTrue(icons.size() == 2);
		String icon1 = getLocalIconPath(widget,((Element)icons.get(0)));
		String icon2 = getLocalIconPath(widget,((Element)icons.get(1)));
		assertTrue((icon1.equals("locales/en/icon.jpg") && (icon2.equals("icons/pass.png"))) || (icon2.equals("locales/en/icon.jpg") && (icon1.equals("icons/pass.png"))));

	}

	@Test
	public void bo(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/005/bo.wgt");
		List icons = widget.getChildren("icon");
		assertTrue(icons.size() == 2);
		String icon1 = getLocalIconPath(widget,((Element)icons.get(0)));
		String icon2 = getLocalIconPath(widget,((Element)icons.get(1)));
		assertTrue((icon1.equals("icon.png") && (icon2.equals("icon.jpg"))) || (icon1.equals("icon.jpg") && (icon2.equals("icon.png"))));

	}

	@Test
	public void bp(){
		// Note the original test case is in error here
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/006/bp.wgt");
		List icons = widget.getChildren("icon");
		assertTrue(icons.size() == 2);
		String icon1 = getLocalIconPath(widget,((Element)icons.get(0)));
		String icon2 = getLocalIconPath(widget,((Element)icons.get(1))); 
		assertTrue((icon1.equals("locales/en/icon.png") && (icon2.equals("icon.png"))) || (icon2.equals("locales/en/icon.png") && (icon1.equals("icon.png"))));
	}
	@Test
	public void ad(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/007/ad.wgt");
		assertEquals("icon.png",getIcon(widget));	}

	@Test
	public void ae(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/008/ae.wgt");
		assertEquals("locales/en/icon.png",getIcon(widget));
	}
	
	/**
	 * This test case has been deprecated by W3C

	@Test
	//"Test the UA's ability to correctly find config document. To pass, the
	//user agent must correctly load "pass.html" from "config.xml" and
	//treat "CONFIG.xml" as an arbitrary file.
	public void hh(){
		Element widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-ZjcdAxFMSx/002/hh.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
	}
	*/

	// Utility methods
	protected Element processWidgetNoErrors(String widgetfname){
		try {
		  String id = null;
		  //
		  // Upload the widget, and process the response to get the widget uri
		  //
		  SAXBuilder builder = new SAXBuilder();
		  String result = WidgetUploader.uploadWidget(widgetfname);
		  System.out.println(result);
		  id = storeImportedPackageId(result);
		  // id should not be null as they are generated if not found during the initial parse    
		  assertNotNull(id);
		  //
		  // Get and return the widget xml. We don't return the original response
		  // as that is the raw config.xml data - we have to make another call to
		  // get the corrected and normalized metadata to check we have correctly
		  // processed the .wgt package according to the W3C spec.
		  //
		  return builder.build(TEST_WIDGETS_SERVICE_URL_VALID+"/"+id).getRootElement().getChild("widget");
		} catch (Exception e) {
			fail("couldn't upload widget:"+e.getMessage());
		}		
		fail("widget not found after upload");
		return null;
	}

	private String getStartFileEncoding(Element widget){
		String response = instantiateWidget(widget);
		String startFile = getStartFile(response);
		// Download and check text encoding
		try {
			HttpClient client = new HttpClient();
			GetMethod get = new GetMethod(startFile);
			client.executeMethod(get);
			int code = get.getStatusCode();
			assertEquals(200,code);
			String charset = get.getRequestCharSet();

	    //
	    // Check for charset overrides in the HTML start page
	    //
      HtmlCleaner cleaner = new HtmlCleaner();
 
      TagNode httpEquivNode = cleaner.clean(get.getResponseBodyAsStream()).findElementByAttValue("http-equiv", "content-type", true, false);
      if (httpEquivNode != null && httpEquivNode.hasAttribute("content")){
        String value = httpEquivNode.getAttributeByName("content");
        int offset = value.indexOf("charset=");
        if (offset >= -1){
            charset = value.substring(offset+8).toUpperCase();
        }
      }
      
			get.releaseConnection();
			return charset;
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("failed to get start file");
		}
		return null;	
	}
	
	private String getStartFileContentType(Element widget){
		String response = instantiateWidget(widget);
		String startFile = getStartFile(response);
		// Download and check content-type
		try {
			HttpClient client = new HttpClient();
			GetMethod get = new GetMethod(startFile);
			client.executeMethod(get);
			int code = get.getStatusCode();
			assertEquals(200,code);
			get.releaseConnection();
			return get.getResponseHeader("CONTENT-TYPE").getValue();
		}
		catch (Exception e) {
			//e.printStackTrace();
			fail("failed to get start file");
		}
		return null;
	}

	private String locateStartFile(Element widget){

		String response = instantiateWidget(widget);
		String start = getStartFile(response);

		// split off the end file name
		// http://localhost:8080/wookie/deploy/ + uid + / + name + ?idkey=...
		URL url;
		try {
			url = new URL(start);
		} catch (MalformedURLException e) {
				System.out.println("start file URL was invalid");
				return null;
		}
		String[] parts = url.getPath().split("/");
		start = parts[parts.length-1];
		return start;
	}

	private String processWidgetWithErrors(String widgetfname){
		try {
		    String result = WidgetUploader.uploadWidget(widgetfname);
		    storeImportedPackageId(result);
		    return result;
		} catch (Exception e) {
			fail("couldn't upload widget");
		}		
		fail("widget not found after upload");
		return null;		
	}

	private String getIcon(Element widget){
		Element iconElem = widget.getChild("icon");
		return getLocalIconPath(widget,iconElem);
	}
	
	private String getLocalIconPath(Element widget, Element iconElem){
		String id = widget.getAttributeValue("id");
		id = WidgetPackageUtils.convertIdToFolderName(id);
		String baseUrl = "http://localhost:8080/wookie/deploy/"+id+"/";	
		if(iconElem == null) return null;
		String iconUrl = iconElem.getAttributeValue("src");
		String icon = StringUtils.difference(baseUrl,iconUrl);
		return icon;
	}

	private String getWidgetShortName(Element widget){
		return widget.getChild("name").getAttributeValue("short");
	}

	private String getWidgetname(Element widget){
		return widget.getChild("name").getText();
	}

	private String getWidgetWidth(Element widget){
		return widget.getAttributeValue("width");
	}
	private String getWidgetHeight(Element widget){
		return widget.getAttributeValue("height");
	}

}
