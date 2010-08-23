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

import org.apache.jackrabbit.ocm.manager.beanconverter.impl.ReferenceBeanConverterImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Bean;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetDefault;
import org.apache.wookie.beans.jcr.IPathBean;
import org.apache.wookie.beans.jcr.JCRPersistenceManager;

/**
 * WidgetDefaultImpl - JCR OCM IWidgetDefault implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:widgetDefault", discriminator=false)
public class WidgetDefaultImpl implements IWidgetDefault, IPathBean
{
    @Field(path=true)
    private String path;
    
    @Field(jcrName="wookie:widgetContext")
    private String widgetContext;

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
            path = nodeRootPath+"/"+JCRPersistenceManager.escapeJCRName(widgetContext);
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
     * @see org.apache.wookie.beans.IWidgetDefault#getWidget()
     */
    public IWidget getWidget()
    {
        return widgetImpl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetDefault#setWidget(org.apache.wookie.beans.IWidget)
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
     * @see org.apache.wookie.beans.IWidgetDefault#getWidgetContext()
     */
    public String getWidgetContext()
    {
        return widgetContext;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetDefault#setWidgetContext(java.lang.String)
     */
    public void setWidgetContext(String widgetContext)
    {
        this.widgetContext = widgetContext;
    }
}
