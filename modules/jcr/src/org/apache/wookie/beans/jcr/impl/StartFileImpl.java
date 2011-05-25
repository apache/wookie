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

import org.apache.wookie.beans.IStartFile;
import org.apache.wookie.beans.jcr.IIdElement;

/**
 * StartFileImpl - JCR OCM IStartFile implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:startFile", discriminator=false)
public class StartFileImpl implements IStartFile, IIdElement
{
    @Field(id=true, jcrName="wookie:elementId")
    private long elementId = -1;
    
    @Field(jcrName="wookie:url")
    private String url;
    
    @Field(jcrName="wookie:charset")
    private String charset;

    @Field(jcrName="wookie:lang")
    private String lang;
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#getCharset()
     */
    public String getCharset()
    {
        return charset;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#setCharset(java.lang.String)
     */
    public void setCharset(String charset)
    {
        this.charset = charset;
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
     * @see org.apache.wookie.beans.IStartFile#getLang()
     */
    public String getLang()
    {
        return lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#setLang(java.lang.String)
     */
    public void setLang(String lang)
    {
        this.lang = lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#getUrl()
     */
    public String getUrl()
    {
        return url;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IStartFile#setUrl(java.lang.String)
     */
    public void setUrl(String url)
    {
        this.url = url;
    }
}
