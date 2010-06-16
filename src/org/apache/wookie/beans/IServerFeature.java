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
 * IServerFeature - an installed widget feature.
 * 
 * This interface models ALREADY installed features, such as polling for example
 * NOTE: The config.xml file can also define other features, which may 
 * or may not be supported. These types are modeled in the "IFeature" interface.
 * 
 * @author Paul Sharples
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IServerFeature extends IBean
{
    /**
     * Get server installed widget feature name.
     * 
     * @return feature name
     */
    String getFeatureName();
    
    /**
     * Set server installed widget feature name.
     * 
     * @param featureName feature name
     */
    void setFeatureName(String featureName);
    
    /**
     * Get server installed widget feature class name.
     * 
     * @return class name
     */
    String getClassName();
    
    /**
     * Set server installed widget feature class name.
     * 
     * @param className class name
     */
    void setClassName(String className);
}
