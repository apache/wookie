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

import org.apache.wookie.w3c.IPreference;
import org.apache.wookie.beans.jcr.IIdElement;

/**
 * PreferenceDefaultImpl - JCR OCM IPreferenceDefault implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:preferenceDefault", discriminator=false)
public class PreferenceDefaultImpl implements IPreference, IIdElement
{
    @Field(id=true, jcrName="wookie:elementId")
    private long elementId = -1;
    
    @Field(jcrName="wookie:preference")
    private String preference;
    
    @Field(jcrName="wookie:value")
    private String value;
    
    @Field(jcrName="wookie:readOnly")
    private Boolean readOnly;
    
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
     * @see org.apache.wookie.beans.IPreferenceDefault#getPreference()
     */
    public String getName()
    {
        return preference;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreferenceDefault#setPreference(java.lang.String)
     */
    public void setName(String preference)
    {
        this.preference = preference;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreferenceDefault#getValue()
     */
    public String getValue()
    {
        return value;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreferenceDefault#setValue(java.lang.String)
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreferenceDefault#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return ((readOnly != null) ? readOnly.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreferenceDefault#setReadOnly(boolean)
     */
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = (readOnly ? Boolean.TRUE : Boolean.FALSE);
    }
}
