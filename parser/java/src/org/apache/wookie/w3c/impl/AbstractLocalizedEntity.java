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

import org.apache.wookie.w3c.ILocalizedEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.jdom.Element;
import org.jdom.Namespace;


public abstract class AbstractLocalizedEntity implements ILocalizedEntity {
	
	/**
	 * a Language string conforming to BCP47
	 */
	protected String lang;
	/**
	 * Text direction conforming to http://www.w3.org/TR/2007/REC-its-20070403/
	 */
	protected String dir;
	
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public String getDir() {
		return dir;
	}
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	/**
	 * Checks whether the language tag for the entity is OK.
	 * A null value is OK, as is a BCP47 tag
	 */
	public boolean isValid(){
		if (getLang() == null) return true;
		if (LocalizationUtils.isValidLanguageTag(getLang())) return true;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.manifestmodel.IManifestModelBase#fromXML(org.jdom.Element)
	 */
	public void fromXML(Element element) {
		String lang =  UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.LANG_ATTRIBUTE, Namespace.XML_NAMESPACE));
		if (!lang.equals("")) setLang(lang);
		dir = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.DIR_ATRRIBUTE));
		if (dir.equals("")) dir = null;
	}

}
