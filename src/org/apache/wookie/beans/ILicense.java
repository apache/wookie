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

/**
 * ILicense - a widget license.
 * 
 * @author Paul Sharples
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface ILicense extends ILocalizedBean
{
    /**
     * Get widget license text.
     * 
     * @return license text
     */
    String getLicenseText();
    
    /**
     * Set widget license text.
     * 
     * @param text license text
     */
    void setLicenseText(String text);
    
    /**
     * Get widget license href URL.
     * 
     * @return href URL
     */
    String getHref();
    
    /**
     * Set widget license href URL.
     * 
     * @param href href URL
     */
    void setHref(String href);
}
