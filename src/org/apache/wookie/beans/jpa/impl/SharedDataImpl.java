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

import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.jpa.IInverseRelationship;

/**
 * DescriptionImpl - JPA IDescription implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="SharedData")
@Table(name="SharedData")
public class SharedDataImpl implements ISharedData, IInverseRelationship<WidgetImpl>
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
    @Column(name="sharedDataKey")
    private String sharedDataKey;

    @Basic
    @Column(name="dkey")
    private String dkey;

    @Basic
    @Column(name="dvalue")
    private String dvalue;

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

    /*
     * (non-Javadoc)
     * @see org.apache.wookie.beans.IBean#getId()
     */
	public Object getId() {
		return id;
	}
}
