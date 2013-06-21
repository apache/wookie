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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.updates.UpdateDescription;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

/**
 * Tests for Update Description. These should be replaced at some point by official W3C test cases.
 */
public class UpdateDescriptionTest {
	
	@Test
	public void create(){
		UpdateDescription desc = new UpdateDescription();
		Element update = new Element(IW3CXMLConfiguration.UPDATE_ELEMENT, Namespace.getNamespace(IW3CXMLConfiguration.MANIFEST_NAMESPACE));
		update.setAttribute(IW3CXMLConfiguration.HREF_ATTRIBUTE, "http://localhost");
		try {
			desc.fromXML(update);
		} catch (BadManifestException e) {
			fail();
		}
		assertEquals("http://localhost", desc.getHref());
	}
	
	@Test
	public void createInvalidURL(){
		UpdateDescription desc = new UpdateDescription();
		Element update = new Element(IW3CXMLConfiguration.UPDATE_ELEMENT, Namespace.getNamespace(IW3CXMLConfiguration.MANIFEST_NAMESPACE));
		update.setAttribute(IW3CXMLConfiguration.HREF_ATTRIBUTE, "notavalidurl!");
		try {
			desc.fromXML(update);
		} catch (BadManifestException e) {
			fail();
		}
		assertNull(desc.getHref());
	}

	@Test
	public void export(){
	   UpdateDescription desc = new UpdateDescription("http://localhost");
	   Element el = desc.toXML();
	   assertEquals("http://localhost", el.getAttributeValue(IW3CXMLConfiguration.HREF_ATTRIBUTE));
	   
	}
}
