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
package org.apache.wookie.w3c;

import org.apache.wookie.w3c.exceptions.BadManifestException;
import org.jdom.Element;

/**
 * Base class for model objects that are unmarshalled from an XML element
 */
public interface IElement {
	
    /**
     * Unmarshall the given XML Element to this Object
     * 
     * @param element The Element to unmarshall
     */
    void fromXML(Element element) throws BadManifestException;

    /**
     * Marshall the entity into an XML Element
     * @return the W3C Widgets Element representing the content of the entity
     */
    Element toXml();
}
