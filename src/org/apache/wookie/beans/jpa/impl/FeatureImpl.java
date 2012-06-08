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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.openjpa.persistence.ElementDependent;
import org.apache.openjpa.persistence.ExternalValues;
import org.apache.openjpa.persistence.Type;

import org.apache.wookie.beans.IFeature;
import org.apache.wookie.beans.IParam;
import org.apache.wookie.beans.jpa.IInverseRelationship;
import org.apache.wookie.beans.jpa.InverseRelationshipCollection;

/**
 * FeatureImpl - JPA IFeature implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="Feature")
@Table(name="Feature")
public class FeatureImpl implements IFeature, IInverseRelationship<WidgetImpl>
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
    @Column(name="featureName", nullable=false)
    private String featureName;

    @Basic
    @Column(name="required")
    @ExternalValues({"true=t","false=f","null="})
    @Type(String.class)
    private Boolean required;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="widget_id", referencedColumnName="id")
    @SuppressWarnings("unused")
    private WidgetImpl widget;

    @OneToMany(mappedBy="parentFeature", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<ParamImpl> parameters;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IInverseRelationship#updateInverseRelationship(java.lang.Object)
     */
    public void updateInverseRelationship(WidgetImpl owningObject)
    {
        widget = owningObject;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#getFeatureName()
     */
    public String getName()
    {
        return featureName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#setFeatureName(java.lang.String)
     */
    public void setName(String featureName)
    {
        this.featureName = featureName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#getParameters()
     */
    public List<IParam> getParameters()
    {
        if (parameters == null)
        {
            parameters = new ArrayList<ParamImpl>();
        }
        ArrayList<IParam> params = new ArrayList<IParam>();
        params.addAll( new InverseRelationshipCollection<FeatureImpl,ParamImpl,IParam>(this, parameters));
        return params;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#setParameters(java.util.Collection)
     */
    public void setParameters(List<IParam> parameters)
    {
        getParameters().clear();
        if (parameters != null)
        {
            for (IParam parameter : parameters)
            {
                getParameters().add((ParamImpl)parameter);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#isRequired()
     */
    public boolean isRequired()
    {
        return ((required != null) ? required.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IFeature#setRequired(boolean)
     */
    public void setRequired(boolean required)
    {
        this.required = (required ? Boolean.TRUE : Boolean.FALSE);
    }
}
