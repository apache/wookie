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
package org.apache.wookie.w3c.updates;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.util.IRIValidator;
import org.jdom.Element;

/**
 * An UpdateDescription for a Widget
 */
public class UpdateDescription{
	
	private String _href;
	
	public UpdateDescription(){	
	}

	public UpdateDescription(String href){
		_href = href;
	}
	
	public String getHref() {
		return _href;
	}

	public void fromXML(Element element) throws BadManifestException {
		String href = element.getAttributeValue("href");
		if (href != null && href != "" && IRIValidator.isValidIRI(href)){
			_href = href;
		}
	}
	
	public Element toXML(){
		Element element = new Element(IW3CXMLConfiguration.UPDATE_ELEMENT, IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		element.setAttribute(IW3CXMLConfiguration.HREF_ATTRIBUTE, getHref());
		return element;
	}

}
