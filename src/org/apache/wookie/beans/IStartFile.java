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
 * IStartFile - a localized start page for a widget.
 * 
 * @author Scott Wilson
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IStartFile extends ILocalizedElement
{
    /**
     * Get widget start file URL.
     * 
     * @return start file URL
     */
    String getUrl();
    
    /**
     * Set widget start file URL.
     * 
     * @param url start file URL
     */
    void setUrl(String url);
    
    /**
     * Get widget start file character set.
     * 
     * @return character set
     */
    String getCharset();
    
    /**
     * Set widget start file character set.
     * 
     * @param charset character set
     */
    void setCharset(String charset);

    /**
     * Set widget start file language.
     * 
     * @param lang file language
     */
    void setLang(String lang);
}
