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

import org.apache.wookie.manifestmodel.IContentEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.util.UnicodeUtils;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: ContentEntity.java,v 1.3 2009-09-02 18:37:31 scottwilson Exp $
 */
public class ContentEntity implements IContentEntity {
	
	private String fSrc;
	private String fCharSet;
	private String fType;
	
	public ContentEntity(){
		fSrc = "";
		fCharSet = "";
		fType = "";
	}
	
	public ContentEntity(String src, String charSet, String type) {
		super();
		fSrc = src;
		fCharSet = charSet;
		fType = type;
	}

	public String getSrc() {
		return fSrc;
	}

	public void setSrc(String src) {
		fSrc = src;
	}

	public String getCharSet() {
		return fCharSet;
	}

	public void setCharSet(String charSet) {
		fCharSet = charSet;
	}

	public String getType() {
		return fType;
	}

	public void setType(String type) {
		fType = type;
	}
	
	public String getXMLTagName() {
		return IW3CXMLConfiguration.CONTENT_ELEMENT;
	}
	
	public void fromXML(Element element) {				
		fSrc = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.SOURCE_ATTRIBUTE));
		// just in case it's there, but they leave it empty
		if(fSrc.equals("")){
			fSrc = IW3CXMLConfiguration.DEFAULT_SRC_PAGE;
		}
		fCharSet = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.CHARSET_ATTRIBUTE));
		if(fSrc.equals("")){
			fCharSet = IW3CXMLConfiguration.DEFAULT_CHARSET;
		}
		fType = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.TYPE_ATTRIBUTE));
		if(fSrc.equals("")){
			fType = IW3CXMLConfiguration.DEFAULT_MEDIA_TYPE;
		}	
	}	

}
