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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.wookie.beans.IServerFeature;

/**
 * ServerFeatureImpl - JPA IServerFeature implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="ServerFeature")
@Table(name="ServerFeature")
@NamedQueries({@NamedQuery(name="SERVER_FEATURE", query="SELECT sf FROM ServerFeature sf WHERE sf.featureName = :featureName"),
               @NamedQuery(name="SERVER_FEATURE_NAMES", query="SELECT sf.featureName FROM ServerFeature sf")})
public class ServerFeatureImpl implements IServerFeature
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", nullable=false)
    private int id;

    @Version
    @Column(name="jpa_version")
    @SuppressWarnings("unused")
    private int jpaVersion;

    @Basic(optional=false)
    @Column(name="featureName", nullable=false)
    private String featureName;

    @Basic(optional=false)
    @Column(name="className", nullable=false)
    private String className;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IServerFeature#getClassName()
     */
    public String getClassName()
    {
        return className;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IServerFeature#setClassName(java.lang.String)
     */
    public void setClassName(String className)
    {
        this.className = className;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IServerFeature#getFeatureName()
     */
    public String getFeatureName()
    {
        return featureName;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IServerFeature#setFeatureName(java.lang.String)
     */
    public void setFeatureName(String featureName)
    {
        this.featureName = featureName;
    }
    
    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IBean#getId()
     */
    public Object getId()
    {
        return id;
    }
}
