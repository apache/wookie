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

import org.apache.wookie.manifestmodel.ILocalizedEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.util.LocalizationUtils;
import org.apache.wookie.util.UnicodeUtils;
import org.jdom.Element;
import org.jdom.Namespace;


public abstract class LocalizedEntity implements ILocalizedEntity {
	
	private String language;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	/**
	 * Checks whether the language tag for the entity is OK.
	 * A null value is OK, as is a BCP47 tag
	 */
	public boolean isValid(){
		if (getLanguage() == null) return true;
		if (LocalizationUtils.isValidLanguageTag(getLanguage())) return true;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.manifestmodel.IManifestModelBase#fromXML(org.jdom.Element)
	 */
	public void fromXML(Element element) {
		String lang =  UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.LANG_ATTRIBUTE, Namespace.XML_NAMESPACE));
		if (!lang.equals("")) setLanguage(lang);
	}

}
