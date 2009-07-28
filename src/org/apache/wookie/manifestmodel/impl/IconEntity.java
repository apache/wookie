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

import org.apache.wookie.manifestmodel.IIconEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: IconEntity.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 */
public class IconEntity implements IIconEntity{
	
	private String fSrc;
	private int fHeight;
	private int fWidth;
	
	public IconEntity(){
		fSrc = "";
		fHeight = IW3CXMLConfiguration.DEFAULT_HEIGHT_SMALL;
		fWidth = IW3CXMLConfiguration.DEFAULT_WIDTH_SMALL;
	}
	
	public IconEntity(String src, int height, int width) {
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
	public int getHeight() {
		return fHeight;
	}
	public void setHeight(int height) {
		fHeight = height;
	}
	public int getWidth() {
		return fWidth;
	}
	public void setWidth(int width) {
		fWidth = width;
	}

	public String getTagName() {
		return IW3CXMLConfiguration.ICON_ELEMENT;
	}
	
	public void fromJDOM(Element element) {		
		// src is required
		fSrc = element.getAttributeValue(IW3CXMLConfiguration.SOURCE_ATTRIBUTE);
		// TODO - do we test that this file exists?
		if(fSrc==null){
			fSrc="";
		}
		// height is optional
		String tempHeight = element.getAttributeValue(IW3CXMLConfiguration.HEIGHT_ATTRIBUTE);
		if(tempHeight == null){
			fHeight = IW3CXMLConfiguration.DEFAULT_HEIGHT_SMALL;
		}
		else{
			fHeight = Integer.valueOf(tempHeight);
		}
		// width is optional
		String tempWidth = element.getAttributeValue(IW3CXMLConfiguration.WIDTH_ATTRIBUTE);
		if(tempWidth == null){
			fWidth = IW3CXMLConfiguration.DEFAULT_WIDTH_SMALL;
		}
		else{
			fWidth = Integer.valueOf(tempWidth);
		}	
	}


}
