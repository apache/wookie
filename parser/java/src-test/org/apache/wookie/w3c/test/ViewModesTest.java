package org.apache.wookie.w3c.test;

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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.wookie.w3c.W3CWidget;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Conformance tests for the ViewModes feature of W3C Widgets
 */
public class ViewModesTest extends ConformanceTest{
	
	@Test
	@Ignore // deprecated
	public void viewa(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-viewmodes/000/viewa.wgt");
		assertEquals("windowed",widget.getViewModes());
	}
	@Test
	public void viewb(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-viewmodes/001/viewb.wgt");
		assertTrue(widget.getViewModes().equals("floating"));
	}
	@Test
	@Ignore // deprecated
	public void viewc(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-viewmodes/002/viewc.wgt");
		assertTrue(widget.getViewModes().equals("fullscreen"));
	}
	@Test
	@Ignore // deprecated
	public void viewd(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-viewmodes/003/viewd.wgt");
		assertTrue(widget.getViewModes().equals("maximized"));
	}
	@Test
	@Ignore // deprecated
	public void viewe(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-viewmodes/004/viewe.wgt");
		assertTrue(widget.getViewModes().equals("minimized"));
	}
	@Test
	public void viewf(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-viewmodes/005/viewf.wgt");
		assertEquals("",widget.getViewModes());
	}
	@Test
	public void viewg(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-viewmodes/006/viewg.wgt");
		assertTrue(widget.getViewModes().equals("windowed floating maximized"));
	}
	@Test
	public void viewh(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-viewmodes/007/viewh.wgt");
		assertEquals("windowed floating",widget.getViewModes());
	}
	@Test
	public void viewi(){
		W3CWidget widget = processWidgetNoErrors("http://dev.w3.org/2006/waf/widgets/test-suite/test-cases/ta-viewmodes/008/viewi.wgt");
		assertTrue(widget.getViewModes().equals(""));
	}
	
}
