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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.wookie.w3c.IContentEntity;
import org.apache.wookie.w3c.IDescriptionEntity;
import org.apache.wookie.w3c.IFeatureEntity;
import org.apache.wookie.w3c.IIconEntity;
import org.apache.wookie.w3c.ILicenseEntity;
import org.apache.wookie.w3c.INameEntity;
import org.apache.wookie.w3c.IParamEntity;
import org.apache.wookie.w3c.IPreferenceEntity;
import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.BadWidgetZipFileException;
import org.apache.wookie.w3c.exceptions.InvalidContentTypeException;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Conformance testing for the Packaging and Configuration spec
 * These are functional tests and need to run against a running Wookie server on localhost:8080
 * You need to be online to run these tests as they download test widgets hosted externally.
 * For more information see http://dev.w3.org/2006/waf/widgets/test-suite/index.html
 * 
 * Note that in order to run the Feature tests, a ServerFeature named "feature:a9bb79c1" must be set up in the DB
 * 
 * @author scott
 *
 */
public class PackagingAndConfiguration extends ConformanceTest{
	
	// 1 files
	@Test
	public void b5(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RRZxvvTFHx/000/b5.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void b6(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RRZxvvTFHx/001/b6.wgt");
		assertEquals("index.html",getSrc(widget));
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
	
	// ta-GVVIvsdEUo
	
	@Test
	public void z3(){
		try {
			downloadWidget("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-GVVIvsdEUo/000/z3",false);
		} catch (Exception e) {
			fail();
		}
	}
	@Test
	public void z4(){
		try {
			downloadWidget("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-GVVIvsdEUo/001/z4.html",false);
		} catch (Exception e) {
			fail();
		}
	}
	@Test(expected=InvalidContentTypeException.class)
	public void z5() throws InvalidContentTypeException, BadWidgetZipFileException, BadManifestException, Exception{
		downloadWidget("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-GVVIvsdEUo/002/z5.wgt",false);
	}

	// ta-qxLSCRCHlN
	@Test
	public void dk(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-qxLSCRCHlN/000/dk.wgt");
		assertFalse(err == null||err.equals(""));
	}	

	// ta-FDGQBROtzW
	@Test
	public void dn(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FDGQBROtzW/000/dn.test");
		assertEquals("index.htm",getSrc(widget));
	}
	@Test
	public void dm(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FDGQBROtzW/001/dm");
		assertEquals("index.htm",getSrc(widget));
	}


	// ta-uLHyIMvLwz
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
	
/** This test has been deprecated by W3C

	// 6 defaults
	@Test 
	public void ds(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-xGYcVTMoAX/000/ds.wgt");
		// To pass, author email must be null, 
		// author href must be null, 
		// author name must be null, 
		assertEquals(null, widget.getAuthor());
		// feature list must be empty (null)
		assertEquals(0, widget.getFeatures().size());
		// icons must be null, 
		assertEquals(0, widget.getIconsList().size());
		// start file encoding must be UTF-8, 
		assertEquals("UTF-8", getStartFileEncoding(widget));
		// start file content-type must be text/html, 
		assertTrue(getDefaultStartFile(widget).getType().startsWith("text/html"));
		// widget config doc must be 'config.xml' at the root of the widget package, 
		// widget description must be null, 
		assertEquals(0, widget.getDescriptions().size());
		// widget height must be null, 
		assertEquals(null, widget.getHeight());
		// widget id must be null, 
		/// We have to use generated ids - no nulls allowed
		assertTrue(widget.getIdentifier().startsWith("generated-uid"));
		// widget license must be null, 
		// widget license file must be null, 
		// widget license href must be null, 
		assertEquals(0, widget.getLicensesList().size());
		// widget name must be null, 
		/// We put a default of "unknown" when exporting this when the value is "null"
		assertEquals(0, widget.getNames().size());
		// widget preferences must be null, 
		assertEquals(0, widget.getPrefences().size());
		// widget short name must be null, 
		/// We put a default of "unknown" when exporting this when the value is "null"
		assertNull(getShortName(widget));
		// widget version must be null, 
		assertEquals("", widget.getVersion());
		// widget width must be null, 
		assertEquals(null, widget.getHeight());
		// widget window modes must a list with one entry whose value is 'floating', 
		assertEquals("floating", widget.getViewModes());
		// widget start file must be index.htm at the root of the widget, 
		assertEquals("index.htm",getSrc(widget));
		// user agent locales must contain at least one item whose value is 'en' 
	}
*/
	
	
	// ta-ZjcdAxFMSx
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

	// ta-klLDaEgJeU
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

	@Test 
	public void bv(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-klLDaEgJeU/002/bv.wgt");
		assertEquals("pass&.html",getSrc(widget));
	}
	@Test
	public void bw(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-klLDaEgJeU/003/bw.wgt");
		assertEquals("PASS", widget.getAuthor().getAuthorName());
	}
	
	@Test
	public void lt(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-klLDaEgJeU/004/lt.wgt");
		assertFalse(err == null||err.equals(""));
	}
	@Test
	public void amp(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-klLDaEgJeU/005/amp.wgt");
		assertFalse(err == null||err.equals(""));
	}
	
	// ta-ACCJfDGwDQ
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

	// ta-RawAIWHoMs
	@Test
	public void b1(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RawAIWHoMs/000/b1.wgt");
		assertEquals("pass:", widget.getIdentifier());
	}
	@Test
	public void rd(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RawAIWHoMs/001/rd.wgt");
		assertTrue(widget.getIdentifier().startsWith("generated-uid"));
	}
	@Test
	public void b2(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RawAIWHoMs/002/b2.wgt");
		assertEquals("pass:", widget.getIdentifier());
	}

	// ta-VerEfVGeTc

	@Test
	public void cf(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VerEfVGeTc/000/cf.wgt");
		assertEquals("PASS", widget.getVersion());
	}
	@Test
	public void cg(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VerEfVGeTc/001/cg.wgt");
		assertEquals("", widget.getVersion());
	}
	@Test
	public void ch(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VerEfVGeTc/002/ch.wgt");
		assertEquals("PASS", widget.getVersion());
	}


	// ta-BxjoiWHaMr

	@Test
	public void ax(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/000/ax.wgt");
		assertEquals(123, widget.getHeight().intValue());
	}
	@Test
	public void ay(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/001/ay.wgt");
		assertEquals(0, widget.getHeight().intValue());
	}
	@Test
	public void az(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/002/az.wgt");
		assertEquals(100, widget.getHeight().intValue());
	}
	@Test
	public void a1(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/003/a1.wgt");
		assertEquals(123, widget.getHeight().intValue());
	}
	@Test
	public void a2(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/004/a2.wgt");
		assertNull(widget.getHeight());
	}
	@Test
	public void a3(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/005/a3.wgt");
		assertNull(widget.getHeight());
	}
	@Test
	public void a4(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BxjoiWHaMr/006/a4.wgt");
		assertEquals(0, widget.getHeight().intValue());
	}	
	// ta-UScJfQHPPy

	@Test
	public void c9(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/000/c9.wgt");
		assertEquals(0, widget.getWidth().intValue());
	}
	@Test
	public void cq(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/001/cq.wgt");
		assertEquals(123, widget.getWidth().intValue());
	}
	@Test
	public void cw(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/002/cw.wgt");
		assertEquals(200, widget.getWidth().intValue());
	}
	@Test
	public void ce(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/003/ce.wgt");
		assertEquals(123, widget.getWidth().intValue());
	}
	@Test
	public void cr(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/004/cr.wgt");
		assertNull(widget.getWidth());
	}
	@Test
	public void ct(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/005/ct.wgt");
		assertNull(widget.getWidth());
	}
	@Test
	public void cy(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UScJfQHPPy/006/cy.wgt");
		assertEquals(0, widget.getWidth().intValue());
	}

	// ta-MFcsScFEaC

	@Test
	public void d3(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-MFcsScFEaC/000/d3.wgt");
		assertEquals("index.htm",getSrc(widget));
	}

	// ta-LYLMhryBBT

	@Test
	public void bx(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LYLMhryBBT/000/bx.wgt");
		assertEquals("PASS", getName(widget));
	}

	@Test
	public void by(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LYLMhryBBT/001/by.wgt");
		assertEquals("", getName(widget));
	}

	@Test
	public void bz(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LYLMhryBBT/002/bz.wgt");
		assertEquals("PASS", getName(widget));
	}
 
	// ta-AYLMhryBnD

	@Test
	public void ao(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/000/ao.wgt");
		assertEquals("PASS", getName(widget));
	}

	@Test
	public void ap(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/001/ap.wgt");
		assertEquals("P A S S", getName(widget));
	}
	

	@Test
	public void aq(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/002/aq.wgt");
		assertEquals("PASS", getName(widget));
	}

	@Test
	public void ar(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/003/ar.wgt");
		assertEquals("PASS", getShortName(widget));
	}

	@Test
	public void as(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/004/as.wgt");
		assertEquals("PASS", getName(widget));
		assertEquals("PASS", getShortName(widget));
	}

	@Test
	public void at(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/005/at.wgt");
		assertEquals("PASS", getName(widget));
		assertEquals("PASS", getShortName(widget));
	}

	@Test
	public void au(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/006/au.wgt");
		assertEquals("", getShortName(widget));
	}

	@Test
	public void av(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/007/av.wgt");
		assertEquals("", getName(widget));
	}

	@Test
	public void oa(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-AYLMhryBnD/008/oa.wgt");
		assertEquals("PASS", getName(widget));
	}

	// ta-UEMbyHERkI

	@Test
	public void c6(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UEMbyHERkI/000/c6.wgt");
		assertEquals("PASS",  getDescription(widget));
	}
	@Test
	public void c7(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UEMbyHERkI/001/c7.wgt");
		assertEquals("",  getDescription(widget));
	}
	@Test
	public void rb(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UEMbyHERkI/002/rb.wgt");
		assertEquals("PASS",  getDescription(widget));
	}
	@Test
	public void c8(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-UEMbyHERkI/003/c8.wgt");
		assertEquals("PASS", getDescription(widget));
	}

	// ta-VdCEyDVSA
	@Test
	public void cp(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/000/cp.wgt");
		assertEquals("PASS",  getDescription(widget));
	}
	@Test
	public void ca(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/001/ca.wgt");
		assertEquals("PASS",  getDescription(widget));
	}
	@Test
	public void cs(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/002/cs.wgt");
		assertEquals("",  getDescription(widget));
	}
	@Test
	public void cd(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/003/cd.wgt");
		assertEquals("P A S S",  getDescription(widget));
	}
	@Test
	public void x1(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/004/x1.wgt");
		assertEquals("PASS",  getDescription(widget));
	}
	@Test
	public void x2(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-VdCEyDVSA/005/x2.wgt");
		assertEquals("PASS",  getDescription(widget));
	}

	// ta-vcYJAPVEym
	@Test
	public void cu(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-vcYJAPVEym/000/cu.wgt");
		assertEquals("PASS",  getLicenseText(widget));	
		assertEquals("PASS:",  getLicenseHref(widget));
	}
	@Test
	public void ci(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-vcYJAPVEym/001/ci.wgt");
		assertEquals("",  getLicenseText(widget));	
		assertEquals(null, getLicenseHref(widget));
	}
	@Test
	public void ra(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-vcYJAPVEym/002/ra.wgt");
		assertEquals("PASS",  getLicenseText(widget));	
	}
	@Test
	public void co(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-vcYJAPVEym/003/co.wgt");
		assertEquals("PASS",  getLicenseText(widget));			
	}
	
	
	// ta-YUMJAPVEgI
	@Test
	public void cj(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-YUMJAPVEgI/000/cj.wgt");
		assertEquals("PASS",  getLicenseText(widget));	
	}
	@Test
	public void ck(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-YUMJAPVEgI/001/ck.wgt");
		assertEquals("PASS",  getLicenseText(widget));			
	}
	@Test
	public void cl(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-YUMJAPVEgI/002/cl.wgt");
		assertEquals("",  getLicenseText(widget));	
	}
	@Test
	public void cz(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-YUMJAPVEgI/003/cz.wgt");
		assertEquals("P A S S",  getLicenseText(widget));	
		
	}
	@Test
	public void cx(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-YUMJAPVEgI/004/cx.wgt");
		assertEquals("",  getLicenseText(widget));	
		assertEquals("test/pass.html", getLicenseHref(widget));
	}

	// ta-iipTwNshRg
	@Test
	public void d1(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iipTwNshRg/000/d1.wgt");
		assertEquals("icon.png",getIcon(widget));
	}

	@Test
	public void ga(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iipTwNshRg/001/ga.wgt");
		assertEquals("icon.png",getIcon(widget));
	}
	
	// ta-roCaKRxZhS
	@Test
	public void d2(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-roCaKRxZhS/000/d2.wgt");
		assertEquals("icon.png",getIcon(widget));
	}
	
	// ta-iuJHnskSHq
	@Test
	public void zz(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iuJHnskSHq/000/zz.wgt");
		assertEquals(0,widget.getIconsList().size());
	}
	@Test
	public void za(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iuJHnskSHq/001/za.wgt");
		assertEquals("pass.png",getIcon(widget));
	}
	@Test
	public void zb(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iuJHnskSHq/002/zb.wgt");
		assertEquals("locales/en/icon.png",getIcon(widget));
		assertEquals(null, getIconEntity(widget).getWidth());//getIconEntity(widget).getWidth());
		assertEquals(null, getIconEntity(widget).getHeight());
	}
	@Test
	public void zc(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-iuJHnskSHq/003/zc.wgt");
		assertEquals("locales/en/custom.png",getIcon(widget));
		assertEquals(null, getIconEntity(widget).getWidth());
		assertEquals(null, getIconEntity(widget).getHeight());
	}
	
	// ta-eHUaPbgfKg
	@Test
	public void ix(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/000/ix.wgt");
		assertEquals(123, getIconEntity(widget).getHeight().intValue());
	}
	@Test
	public void iy(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/001/iy.wgt");
		assertEquals(0, getIconEntity(widget).getHeight().intValue());
	}
	@Test
	public void iz(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/002/iz.wgt");
		assertEquals(100, getIconEntity(widget).getHeight().intValue());
	}
	@Test
	public void i1(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/003/i1.wgt");
		assertEquals(123, getIconEntity(widget).getHeight().intValue());
	}
	@Test
	public void i2(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/004/i2.wgt");
		assertEquals(null, getIconEntity(widget).getHeight());
	}
	@Test
	public void i3(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/005/i3.wgt");
		assertEquals(null, getIconEntity(widget).getHeight());
	}
	@Test
	public void i4(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-eHUaPbgfKg/006/i4.wgt");
		assertEquals(0, getIconEntity(widget).getHeight().intValue());	}
	
	// ta-nYAcofihvj
	@Test
	public void iq(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/000/iq.wgt");
		assertEquals(123, getIconEntity(widget).getWidth().intValue());	
	}
	@Test
	public void i9(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/001/i9.wgt");
		assertEquals(0, getIconEntity(widget).getWidth().intValue());	
	}
	@Test
	public void iw(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/002/iw.wgt");
		assertEquals(100, getIconEntity(widget).getWidth().intValue());	
	}
	@Test
	public void ie(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/003/ie.wgt");
		assertEquals(123, getIconEntity(widget).getWidth().intValue());	
	}
	@Test
	public void ir(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/004/ir.wgt");
		assertEquals(null, getIconEntity(widget).getWidth());	
	}
	@Test
	public void it(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/005/it.wgt");
		assertEquals(null, getIconEntity(widget).getWidth());	
	}
	@Test
	public void ib(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-nYAcofihvj/006/ib.wgt");
		assertEquals(0, getIconEntity(widget).getWidth().intValue());	
	}
	
	// ta-sdwhMozwIc
	@Test
	public void b7(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-sdwhMozwIc/000/b7.wgt");
		assertEquals("PASS", widget.getAuthor().getAuthorName());
	}
	@Test
	public void b8(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-sdwhMozwIc/001/b8.wgt");
		assertEquals("", widget.getAuthor().getAuthorName());

	}
	@Test
	public void b9(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-sdwhMozwIc/002/b9.wgt");
		assertEquals("PASS", widget.getAuthor().getAuthorName());

	}

	// ta-argMozRiC
	@Test
	public void af(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/000/af.wgt");
		assertEquals("PASS", widget.getAuthor().getAuthorName());
	}
	@Test
	public void ag(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/001/ag.wgt");
		assertEquals("P A S S", widget.getAuthor().getAuthorName());
	}
	@Test
	public void ah(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/002/ah.wgt");
		assertEquals("PASS", widget.getAuthor().getAuthorName());
	}
	@Test
	public void ai(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/003/ai.wgt");
		assertEquals("PASS", widget.getAuthor().getEmail());
	}
	@Test
	public void aj(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/004/aj.wgt");
		assertEquals("PASS", widget.getAuthor().getAuthorName());
	}
	@Test
	public void ak(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/005/ak.wgt");
		assertEquals("PASS", widget.getAuthor().getAuthorName());
		}
	@Test
	public void al(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/006/al.wgt");
		assertEquals("", widget.getAuthor().getAuthorName());
	}
	@Test
	public void am(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/007/am.wgt");
		assertEquals("PASS:PASS", widget.getAuthor().getHref());	}
	@Test
	public void an(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-argMozRiC/008/an.wgt");
		assertEquals(null, widget.getAuthor().getHref());	}


	// ta-DwhJBIJRQN
	@Test
	public void a5(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/000/a5.wgt");
		assertEquals(0,widget.getPrefences().size());
	}

	@Test
	public void a6(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/001/a6.wgt");
		assertTrue(widget.getPrefences().size() == 1);
		IPreferenceEntity pref = widget.getPrefences().get(0);
		assertEquals("PASS",pref.getName());
		assertEquals("PASS", pref.getValue()); 
		assertEquals(false, pref.isReadOnly());
	}		
	@Test
	public void a7(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/002/a7.wgt");
		assertTrue(widget.getPrefences().size() == 1);
		IPreferenceEntity pref = widget.getPrefences().get(0);
		assertEquals("PASS",pref.getName());
		assertEquals("PASS", pref.getValue()); 
		assertEquals(false, pref.isReadOnly());
	}			
	@Test
	public void a8(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/003/a8.wgt");
		assertTrue(widget.getPrefences().size() == 1);
		IPreferenceEntity pref = widget.getPrefences().get(0);
		assertEquals("PASS",pref.getName());
		assertEquals("PASS", pref.getValue()); 
		assertEquals(true, pref.isReadOnly());
	}	
	@Test
	public void a9(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/004/a9.wgt");
		assertTrue(widget.getPrefences().size() == 1);
		IPreferenceEntity pref = widget.getPrefences().get(0);
		assertEquals("PASS",pref.getName());
		assertEquals("PASS", pref.getValue()); 
		assertEquals(false, pref.isReadOnly());
	}	
	@Test
	public void ba(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/005/ba.wgt");
		assertTrue(widget.getPrefences().size() == 1);
		IPreferenceEntity pref = widget.getPrefences().get(0);
		assertEquals("a",pref.getName());
		assertEquals("a", pref.getValue()); 
		assertEquals(false, pref.isReadOnly());
	}

	@Test
	public void bb(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/006/bb.wgt");
		assertTrue(widget.getPrefences().size() == 2);
		IPreferenceEntity pref1 = widget.getPrefences().get(0);
		IPreferenceEntity pref2 = widget.getPrefences().get(1);
		
		assertEquals("a", pref1.getName());
		assertEquals("a", pref1.getValue()); 
		assertEquals(false, pref1.isReadOnly());
		assertEquals("A", pref2.getName());
		assertEquals("b", pref2.getValue()); 
		assertEquals(false, pref2.isReadOnly());
	}

	@Test
	public void bc(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/007/bc.wgt");
		assertTrue(widget.getPrefences().size() == 1);
		IPreferenceEntity pref = widget.getPrefences().get(0);
		assertEquals("PASS",pref.getName());
		assertEquals("PASS", pref.getValue()); 
		assertEquals(false, pref.isReadOnly());
	}
	@Test
	@Ignore
	public void bd(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/008/bd.wgt");
		assertTrue(widget.getPrefences().size() == 1);
		IPreferenceEntity pref = widget.getPrefences().get(0);
		assertEquals(false, pref.isReadOnly());
	}
	@Test
	@Ignore
	public void be(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/009/be.wgt");
		assertTrue(widget.getPrefences().size() == 1);
		IPreferenceEntity pref = widget.getPrefences().get(0);
		assertEquals(false, pref.isReadOnly());
	}
	@Test
	@Ignore
	public void bf(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-DwhJBIJRQN/010/bf.wgt");
		assertTrue(widget.getPrefences().size() == 1);
		IPreferenceEntity pref = widget.getPrefences().get(0); 
		assertEquals(false, pref.isReadOnly());
	}

	// ta-hkWmGJgfve
	@Test
	public void bq(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-hkWmGJgfve/000/bq.wgt");
		assertEquals("pass.html",getSrc(widget));
	}
	@Test
	public void br(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-hkWmGJgfve/001/br.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void bs(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-hkWmGJgfve/002/bs.wgt");
		assertEquals("pass.html",getSrc(widget));
	}

	// ta-LTUJGJFCOU
	@Test
	public void d7(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LTUJGJFCOU/000/d7.wgt");
		assertEquals("index.htm",getSrc(widget));
	}
	@Test
	public void d8(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LTUJGJFCOU/001/d8.wgt");
		assertEquals("index.htm",getSrc(widget));
	}
	@Test
	public void gb(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LTUJGJFCOU/002/gb.wgt");
		assertEquals("index.htm",getSrc(widget));
	}
	
	// ta-pIffQywZin
	@Test
	public void db(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-pIffQywZin/000/db.wgt");
		assertEquals("index.htm",getSrc(widget));
	}
	// ta-LQcjNKBLUZ
	@Test
	public void d9(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LQcjNKBLUZ/000/d9.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void d0(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-LQcjNKBLUZ/001/d0.wgt");
		assertEquals("index.htm",getSrc(widget));
	}

	// ta-dPOgiLQKNK
	@Test
	public void e4(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-dPOgiLQKNK/000/e4.wgt");
		assertEquals("UTF-8",getStartFileEncoding(widget));
	}
	
	
	@Test
	// We don't support non-UTF-8 encodings, so this requires a manual test
	public void e5(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-dPOgiLQKNK/001/e5.wgt");
		assertEquals("ISO-8859-1",getStartFileEncoding(widget));
	}
	@Test
	// We don't support non-UTF-8 encodings, so this requires a manual test
	public void e6(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-dPOgiLQKNK/002/e6.wgt");
		assertEquals("ISO-8859-1",getStartFileEncoding(widget));	}
	@Test
	public void e7(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-dPOgiLQKNK/003/e7.wgt");
		assertEquals("UTF-8",getStartFileEncoding(widget));
	}
	
	// ta-paIabGIIMC
	@Test
	public void dc(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-paIabGIIMC/000/dc.wgt");
		assertEquals("index.php",getSrc(widget));
		assertEquals("text/html",getStartFileContentType(widget));
	}
	@Test
	public void dv(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-paIabGIIMC/001/dv.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	
	// ta-aaaaaaaaaa
	@Test
	public void z1(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-aaaaaaaaaa/000/z1.wgt");
		assertEquals("ISO-8859-1",getStartFileEncoding(widget));
	}
	
	@Test
	public void z2(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-aaaaaaaaaa/001/z2.wgt");
		assertEquals("Windows-1252",getStartFileEncoding(widget));
	}
	

	
	 
	// ta-KNiLPOKdgQ
	@Test
	public void e1(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-KNiLPOKdgQ/000/e1.wgt");
		assertEquals(0,getTestFeature(widget).getParams().size());
	}
 
	// ta-rZdcMBExBX
	@Test
	public void df(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-rZdcMBExBX/000/df.wgt");
		assertEquals(0,widget.getFeatures().size());
	}
	
	@Test
	@Ignore
	public void dr(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-rZdcMBExBX/001/dr.wgt");
		assertNotNull(getTestFeature(widget));
	}
	
	@Test
	public void ha(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-rZdcMBExBX/002/ha.wgt");
		assertEquals(2,widget.getFeatures().size());
		IFeatureEntity feature1 = widget.getFeatures().get(0);
		IFeatureEntity feature2 = widget.getFeatures().get(1);
		assertEquals("feature:a9bb79c1", feature1.getName());
		assertEquals("feature:a9bb79c1", feature2.getName());
		assertEquals(1,feature1.getParams().size());
		assertEquals(1,feature2.getParams().size());
		assertEquals("test",feature1.getParams().get(0).getName());
		assertEquals("test",feature2.getParams().get(0).getName());
		assertTrue(
			feature1.getParams().get(0).getValue().equals("pass1") && feature2.getParams().get(0).getValue().equals("pass2")
		||  feature2.getParams().get(0).getValue().equals("pass1") && feature1.getParams().get(0).getValue().equals("pass2")
		);
	}


	// ta-paWbGHyVrG
	@Test
	public void d4(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-paWbGHyVrG/000/d4.wgt");
		assertFalse(err == null||err.equals(""));
	}
	
	// ta-ignore-unrequired-feature-with-invalid-name
	@Test
	public void gg(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-ignore-unrequired-feature-with-invalid-name/000/gg.wgt");
		assertEquals(0,widget.getFeatures().size());
	}
	
	// ta-vOBaOcWfll
	@Test
	public void e8(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-vOBaOcWfll/000/e8.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	// ta-luyKMFABLX
	@Test
	public void d5(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-luyKMFABLX/000/d5.wgt");
		assertEquals(0,widget.getFeatures().size());
	}
	// ta-EGkPfzCBOz
	@Test
	public void dt(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-EGkPfzCBOz/000/dt.wgt");
		assertEquals(0,getTestFeature(widget).getParams().size());
	}
	@Test
	public void dg(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-EGkPfzCBOz/001/dg.wgt");
		assertEquals(1,widget.getFeatures().size());
		assertEquals(1,widget.getFeatures().get(0).getParams().size());
		assertEquals("PASS", getTestFeature(widget).getParams().get(0).getName());
		assertEquals("PASS", getTestFeature(widget).getParams().get(0).getValue());
	}	
	@Test
	public void v9(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-EGkPfzCBOz/002/v9.wgt");
		assertEquals(1,widget.getFeatures().size());
		IFeatureEntity feature1 = widget.getFeatures().get(0);
		assertEquals("feature:a9bb79c1", feature1.getName());
		assertEquals(2,feature1.getParams().size());
		IParamEntity param1 = feature1.getParams().get(0);
		IParamEntity param2 = feature1.getParams().get(1);
		assertEquals("PASS",param1.getName());
		assertEquals("PASS",param2.getName());
		assertTrue(
			param1.getValue().equals("value1") && param2.getValue().equals("value2")
		||  param1.getValue().equals("value2") && param2.getValue().equals("value1")
		);
	}
	// ta-xlgUWUVzCY
	@Test
	public void d6(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-xlgUWUVzCY/000/d6.wgt");
		assertEquals(0,getTestFeature(widget).getParams().size());
	}

	// ta-CEGwkNQcWo
	@Test
	public void e2(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-CEGwkNQcWo/000/e2.wgt");
		assertNotNull(getTestFeature(widget));
		assertEquals(0,getTestFeature(widget).getParams().size());
	}
	@Test
	public void e3(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-CEGwkNQcWo/001/e3.wgt");
		assertNotNull(getTestFeature(widget));
		assertEquals(0,getTestFeature(widget).getParams().size());
	}
	
	// ta-bbbbbbbbbb
	@Test
	public void xx(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-bbbbbbbbbb/000/xx.wgt");
		String start = getSrc(widget);
		assertEquals("pass.html",start);
	}
	
	// ta-BnWPqNvNVo
	@Test
	public void aw(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-BnWPqNvNVo/000/aw.wgt");
		String start = getSrc(widget);
		assertEquals("pass.html",start);
		assertEquals("icon.png",getIcon(widget));
	}

	// ta-RGNHRBWNZV
	@Test
	public void cc(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/008/cc.wgt");
		String start = getSrc(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void cv(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/009/cv.wgt");
		String start = getSrc(widget);
		assertEquals("index.html",start);
	}
	@Test
	public void b3(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/000/b3.wgt");
		String start = getSrc(widget);
		assertEquals("index.htm",start);
		assertTrue(getStartFileContentType(widget).startsWith("text/html"));
	}
	@Test
	public void b4(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/001/b4.wgt");
		String start = getSrc(widget);
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
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/006/c4.wgt");
		String start = getSrc(widget);
		assertEquals("index.html",start);
	}
	@Test
	public void c5(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-RGNHRBWNZV/007/c5.wgt");
		String start = getSrc(widget);
		assertEquals("index.html",start);
	}
	// ta-FAFYMEGELU
	@Test
	public void bj(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/000/bj.wgt");
		assertEquals("icon.png",getIcon(widget));	
	}
	@Test
	public void bk(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/001/bk.wgt");
		assertEquals("locales/en/icon.png",getIcon(widget));	
	}
	@SuppressWarnings("unchecked")
	@Test
	public void bl(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/002/bl.wgt");
		List icons = widget.getIconsList();
		assertTrue(icons.size() == 2);
		String icon1 = getLocalIconPath(widget,0);
		String icon2 = getLocalIconPath(widget,1); 
		assertTrue((icon1.equals("locales/en/icon.jpg") && (icon2.equals("icon.png"))) || (icon2.equals("locales/en/icon.jpg") && (icon1.equals("icon.png"))));
	}
	@SuppressWarnings("unchecked")
	@Test
	public void bm(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/003/bm.wgt");
		List icons = widget.getIconsList();
		assertEquals(2,icons.size());
		String icon1 = getLocalIconPath(widget,0);
		String icon2 = getLocalIconPath(widget,1); 
		assertTrue((icon1.equals("locales/en/icon.jpg") && (icon2.equals("icon.png"))) || (icon2.equals("locales/en/icon.jpg") && (icon1.equals("icon.png"))));

	}
	@SuppressWarnings("unchecked")
	@Test
	public void bn(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/004/bn.wgt");
		List icons = widget.getIconsList();
		assertTrue(icons.size() == 2);
		String icon1 = getLocalIconPath(widget,0);
		String icon2 = getLocalIconPath(widget,1);
		assertTrue((icon1.equals("locales/en/icon.png") && (icon2.equals("icons/pass.png"))) || (icon2.equals("locales/en/icon.png") && (icon1.equals("icons/pass.png"))));
	}
	@SuppressWarnings("unchecked")
	@Test
	public void bo(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/005/bo.wgt");
		List icons = widget.getIconsList();
		assertTrue(icons.size() == 2);
		String icon1 = getLocalIconPath(widget,0);
		String icon2 = getLocalIconPath(widget,1);
		assertTrue((icon1.equals("icon.jpg") && (icon2.equals("icon.png"))) || (icon2.equals("icon.jpg") && (icon1.equals("icon.png"))));
	}
	@SuppressWarnings("unchecked")
	@Test
	public void bp(){
		// Note the original test case is in error here
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/006/bp.wgt");
		List icons = widget.getIconsList();
		assertTrue(icons.size() == 2);
		assertEquals("icon.png",getLocalIconPath(widget,0));
		assertEquals("locales/en/icon.jpg",getLocalIconPath(widget,1));
	}
	@Test
	public void ad(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/007/ad.wgt");
		assertEquals("icon.png",getIcon(widget));	}

	@Test
	public void ae(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-FAFYMEGELU/008/ae.wgt");
		assertEquals("locales/en/icon.png",getIcon(widget));
	}
	
	/**
	 * This test has been deprecated by W3C

	@Test
	//"Test the UA's ability to correctly find config document. To pass, the
	//user agent must correctly load "pass.html" from "config.xml" and
	//treat "CONFIG.xml" as an arbitrary file.
	public void hh(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-ZjcdAxFMSx/002/hh.wgt");
		String start = getSrc(widget);
		assertEquals("pass.html",start);
	}
	*/


	// Utility methods
	
	private ILicenseEntity getLicense(W3CWidget widget){
		ILicenseEntity[] descs = widget.getLicensesList().toArray(new ILicenseEntity[widget.getLicensesList().size()]);
		return (ILicenseEntity) LocalizationUtils.getLocalizedElement(descs, null);		
	}
	
	private String getLicenseText(W3CWidget widget){
		return getLicense(widget).getLicenseText();		
	}
	
	private String getLicenseHref(W3CWidget widget){
		return getLicense(widget).getHref();		
	}
	
	private String getDescription(W3CWidget widget){
		IDescriptionEntity[] descs = widget.getDescriptions().toArray(new IDescriptionEntity[widget.getDescriptions().size()]);
		IDescriptionEntity desc = (IDescriptionEntity) LocalizationUtils.getLocalizedElement(descs, null);
		return desc.getDescription();
	}
	
	private String getName(W3CWidget widget){
		return getNameEntity(widget).getName();
	}
	
	private String getShortName(W3CWidget widget){
		if (widget.getNames().size() == 0) return null;
		return getNameEntity(widget).getShort();
	}
	
	private INameEntity getNameEntity(W3CWidget widget){
		INameEntity[] names = widget.getNames().toArray(new INameEntity[widget.getNames().size()]);
		return (INameEntity) LocalizationUtils.getLocalizedElement(names, null);
	}
	
	private String getSrc(W3CWidget widget){
		IContentEntity startFile = getDefaultStartFile(widget);
		String start = startFile.getSrc();
		return getLocalUrl(start);
	}
	
	private String getStartFileEncoding(W3CWidget widget){
		return getDefaultStartFile(widget).getCharSet();
	}
	
	private String getStartFileContentType(W3CWidget widget){
		return getDefaultStartFile(widget).getType();
	}

	private IContentEntity getDefaultStartFile(W3CWidget widget){
		IContentEntity[] contents = widget.getContentList().toArray(new IContentEntity[widget.getContentList().size()]);
		return (IContentEntity) LocalizationUtils.getLocalizedElement(contents, null);		
	}

	private IIconEntity getIconEntity(W3CWidget widget){
		IIconEntity[] icons = widget.getIconsList().toArray(new IIconEntity[widget.getIconsList().size()]);
		return (IIconEntity) LocalizationUtils.getLocalizedElement(icons, null);		
	}
	
	private String getIcon(W3CWidget widget){
		return getLocalUrl(getIconEntity(widget).getSrc());
	}
	
	private String getLocalIconPath(W3CWidget widget, int index){
		return getLocalUrl(widget.getIconsList().get(index).getSrc());
	}
	
	private String getLocalUrl(String src){
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

	private IFeatureEntity getTestFeature(W3CWidget widget){
		for (IFeatureEntity feature:widget.getFeatures()){
			if (feature.getName().equals("feature:a9bb79c1")) return feature;
		}
		return null;
	}
}
