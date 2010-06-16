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
 * IParam - a widget feature parameter found in the config.xml.
 * 
 * @author Paul Sharples
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IParam
{
    /**
     * Get widget feature parameter name.
     * 
     * @return parameter name
     */
    String getParameterName();
    
    /**
     * Set widget feature parameter name.
     * 
     * @return parameterName parameter name
     */
    void setParameterName(String parameterName);
    
    /**
     * Get widget feature parameter value.
     * 
     * @return parameter value
     */
    String getParameterValue();
    
    /**
     * Set widget feature parameter value.
     * 
     * @return parameterValue parameter value
     */
    void setParameterValue(String parameterValue);
}
