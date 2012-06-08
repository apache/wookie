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

import org.apache.wookie.beans.IParam;
import org.apache.wookie.beans.jpa.IInverseRelationship;

/**
 * ParamImpl - JPA IParam implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="Param")
@Table(name="Param")
public class ParamImpl implements IParam, IInverseRelationship<FeatureImpl>
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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="feature_id", referencedColumnName="id")
    @SuppressWarnings("unused")
    private FeatureImpl parentFeature;

    @Basic(optional=false)
    @Column(name="parameterName", nullable=false)
    private String parameterName;

    @Basic(optional=false)
    @Column(name="parameterValue", nullable=false)
    private String parameterValue;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IInverseRelationship#updateInverseRelationship(java.lang.Object)
     */
    public void updateInverseRelationship(FeatureImpl owningObject)
    {
        parentFeature = owningObject;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParam#getParameterName()
     */
    public String getName()
    {
        return parameterName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParam#setParameterName(java.lang.String)
     */
    public void setName(String parameterName)
    {
        this.parameterName = parameterName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParam#getParameterValue()
     */
    public String getValue()
    {
        return parameterValue;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IParam#setParameterValue(java.lang.String)
     */
    public void setValue(String parameterValue)
    {
        this.parameterValue = parameterValue;
    }
}
