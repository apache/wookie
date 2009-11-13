package org.apache.wookie.manifestmodel.impl;

import org.apache.wookie.manifestmodel.ILocalizedEntity;
import org.apache.wookie.manifestmodel.IW3CXMLConfiguration;
import org.apache.wookie.util.UnicodeUtils;
import org.jdom.Element;
import org.jdom.Namespace;


public abstract class LocalizedEntity implements ILocalizedEntity {
	
	private String language;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.manifestmodel.IManifestModelBase#fromXML(org.jdom.Element)
	 */
	public void fromXML(Element element) {				
		setLanguage( UnicodeUtils.normalizeSpaces(element.getAttributeValue(IW3CXMLConfiguration.LANG_ATTRIBUTE, Namespace.XML_NAMESPACE)));
	}

}
