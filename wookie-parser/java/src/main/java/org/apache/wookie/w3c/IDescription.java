/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.w3c;

/**
 * A Description of a Widget
 */
public interface IDescription extends ILocalized, IDirectional  {
	
    /**
     * Get widget description content.
     * 
     * @return content
     */
    String getDescription();
    
    /**
     * Set widget description content.
     * 
     * @param text content
     */
    void setDescription(String text);

}
