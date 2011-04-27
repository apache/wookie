package org.apache.wookie.w3c.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.updates.InvalidUDDException;
import org.apache.wookie.w3c.updates.UpdateDescriptionDocument;
import org.junit.Ignore;
import org.junit.Test;

public class UpdateConformanceTest extends ConformanceTest{
	
	
	/**
	 * This first set of tests checks that we process the update description element OK in the config.xml
	 */
	
	@Test
	public void pr001(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/001/ta-pr-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
	}	

	@Test
	// This test is incorrect
	public void pr002(){
		String err = processWidgetWithErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/002/ta-pr-002.wgt");
		assertFalse(err == null||err.equals(""));
	}
	
	@Test
	public void pr003(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/003/ta-pr-003.wgt");
		assertNull(widget.getUpdate());
	}
	
	
	@Test
	public void pr004(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/004/ta-pr-004.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
	}	
	
	// Requires runtime testing
	@Ignore
	public void pr005(){
	}	
	
	@Test
	public void pr006(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/006/ta-pr-006.wgt");
		assertEquals(null, widget.getUpdate());
	}	
	
	@Test
	public void pr007(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/007/ta-pr-007.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
	}	
	
	// Error in test
	@Test
	public void pr008(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/008/ta-pr-008.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
	}	
	
	@Test
	public void pr009(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/009/ta-pr-009.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
	}	
	
	@Test
	public void pr010(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/010/ta-pr-010.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
	}	
	
	@Test
	public void pr011(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/011/ta-pr-011.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
	}	
	
	@Test
	public void pr012(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/012/ta-pr-012.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
	}	
	
	@Test
	public void pr013(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/013/ta-pr-013.wgt");
		assertEquals(null, widget.getUpdate());
	}	
	
	@Test
	public void pr014(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/014/ta-pr-014.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
	}	
	
	/**
	 * This set of tests checks the acquisition of the UDD. These should also be tested in the server at runtime using functional testing.
	 */
	// Requires runtime testing
	@Test
	public void ac1(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition1/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-if-none-match", widget.getUpdate());
	}
	// Requires runtime testing
	@Test
	public void ac2(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition2/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?check-if-modified-since", widget.getUpdate());
	}
	// Requires runtime testing
	@Test
	public void ac3(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition3/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-accept-language", widget.getUpdate());
	}
	// Requires runtime testing
	@Test
	public void ac4(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition4/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-cache-control=must-revalidate", widget.getUpdate());
		widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition4/002/ta-ac-002.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-cache-control=no-cache", widget.getUpdate());
		widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition4/003/ta-ac-003.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pragma=no-cache", widget.getUpdate());
	}
	
	// Requires runtime testing
	@Test
	public void ac5(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition5/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-expires-in=300", widget.getUpdate());
	}
	
	// Requires runtime testing
	@Test
	public void ac6() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition6/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}

	@Test(expected=InvalidUDDException.class)
	public void ac7() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition7/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-wrong-ct", widget.getUpdate());
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	@Test(expected=InvalidUDDException.class)
	public void ac81() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition8/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-204", widget.getUpdate());
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	@Test(expected=InvalidUDDException.class)
	public void ac82() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition8/002/ta-ac-002.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-304", widget.getUpdate());
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	// Requires runtime testing - this just checks we parse the UDD OK
	@Test
	public void ac83() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition8/003/ta-ac-003.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-udi-204", widget.getUpdate());
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	@Test(expected=InvalidUDDException.class)
	public void ac9() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition9/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-206", widget.getUpdate());
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	@Test
	public void ac101() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition10/001/ta-ac-001.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}

	@Test
	public void ac102() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition10/002/ta-ac-002.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	// Requires runtime testing
	@Test
	public void ac103() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition10/003/ta-ac-003.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	// Requires runtime testing
	@Test
	public void ac104() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition10/004/ta-ac-004.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	//Requires manual runtime testing 
	@Ignore
	public void ac11(){
	}
	
	@Test(expected=InvalidUDDException.class)
	public void ac12() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition12/001/ta-ac-001.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}

	//Requires manual testing - break the network connection during testing
	@Ignore
	@Test(expected=InvalidUDDException.class)
	public void ac13() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition13/001/ta-ac-001.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	/**
	 * Tests processing the UDD
	 */
	
	@Test
	public void pr1(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing1/001/ta-pr-001.wgt");
		assertNull(widget.getUpdate());
	}	
	
	@Test(expected=InvalidUDDException.class)
	public void pr201() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/001/ta-pr-001.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}	
	@Test(expected=InvalidUDDException.class)
	public void pr202() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/002/ta-pr-002.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}	
	
	//Requires runtime testing - this changes the ID!
	//Test error?
	@Test
	public void pr203() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/003/ta-pr-003.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}	
	
	@Test(expected=InvalidUDDException.class)
	public void pr204() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/004/ta-pr-004.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}	
	
	//Requires runtime testing
	@Test
	public void pr205() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/005/ta-pr-005.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}	
	
	//Requires runtime testing
	@Test
	public void pr206() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/006/ta-pr-006.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}	
	
	//Requires runtime testing
	//Test error?
	@Test
	public void pr207() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/007/ta-pr-007.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}		
	
	//Requires runtime testing
	@Test
	public void pr208() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/008/ta-pr-008.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}		
	
	//Requires runtime testing
	@Test
	public void pr209() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/009/ta-pr-009.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	//Requires runtime testing
	@Test
	public void pr210() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/010/ta-pr-010.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}	
	
	//Requires runtime testing
	@Test
	public void pr211() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/011/ta-pr-011.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}		
	
	//Requires runtime testing
	@Test
	public void pr212() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/012/ta-pr-012.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}	
	
	//Requires runtime testing
	@Test
	public void pr213() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/013/ta-pr-013.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}	
	
	@Test(expected=InvalidUDDException.class)
	public void pr214() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/014/ta-pr-014.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	//Requires runtime testing
	@Test
	public void pr215() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/015/ta-pr-015.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	//Requires runtime testing
	//Note that we throw an exception when a UDD has a relative src attr, which I think is the correct behaviour - this
	//test seems to assume you just silently fail to update the widget?
	@Test(expected=InvalidUDDException.class)
	public void pr216() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/016/ta-pr-016.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	//Requires runtime testing
	@Test
	public void pr217() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/017/ta-pr-017.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	@Test
	public void pr218() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/018/ta-pr-018.wgt");
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
		assertTrue(udd.getDetails("en").contains("PASS"));
	}
	
	@Test
	public void pr219() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/019/ta-pr-019.wgt");
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
		assertEquals("PASS", udd.getDetails("en"));
	}
	
	@Test
	public void pr220() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/020/ta-pr-020.wgt");
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
		assertEquals("P A S S", udd.getDetails("en"));
	}
	
	//Requires runtime testing
	@Test
	public void pr221() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/021/ta-pr-021.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	@Test(expected=InvalidUDDException.class)
	public void pr301() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing3/001/ta-pr-001.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	@Test(expected=InvalidUDDException.class)
	public void pr302() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing3/002/ta-pr-002.wgt");
		@SuppressWarnings("unused")
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
	}
	
	//Requires runtime testing
	@Ignore
	public void ve1(){}
	
	//Requires runtime testing
	@Ignore
	public void ve2(){}	
}
