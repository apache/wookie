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
 * A Name/Value parameter
 */
public interface IParam {
	
	/**
     * Get widget feature parameter name.
     * 
     * @return parameter name
     */
    String getName();
    
    /**
     * Set widget feature parameter name. 
     */
    void setName(String name);
    
    /**
     * Get widget feature parameter value.
     * 
     * @return parameter value
     */
    String getValue();
    
    /**
     * Set widget feature parameter value.
     */
    void setValue(String value);

}
