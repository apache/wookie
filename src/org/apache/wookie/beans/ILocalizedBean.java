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
 *  limitations under the License.
 */

package org.apache.wookie.beans;

import org.apache.wookie.w3c.ILocalizedElement;

/**
 * ILocalizedKeyBean - key bean with localized language and text direction.
 * 
 * Text direction conforms to http://www.w3.org/TR/2007/REC-its-20070403/
 *   
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface ILocalizedBean extends ILocalizedElement
{  
    /**
     * Get localized text direction.
     * 
     * @return name text direction
     */
    String getDir();
    
    /**
     * Set localized text direction.
     * 
     * @param dir name text direction
     */
    void setDir(String dir);    
}
