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

import org.apache.wookie.w3c.impl.AbstractLocalizedEntity;
import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;

/**
 * Test text-direction processing
 */
public class DirectionalityUtilsTest {
	
	@Test
	public void getDirectionalityRootElementDefault(){
		Element widget = new Element("widget");
		@SuppressWarnings("unused")
		Document document = new Document(widget);
		assertEquals(AbstractLocalizedEntity.getTextDirection(widget), AbstractLocalizedEntity.LEFT_TO_RIGHT);
	}
	
	@Test
	public void getDirectionalityRootElementOverride(){
		Element widget = new Element("widget");
		widget.setAttribute("dir","rtl");
		assertEquals(AbstractLocalizedEntity.getTextDirection(widget), AbstractLocalizedEntity.RIGHT_TO_LEFT);
	}
	
	@Test
	public void getDirectionalityChildElementDefault(){
		Element widget = new Element("widget");
		Element name = new Element("name");
		@SuppressWarnings("unused")
		Document document = new Document(widget);
		widget.addContent(name);
		assertEquals(AbstractLocalizedEntity.getTextDirection(name), AbstractLocalizedEntity.LEFT_TO_RIGHT);
	}

	@Test
	public void getDirectionalityChildElementOverrides(){
		Element widget = new Element("widget");
		Element name = new Element("name");
		name.setAttribute("dir", "rtl");
		widget.addContent(name);
		@SuppressWarnings("unused")
		Document document = new Document(widget);
		assertEquals(AbstractLocalizedEntity.getTextDirection(name), AbstractLocalizedEntity.RIGHT_TO_LEFT);
	}
	
	@Test
	public void getDirectionalityChildElementParentOverrides(){
		Element widget = new Element("widget");
		widget.setAttribute("dir","rtl");
		Element name = new Element("name");
		widget.addContent(name);
		assertEquals(AbstractLocalizedEntity.getTextDirection(name), AbstractLocalizedEntity.RIGHT_TO_LEFT);
	}
	
	@Test
	public void getDirectionalityChildElementOverridesWrongCase(){
		Element widget = new Element("widget");
		Element name = new Element("name");
		name.setAttribute("dir", "rTl");
		widget.addContent(name);
		@SuppressWarnings("unused")
		Document document = new Document(widget);
		assertEquals(AbstractLocalizedEntity.getTextDirection(name), AbstractLocalizedEntity.LEFT_TO_RIGHT);
	}
	
	@Test
	public void getDirectionalityChildElementParentOverridesWrongCase(){
		Element widget = new Element("widget");
		widget.setAttribute("dir","RTL");
		Element name = new Element("name");
		widget.addContent(name);
		@SuppressWarnings("unused")
		Document document = new Document(widget);
		assertEquals(AbstractLocalizedEntity.getTextDirection(name), AbstractLocalizedEntity.LEFT_TO_RIGHT);
	}
	
	@Test
	public void getDirectionalityRecursion1(){
		Element widget = new Element("widget");// RTL
		widget.setAttribute("dir","rtl"); 
		Element name = new Element("name"); // LTR
		name.setAttribute("dir","ltr");
		Element sub = new Element("sub"); // RTL
		sub.setAttribute("dir","rtl");
		widget.addContent(name);
		name.addContent(sub);
		assertEquals(AbstractLocalizedEntity.getTextDirection(sub), AbstractLocalizedEntity.RIGHT_TO_LEFT);
	}
	
	@Test
	public void getDirectionalityRecursion2(){
		Element widget = new Element("widget");
		widget.setAttribute("dir","rtl"); 
		Element name = new Element("name"); 
		Element sub = new Element("sub"); 
		widget.addContent(name);
		name.addContent(sub);
		assertEquals(AbstractLocalizedEntity.getTextDirection(sub), AbstractLocalizedEntity.RIGHT_TO_LEFT);
	}
	
	@Test
	public void getDirectionalityRecursion3(){
		Element widget = new Element("widget");
		widget.setAttribute("dir","ltr"); 
		Element name = new Element("name"); 
		widget.setAttribute("dir","rtl"); 
		Element sub = new Element("sub"); 
		widget.addContent(name);
		name.addContent(sub);
		assertEquals(AbstractLocalizedEntity.getTextDirection(sub), AbstractLocalizedEntity.RIGHT_TO_LEFT);
	}
}
