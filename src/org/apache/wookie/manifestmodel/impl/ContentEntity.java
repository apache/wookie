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
package org.apache.wookie.manifestmodel.impl;

import org.apache.wookie.manifestmodel.IContentEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: ContentEntity.java,v 1.1 2009-07-22 09:31:30 scottwilson Exp $
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
	
	public String getTagName() {
		return IW3CXMLConfiguration.CONTENT_ELEMENT;
	}
	
	public void fromJDOM(Element element) {				
		fSrc = element.getAttributeValue(IW3CXMLConfiguration.SOURCE_ATTRIBUTE);
		if(fSrc == null){
			fSrc = IW3CXMLConfiguration.DEFAULT_SRC_PAGE;
		}
		// just in case it's there, but they leave it empty
		if(fSrc == ""){
			fSrc = IW3CXMLConfiguration.DEFAULT_SRC_PAGE;
		}
		fCharSet = element.getAttributeValue(IW3CXMLConfiguration.CHARSET_ATTRIBUTE);
		if(fCharSet == null){
			fCharSet = IW3CXMLConfiguration.DEFAULT_CHARSET;
		}
		fType = element.getAttributeValue(IW3CXMLConfiguration.TYPE_ATTRIBUTE);
		if(fType == null){
			fType = IW3CXMLConfiguration.DEFAULT_MEDIA_TYPE;
		}	
	}	

}
