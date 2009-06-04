/*
 * Copyright (c) 2007, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.widgetservice.manifestmodel.impl;

import org.jdom.Element;
import org.tencompetence.widgetservice.manifestmodel.IIconEntity;
import org.tencompetence.widgetservice.manifestmodel.IW3CXMLConfiguration;
/**
 * @author Paul Sharples
 * @version $Id: IconEntity.java,v 1.1 2009-06-04 15:05:25 ps3com Exp $
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
