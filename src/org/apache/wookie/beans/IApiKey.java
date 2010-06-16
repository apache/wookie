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
 * IApiKey - an API key.
 * 
 * @author Scott Wilson
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IApiKey extends IBean
{
    /**
     * Get API key value.
     * 
     * @return key value
     */
    String getValue();
    
    /**
     * Set API key value.
     * 
     * @param value key value
     */
    void setValue(String value);
    
    /**
     * Get API key email address.
     * 
     * @return email address
     */
    String getEmail();
    
    /**
     * Set API key email address.
     * 
     * @param email email address
     */
    void setEmail(String email);
}
