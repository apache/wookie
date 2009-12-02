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

import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wookie.tests.functional.AbstractControllerTest;
import org.apache.wookie.tests.helpers.WidgetUploader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Conformance testing for the Packaging and Configuration spec
 * These are functional tests and need to run against a running Wookie server on localhost:8080
 * You need to be online to run these tests as they download test widgets hosted externally.
 * For more information see http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/index.html
 * 
 * Note that in order to run the Feature tests, a ServerFeature named "feature:a9bb79c1" must be set up in the DB
 * 
 * @author scott
 *
 */
public class PackagingAndConfiguration extends AbstractControllerTest {
	// 1 files
	@Test
	public void b5(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RRZxvvTFHx/000/b5.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void b6(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RRZxvvTFHx/001/b6.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.html",start);
	}

	// 2 files
	@Test
	public void bg(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-dxzVDWpaWg/000/bg.wgt");
		assertFalse(err == null||err.equals(""));
	}	

	@Test
	public void bh(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-dxzVDWpaWg/001/bh.wgt");
		assertFalse(err == null||err.equals(""));
	}	

	// 3 bad magic number
	@Test
	public void dk(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-qxLSCRCHlN/000/dk.wgt");
		assertFalse(err == null||err.equals(""));
	}	

	// 4 start files
	@Test
	public void dn(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FDGQBROtzW/000/dn.test");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void dm(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FDGQBROtzW/001/dm");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}


	// 5
	@Test
	public void dl(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-uLHyIMvLwz/000/dl.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void doh(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-uLHyIMvLwz/001/split.wgt.001");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void dp(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-uLHyIMvLwz/002/dp.wgt");
		assertFalse(err == null||err.equals(""));
	}
	// 6 defaults
	@Test 
	@Ignore
	public void ds(){
		// Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-klLDaEgJeU/002/bv.wgt");
		// TODO 
		// To pass, author email must be null, 
		// author href must be null, 
		// author name must be null, 
		// feature list must be empty (null)
		// icons must be null, 
		// start file encoding must be UTF-8, 
		// start file content-type must be text/html, 
		// widget config doc must be 'config.xml' at the root of the widget package, 
		// widget description must be null, 
		// widget height must be null, 
		// widget id must be null, 
		// widget license must be null, 
		// widget license file must be null, 
		// widget license href must be null, 
		// widget name must be null, 
		// widget preferences must be null, 
		// widget short name must be null, 
		// widget version must be null, 
		// widget width must be null, 
		// widget window modes must a list with one entry whose value is 'floating', 
		// widget start file must be index.htm at the root of the widget, 
		// user agent locales must contain at least one item whose value is 'en' 
	}

	// 7 config.xml
	@Test
	public void dq(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-ZjcdAxFMSx/000/dq.wgt");
		assertFalse(err == null||err.equals(""));
	}
	@Test
	public void dw(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-ZjcdAxFMSx/001/dw.wgt");
		assertFalse(err == null||err.equals(""));
	}

	// 8 xml
	@Test
	public void bt(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-klLDaEgJeU/000/bt.wgt");
		assertFalse(err == null||err.equals(""));
	}
	@Test
	public void bu(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-klLDaEgJeU/001/bu.wgt");
		assertFalse(err == null||err.equals(""));
	}

	@Test 
	public void bv(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-klLDaEgJeU/002/bv.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass&.html",start);
	}
	@Test
	public void bw(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-klLDaEgJeU/003/bw.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
	}
	// 9 widget
	@Test
	public void aa(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-ACCJfDGwDQ/000/aa.wgt");
		assertTrue(err.contains("bad namespace"));
	}
	@Test
	public void ab(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-ACCJfDGwDQ/001/ab.wgt");
		assertTrue(err.contains("bad namespace"));
	}
	@Test
	public void ac(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-ACCJfDGwDQ/002/ac.wgt");
		assertTrue(err.contains("bad namespace"));
	}

	// 10 id
	@Test
	public void b1(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RawAIWHoMs/000/b1.wgt");
		assertEquals("pass:", widget.getAttributeValue("identifier"));
	}
	@Test
	public void rd(){
		// We can't allow null ids, we use generated IDs where they aren't valid
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RawAIWHoMs/001/rd.wgt");
		assertTrue(widget.getAttributeValue("identifier").contains("generated"));
	}
	@Test
	public void b2(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RawAIWHoMs/002/b2.wgt");
		assertEquals("pass:", widget.getAttributeValue("identifier"));

	}

	// 11 Version

	@Test
	public void cf(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-VerEfVGeTc/000/cf.wgt");
		assertEquals("PASS", widget.getAttributeValue("version"));
	}
	@Test
	public void cg(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-VerEfVGeTc/001/cg.wgt");
		assertEquals("", widget.getAttributeValue("version"));
	}
	@Test
	public void ch(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-VerEfVGeTc/002/ch.wgt");
		assertEquals("PASS", widget.getAttributeValue("version"));
	}


	// 12  Height

	@Test
	public void ax(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-BxjoiWHaMr/000/ax.wgt");
		assertEquals("1234", getWidgetHeight(widget));
	}
	@Test
	public void ay(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-BxjoiWHaMr/001/ay.wgt");
		assertEquals("0", getWidgetHeight(widget));
	}
	@Test
	public void az(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-BxjoiWHaMr/002/az.wgt");
		assertEquals("100", getWidgetHeight(widget));
	}
	@Test
	public void a1(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-BxjoiWHaMr/003/a1.wgt");
		assertEquals("123", getWidgetHeight(widget));
	}
	@Test
	public void a2(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-BxjoiWHaMr/004/a2.wgt");
		assertEquals("", getWidgetHeight(widget));
	}
	@Test
	public void a3(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-BxjoiWHaMr/005/a3.wgt");
		assertEquals("", getWidgetHeight(widget));
	}
	@Test
	public void a4(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-BxjoiWHaMr/006/a4.wgt");
		assertEquals("0", getWidgetHeight(widget));
	}	
	// 13 Width

	@Test
	public void c9(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UScJfQHPPy/000/c9.wgt");
		assertEquals("0", getWidgetWidth(widget));
	}
	@Test
	public void cq(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UScJfQHPPy/001/cq.wgt");
		assertEquals("1234", getWidgetWidth(widget));
	}
	@Test
	public void cw(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UScJfQHPPy/002/cw.wgt");
		assertEquals("100", getWidgetWidth(widget));
	}
	@Test
	public void ce(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UScJfQHPPy/003/ce.wgt");
		assertEquals("123", getWidgetWidth(widget));
	}
	@Test
	public void cr(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UScJfQHPPy/004/cr.wgt");
		assertEquals("", getWidgetWidth(widget));
	}
	@Test
	public void ct(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UScJfQHPPy/005/ct.wgt");
		assertEquals("", getWidgetWidth(widget));
	}
	@Test
	public void cy(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UScJfQHPPy/006/cy.wgt");
		assertEquals("0", getWidgetWidth(widget));
	}


	// 14 empty widget config

	@Test
	public void d3(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-MFcsScFEaC/000/d3.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}

	// 15 Title

	@Test
	public void bx(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-LYLMhryBBT/000/bx.wgt");
		assertEquals("PASS", getWidgetTitle(widget));
	}

	@Test
	public void by(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-LYLMhryBBT/001/by.wgt");
		assertEquals("", getWidgetTitle(widget));
	}

	@Test
	public void bz(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-LYLMhryBBT/002/bz.wgt");
		assertEquals("PASS", getWidgetTitle(widget));
	}

	// 16 Title

	@Test
	public void ao(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-AYLMhryBnD/000/ao.wgt");
		assertEquals("PASS", getWidgetTitle(widget));
	}

	@Test
	public void ap(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-AYLMhryBnD/001/ap.wgt");
		assertEquals("P A S S", getWidgetTitle(widget));
	}

	@Test
	public void aq(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-AYLMhryBnD/002/aq.wgt");
		assertEquals("PASS", getWidgetTitle(widget));
	}

	@Test
	public void ar(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-AYLMhryBnD/003/ar.wgt");
		assertEquals("PASS", getWidgetShortName(widget));
	}

	@Test
	public void as(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-AYLMhryBnD/004/as.wgt");
		assertEquals("PASS", getWidgetTitle(widget));
		assertEquals("PASS", getWidgetShortName(widget));
	}

	@Test
	public void at(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-AYLMhryBnD/005/at.wgt");
		assertEquals("PASS", getWidgetTitle(widget));
		assertEquals("PASS", getWidgetShortName(widget));
	}

	@Test
	public void au(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-AYLMhryBnD/006/au.wgt");
		assertEquals("", getWidgetShortName(widget));
	}

	@Test
	public void av(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-AYLMhryBnD/007/av.wgt");
		assertEquals("", getWidgetTitle(widget));
	}

	@Test
	public void oa(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-AYLMhryBnD/008/oa.wgt");
		assertEquals("PASS", getWidgetTitle(widget));
	}

	// 17 Description

	@Test
	public void c6(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UEMbyHERkI/000/c6.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}
	@Test
	public void c7(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UEMbyHERkI/001/c7.wgt");
		assertEquals("",  widget.getChild("description").getText());
	}
	@Test
	public void rb(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UEMbyHERkI/002/rb.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}
	@Test
	public void c8(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UEMbyHERkI/003/c8.wgt");
		assertEquals("PASS", widget.getChild("description").getText());
	}

	// 18 Description
	@Test
	public void cp(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-VdCEyDVSA/000/cp.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}
	@Test
	public void ca(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-VdCEyDVSA/001/ca.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}
	@Test
	public void cs(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-VdCEyDVSA/002/cs.wgt");
		assertEquals("",  widget.getChild("description").getText());
	}
	@Test
	public void cd(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-VdCEyDVSA/003/cd.wgt");
		assertEquals("P A S S",  widget.getChild("description").getText());
	}
	@Test
	public void x1(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-VdCEyDVSA/004/x1.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}
	@Test
	public void x2(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-VdCEyDVSA/005/x2.wgt");
		assertEquals("PASS",  widget.getChild("description").getText());
	}

	// 19 License
	@Test
	public void cu(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-vcYJAPVEym/000/cu.wgt");
		assertEquals("PASS",  widget.getChild("license").getText());	
		assertEquals("PASS:",  widget.getChild("license").getAttributeValue("href"));
	}
	@Test
	public void ci(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-vcYJAPVEym/001/ci.wgt");
		assertEquals("",  widget.getChild("license").getText());	
		assertEquals(null, widget.getChild("license").getAttribute("href"));
	}
	@Test
	public void ra(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-vcYJAPVEym/002/ra.wgt");
		assertEquals("PASS",  widget.getChild("license").getText());	
	}
	@Test
	public void co(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-vcYJAPVEym/003/co.wgt");
		assertEquals("PASS",  widget.getChild("license").getText());			
	}
	
	
	// 20 License
	@Test
	public void cj(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-YUMJAPVEgI/000/cj.wgt");
		assertEquals("PASS",  widget.getChild("license").getText());	
	}
	@Test
	public void ck(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-YUMJAPVEgI/001/ck.wgt");
		assertEquals("PASS",  widget.getChild("license").getText());			
	}
	@Test
	public void cl(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-YUMJAPVEgI/002/cl.wgt");
		assertEquals("",  widget.getChild("license").getText());	
	}
	@Test
	public void cz(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-YUMJAPVEgI/003/cz.wgt");
		assertEquals("P A S S",  widget.getChild("license").getText());	
		
	}
	@Test
	public void cx(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-YUMJAPVEgI/004/cx.wgt");
		assertEquals("",  widget.getChild("license").getText());	
		assertEquals("license/pass.html", widget.getChild("license").getAttributeValue("href"));
	}

	// 21 Icon
	@Test
	public void d1(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-iipTwNshRg/000/d1.wgt");
		assertEquals("icon.png",getIcon(widget));
	}

	@Test
	public void ga(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-iipTwNshRg/001/ga.wgt");
		assertEquals("icon.png",getIcon(widget));
	}
	
	// 22 Icon
	@Test
	public void d2(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-roCaKRxZhS/000/d2.wgt");
		assertEquals("icon.png",getIcon(widget));
	}
	
	//23
	@Test
	public void zz(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-iuJHnskSHq/000/zz.wgt");
		assertEquals("pass.png",getIcon(widget));
	}
	@Test
	public void za(){
		// TODO this is actually pretty hard to validate as it requires a list of all icons
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-iuJHnskSHq/001/za.wgt");
		assertEquals("pass.png",getIcon(widget));
	}
	@Test
	public void zb(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-iuJHnskSHq/002/zb.wgt");
		assertEquals("locales/en/icon.png",getIcon(widget));
		assertEquals(null, widget.getChild("icon").getAttribute("width"));
		assertEquals(null, widget.getChild("icon").getAttribute("height"));
	}
	@Test
	public void zc(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-iuJHnskSHq/003/zc.wgt");
		assertEquals("locales/en/custom.png",getIcon(widget));
		assertEquals(null, widget.getChild("icon").getAttribute("width"));
		assertEquals(null, widget.getChild("icon").getAttribute("height"));
	}
	
	//24
	@Test
	public void ix(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-eHUaPbgfKg/000/ix.wgt");
		assertEquals("1234", widget.getChild("icon").getAttributeValue("height"));
	}
	@Test
	public void iy2(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-eHUaPbgfKg/001/iy.wgt");
		assertEquals("0", widget.getChild("icon").getAttributeValue("height"));
	}
	@Test
	public void iz(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-eHUaPbgfKg/002/iz.wgt");
		assertEquals("100", widget.getChild("icon").getAttributeValue("height"));
	}
	@Test
	public void i1(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-eHUaPbgfKg/003/i1.wgt");
		assertEquals("123", widget.getChild("icon").getAttributeValue("height"));
	}
	@Test
	public void i2(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-eHUaPbgfKg/004/i2.wgt");
		assertEquals(null, widget.getChild("icon").getAttribute("height"));
	}
	@Test
	public void i3(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-eHUaPbgfKg/005/i3.wgt");
		assertEquals(null, widget.getChild("icon").getAttribute("height"));
	}
	@Test
	public void i4(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-eHUaPbgfKg/006/i4.wgt");
		assertEquals("0", widget.getChild("icon").getAttributeValue("height"));	}
	
	//25
	@Test
	public void iq(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-nYAcofihvj/000/iq.wgt");
		assertEquals("1234", widget.getChild("icon").getAttributeValue("width"));	
	}
	@Test
	public void i9(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-nYAcofihvj/001/i9.wgt");
		assertEquals("0", widget.getChild("icon").getAttributeValue("width"));	
	}
	@Test
	public void iw(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-nYAcofihvj/002/iw.wgt");
		assertEquals("100", widget.getChild("icon").getAttributeValue("width"));	
	}
	@Test
	public void ie(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-nYAcofihvj/003/ie.wgt");
		assertEquals("123", widget.getChild("icon").getAttributeValue("width"));	
	}
	@Test
	public void ir(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-nYAcofihvj/004/ir.wgt");
		assertEquals(null, widget.getChild("icon").getAttribute("width"));	
	}
	@Test
	public void it(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-nYAcofihvj/005/it.wgt");
		assertEquals(null, widget.getChild("icon").getAttribute("width"));	
	}
	@Test
	public void iy(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-nYAcofihvj/006/iy.wgt");
		assertEquals("0", widget.getChild("icon").getAttributeValue("width"));	
	}
	
	// 26 Author
	@Test
	public void b7(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-sdwhMozwIc/000/b7.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
	}
	@Test
	public void b8(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-sdwhMozwIc/001/b8.wgt");
		assertEquals("", widget.getChild("author").getText());

	}
	@Test
	public void b9(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-sdwhMozwIc/002/b9.wgt");
		assertEquals("PASS", widget.getChild("author").getText());

	}

	// 27 Author  
	@Test
	public void af(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-argMozRiC/000/af.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
	}
	@Test
	public void ag(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-argMozRiC/001/ag.wgt");
		assertEquals("P A S S", widget.getChild("author").getText());
	}
	@Test
	public void ah(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-argMozRiC/002/ah.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
	}
	@Test
	public void ai(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-argMozRiC/003/ai.wgt");
		assertEquals("PASS", widget.getChild("author").getAttributeValue("email"));
	}
	@Test
	public void aj(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-argMozRiC/004/aj.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
		assertEquals("PASS", widget.getChild("author").getAttributeValue("email"));
		assertEquals("PASS:PASS", widget.getChild("author").getAttributeValue("href"));
	}
	@Test
	public void ak(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-argMozRiC/005/ak.wgt");
		assertEquals("PASS", widget.getChild("author").getText());
		assertEquals("PASS", widget.getChild("author").getAttributeValue("email"));
		assertEquals("PASS:PASS", widget.getChild("author").getAttributeValue("href"));	}
	@Test
	public void al(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-argMozRiC/006/al.wgt");
		assertEquals("", widget.getChild("author").getText());
	}
	@Test
	public void am(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-argMozRiC/007/am.wgt");
		assertEquals("PASS:PASS", widget.getChild("author").getAttributeValue("href"));	}
	@Test
	public void an(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-argMozRiC/008/an.wgt");
		assertEquals(null, widget.getChild("author").getAttribute("href"));	}

	// 28 Preference
	@Test
	public void a5(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/000/a5.wgt");
		assertTrue(widget.getChildren("preference") == null || widget.getChildren("preference").size() == 0);
	}

	@Test
	public void a6(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/001/a6.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); 
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}		
	@Test
	public void a7(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/002/a7.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); 
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}			
	@Test
	public void a8(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/003/a8.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); 
		assertEquals("true", widget.getChild("preference").getAttributeValue("readonly"));
	}	
	@Test
	public void a9(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/004/a9.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); 
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}	
	@Test
	public void ba(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/005/ba.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("a", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("a", widget.getChild("preference").getAttributeValue("value")); 
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}
	@SuppressWarnings("unchecked")
	@Test
	public void bb(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/006/bb.wgt");
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
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/007/bc.wgt");
		List<Element> preferences = widget.getChildren("preference");
		assertTrue(preferences.size() == 1);
		Element pref1 = preferences.get(0);
		
		assertEquals("PASS", pref1.getAttributeValue("name"));
		assertEquals("PASS", pref1.getAttributeValue("value")); 
		assertEquals("false", pref1.getAttributeValue("readonly"));
	}
	@Test
	public void bd(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/008/bd.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}
	@Test
	public void be(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/009/be.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}
	@Test
	public void bf(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/010/bf.wgt");
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));
	}

	// 29
	@Test
	public void bq(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-hkWmGJgfve/000/bq.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
	}
	@Test
	public void br(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-hkWmGJgfve/001/br.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void bs(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-hkWmGJgfve/002/bs.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
	}

	//30
	@Test
	public void d7(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-LTUJGJFCOU/000/d7.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void d8(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-LTUJGJFCOU/001/d8.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void gb(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-LTUJGJFCOU/002/gb.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	
	//31
	@Test
	public void db(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-pIffQywZin/000/db.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	//32
	@Test
	public void d9(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-LQcjNKBLUZ/000/d9.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void d0(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-LQcjNKBLUZ/001/d0.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}

	// 33 Start File Text Encoding
	@Test
	public void e4(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-dPOgiLQKNK/000/e4.wgt");
		assertEquals("UTF-8",getStartFileEncoding(widget));
	}
	@Test
	public void e5(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-dPOgiLQKNK/001/e5.wgt");
		assertEquals("ISO-8859-1",getStartFileEncoding(widget));
	}
	@Test
	public void e6(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-dPOgiLQKNK/002/e6.wgt");
		assertEquals("ISO-8859-1",getStartFileEncoding(widget));
	}
	@Test
	public void e7(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-dPOgiLQKNK/003/e7.wgt");
		assertEquals("UTF-8",getStartFileEncoding(widget));
	}
	
	//34
	@Test
	public void dc(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-paIabGIIMC/000/dc.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.php",start);
		assertEquals("text/html",getStartFileContentType(widget));
	}
	@Test
	public void dv(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-paIabGIIMC/001/dv.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	
	//35
	@Test
	@Ignore
	public void z1(){
		// TODO
	}
	@Test
	@Ignore
	public void z2(){
		// TODO
	}

	//36
	@Test
	public void e1(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-KNiLPOKdgQ/000/e1.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("identifier")+":the feature feature:a9bb79c1 must not have any params associated with it.");
	}
	//37
	@Test
	public void df(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-rZdcMBExBX/000/df.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("identifier")+":To pass, the feature list must remain empty. ");

	}
	@Test
	public void dr(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-rZdcMBExBX/001/dr.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("identifier")+":the feature list must contain one feature named 'feature:a9bb79c1' whose required value is false.");
	}

	//38
	@Test
	public void d4(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-paWbGHyVrG/000/d4.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	//39
	@Test
	public void e8(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-vOBaOcWfll/000/e8.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	//40
	@Test
	public void d5(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-luyKMFABLX/000/d5.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("identifier")+":the user agent must not contain any values in the feature list (i.e., the unknown feature is skipped).");
	}
	//41
	@Test
	public void dt(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-EGkPfzCBOz/000/dt.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("identifier")+":the feature list must contain one feature named 'feature:a9bb79c1' with no associated parameters. ");
	}
	@Test
	public void dg(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-EGkPfzCBOz/001/dg.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("identifier")+":the feature list must contain one feature named 'feature:a9bb79c1' with one associated parameter whose name is 'PASS' and whose value is 'PASS'. ");
	}	
	//42
	@Test
	public void d6(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-xlgUWUVzCY/000/d6.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("identifier")+":feature 'feature:a9bb79c1' must not have any associated parameters.");
	}

	// 43 Feature
	@Test
	public void e2(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-CEGwkNQcWo/000/e2.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("identifier")+":the feature feature:a9bb79c1 must not have any associated params. ");
	}
	@Test
	public void e3(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-CEGwkNQcWo/001/e3.wgt");
		System.out.println("Manual test:"+widget.getAttributeValue("identifier")+":the feature feature:a9bb79c1 must not have any associated params. ");
	}
	
	//44
	@Test
	public void xx(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-bbbbbbbbbb/000/xx.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
	}
	
	//45 Start file and icons
	@Test
	public void aw(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-BnWPqNvNVo/000/aw.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
		assertEquals("icon.png",getIcon(widget));
	}

	//46  Start file
	@Test
	public void cc(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RGNHRBWNZV/008/cc.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void cv(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RGNHRBWNZV/009/cv.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.html",start);
	}
	@Test
	public void b3(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RGNHRBWNZV/000/b3.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
		assertEquals("text/html",getStartFileContentType(widget));
	}
	@Test
	public void b4(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RGNHRBWNZV/001/b4.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.html",start);
		assertEquals("text/html",getStartFileContentType(widget));
	}
	@Test
	public void b0(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RGNHRBWNZV/002/b0.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void c1(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RGNHRBWNZV/003/c1.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void c2(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RGNHRBWNZV/004/c2.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void c3(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RGNHRBWNZV/005/c3.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void c4(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RGNHRBWNZV/006/c4.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.html",start);
	}
	@Test
	public void c5(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-RGNHRBWNZV/007/c5.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.html",start);
	}
	// 47 Icons
	@Test
	public void bj(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/000/bj.wgt");
		assertEquals("icon.png",getIcon(widget));	
	}
	@Test
	public void bk(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/001/bk.wgt");
		assertEquals("locales/en/icon.png",getIcon(widget));	
	}
	@Test
	public void bl(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/002/bl.wgt");
		List icons = widget.getChildren("icon");
		assertTrue(icons.size() == 2);
		assertEquals("locales/en/icon.jpg",getLocalIconPath(widget,((Element)icons.get(0))));
		assertEquals("icon.png",getLocalIconPath(widget,((Element)icons.get(1))));
	}
	@Test
	public void bm(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/003/bm.wgt");
		List icons = widget.getChildren("icon");
		assertTrue(icons.size() == 2);
		assertEquals("locales/en/icon.jpg",getLocalIconPath(widget,((Element)icons.get(0))));
		assertEquals("icon.png",getLocalIconPath(widget,((Element)icons.get(1))));
	}
	@Test
	public void bn(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/004/bn.wgt");
		List icons = widget.getChildren("icon");
		assertTrue(icons.size() == 2);
		assertEquals("icons/pass.png",getLocalIconPath(widget,((Element)icons.get(0))));
		assertEquals("icon.png",getLocalIconPath(widget,((Element)icons.get(1))));
	}
	@Test
	public void bo(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/005/bo.wgt");
		List icons = widget.getChildren("icon");
		assertTrue(icons.size() == 2);
		assertEquals("icon.png", getLocalIconPath(widget,((Element)icons.get(0))));
		assertEquals("icon.jpg", getLocalIconPath(widget,((Element)icons.get(1))));
	}
	@Test
	public void bp(){
		// Note the original test case is in error here
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/006/bp.wgt");
		List icons = widget.getChildren("icon");
		assertTrue(icons.size() == 2);
		assertEquals("icon.png",getLocalIconPath(widget,((Element)icons.get(0))));
		assertEquals("locales/en/icon.jpg",getLocalIconPath(widget,((Element)icons.get(1))));
	}
	@Test
	public void ad(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/007/ad.wgt");
		assertEquals("icon.png",getIcon(widget));	}

	@Test
	public void ae(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/008/ae.wgt");
		assertEquals("locales/en/icon.png",getIcon(widget));
	}

	// Utility methods
	private Element processWidgetNoErrors(String widgetfname){
		try {
			//File file = new File("src-tests/testdata/conformance/"+widgetfname);
			String error = WidgetUploader.uploadWidget(widgetfname);
			if (error != null && !error.equals("")){
				fail("widget failed to upload correctly:"+error);
			}
			Element widget = WidgetUploader.getLastWidget();
			assertNotNull(widget);
			return widget;
		} catch (Exception e) {
			//e.printStackTrace();
			fail("couldn't upload widget");
		}		
		fail("widget not found after upload");
		return null;
	}

	private String instantiateWidget(Element widget){
		String response = null;
		String widgetUri = widget.getAttributeValue("identifier");
		// instantiate widget and parse results
		try {
			HttpClient client = new HttpClient();
			PostMethod post = new PostMethod(TEST_INSTANCES_SERVICE_URL_VALID);
			post.setQueryString("api_key="+API_KEY_VALID+"&widgetid="+widgetUri+"&userid=test&shareddatakey=test");
			client.executeMethod(post);
			response = IOUtils.toString(post.getResponseBodyAsStream());
			post.releaseConnection();
		}
		catch (Exception e) {
			//e.printStackTrace();
			fail("failed to instantiate widget");
		}
		return response;		
	}

	private String getStartFile(String response){
		SAXBuilder builder = new SAXBuilder();
		Reader in = new StringReader(response);
		Document doc;
		try {
			doc = builder.build(in);
		} catch (Exception e) {
			return null;
		} 
		return doc.getRootElement().getChild("url").getText();
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
			get.releaseConnection();
			return get.getResponseCharSet();
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
		// http://localhost:8080/wookie/wservices/ + uid + / + name + ?idkey=...
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
			return WidgetUploader.uploadWidget(widgetfname);
		} catch (Exception e) {
			fail("couldn't upload widget");
		}		
		fail("widget not found after upload");
		return null;		
	}

	private String getIcon(Element widget){
		String icon = null;
		// split off the end icon path
		// http://localhost:8080/wookie/wservices/ + uid + / 
		String baseUrl = "http://localhost:8080/wookie/wservices/"+widget.getAttributeValue("identifier")+"/";
		Element iconElem = widget.getChild("icon");
		if(iconElem == null) return null;
		String iconUrl = iconElem.getText();
		icon = StringUtils.difference(baseUrl,iconUrl);
		return icon;
	}
	
	private String getLocalIconPath(Element widget, Element iconElem){
		String baseUrl = "http://localhost:8080/wookie/wservices/"+widget.getAttributeValue("identifier")+"/";	
		if(iconElem == null) return null;
		String iconUrl = iconElem.getText();
		String icon = StringUtils.difference(baseUrl,iconUrl);
		return icon;
	}

	private String getWidgetShortName(Element widget){
		return widget.getChild("title").getAttributeValue("short");
	}

	private String getWidgetTitle(Element widget){
		return widget.getChild("title").getText();
	}

	private String getWidgetWidth(Element widget){
		return widget.getAttributeValue("width");
	}
	private String getWidgetHeight(Element widget){
		return widget.getAttributeValue("height");
	}

}
