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

import org.apache.wookie.w3c.ILicenseEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.util.UnicodeUtils;

import org.jdom.Element;
/**
 * the <license> element
 */
public class LicenseEntity extends AbstractLocalizedEntity implements ILicenseEntity {
	
	private String fLicenseText;
	private String fHref;
	
	public LicenseEntity(){
		fLicenseText = "";
	}
	
	public LicenseEntity(String licenseText, String href, String language, String dir) {
		super();
		fLicenseText = licenseText;
		fHref = href;
		setDir(dir);
		setLang(language);
	}

	public String getLicenseText() {
		return fLicenseText;
	}

	public void setLicenseText(String licenseText) {
		fLicenseText = licenseText;
	}

	public String getHref() {
		return fHref;
	}

	public void setHref(String href) {
		fHref = href;
	}
	
	public void fromXML(Element element) {
		super.fromXML(element);
		fLicenseText = getLocalizedTextContent(element);
		fHref = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.HREF_ATTRIBUTE));		
		if (fHref.equals("")) fHref = null;
	}

}
