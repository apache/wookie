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
 * IPreference - a widget instance preference entity.
 * 
 * @author Paul Sharples
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IPreference
{
    /**
     * Get widget instance preference value key.
     * 
     * @return value key
     */
    String getDkey();
    
    /**
     * Set widget instance preference value key.
     * 
     * @param dkey value key
     */
    void setDkey(String dkey);
    
    /**
     * Get widget instance preference value.
     * 
     * @return value
     */
    String getDvalue();
    
    /**
     * Set widget instance preference value.
     * 
     * @param dvalue value
     */
    void setDvalue(String dvalue);
    
    /**
     * Get widget instance preference read only flag.
     * 
     * @return read only flag
     */
    boolean isReadOnly();
    
    /**
     * Set widget instance preference read only flag.
     * 
     * @param readOnly read only flag
     */
    void setReadOnly(boolean readOnly);
}
