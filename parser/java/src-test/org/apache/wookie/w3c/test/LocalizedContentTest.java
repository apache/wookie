package org.apache.wookie.w3c.test;

import static org.junit.Assert.*;

import org.apache.wookie.w3c.impl.AbstractLocalizedEntity;
import org.jdom.Element;
import org.junit.Test;

/**
 * Tests localized content extraction features
 */
public class LocalizedContentTest {

	@Test
	public void testLocalizedContent(){
		Element test = new Element("test");
		test.setText("hello ");
		Element world = new Element("span");
		world.setText("welt");
		world.setAttribute("lang","de");
		test.addContent(world);
		assertEquals("hello <span dir=\"ltr\" xml:lang=\"de\">welt</span>",AbstractLocalizedEntity.getLocalizedTextContent(test));
	}
	
	@Test
	public void testLocalizedContent2(){
		Element test = new Element("test");
		test.setText("hello ");
		
		Element good = new Element("span");
		good.setText("bon");
		good.setAttribute("lang","fr");
		test.addContent(good);
		
		Element world = new Element("span");
		world.setText("welt");
		world.setAttribute("lang","de");
		test.addContent(world);
		
		assertEquals("hello <span dir=\"ltr\" xml:lang=\"fr\">bon</span><span dir=\"ltr\" xml:lang=\"de\">welt</span>",AbstractLocalizedEntity.getLocalizedTextContent(test));
	}
	
	@Test
	public void testLocalizedContent3(){
		Element test = new Element("test");
		test.setText("hello ");
		
		// Should strip the element but keep the content
		Element good = new Element("fail");
		good.setText("PASS");
		good.setAttribute("lang","fr");
		test.addContent(good);
		
		Element world = new Element("span");
		world.setText("welt");
		world.setAttribute("lang","de");
		test.addContent(world);
		
		assertEquals("hello PASS<span dir=\"ltr\" xml:lang=\"de\">welt</span>",AbstractLocalizedEntity.getLocalizedTextContent(test));
	}
	
	@Test
	public void testNestedLocalizedContent(){
		Element test = new Element("test");
		test.setText("left ");
		
		Element rtl = new Element("span");
		rtl.setText("right");
		rtl.setAttribute("dir","rtl");
		test.addContent(rtl);
		
		Element ltr = new Element("span");
		ltr.setText("left");
		ltr.setAttribute("dir","ltr");
		rtl.addContent(ltr);
		
		assertEquals("left <span dir=\"rtl\">right<span dir=\"ltr\">left</span></span>",AbstractLocalizedEntity.getLocalizedTextContent(test));
	}
}
