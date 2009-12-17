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

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang.StringUtils;
import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.exceptions.InvalidContentTypeException;
import org.apache.wookie.manifestmodel.IContentEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.util.UnicodeUtils;
import org.apache.wookie.util.WidgetPackageUtils;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: ContentEntity.java,v 1.3 2009-09-02 18:37:31 scottwilson Exp $
 */
public class ContentEntity extends AbstractLocalizedEntity implements IContentEntity {
	
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
	
	public void fromXML(Element element){

	}
	
	private static boolean isSupportedContentType(String atype){
		boolean supported = false;
		for (String type: IW3CXMLConfiguration.SUPPORTED_CONTENT_TYPES){
			if (StringUtils.equals(atype, type)) supported = true;
		}
		return supported;
	}

	public void fromXML(Element element, String[] locales, ZipFile zip) throws BadManifestException {
		fSrc = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.SOURCE_ATTRIBUTE));
		
		// Check custom icon file exists; remove the src value if it doesn't
		try {
			fSrc = WidgetPackageUtils.locateFilePath(fSrc,locales, zip);
			setLang(WidgetPackageUtils.languageTagForPath(fSrc));
		} catch (Exception e) {
			e.printStackTrace();
			fSrc = null;
		}

		fCharSet = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.CHARSET_ATTRIBUTE));
		if(fCharSet.equals("")){
			fCharSet = IW3CXMLConfiguration.DEFAULT_CHARSET;
		}
		fType = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.TYPE_ATTRIBUTE));
		if(fType.equals("")){
			fType = IW3CXMLConfiguration.DEFAULT_MEDIA_TYPE;
		} else {
			// If a type attribute is specified, and is either invalid or unsupported, we must treat it as an invalid widget
			if (!isSupportedContentType(fType)) throw new InvalidContentTypeException();
		}
		
	}

}
