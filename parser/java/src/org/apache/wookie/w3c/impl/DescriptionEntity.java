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

import org.apache.wookie.w3c.IDescriptionEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.jdom.Element;
/**
 * The <description> element
 */
public class DescriptionEntity extends AbstractLocalizedEntity  implements IDescriptionEntity {
	
	private String fDescription;
	
	public DescriptionEntity(){
		fDescription = "";
		setLang(null);
	}
	
	public DescriptionEntity(String description, String language) {
		super();
		fDescription = description;
		setLang(language);
	}
	
	public String getDescription() {
		return fDescription;
	}
	
	public void setDescription(String description) {
		fDescription = description;
	}
	
	public void fromXML(Element element) {
		super.fromXML(element);
		fDescription = getLocalizedTextContent(element);
	}

	public Element toXml() {
		Element element = new Element(IW3CXMLConfiguration.DESCRIPTION_ELEMENT, IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		element.setText(getDescription());
		element = setLocalisationAttributes(element);
		return element;
	}


}
