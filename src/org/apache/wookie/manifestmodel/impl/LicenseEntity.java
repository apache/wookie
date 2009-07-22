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

import org.apache.wookie.manifestmodel.ILicenseEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.jdom.Element;
import org.jdom.Namespace;
/**
 * @author Paul Sharples
 * @version $Id: LicenseEntity.java,v 1.1 2009-07-22 09:31:30 scottwilson Exp $
 */
public class LicenseEntity implements ILicenseEntity {
	
	private String fLicenseText;
	private String fHref;
	private String fLanguage;
	
	public LicenseEntity(){
		fLicenseText = "";
		fHref = "";
		fLanguage = "";
	}
	
	public LicenseEntity(String licenseText, String href, String language) {
		super();
		fLicenseText = licenseText;
		fHref = href;
		fLanguage = language;
	}

	public String getLicenseText() {
		return fLicenseText;
	}

	public void setLicenseText(String licenseText) {
		fLicenseText = licenseText;
	}

	public String getHref() {
		return fHref;
	}

	public void setHref(String href) {
		fHref = href;
	}

	public String getLanguage() {
		return fLanguage;
	}

	public void setLanguage(String language) {
		fLanguage = language;
	}

	public String getTagName() {
		return IW3CXMLConfiguration.LICENSE_ELEMENT;
	}
	
	public void fromJDOM(Element element) {
		fLicenseText = element.getText();

		if(fLicenseText == null){					
			fLicenseText = "";
		}
		fHref = element.getAttributeValue(IW3CXMLConfiguration.HREF_ATTRIBUTE);
		if(fHref == null){
			fHref = "";
		}				
		fLanguage = element.getAttributeValue(IW3CXMLConfiguration.LANG_ATTRIBUTE, Namespace.XML_NAMESPACE);
		if(fLanguage == null){
			fLanguage = IW3CXMLConfiguration.DEFAULT_LANG;
		}		
	}


	
	

}
