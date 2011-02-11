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

import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.lang.StringUtils;
import org.apache.wookie.w3c.IContentEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.exceptions.InvalidContentTypeException;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.apache.wookie.w3c.util.WidgetPackageUtils;
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
	
	public void fromXML(Element element){

	}

	public void fromXML(Element element, String[] locales, String[] encodings, ZipFile zip) throws BadManifestException {
		
		// Src
		fSrc = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.SOURCE_ATTRIBUTE));
		// Check file exists; remove the src value if it doesn't
		try {
			fSrc = WidgetPackageUtils.locateFilePath(fSrc,locales, zip);
			setLang(WidgetPackageUtils.languageTagForPath(fSrc));
		} catch (Exception e) {
			fSrc = null;
		}

		String charsetParameter = null;
		
		// Content Type
		fType = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.TYPE_ATTRIBUTE));
		if(fType.equals("")){
			fType = IW3CXMLConfiguration.DEFAULT_MEDIA_TYPE;
		} else {
			// Split the content type, as we may also have a charset parameter
			String[] type = fType.split(";");
			// If a type attribute is specified, and is either invalid or unsupported, we must treat it as an invalid widget
			if (!isSupported(type[0], IW3CXMLConfiguration.SUPPORTED_CONTENT_TYPES)) throw new InvalidContentTypeException("Content type is not supported");
			fType = type[0];
			// Get the charset parameter if present
			if (type.length > 1){
				String charset[] = type[type.length-1].split("=");
				charsetParameter = charset[charset.length-1];	
			}
		}
		
		// Charset encoding. Use encoding attribute by preference, and the use the charset parameter of the content type
		String charset = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.CHARSET_ATTRIBUTE));
		if (isSupported(charset, encodings)) setCharSet(charset);
		if (getCharSet()==null && isSupported(charsetParameter, encodings)) setCharSet(charsetParameter);		
		if (getCharSet()==null) setCharSet(IW3CXMLConfiguration.DEFAULT_CHARSET);
	}
	
	/**
	 * Checks to see if the supplied value is one of the supported values
	 * @param value
	 * @param supportedValues
	 * @return true if the value is one of the supported values
	 */
	private boolean isSupported(String value, String[] supportedValues){
		if (value == null) return false;
		boolean supported = false;
		for (String type: supportedValues){
			if (StringUtils.equals(value, type)) supported = true;
		}
		return supported;
	}

	public Element toXml() {
		Element contentElem = new Element(IW3CXMLConfiguration.CONTENT_ELEMENT,IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		contentElem.setAttribute(IW3CXMLConfiguration.SOURCE_ATTRIBUTE,getSrc());
		if (getType() != null) contentElem.setAttribute(IW3CXMLConfiguration.TYPE_ATTRIBUTE,getType());
		if (getCharSet() != null) contentElem.setAttribute(IW3CXMLConfiguration.CHARSET_ATTRIBUTE,getCharSet());
		
		contentElem = setLocalisationAttributes(contentElem);
		return contentElem;
	}

}
