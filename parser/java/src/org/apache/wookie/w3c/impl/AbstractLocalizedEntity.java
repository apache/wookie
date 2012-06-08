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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.wookie.w3c.ILocalizedEntity;
import org.apache.wookie.w3c.IW3CXMLConfiguration;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.apache.wookie.w3c.util.UnicodeUtils;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;

/**
 * Abstract base class for entities containing i18n or l10n content, including
 * utility methods for extracting and processing text that uses language tags
 * and text direction (e.g. RTL)
 */
public abstract class AbstractLocalizedEntity implements ILocalizedEntity {
	
	/**
	 * a Language string conforming to BCP47
	 */
	protected String lang;
	/**
	 * Text direction conforming to http://www.w3.org/TR/2007/REC-its-20070403/
	 */
	protected String dir;
	
	public String getLang() {
		return lang;
	}
	
	/**
	 * Set the language tag for the object
	 * @param lang the language tag to set; this should be a valid BCP-47 language tag
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.ILocalizedEntity#getDir()
	 */
	public String getDir() {
		return dir;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.w3c.ILocalizedEntity#setDir(java.lang.String)
	 */
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	/**
	 * Checks whether the language tag for the entity is OK.
	 * A null value is OK, as is a BCP47 tag
	 */
	public boolean isValid(){
		if (getLang() == null) return true;
		if (LocalizationUtils.isValidLanguageTag(getLang())) return true;
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.manifestmodel.IManifestModelBase#fromXML(org.jdom.Element)
	 */
	public void fromXML(Element element) {
		String lang =  UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.LANG_ATTRIBUTE, Namespace.XML_NAMESPACE));
		if (!lang.equals("")) setLang(lang);
		dir = getTextDirection(element);
	}

	/**
	 * Returns the text content of an element, recursively adding
	 * any text nodes found in its child nodes AND any <span> elements
	 * that include localization information
	 * @param element
	 * @return a string containing the element text
	 */
	public static String getLocalizedTextContent(Element element){
		StringBuffer content = new StringBuffer();
		for (Object node:element.getContent()){
			if (node instanceof Element){
				if ((((Element) node).getAttribute("dir")!= null || ((Element) node).getAttribute("lang")!= null)  && ((Element)node).getName().equals("span")){
					content.append("<span dir=\""+getTextDirection((Element)node)+"\"");
					if (((Element)node).getAttribute("lang")!=null)
						content.append(" xml:lang=\""+((Element)node).getAttribute("lang").getValue()+"\"");
					content.append(">");
					content.append(getLocalizedTextContent((Element)node));
					content.append("</span>");
				} else {
					content.append(getLocalizedTextContent((Element)node));
				}
			}
			// Append text to the string
			// First we have to unescape any XML special characters so we don't
			// double-encode them (e.g. &acute; = &amp;acute;) when exporting to 
			// HTML or XML later
			if (node instanceof Text){
			  String text = ((Text)node).getText();
				content.append(StringEscapeUtils.unescapeXml(text));
			}
		}
		return UnicodeUtils.normalizeWhitespace(content.toString());
	}
	

	public static final String LEFT_TO_RIGHT = "ltr";
	
	public static final String RIGHT_TO_LEFT = "rtl";
	
	public static final String LEFT_TO_RIGHT_OVERRIDE = "lro";
	
	public static final String RIGHT_TO_LEFT_OVERRIDE = "rlo";
	
	/**
	 * Returns the direction (rtl, ltr, lro, rlo) of the child text of an element
	 * @param element the element to parse
	 * @return the string "ltr", "rtl", "lro" or "rlo"
	 */
	public static String getTextDirection(Element element){
		try {
			Attribute dir = element.getAttribute(IW3CXMLConfiguration.DIR_ATRRIBUTE);
			if (dir == null){
				if (element.isRootElement()) return null;
				return getTextDirection(element.getParentElement());
			} else {
				String dirValue = UnicodeUtils.normalizeSpaces(dir.getValue());
				if (dirValue.equals("rtl")) return RIGHT_TO_LEFT;
				if (dirValue.equals("ltr")) return LEFT_TO_RIGHT;
				if (dirValue.equals("rlo")) return RIGHT_TO_LEFT_OVERRIDE;
				if (dirValue.equals("lro")) return LEFT_TO_RIGHT_OVERRIDE;
				return getTextDirection(element.getParentElement());			
			}
		} catch (Exception e) {
			// In the case of an error we always return the default value
			return LEFT_TO_RIGHT;
		}
	}

	/**
	 * Set the dir and lang attributes of an element representing the entity - used when marshalling an entity to XML
	 * @param element the element to add attributes to
	 * @return the element with dir and lang attributes added if appropriate
	 */
	protected Element setLocalisationAttributes(Element element){
		if (getDir() != null) element.setAttribute(IW3CXMLConfiguration.DIR_ATRRIBUTE,getDir());
		if (getLang() != null) element.setAttribute(IW3CXMLConfiguration.LANG_ATTRIBUTE,getLang(), Namespace.XML_NAMESPACE);
		return element;
	}

}
