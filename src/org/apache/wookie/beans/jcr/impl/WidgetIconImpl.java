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

import org.apache.wookie.beans.IWidgetIcon;
import org.apache.wookie.beans.jcr.IIdElement;

/**
 * WidgetIconImpl - JCR OCM IWidgetIcon implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:widgetIcon", discriminator=false)
public class WidgetIconImpl implements IWidgetIcon, IIdElement
{
    @Field(id=true, jcrName="wookie:elementId")
    private long elementId = -1;
    
    @Field(jcrName="wookie:src")
    private String src;

    @Field(jcrName="wookie:height")
    private Integer height;

    @Field(jcrName="wookie:width")
    private Integer width;

    @Field(jcrName="wookie:lang")
    private String lang;

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
     * @see org.apache.wookie.beans.IWidgetIcon#getHeight()
     */
    public Integer getHeight()
    {
        return height;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#setHeight(java.lang.Integer)
     */
    public void setHeight(Integer height)
    {
        this.height = height;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#getLang()
     */
    public String getLang()
    {
        return lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#setLang(java.lang.String)
     */
    public void setLang(String lang)
    {
        this.lang = lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#getSrc()
     */
    public String getSrc()
    {
        return src;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#setSrc(java.lang.String)
     */
    public void setSrc(String src)
    {
        this.src = src;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#getWidth()
     */
    public Integer getWidth()
    {
        return width;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetIcon#setWidth(java.lang.Integer)
     */
    public void setWidth(Integer width)
    {
        this.width = width;
    }
}
