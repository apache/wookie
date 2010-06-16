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

import java.net.URI;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.openjpa.persistence.ExternalValues;
import org.apache.openjpa.persistence.Type;

import org.apache.wookie.beans.IAccessRequest;
import org.apache.wookie.beans.IWidget;

/**
 * AccessRequestImpl - JPA IAccessRequest implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="AccessRequest")
@Table(name="AccessRequest")
@NamedQueries({@NamedQuery(name="ACCESS_REQUESTS", query="SELECT ar FROM AccessRequest ar WHERE ar.widget = :widget AND ar.granted = TRUE")})
public class AccessRequestImpl implements IAccessRequest
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
    @Column(name="origin")
    private String origin;

    @Basic
    @Column(name="subdomains")
    @ExternalValues({"true=t","false=f","null="})
    @Type(String.class)
    private Boolean subdomains;

    @Basic
    @Column(name="granted")
    @ExternalValues({"true=t","false=f","null="})
    @Type(String.class)
    private Boolean granted;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="widget_id", referencedColumnName="id", nullable=false)
    private WidgetImpl widget;
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IBean#getId()
     */
    public Object getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#getOrigin()
     */
    public String getOrigin()
    {
        return origin;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#setOrigin(java.lang.String)
     */
    public void setOrigin(String origin)
    {
        this.origin = origin;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#isGranted()
     */
    public boolean isGranted()
    {
        return ((granted != null) ? granted.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#setGranted(boolean)
     */
    public void setGranted(boolean granted)
    {
        this.granted = (granted ? Boolean.TRUE : Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#isSubdomains()
     */
    public boolean isSubdomains()
    {
        return ((subdomains != null) ? subdomains.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#setSubdomains(boolean)
     */
    public void setSubdomains(boolean subdomains)
    {
        this.subdomains = (subdomains ? Boolean.TRUE : Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#getWidget()
     */
    public IWidget getWidget()
    {
        return widget;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#setWidget(org.apache.wookie.beans.IWidget)
     */
    public void setWidget(IWidget widget)
    {
        this.widget = (WidgetImpl)widget;
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IAccessRequest#isAllowed(java.net.URI)
     */
    public boolean isAllowed(URI requestedUri)
    {
        return Utilities.isAllowed(this, requestedUri);
    }
}
