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
import org.jdom.Namespace;
import org.tencompetence.widgetservice.manifestmodel.IDescriptionEntity;
import org.tencompetence.widgetservice.manifestmodel.IW3CXMLConfiguration;
/**
 * @author Paul Sharples
 * @version $Id: DescriptionEntity.java,v 1.1 2009-06-04 15:05:25 ps3com Exp $
 */
public class DescriptionEntity implements IDescriptionEntity {
	
	private String fDescription;
	private String fLanguage;
	
	public DescriptionEntity(){
		fDescription = "";
		fLanguage = "";
	}
	
	public DescriptionEntity(String description, String language) {
		super();
		fDescription = description;
		fLanguage = language;
	}
	
	public String getDescription() {
		return fDescription;
	}
	
	public void setDescription(String description) {
		fDescription = description;
	}
	
	public String getLanguage() {
		return fLanguage;
	}
	
	public void setLanguage(String language) {
		fLanguage = language;
	}
	
	public String getTagName() {
		return IW3CXMLConfiguration.DESCRIPTION_ELEMENT;
	}
	
	public void fromJDOM(Element element) {
		fDescription = element.getText();
		if(fDescription == null){					
			fDescription = "";
		}
		fLanguage = element.getAttributeValue(IW3CXMLConfiguration.LANG_ATTRIBUTE, Namespace.XML_NAMESPACE);
		if(fLanguage == null){
			fLanguage = IW3CXMLConfiguration.DEFAULT_LANG;
		}
	}


}
