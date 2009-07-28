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

package org.apache.wookie.manifestmodel;

import org.apache.wookie.exceptions.BadManifestException;
import org.jdom.Element;

/**
 * Interface for XML persistence
 * 
 * @author Phillip Beauvoir
 * @author Paul Sharples
 * @version $Id: IManifestModelJDOM.java,v 1.2 2009-07-28 16:05:22 scottwilson Exp $
 */
public interface IManifestModelJDOM {
	
    /**
     * @return The XML Element tag name for this object
     */
    String getTagName();
   
    /**
     * Unmarshall the given JDOM XML Element to this Object
     * 
     * @param element The JDOM Element to unmarshall
     */
    void fromJDOM(Element element) throws BadManifestException;
    
}
