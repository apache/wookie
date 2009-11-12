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

	// 3 Start files
	@Test
	public void taGVVIvsdEUo2(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-GVVIvsdEUo/000/test");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void taGVVIvsdEUo1(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-GVVIvsdEUo/001/test.html");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}

	// 4 MIME
	@Test
	public void di(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-YrdwUYRXwc/000/di.wgt");
		assertFalse(err == null||err.equals(""));
	}	

	// 5 bad magic number
	@Test
	public void dk(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-qxLSCRCHlN/000/dk.wgt");
		assertFalse(err == null||err.equals(""));
	}	

	// 6 start files
	@Test
	public void taFDGQBROtzW0(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FDGQBROtzW/000/test.test");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	@Test
	public void taFDGQBROtzW1(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FDGQBROtzW/001/test");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}


	// 7
	@Test
	public void dl(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-uLHyIMvLwz/000/dl.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void split(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-uLHyIMvLwz/001/split.wgt.001");
		assertFalse(err == null||err.equals(""));
	}	
	@Test
	public void dp(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-uLHyIMvLwz/002/dp.wgt");
		assertFalse(err == null||err.equals(""));
	}
	// 8 defaults
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

	// 9 config.xml
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

	// 10 xml
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
	@Ignore
	public void bw(){
		//Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-klLDaEgJeU/003/bw.wgt");
		// TODO include author in metadata generated from REST API
		// assertEquals("PASS", widget.getChild("author").getText());
	}
	// 11 widget
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

	// 12 id
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

	// 13 Version

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


	// 14  Height

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
	// 15 Width

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


	// 16 empty widget config

	@Test
	public void d3(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-MFcsScFEaC/000/d3.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}

	// 17 Title

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

	// 18 Title

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

	// 19 Description

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

	// 20 Description
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

	// 21 License TODO can't test without license exposed in metadata
	@Test
	@Ignore
	public void cu(){
		
	}
	@Test
	@Ignore
	public void ci(){
		
	}
	@Test
	@Ignore
	public void ra(){
		
	}
	@Test
	@Ignore
	public void co(){
		
	}
	
	
	// 22 License TODO can't test without license exposed in metadata
	@Test
	@Ignore
	public void cj(){
		
	}
	@Test
	@Ignore
	public void ck(){
		
	}
	@Test
	@Ignore
	public void cl(){
		
	}
	@Test
	@Ignore
	public void cz(){
		
	}
	@Test
	@Ignore
	public void cx(){
		
	}

	// 23 Icon
	@Test
	public void d1(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-iipTwNshRg/000/d1.wgt");
		assertEquals("icon.png",getIcon(widget));
	}

	// 24 Icon
	@Test
	public void d2(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-roCaKRxZhS/000/d2.wgt");
		assertEquals("icon.png",getIcon(widget));
	}

	// 25-28 IGNORE

	// 29 Author
	@Test
	@Ignore
	public void b7(){
		// TODO can't test without author exposed in metadata
	}
	@Test
	@Ignore
	public void b8(){
		// TODO can't test without author exposed in metadata
	}
	@Test
	@Ignore
	public void b9(){
		// TODO can't test without author exposed in metadata
	}

	// 30 Author  
	@Test
	@Ignore
	public void af(){
		// TODO can't test without author exposed in metadata
	}
	@Test
	@Ignore
	public void ag(){
		// TODO can't test without author exposed in metadata
	}
	@Test
	@Ignore
	public void ah(){
		// TODO can't test without author exposed in metadata
	}
	@Test
	@Ignore
	public void ai(){
		// TODO can't test without author exposed in metadata
	}
	@Test
	@Ignore
	public void aj(){
		// TODO can't test without author exposed in metadata
	}
	@Test
	@Ignore
	public void ak(){
		// TODO can't test without author exposed in metadata
	}
	@Test
	@Ignore
	public void al(){
		// TODO can't test without author exposed in metadata	
	}
	@Test
	@Ignore
	public void am(){
		// TODO can't test without author exposed in metadata	
	}
	@Test
	@Ignore
	public void an(){
		// TODO can't test without author exposed in metadata
	}

	// 31 Preference
	@Test
	public void a5(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/000/a5.wgt");
		assertTrue(widget.getChildren("preference") == null || widget.getChildren("preference").size() == 0);
	}

	@Test
	@Ignore
	public void a6(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/001/a6.wgt");
		System.out.println(widget.getChildren("preference"));
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); //TODO
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));//TODO
	}		
	@Test
	@Ignore
	public void a7(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/002/a7.wgt");
		System.out.println(widget.getChildren("preference"));
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); //TODO
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));//TODO
	}			
	@Test
	@Ignore
	public void a8(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/003/a8.wgt");
		System.out.println(widget.getChildren("preference"));
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); //TODO
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));//TODO
	}	
	@Test
	@Ignore
	public void a9(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/004/a9.wgt");
		System.out.println(widget.getChildren("preference"));
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("PASS", widget.getChild("preference").getAttributeValue("value")); //TODO
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));//TODO
	}	
	@Test
	@Ignore
	public void ba(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-DwhJBIJRQN/005/ba.wgt");
		System.out.println(widget.getChildren("preference"));
		assertTrue(widget.getChildren("preference").size() == 1);
		assertEquals("a", widget.getChild("preference").getAttributeValue("name"));
		assertEquals("a", widget.getChild("preference").getAttributeValue("value")); //TODO
		assertEquals("false", widget.getChild("preference").getAttributeValue("readonly"));//TODO
	}
	@Test
	@Ignore
	public void bb(){
	}
	@Test
	@Ignore
	public void bc(){
	}
	@Test
	@Ignore
	public void bd(){
	}
	@Test
	@Ignore
	public void be(){
	}
	@Test
	@Ignore
	public void bf(){
	}

	// 32
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

	//33
	@Test
	public void dz(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UJGJCaaFLT/000/dz.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
	}
	@Test
	public void da(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-UJGJCaaFLT/001/da.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
	}
	//34
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
	//35
	@Test
	public void db(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-pIffQywZin/000/db.wgt");
		String start = locateStartFile(widget);
		assertEquals("index.htm",start);
	}
	//36
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

	//37
	@Test
	@Ignore
	public void dc(){
		// TODO needs to test start file location
	}
	@Test
	public void dv(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-paIabGIIMC/001/dv.wgt");
		assertFalse(err == null||err.equals(""));
	}	

	// 38 Start File Encoding
	@Test
	@Ignore
	public void e4(){
		// TODO needs to test start file encoding
	}
	@Test
	@Ignore
	public void e5(){
		// TODO needs to test start file encoding
	}
	@Test
	@Ignore
	public void e6(){
		// TODO needs to test start file encoding
	}
	@Test
	@Ignore
	public void e7(){
		// TODO needs to test start file encoding
	}

	//39
	@Test
	@Ignore
	public void e1(){
		// TODO needs to test features
	}
	//40
	@Test
	@Ignore
	public void df(){
		// TODO needs to test features
	}
	@Test
	@Ignore
	public void dr(){
		// TODO needs to test features
	}

	//41
	@Test
	public void d4(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-paWbGHyVrG/000/d4.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	//42
	@Test
	public void e8(){
		String err = processWidgetWithErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-vOBaOcWfll/000/e8.wgt");
		assertFalse(err == null||err.equals(""));
	}	
	//43
	@Test
	@Ignore
	public void d5(){
		// TODO needs to test features
	}
	//44
	@Test
	@Ignore
	public void dt(){
		// TODO needs to test features
	}
	@Test
	@Ignore
	public void dg(){
		// TODO needs to test features
	}	
	//45
	@Test
	@Ignore
	public void d6(){
		// TODO needs to test features
	}

	// 46 Feature
	@Test
	@Ignore
	public void e2(){
		// TODO needs to test features
	}
	@Test
	@Ignore
	public void e3(){
		// TODO needs to test features
	}

	//47 Start file and icons
	@Test
	public void aw(){
		Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-BnWPqNvNVo/000/aw.wgt");
		String start = locateStartFile(widget);
		assertEquals("pass.html",start);
		assertEquals("icon.png",getIcon(widget));
	}

	//48  Start file
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
		assertEquals("index.htm",start);
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
	// 49 Icons
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
		//Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/002/bl.wgt");
		// TODO requires an icons list = we just currently have one icon.
		fail("multiple icons not supported");
	}
	@Test
	public void bm(){
		//Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/003/bm.wgt");
		// TODO requires an icons list = we just currently have one icon.
		fail("multiple icons not supported");
	}
	@Test
	public void bn(){
		//Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/004/bn.wgt");
		// TODO requires an icons list = we just currently have one icon.
		fail("multiple icons not supported");
	}
	@Test
	public void bo(){
		//Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/005/bo.wgt");
		// TODO requires an icons list = we just currently have one icon.
		fail("multiple icons not supported");
	}
	@Test
	public void bp(){
		//Element widget = processWidgetNoErrors("http://samaxes.svn.beanstalkapp.com/widgets_compatibility_matrix/trunk/test-cases/ta-FAFYMEGELU/006/bp.wgt");
		// TODO requires an icons list = we just currently have one icon.
		fail("multiple icons not supported");
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
			if (error != null){
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
			e.printStackTrace();
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
		String iconUrl = widget.getChild("icon").getText();
		icon = StringUtils.difference(baseUrl,iconUrl);
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
