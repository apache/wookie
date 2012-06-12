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

package org.apache.wookie.w3c.impl;

import java.net.IDN;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;

import org.apache.wookie.w3c.IAccess;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.util.IRIValidator;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.apache.wookie.w3c.xml.IElement;
import org.jdom.Attribute;
import org.jdom.Element;
/**
 * The Access element is defined in: http://www.w3.org/TR/widgets-access/
 */
public class AccessEntity implements IAccess, IElement {

	private String fOrigin;
	private boolean fSubDomains;

	public AccessEntity(){
		fOrigin = null;
		fSubDomains = false;
	}
	
	public String getOrigin() {
		return fOrigin;
	}

	public boolean hasSubDomains() {
		return fSubDomains;
	}

	public void fromXML(Element element) {	
		// Origin is required
		if (element.getAttribute(IW3CXMLConfiguration.ORIGIN_ATTRIBUTE)==null) return;
		fOrigin = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.ORIGIN_ATTRIBUTE));
		if (fOrigin.equals("*")) return;
		try {
			processOrigin();
		} catch (Exception e) {
			fOrigin =  null;
			return;
		}
		// Subdomains is optional
		try {
			processSubdomains(element.getAttribute(IW3CXMLConfiguration.SUBDOMAINS_ATTRIBUTE));
		} catch (Exception e) {
			fSubDomains = false;
			fOrigin = null;
		}			
	}

	/**
	 * Processes an origin attribute
	 * @throws Exception if the origin attribute is not valid
	 */
	private void processOrigin() throws Exception{
		
		/**
		 * Note that the Java implementation of URI is currently broken as it
		 * does not properly support IDNs and IRIs. Because of this we use
		 * URL which is slightly less buggy. 
		 */
		
		if (!IRIValidator.isValidIRI(fOrigin)) throw new Exception("origin is not a valid IRI");
		URL uri = new URL(fOrigin);
		if (uri.getHost() == null || uri.getHost().isEmpty()) throw new Exception("origin has no host");
		if (uri.getUserInfo()!=null) throw new Exception("origin has userinfo");
		if (uri.getPath()!=null && uri.getPath().length()>0) throw new Exception("origin has path information");
		if (uri.getRef()!=null) throw new Exception("origin has fragment information");
		if (uri.getQuery()!=null) throw new Exception("origin has query information");
		
		// Is the scheme supported?
		if (!Arrays.asList(IW3CXMLConfiguration.SUPPORTED_SCHEMES).contains(uri.getProtocol()))
			throw new Exception("scheme is not supported");
		
		// Default ports
		int port = uri.getPort();
		if (uri.getProtocol().equals("http") && port == -1) port = 80;
		if (uri.getProtocol().equals("https") && port == -1) port = 443;
		
		// Convert host to ASCII
		String host = IDN.toASCII(uri.getHost());
		
		URI processedURI = new URI(uri.getProtocol(),null,host,port,null,null,null);
		fOrigin = processedURI.toString();
	}

	/**
	 * Processes a subdomains attribute
	 * @param subDomains
	 * @throws Exception if the attribute is not valid
	 */
	private void processSubdomains(Attribute attr) throws Exception{
		if (attr != null){
			String subDomains = UnicodeUtils.normalizeSpaces(attr.getValue());
			if (subDomains.equals("true")||subDomains.equals("false")){
				fSubDomains = Boolean.valueOf(subDomains);
			} else {
				throw new Exception("subdomains is not a valid boolean value");
			}
		}
	}

	public Element toXml() {
		Element element = new Element(IW3CXMLConfiguration.ACCESS_ELEMENT,IW3CXMLConfiguration.MANIFEST_NAMESPACE);
		element.setAttribute(IW3CXMLConfiguration.SUBDOMAINS_ATTRIBUTE, String.valueOf(hasSubDomains()));
		element.setAttribute(IW3CXMLConfiguration.ORIGIN_ATTRIBUTE, getOrigin());
		return element;
	}
}
