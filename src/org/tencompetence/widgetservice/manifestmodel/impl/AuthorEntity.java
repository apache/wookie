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
import org.tencompetence.widgetservice.manifestmodel.IAuthorEntity;
import org.tencompetence.widgetservice.manifestmodel.IW3CXMLConfiguration;
/**
 * @author Paul Sharples
 * @version $Id: AuthorEntity.java,v 1.1 2009-06-04 15:05:25 ps3com Exp $
 */
public class AuthorEntity implements IAuthorEntity {
	
	private String fAuthorName;
	private String fHref;
	private String fEmail;
	
	public AuthorEntity(){
		fAuthorName = "";
		fHref = "";
		fEmail = "";
	}
	
	public AuthorEntity(String authorName, String href, String email) {
		super();
		fAuthorName = authorName;
		fHref = href;
		fEmail = email;
	}
	
	public String getAuthorName() {
		return fAuthorName;
	}
	
	public void setAuthorName(String authorName) {
		fAuthorName = authorName;
	}
	
	public String getHref() {
		return fHref;
	}
	
	public void setHref(String href) {
		fHref = href;
	}
	
	public String getEmail() {
		return fEmail;
	}
	
	public void setEmail(String email) {
		fEmail = email;
	}
	
	public String getTagName() {
		return IW3CXMLConfiguration.AUTHOR_ELEMENT;
	}
	
	public void fromJDOM(Element element) {
		fAuthorName = element.getText();		
		if(fAuthorName == null){					
			fAuthorName = "";
		}
		fHref = element.getAttributeValue(IW3CXMLConfiguration.HREF_ATTRIBUTE);
		if(fHref == null){
			fHref = "";
		}				
		fEmail = element.getAttributeValue(IW3CXMLConfiguration.EMAIL_ATTRIBUTE);
		if(fEmail == null){
			fEmail = "";
		}
		
	}

}
