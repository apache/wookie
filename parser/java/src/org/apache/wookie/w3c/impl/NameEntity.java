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

package org.apache.wookie.w3c.impl;

import org.apache.wookie.w3c.INameEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.jdom.Element;

/**
 * the <name> element
 */
public class NameEntity extends AbstractLocalizedEntity implements INameEntity {

	private String fName;
	private String fShort;

	
	public NameEntity(){
		fName = "";
		fShort = "";
		setLang(null);
	}
	
	public NameEntity(String name, String short1, String language) {
		super();
		fName = name;
		fShort = short1;
		setLang(language);
	}
	
	public String getName() {
		return fName;
	}
	
	public void setName(String name) {
		fName = name;
	}
	
	public String getShort() {
		return fShort;
	}
	
	public void setShort(String short1) {
		fShort = short1;
	}
	
	public void fromXML(Element element) {		
		super.fromXML(element);
		// Get the text value of name
		fName = getLocalizedTextContent(element);
		// Get the short attribute (if exists)
		fShort = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.SHORT_ATTRIBUTE));
	}
	
	public Element toXml() {
		Element nameElem = new Element(IW3CXMLConfiguration.NAME_ELEMENT, IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		nameElem.setText(getName());
		if (getShort() != null && getShort().length() > 0) nameElem.setAttribute(IW3CXMLConfiguration.SHORT_ATTRIBUTE, getShort());
		nameElem = setLocalisationAttributes(nameElem);
		return nameElem;
	}

}
