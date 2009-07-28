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
import org.jdom.Element;
import org.jdom.Namespace;
/**
 * @author Paul Sharples
 * @version $Id: DescriptionEntity.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 */
public class DescriptionEntity implements IDescriptionEntity {
	
	private String fDescription;
	private String fLanguage;
	
	public DescriptionEntity(){
		fDescription = "";
		fLanguage = "";
	}
	
	public DescriptionEntity(String description, String language) {
		super();
		fDescription = description;
		fLanguage = language;
	}
	
	public String getDescription() {
		return fDescription;
	}
	
	public void setDescription(String description) {
		fDescription = description;
	}
	
	public String getLanguage() {
		return fLanguage;
	}
	
	public void setLanguage(String language) {
		fLanguage = language;
	}
	
	public String getTagName() {
		return IW3CXMLConfiguration.DESCRIPTION_ELEMENT;
	}
	
	public void fromJDOM(Element element) {
		fDescription = element.getText();
		if(fDescription == null){					
			fDescription = "";
		}
		fLanguage = element.getAttributeValue(IW3CXMLConfiguration.LANG_ATTRIBUTE, Namespace.XML_NAMESPACE);
		if(fLanguage == null){
			fLanguage = IW3CXMLConfiguration.DEFAULT_LANG;
		}
	}


}
