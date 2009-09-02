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

import org.apache.wookie.exceptions.BadManifestException;
import org.apache.wookie.manifestmodel.IParamEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: ParamEntity.java,v 1.3 2009-09-02 18:37:31 scottwilson Exp $
 */
public class ParamEntity implements IParamEntity {
	
	private String fName;
	private String fValue;
	
	public ParamEntity(){
		fName = "";
		fValue = "";
	}
	
	public ParamEntity(String name, String value) {
		super();
		fName = name;
		fValue = value;
	}

	public String getName() {
		return fName;
	}

	public void setName(String name) {
		fName = name;
	}

	public String getValue() {
		return fValue;
	}

	public void setValue(String value) {
		fValue = value;
	}

	public String getXMLTagName() {
		return IW3CXMLConfiguration.PARAM_ELEMENT;
	}
	
	public void fromXML(Element element) throws BadManifestException {
		// Name
		fName = element.getAttributeValue(IW3CXMLConfiguration.NAME_ATTRIBUTE);
		if(fName == null){
			fName = "";
		}
		// Value
		fValue = element.getAttributeValue(IW3CXMLConfiguration.VALUE_ATTRIBUTE);
		if(fValue == null){
			fValue = "";
		}
	}	

}
