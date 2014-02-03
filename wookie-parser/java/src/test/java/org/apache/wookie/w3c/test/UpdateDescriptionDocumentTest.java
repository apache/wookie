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

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.updates.InvalidUDDException;
import org.apache.wookie.w3c.updates.UpdateDescriptionDocument;
import org.jdom.Document;
import org.jdom.Element;
import org.junit.Test;

public class UpdateDescriptionDocumentTest {

	@Test
	public void createDocument() throws InvalidUDDException, MalformedURLException{
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument("test", new URL("http://incubator.apache.org/wookie"), "1.0");
		assertEquals("1.0", udd.getVersionTag());
		assertEquals("http://incubator.apache.org/wookie", udd.getUpdateSource().toString());
		assertEquals("test", udd.getDetails("en"));
	}
	
	@Test (expected=InvalidUDDException.class)
	public void emptyDocument() throws InvalidUDDException{
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(null, null, null);
		Document doc = new Document();
		udd.fromXML(doc);
	}
	
	@Test (expected=InvalidUDDException.class)
	public void nullDocument() throws InvalidUDDException{
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(null, null, null);
		udd.fromXML(null);
	}
	
	@Test (expected=InvalidUDDException.class)
	public void invalidRootDocument() throws InvalidUDDException{
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(null, null, null);
		Document doc = new Document();
		Element el = new Element("update", IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		doc.setRootElement(el);
		udd.fromXML(doc);
	}
	
	@Test (expected=InvalidUDDException.class)
	public void badNamespaceDocument() throws InvalidUDDException{
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(null, null, null);
		Document doc = new Document();
		Element el = new Element("update-info", "http://bogus.net");
		doc.setRootElement(el);
		udd.fromXML(doc);
	}
	
	@Test (expected=InvalidUDDException.class)
	public void missingVersionAttrDocument() throws InvalidUDDException{
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(null, null, null);
		Document doc = new Document();
		Element el = new Element("update-info", IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		el.setAttribute("src","http://incubator.apache.org/wookie");
		doc.setRootElement(el);
		udd.fromXML(doc);
	}
	
	@Test (expected=InvalidUDDException.class)
	public void missingSrcAttrDocument() throws InvalidUDDException{
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(null, null, null);
		Document doc = new Document();
		Element el = new Element("update-info", IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		el.setAttribute("version","1.0");
		doc.setRootElement(el);
		udd.fromXML(doc);
	}
	
	@Test (expected=InvalidUDDException.class)
	public void invalidSrcAttrDocument() throws InvalidUDDException{
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(null, null, null);
		Document doc = new Document();
		Element el = new Element("update-info", IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		el.setAttribute("version","1.0");
		el.setAttribute("src","!notavaliduri");
		doc.setRootElement(el);
		udd.fromXML(doc);
	}
	
	@Test
	public void validDocument() throws InvalidUDDException{
		UpdateDescriptionDocument udd = new UpdateDescriptionDocument(null, null, null);
		Document doc = new Document();
		Element el = new Element("update-info", IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		el.setAttribute("version","1.0");
		el.setAttribute("src","http://incubator.apache.org/wookie/test.wgt");
		Element details = new Element("details", IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		details.setText("test");
		el.addContent(details);
		doc.setRootElement(el);
		udd.fromXML(doc);
		assertEquals("1.0", udd.getVersionTag());
		assertEquals("http://incubator.apache.org/wookie/test.wgt", udd.getUpdateSource().toString());
		assertEquals("test", udd.getDetails("en"));
	}
	
  @Test
  public void exportDocument() throws InvalidUDDException{
    // Create document
    UpdateDescriptionDocument udd = new UpdateDescriptionDocument(null, null, null);
    Document doc = new Document();
    Element el = new Element("update-info", IW3CXMLConfiguration.MANIFEST_NAMESPACE);
    el.setAttribute("version","1.0");
    el.setAttribute("src","http://incubator.apache.org/wookie/test.wgt");
    Element details = new Element("details", IW3CXMLConfiguration.MANIFEST_NAMESPACE);
    details.setText("test");
    el.addContent(details);
    doc.setRootElement(el);
    udd.fromXML(doc);
    // Export to XML
    doc = new Document();
    doc.setRootElement(udd.toXml());
    // Read in again - should be a valid UDD
    udd.fromXML(doc);
    assertEquals("1.0", udd.getVersionTag());
    assertEquals("http://incubator.apache.org/wookie/test.wgt", udd.getUpdateSource().toString());
    assertEquals("test", udd.getDetails("en"));
  }
	
	@Test (expected = InvalidUDDException.class)
	public void missingRemoteDocument() throws InvalidUDDException, IOException{
		new UpdateDescriptionDocument("http://incubator.apache.org/wookie/nosuchdoc");
	}
	
	@Test (expected = InvalidUDDException.class)
	public void invalidRemoteDocument() throws InvalidUDDException, IOException{
		new UpdateDescriptionDocument("http://incubator.apache.org/wookie/index.data/wookie-overview-sm.png");
	}


}
