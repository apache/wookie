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

import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.jcr.IIdElement;

/**
 * SharedDataImpl - JCR OCM ISharedData implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:sharedData", discriminator=false)
public class SharedDataImpl implements ISharedData, IIdElement
{
    @Field(id=true, jcrName="wookie:elementId")
    private long elementId = -1;
    
    @Field(jcrName="wookie:sharedDataKey")
    private String sharedDataKey;

    @Field(jcrName="wookie:dkey")
    private String dkey;

    @Field(jcrName="wookie:dvalue")
    private String dvalue;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ISharedData#getDkey()
     */
    public String getDkey()
    {
        return dkey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ISharedData#setDkey(java.lang.String)
     */
    public void setDkey(String dkey)
    {
        this.dkey = dkey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ISharedData#getDvalue()
     */
    public String getDvalue()
    {
        return dvalue;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ISharedData#setDvalue(java.lang.String)
     */
    public void setDvalue(String dvalue)
    {
        this.dvalue = dvalue;
    }

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
     * @see org.apache.wookie.beans.ISharedData#getSharedDataKey()
     */
    public String getSharedDataKey()
    {
        return sharedDataKey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ISharedData#setSharedDataKey(java.lang.String)
     */
    public void setSharedDataKey(String sharedDataKey)
    {
        this.sharedDataKey = sharedDataKey;
    }
}
