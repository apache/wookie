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
import org.apache.wookie.w3c.ILicense;
import org.apache.wookie.beans.jcr.IIdElement;

/**
 * LicenseImpl - JCR OCM ILicense implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:license", extend=LocalizedBeanImpl.class, discriminator=false)
public class LicenseImpl extends LocalizedBeanImpl implements ILicense, IIdElement
{
    @Field(id=true, jcrName="wookie:elementId")
    private long elementId = -1;
    
    @Field(jcrName="wookie:href")
    private String href;
    
    @Field(jcrName="wookie:text")
    private String text;
    
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
     * @see org.apache.wookie.beans.ILicense#getHref()
     */
    public String getHref()
    {
        return href;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ILicense#setHref(java.lang.String)
     */
    public void setHref(String href)
    {
        this.href = href;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ILicense#getText()
     */
    public String getLicenseText()
    {
        return text;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.ILicense#setText(java.lang.String)
     */
    public void setLicenseText(String text)
    {
        this.text = text;
    }
}
