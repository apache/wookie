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
import org.tencompetence.widgetservice.manifestmodel.IAccessEntity;
import org.tencompetence.widgetservice.manifestmodel.IW3CXMLConfiguration;
/**
 * @author Paul Sharples
 * @version $Id: AccessEntity.java,v 1.1 2009-06-04 15:05:25 ps3com Exp $
 */
public class AccessEntity implements IAccessEntity {
	
	private String fUri;
	private boolean fSubDomains;
	
	public AccessEntity(){
		fUri = "";
		fSubDomains = false;
	}
	
	public AccessEntity(String uri, boolean subDomains) {
		super();
		fUri = uri;
		fSubDomains = subDomains;
	}
	
	public String getUri() {
		return fUri;
	}
	public void setUri(String uri) {
		fUri = uri;
	}
	public boolean hasSubDomains() {
		return fSubDomains;
	}

	public void setSubDomains(boolean subDomains) {
		fSubDomains = subDomains;
	}

	public String getTagName() {
		return IW3CXMLConfiguration.ACCESS_ELEMENT;
	}
	
	public void fromJDOM(Element element) {		
		fUri = element.getAttributeValue(IW3CXMLConfiguration.URI_ATTRIBUTE);
		//TODO this is required, but may need to be checked
		if(fUri == null){
			fUri = "";
		}
		String subDomains = element.getAttributeValue(IW3CXMLConfiguration.SUBDOMAINS_ATTRIBUTE);
		if(subDomains == null){
			fSubDomains = false;
		}
		else{
			try {
				fSubDomains = Boolean.valueOf(subDomains);
			} 
			catch (Exception e) {
				fSubDomains = false;
			}
		}	
	}
}
