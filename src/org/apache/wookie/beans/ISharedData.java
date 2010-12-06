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
 * ISharedData - a shared data entity.
 * 
 * @author Paul Sharples
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface ISharedData extends IBean
{
    /**
     * Get widget shared data value key.
     * 
     * @return value key
     */
    String getDkey();
    
    /**
     * Set widget shared data value key.
     * 
     * @param dkey value key
     */
    void setDkey(String dkey);
    
    /**
     * Get widget shared data value.
     * 
     * @return value
     */
    String getDvalue();
    
    /**
     * Set widget shared data value.
     * 
     * @param dvalue value
     */
    void setDvalue(String dvalue);
    
    /**
     * Get widget shared data key.
     * 
     * @return data key
     */
    String getSharedDataKey();
    
    /**
     * Set widget shared data key.
     * 
     * @param sharedDataKey data key
     */
    void setSharedDataKey(String sharedDataKey);
}
