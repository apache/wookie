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

package org.apache.wookie.beans.jpa.impl;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.openjpa.persistence.ExternalValues;
import org.apache.openjpa.persistence.Type;

import org.apache.wookie.beans.IPreference;

/**
 * PreferenceImpl - JPA IPreference implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="Preference")
@Table(name="Preference")
public class PreferenceImpl implements IPreference
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", nullable=false)
    private int id;

    @Version
    @Column(name="jpa_version")
    @SuppressWarnings("unused")
    private int jpaVersion;

    @Basic
    @Column(name="dkey")
    private String dkey;

    @Basic
    @Column(name="dvalue")
    private String dvalue;

    @Basic
    @Column(name="readOnly")
    @ExternalValues({"true=t","false=f","null="})
    @Type(String.class)
    private Boolean readOnly;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreference#getDkey()
     */
    public String getName()
    {
        return dkey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreference#setDkey(java.lang.String)
     */
    public void setName(String dkey)
    {
        this.dkey = dkey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreference#getDvalue()
     */
    public String getValue()
    {
        return dvalue;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreference#setDvalue(java.lang.String)
     */
    public void setValue(String dvalue)
    {
        this.dvalue = dvalue;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreference#isReadOnly()
     */
    public boolean isReadOnly()
    {
        return ((readOnly != null) ? readOnly.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreference#setReadOnly(boolean)
     */
    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = (readOnly ? Boolean.TRUE : Boolean.FALSE);
    }

    /*
     * (non-Javadoc)
     * @see org.apache.wookie.beans.IBean#getId()
     */
	public Object getId() {
	      return id;
	}
}
