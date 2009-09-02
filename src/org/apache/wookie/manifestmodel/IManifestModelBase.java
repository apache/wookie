package org.apache.wookie.manifestmodel;

import org.apache.wookie.exceptions.BadManifestException;
import org.jdom.Element;

public interface IManifestModelBase {
	
	
	public String getXMLTagName();
	
    /**
     * Unmarshall the given XML Element to this Object
     * 
     * @param element The Element to unmarshall
     */
    void fromXML(Element element) throws BadManifestException;

}
