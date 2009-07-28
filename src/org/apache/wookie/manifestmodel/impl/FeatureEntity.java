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

import java.util.ArrayList;
import java.util.List;

import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.manifestmodel.IFeatureEntity;
import org.apache.wookie.manifestmodel.IParamEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: FeatureEntity.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
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
