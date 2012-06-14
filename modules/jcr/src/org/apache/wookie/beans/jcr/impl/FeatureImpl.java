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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import org.apache.wookie.w3c.IFeature;
import org.apache.wookie.w3c.IParam;
import org.apache.wookie.beans.jcr.IIdElement;
import org.apache.wookie.beans.jcr.IdCollection;

/**
 * FeatureImpl - JCR OCM IFeature implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:feature", discriminator=false)
public class FeatureImpl implements IFeature, IIdElement
{
    @Field(id=true, jcrName="wookie:elementId")
    private long elementId = -1;
    
    @Field(jcrName="wookie:featureName")
    private String featureName;
    
    @Field(jcrName="wookie:required")
    private Boolean required;
    
    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:parameters", elementClassName=ParamImpl.class)
    private List<ParamImpl> parameterImpls;
    
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
     * @see org.apache.wookie.beans.IFeature#getFeatureName()
     */
    public String getName()
    {
        return featureName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#setFeatureName(java.lang.String)
     */
    public void setName(String featureName)
    {
        this.featureName = featureName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#getParameters()
     */
    public List<IParam> getParameters()
    {
        if (parameterImpls == null)
        {
            parameterImpls = new ArrayList<ParamImpl>();
        }
        return new IdCollection<ParamImpl,IParam>(parameterImpls);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#setParameters(java.util.Collection)
     */
    public void setParameters(List<IParam> parameters)
    {
        getParameters().clear();
        if (parameters != null)
        {
            for (IParam parameter : parameters)
            {
                getParameters().add(parameter);
            }
        }
    }

    /**
     * Get parameter implementations collection.
     * 
     * @return parameter implementations collection
     */
    public Collection<ParamImpl> getParameterImpls()
    {
        return parameterImpls;
    }

    /**
     * Set parameter implementations collection.
     * 
     * @param parameterImpls parameter implementations collection
     */
    public void setParameterImpls(List<ParamImpl> parameterImpls)
    {
        this.parameterImpls = parameterImpls;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#isRequired()
     */
    public boolean isRequired()
    {
        return ((required != null) ? required.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#setRequired(boolean)
     */
    public void setRequired(boolean required)
    {
        this.required = (required ? Boolean.TRUE : Boolean.FALSE);
    }
}
