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

package org.apache.wookie.beans.jcr.impl;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import org.apache.wookie.beans.IParam;
import org.apache.wookie.beans.jcr.IIdElement;

/**
 * ParamImpl - JCR OCM IParam implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:param", discriminator=false)
public class ParamImpl implements IParam, IIdElement
{
    @Field(id=true, jcrName="wookie:elementId")
    private long elementId = -1;
    
    @Field(jcrName="wookie:parameterName")
    private String parameterName;

    @Field(jcrName="wookie:parameterValue")
    private String parameterValue;
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IIdElement#getElementId()
     */
    public long getElementId()
    {
        return elementId;
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IIdElement#setElementId(long)
     */
    public void setElementId(long elementId)
    {
        this.elementId = elementId;
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParam#getParameterName()
     */
    public String getParameterName()
    {
        return parameterName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParam#setParameterName(java.lang.String)
     */
    public void setParameterName(String parameterName)
    {
        this.parameterName = parameterName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParam#getParameterValue()
     */
    public String getParameterValue()
    {
        return parameterValue;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParam#setParameterValue(java.lang.String)
     */
    public void setParameterValue(String parameterValue)
    {
        this.parameterValue = parameterValue;
    }
}
