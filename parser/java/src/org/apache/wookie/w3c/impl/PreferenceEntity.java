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

import org.apache.wookie.w3c.IPreferenceEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: PreferenceEntity.java,v 1.3 2009-09-02 18:37:31 scottwilson Exp $
 */
public class PreferenceEntity extends ParamEntity implements IPreferenceEntity {
	
	private boolean fReadOnly;
	
	public PreferenceEntity() {
		super();
		fReadOnly = false;
	}

	public boolean isReadOnly() {
		return fReadOnly;
	}

	public void setReadOnly(boolean readOnly) {
		fReadOnly = readOnly;
	}

	public String getXMLTagName() {
		return IW3CXMLConfiguration.PREFERENCE_ELEMENT;
	}
	
	public void fromXML(Element element) throws BadManifestException {
		super.fromXML(element);
		String isReadOnly = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.READONLY_ATTRIBUTE));
		if (isReadOnly.equals("true")){
			fReadOnly = true;
		} else {
			fReadOnly = false;
		}	
	}
}
