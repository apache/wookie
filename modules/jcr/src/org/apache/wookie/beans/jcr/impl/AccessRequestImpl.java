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

import java.net.URI;

import org.apache.jackrabbit.ocm.manager.beanconverter.impl.ReferenceBeanConverterImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Bean;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import org.apache.wookie.beans.IAccessRequest;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.jcr.IPathBean;
import org.apache.wookie.beans.jcr.JCRPersistenceManager;

/**
 * AccessRequestImpl - JCR OCM IAccessRequest implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:accessRequest", discriminator=false)
public class AccessRequestImpl implements IAccessRequest, IPathBean
{
    @Field(path=true)
    private String path;
    
    @Field(jcrName="wookie:origin")
    private String origin;
    
    @Field(jcrName="wookie:subdomains")
    private Boolean subdomains;
    
    @Field(jcrName="wookie:granted")
    private Boolean granted;

    @Bean(jcrName="wookie:widget", converter=ReferenceBeanConverterImpl.class)
    private WidgetImpl widgetImpl;
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IBean#getId()
     */
    public Object getId()
    {
        return path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IPathBean#getNodePath()
     */
    public String getNodePath()
    {
        return path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IPathBean#setNodePath(java.lang.String)
     */
    public String setNodePath(String nodeRootPath)
    {
        if (path == null)
        {
            path = nodeRootPath+"/"+JCRPersistenceManager.escapeJCRName(origin)+"/"+JCRPersistenceManager.escapeJCRName(widgetImpl.getGuid());
        }
        return path;
    }

    /**
     * Get raw persistent JCR absolute node path.
     * 
     * @return absolute path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Set raw persistent JCR absolute node path.
     * 
     * @param path absolute path
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#getOrigin()
     */
    public String getOrigin()
    {
        return origin;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#setOrigin(java.lang.String)
     */
    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#isGranted()
     */
    public boolean isGranted()
    {
        return ((granted != null) ? granted.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#setGranted(boolean)
     */
    public void setGranted(boolean granted)
    {
        this.granted = (granted ? Boolean.TRUE : Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#isSubdomains()
     */
    public boolean isSubdomains()
    {
        return ((subdomains != null) ? subdomains.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#setSubdomains(boolean)
     */
    public void setSubdomains(boolean subdomains)
    {
        this.subdomains = (subdomains ? Boolean.TRUE : Boolean.FALSE);
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#getWidget()
     */
    public IWidget getWidget()
    {
        return widgetImpl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#setWidget(org.apache.wookie.beans.IWidget)
     */
    public void setWidget(IWidget widget)
    {
        this.widgetImpl = (WidgetImpl)widget;
    }
    
    /**
     * Get widget implementation.
     * 
     * @return widget implementation
     */
    public WidgetImpl getWidgetImpl()
    {
        return widgetImpl;
    }

    /**
     * Set widget implementation.
     * 
     * @return widgetImpl widget implementation
     */
    public void setWidgetImpl(WidgetImpl widgetImpl)
    {
        this.widgetImpl = widgetImpl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#isAllowed(java.net.URI)
     */
    public boolean isAllowed(URI requestedUri)
    {
        return Utilities.isAllowed(this, requestedUri);
    }
}
