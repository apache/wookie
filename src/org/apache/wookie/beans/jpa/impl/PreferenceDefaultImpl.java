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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.openjpa.persistence.ExternalValues;
import org.apache.openjpa.persistence.Type;

import org.apache.wookie.beans.IPreferenceDefault;
import org.apache.wookie.beans.jpa.IInverseRelationship;

/**
 * PreferenceDefaultImpl - JPA IPreferenceDefault implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="PreferenceDefault")
@Table(name="PreferenceDefault")
public class PreferenceDefaultImpl implements IPreferenceDefault, IInverseRelationship<WidgetImpl>
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", nullable=false)
    @SuppressWarnings("unused")
    private int id;

    @Version
    @Column(name="jpa_version")
    @SuppressWarnings("unused")
    private int jpaVersion;

    @Basic(optional=false)
    @Column(name="preference", nullable=false)
    private String preference;

    @Basic(optional=false)
    @Column(name="value", nullable=false)
    private String value;

    @Basic
    @Column(name="readOnly")
    @ExternalValues({"true=t","false=f","null="})
    @Type(String.class)
    private Boolean readOnly;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="widget_id", referencedColumnName="id")
    @SuppressWarnings("unused")
    private WidgetImpl widget;
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IInverseRelationship#updateInverseRelationship(java.lang.Object)
     */
    public void updateInverseRelationship(WidgetImpl owningObject)
    {
        widget = owningObject;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreferenceDefault#getPreference()
     */
    public String getPreference()
    {
        return preference;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IPreferenceDefault#setPreference(java.lang.String)
     */
    public void setPreference(String preference)
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
