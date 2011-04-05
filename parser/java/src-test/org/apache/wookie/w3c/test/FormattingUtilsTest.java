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

import org.apache.wookie.w3c.impl.NameEntity;
import org.apache.wookie.w3c.util.FormattingUtils;
import org.junit.Test;

/**
 * Tests for some simple cases of encoding text containing direction instructions
 * 
 * Some of these tests are derived from examples ones used in http://dev.w3.org/2006/waf/widgets-api/#getting-localizable-strings-
 * 
 * @author scottbw@apache.org
 *
 */
public class FormattingUtilsTest {
	
	@Test
	public void name(){
		String name = "םפל<span dir=\"lro\">חל</span>ק";
		String expected = "\u202A\u05DD\u05E4\u05DC\u202D\u05D7\u05DC\u202C\u05E7\u202C";
		String expected2 = "\u05DD\u05E4\u05DC\u202D\u05D7\u05DC\u202C\u05E7";
		String dir = null;
		assertEquals(expected.length(), FormattingUtils.getEncoded(dir,name).length());
		assertEquals(expected, FormattingUtils.getEncoded(dir,name));
	}
	
	@Test
	public void version(){
		String version = "DESSAP";
		String dir = "rlo";
		assertEquals("\u202EDESSAP\u202C", FormattingUtils.getEncoded(dir,version));		
	}
	
	@Test
	public void noDirectionEncoded(){
		NameEntity name = new NameEntity();
		name.setName("Hello world");
		assertEquals("Hello world", FormattingUtils.getEncoded(name.getDir(),name.getName()));
		assertEquals(11, FormattingUtils.getFormattedWidgetName(name).length());
	}	
	
	@Test
	public void noDirectionFormatted(){
		NameEntity name = new NameEntity();
		name.setDir("ltr");
		name.setName("Hello world");
		assertEquals("Hello world", FormattingUtils.getFormattedWidgetName(name));
	}
	
	@Test
	public void noDirection(){
		assertEquals("Hello World", FormattingUtils.getEncoded(null, "Hello World"));
	}

	@Test
	public void ltr(){
		assertEquals("Hello World", FormattingUtils.getEncoded("ltr", "Hello World"));
	}
	
	@Test
	public void rtl(){
		assertEquals("\u202bHello World\u202c", FormattingUtils.getEncoded("rtl", "Hello World"));
	}
	
	@Test
	public void rlo(){
		assertEquals("\u202eHello World\u202c", FormattingUtils.getEncoded("rlo", "Hello World"));
	}
	
	@Test
	public void lro(){
		assertEquals("\u202dHello World\u202c", FormattingUtils.getEncoded("lro", "Hello World"));
	}
	
	@Test
	public void embeddedOnly(){
		assertEquals("\u202aGoodbye \u202bleurc\u202c World\u202c", FormattingUtils.getEncoded(null, "Goodbye <span dir=\"rtl\">leurc</span> World"));
	}
	
	@Test
	public void rtlembedded(){
		assertEquals("\u202bGoodbye \u202bleurc\u202c World\u202c", FormattingUtils.getEncoded("rtl", "Goodbye <span dir=\"rtl\">leurc</span> World"));
	}
	
	@Test
	public void ltrembedded(){
		assertEquals("\u202A\u202EolleH\u202C\u202C", FormattingUtils.getEncoded("ltr", "<span dir=\"rlo\">olleH</span>"));
	}
}
