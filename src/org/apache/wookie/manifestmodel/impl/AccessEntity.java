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

import org.apache.wookie.manifestmodel.IAccessEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: AccessEntity.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
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
