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

import java.util.Collection;

/**
 * IFeature - a widget feature found in the config.xml.
 * 
 * @author Paul Sharples
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IFeature
{
    /**
     * Get widget feature name.
     * 
     * @return feature name
     */
    String getFeatureName();
    
    /**
     * Set widget feature name.
     * 
     * @param featureName feature name
     */
    void setFeatureName(String featureName);
    
    /**
     * Get widget feature required flag.
     * 
     * @return required flag
     */
    boolean isRequired();
    
    /**
     * Set widget feature required flag.
     * 
     * @param required required flag
     */
    void setRequired(boolean required);

    /**
     * Get collection of widget feature parameters.
     * 
     * @return parameters collection
     */
    Collection<IParam> getParameters();

    /**
     * Set collection of widget feature parameters.
     * 
     * @param parameters parameters collection
     */
    void setParameters(Collection<IParam> parameters);
}
