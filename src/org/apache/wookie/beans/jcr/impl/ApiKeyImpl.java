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

import org.apache.wookie.beans.IApiKey;
import org.apache.wookie.beans.jcr.IPathBean;
import org.apache.wookie.beans.jcr.JCRPersistenceManager;

/**
 * ApiKeyImpl - JCR OCM IApiKey implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:apiKey", discriminator=false)
public class ApiKeyImpl implements IApiKey, IPathBean
{
    @Field(path=true)
    private String path;
    
    @Field(jcrName="wookie:value")
    private String value;

    @Field(jcrName="wookie:email")
    private String email;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IApiKey#getEmail()
     */
    public String getEmail()
    {
        return email;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IApiKey#setEmail(java.lang.String)
     */
    public void setEmail(String email)
    {
        this.email = email;
    }
    
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
            path = nodeRootPath+"/"+JCRPersistenceManager.escapeJCRName(value);
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
     * @see org.apache.wookie.beans.IApiKey#getValue()
     */
    public String getValue()
    {
        return value;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IApiKey#setValue(java.lang.String)
     */
    public void setValue(String value)
    {
        this.value = value;
    }
}
