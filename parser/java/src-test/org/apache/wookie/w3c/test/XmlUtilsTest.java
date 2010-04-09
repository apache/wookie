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

import org.jdom.Element;
import org.junit.Test;
import org.apache.wookie.w3c.util.XmlUtils;

public class XmlUtilsTest {
	
	@Test
	public void testClass(){
		@SuppressWarnings("unused")
		XmlUtils utils = new XmlUtils();
	}
	
	@Test
	public void nullContent(){
		assertEquals("",XmlUtils.getTextContent(null));
	}
	
	@Test
	public void singleElement(){
		Element element = new Element("test");
		assertEquals("",XmlUtils.getTextContent(element));
	}
	
	@Test
	public void nestedElements(){
		Element element = new Element("test");
		Element child = new Element("test2");
		element.addContent(child);
		assertEquals("",XmlUtils.getTextContent(element));
	}
	
	@Test
	public void singleElementWithContent(){
		Element element = new Element("test");
		element.setText("pass");
		assertEquals("pass",XmlUtils.getTextContent(element));
	}
	
	@Test
	public void nestedElementsWithContent(){
		Element element = new Element("test");
		element.setText("pa");
		Element child = new Element("test2");
		child.setText("ss");
		element.addContent(child);
		assertEquals("pass",XmlUtils.getTextContent(element));
	}
	
	

}
