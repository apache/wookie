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

import java.util.ArrayList;
import java.util.List;

import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.manifestmodel.IFeatureEntity;
import org.apache.wookie.manifestmodel.IParamEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: FeatureEntity.java,v 1.1 2009-07-22 09:31:30 scottwilson Exp $
 */
public class FeatureEntity implements IFeatureEntity {
	
	private String fName;
	private boolean fRequired;
	private List<IParamEntity> fParams;
	
	public FeatureEntity(){
		fName = "";
		fRequired = false;
		fParams = new ArrayList<IParamEntity>();
	}
	
	public FeatureEntity(String name, boolean required,
			List<IParamEntity> params) {
		super();
		fName = name;
		fRequired = required;
		fParams = params;
	}
	
	public FeatureEntity(String name, boolean required) {
		super();
		fName = name;
		fRequired = required;
		fParams = new ArrayList<IParamEntity>();
	}
	
	public boolean hasParams(){
		if(fParams.size() > 0){
			return true;
		}
		return false;
	}
	
	public String getName() {
		return fName;
	}
	
	public void setName(String name) {
		fName = name;
	}
	
	public boolean isRequired() {
		return fRequired;
	}
	
	public void setRequired(boolean required) {
		fRequired = required;
	}
	
	public List<IParamEntity> getParams() {
		return fParams;
	}
	
	public void setParams(List<IParamEntity> params) {
		fParams = params;
	}

	public String getTagName() {
		return IW3CXMLConfiguration.FEATURE_ELEMENT;
	}
	
	public void fromJDOM(Element element) throws BadManifestException {
		fName = element.getAttributeValue(IW3CXMLConfiguration.NAME_ATTRIBUTE);
		if(fName == null || fName == ""){
			throw new BadManifestException("A Feature is defined in the manifest, but its name attribute is empty.");
		}
		String isRequired = element.getAttributeValue(IW3CXMLConfiguration.REQUIRED_ATTRIBUTE);
		if(isRequired == null){
			fRequired = true;
		}
		else{
			try {
				fRequired = Boolean.valueOf(isRequired);
			} 
			catch (Exception e) {
				fRequired = true;
			}
		}
		
		// parse the children (look for <param> elements)
		for(Object o : element.getChildren()) {
			Element child = (Element)o;
			String tag = child.getName();			

			// PARAM optional, can be 0 or many
			if(tag.equals(IW3CXMLConfiguration.PARAM_ELEMENT)) {	
				IParamEntity aParam = new ParamEntity();
				aParam.fromJDOM(child);
				fParams.add(aParam);
			}
		}
		
	}

}
