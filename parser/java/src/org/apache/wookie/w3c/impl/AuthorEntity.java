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

import org.apache.wookie.w3c.IAuthorEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.util.IRIValidator;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.apache.wookie.w3c.util.XmlUtils;
import org.jdom.Element;
/**
 * @author Paul Sharples
 * @version $Id: AuthorEntity.java,v 1.3 2009-09-02 18:37:31 scottwilson Exp $
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
	
	public String getXMLTagName() {
		return IW3CXMLConfiguration.AUTHOR_ELEMENT;
	}
	
	public void fromXML(Element element) {
		fAuthorName = UnicodeUtils.normalizeWhitespace(XmlUtils.getTextContent(element));		
		fHref = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.HREF_ATTRIBUTE));	
		if (fHref.equals("")) fHref = null;
		if (!IRIValidator.isValidIRI(fHref)) fHref = null;
		fEmail = UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.EMAIL_ATTRIBUTE));
		if (fEmail.equals("")) fEmail = null;
	}

}
