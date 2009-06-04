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
import org.tencompetence.widgetservice.exceptions.BadManifestException;
import org.tencompetence.widgetservice.manifestmodel.IPreferenceEntity;
import org.tencompetence.widgetservice.manifestmodel.IW3CXMLConfiguration;
/**
 * @author Paul Sharples
 * @version $Id: PreferenceEntity.java,v 1.1 2009-06-04 15:05:25 ps3com Exp $
 */
public class PreferenceEntity extends ParamEntity implements IPreferenceEntity {
	
	private boolean fReadOnly;
	
	public PreferenceEntity() {
		super();
		fReadOnly = false;
	}

	public boolean isReadOnly() {
		return fReadOnly;
	}

	public void setReadOnly(boolean readOnly) {
		fReadOnly = readOnly;
	}

	public String getTagName() {
		return IW3CXMLConfiguration.PREFERENCE_ELEMENT;
	}
	
	public void fromJDOM(Element element) throws BadManifestException {
		super.fromJDOM(element);
		String isReadOnly = element.getAttributeValue(IW3CXMLConfiguration.READONLY_ATTRIBUTE);
		if(isReadOnly == null){
			fReadOnly = false;
		}
		else{
			try {				
				fReadOnly = Boolean.valueOf(isReadOnly);				
			} 
			catch (Exception ex) {
				fReadOnly = false;
			}
		}
		if (super.getName().equals("")){
			throw new BadManifestException("A <preference> element is declared in the manifest, but it has an empty name attribute.");
		}		
	}
}
