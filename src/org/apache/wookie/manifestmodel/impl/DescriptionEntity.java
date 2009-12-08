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

package org.apache.wookie.manifestmodel.impl;

import org.apache.wookie.manifestmodel.IDescriptionEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.util.UnicodeUtils;
import org.apache.wookie.util.XmlUtils;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: DescriptionEntity.java,v 1.3 2009-09-02 18:37:31 scottwilson Exp $
 */
public class DescriptionEntity extends LocalizedEntity  implements IDescriptionEntity {
	
	private String fDescription;
	
	public DescriptionEntity(){
		fDescription = "";
		setLanguage("");
	}
	
	public DescriptionEntity(String description, String language) {
		super();
		fDescription = description;
		setLanguage(language);
	}
	
	public String getDescription() {
		return fDescription;
	}
	
	public void setDescription(String description) {
		fDescription = description;
	}
	
	public String getXMLTagName() {
		return IW3CXMLConfiguration.DESCRIPTION_ELEMENT;
	}
	
	public void fromXML(Element element) {
		super.fromXML(element);
		fDescription = UnicodeUtils.normalizeWhitespace(XmlUtils.getTextContent(element));
	}


}
