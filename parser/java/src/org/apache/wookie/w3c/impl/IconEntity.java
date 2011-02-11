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
import org.apache.wookie.w3c.IIconEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.apache.wookie.w3c.util.ContentTypeUtils;
import org.apache.wookie.w3c.util.NumberUtils;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.apache.wookie.w3c.util.WidgetPackageUtils;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: IconEntity.java,v 1.3 2009-09-02 18:37:31 scottwilson Exp $
 */
public class IconEntity extends AbstractLocalizedEntity implements IIconEntity{
	
	private String fSrc;
	private Integer fHeight;
	private Integer fWidth;
	
	public IconEntity(){
		fSrc = "";
		fHeight = null;
		fWidth = null;
	}
	
	public IconEntity(String src, Integer height, Integer width) {
		super();
		fSrc = src;
		fHeight = height;
		fWidth = width;
	}
	
	public String getSrc() {
		return fSrc;
	}
	public void setSrc(String src) {
		fSrc = src;
	}
	public Integer getHeight() {
		return fHeight;
	}
	public void setHeight(Integer height) {
		fHeight = height;
	}
	public Integer getWidth() {
		return fWidth;
	}
	public void setWidth(Integer width) {
		fWidth = width;
	}
	
	public void fromXML(Element element){
	}
	
	public void fromXML(Element element,String[] locales, ZipFile zip) throws BadManifestException{		
		// src is required
		fSrc = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.SOURCE_ATTRIBUTE));

		// Check custom icon file exists; remove the src value if it doesn't
		try {
			fSrc = WidgetPackageUtils.locateFilePath(fSrc,locales, zip);
			setLang(WidgetPackageUtils.languageTagForPath(fSrc));
		} catch (Exception e) {
			fSrc = null;
		}
		try {
			if (!ContentTypeUtils.isSupportedImageType(fSrc)) fSrc = null;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// height is optional
		String height  = element.getAttributeValue(IW3CXMLConfiguration.HEIGHT_ATTRIBUTE);
		if(height != null){
			try { 
				fHeight = NumberUtils.processNonNegativeInteger(height); 
			} catch (NumberFormatException e) { 
				// Not a valid number - pass through without setting 
			} 
		}

		// width is optional
		String width  = element.getAttributeValue(IW3CXMLConfiguration.WIDTH_ATTRIBUTE);
		if(width != null){
			try {
				fWidth = NumberUtils.processNonNegativeInteger(width); 
			} catch (NumberFormatException e) {
				// Not a valid number - pass through without setting 
			}
		}
	}

	public Element toXml() {
		Element element = new Element(IW3CXMLConfiguration.ICON_ELEMENT, IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		element.setAttribute(IW3CXMLConfiguration.SOURCE_ATTRIBUTE, getSrc());
		if (getHeight() != null && getHeight() > 0) element.setAttribute(IW3CXMLConfiguration.HEIGHT_ATTRIBUTE,String.valueOf(getHeight()));
		if (getWidth() != null && getWidth() > 0) element.setAttribute(IW3CXMLConfiguration.WIDTH_ATTRIBUTE,String.valueOf(getWidth()));
		return element;
	}


}
