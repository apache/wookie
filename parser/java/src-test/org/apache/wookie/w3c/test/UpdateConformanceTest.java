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

import java.io.File;
import java.io.IOException;

import org.apache.wookie.w3c.W3CWidget;
import org.apache.wookie.w3c.W3CWidgetFactory;
import org.apache.wookie.w3c.updates.InvalidUDDException;
import org.apache.wookie.w3c.updates.UpdateDescriptionDocument;
import org.apache.wookie.w3c.updates.UpdateUtils;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class UpdateConformanceTest extends ConformanceTest{
	
	private static W3CWidgetFactory fac;
	
	@BeforeClass
	public static void setup() throws IOException{
		download = File.createTempFile("wookie-download", "wgt");
		output = File.createTempFile("wookie-output", "tmp");
		fac = new W3CWidgetFactory();
		fac.setLocalPath("http:localhost/widgets");
		fac.setFeatures(new String[]{"feature:a9bb79c1"});
		try {
			fac.setEncodings(new String[]{"UTF-8", "ISO-8859-1","Windows-1252"});
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (download.exists()) download.delete();
		if (output.exists()) output.delete();
		output.mkdir();
		fac.setOutputDirectory(output.getAbsolutePath());
	}
	
	
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
	
	@Ignore
	public void pr005(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/005/ta-pr-005.wgt");
		assertEquals(null, UpdateUtils.checkForUpdate(widget));
	}	
	
	@Test
	public void pr006(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/006/ta-pr-006.wgt");
		assertEquals(null, UpdateUtils.checkForUpdate(widget));
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
		assertNotNull(UpdateUtils.checkForUpdate(widget));
	}	
	
	@Test
	public void pr009(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/009/ta-pr-009.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
		assertNotNull(UpdateUtils.checkForUpdate(widget));
	}	
	
	@Test
	public void pr010(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/010/ta-pr-010.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
		assertNotNull(UpdateUtils.checkForUpdate(widget));
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
		assertNull(UpdateUtils.checkForUpdate(widget));
	}	
	
	@Test
	public void pr014(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-preparation1/014/ta-pr-014.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
		assertNotNull(UpdateUtils.checkForUpdate(widget));
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
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-if-modified-since", widget.getUpdate());
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
	
	@Test
	public void ac6() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition6/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-pass", widget.getUpdate());
		assertNotNull(UpdateUtils.checkForUpdate(widget));
	}

	@Test
	public void ac7() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition7/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-wrong-ct", widget.getUpdate());
		assertNull(UpdateUtils.checkForUpdate(widget));
	}
	
	@Test
	public void ac81() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition8/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-204", widget.getUpdate());
		assertNull(UpdateUtils.checkForUpdate(widget));
	}
	
	@Test
	public void ac82() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition8/002/ta-ac-002.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-304", widget.getUpdate());
		assertNull(UpdateUtils.checkForUpdate(widget));
	}
	
	@Test
	public void ac83() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition8/003/ta-ac-003.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-udi-204", widget.getUpdate());
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	@Test
	public void ac9() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition9/001/ta-ac-001.wgt");
		assertEquals("http://people.opera.com/harig/wupdres/resources/out.php?udd-206", widget.getUpdate());
		assertNull(UpdateUtils.checkForUpdate(widget));
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
	
	@Test
	public void ac103(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition10/003/ta-ac-003.wgt");
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	@Test
	public void ac104(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition10/004/ta-ac-004.wgt");
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	//TODO Requires manual runtime testing 
	@Ignore
	public void ac11(){
	}
	
	@Test
	public void ac12() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-acquisition12/001/ta-ac-001.wgt");
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}

	//TODO Requires manual testing - break the network connection during testing
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
	
	//TODO Requires runtime testing as the update changes the widget ID
	@Test
	public void pr203(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/003/ta-pr-003.wgt");
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}	
	
	@Test
	public void pr204() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/004/ta-pr-004.wgt");
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}	
	
	@Test
	public void pr205() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/005/ta-pr-005.wgt");
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}	
	
	@Test
	public void pr206(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/006/ta-pr-006.wgt");
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}	
	
	//Test error?
	@Test
	public void pr207(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/007/ta-pr-007.wgt");
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}		
	
	//TODO Requires runtime testing
	@Test
	public void pr208(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/008/ta-pr-008.wgt");
		assertNotNull(UpdateUtils.checkForUpdate(widget));
		System.out.println(UpdateUtils.checkForUpdate(widget).getUpdateSource());
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}		
	//TODO Requires runtime testing
	@Test
	public void pr209(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/009/ta-pr-009.wgt");
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	//TODO Requires runtime testing
	@Test
	public void pr210() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/010/ta-pr-010.wgt");
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}	
	
	//TODO Requires runtime testing
	@Test
	public void pr211(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/011/ta-pr-011.wgt");
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}		
	
	//TODO Requires runtime testing
	@Test
	public void pr212(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/012/ta-pr-012.wgt");
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}	
	
	//TODO Requires runtime testing
	@Test
	public void pr213(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/013/ta-pr-013.wgt");
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}	
	
	@Test
	public void pr214() {
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/014/ta-pr-014.wgt");
		System.out.println(UpdateUtils.checkForUpdate(widget).getUpdateSource());
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	//TODO Requires runtime testing
	@Test
	public void pr215(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/015/ta-pr-015.wgt");
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	//TODO Requires runtime testing
	@Test
	public void pr216(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/016/ta-pr-016.wgt");
		assertNotNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	@Test
	public void pr217() {
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/017/ta-pr-017.wgt");
		assertEquals("PASS", UpdateUtils.checkForUpdate(widget).getDetails("en"));
	}
	
	@Test
	public void pr218(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/018/ta-pr-018.wgt");
		assertTrue(UpdateUtils.checkForUpdate(widget).getDetails("en").contains("PASS"));
	}
	
	@Test
	public void pr219(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/019/ta-pr-019.wgt");
		assertEquals("PASS", UpdateUtils.checkForUpdate(widget).getDetails("en"));
	}
	
	@Test
	public void pr220() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/020/ta-pr-020.wgt");
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(widget.getUpdate());
		assertEquals("P A S S", udd.getDetails("en"));
	}
	
	@Test
	public void pr221(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing2/021/ta-pr-021.wgt");
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	@Test
	public void pr301(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing3/001/ta-pr-001.wgt");
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	@Test
	public void pr302() throws InvalidUDDException{
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-processing3/002/ta-pr-002.wgt");
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	@Test
	public void ve1(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-verifying4/001/ta-ve-001.wgt");
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}
	
	@Test
	public void ve2(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets-updates/test-suite/test-cases/ta-verifying4/002/ta-ve-002.wgt");
		assertNull(UpdateUtils.getUpdate(fac, widget));
	}	
}
