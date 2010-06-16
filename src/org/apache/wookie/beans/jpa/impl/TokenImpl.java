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

import org.apache.wookie.beans.IToken;
import org.apache.wookie.beans.jpa.IInverseRelationship;

/**
 * TokenImpl - JPA IToken implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="Token")
@Table(name="Token")
public class TokenImpl implements IToken, IInverseRelationship<WidgetInstanceImpl>
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
    @Column(name="requestUrl", nullable=false)
    private String requestUrl;

    @Basic(optional=false)
    @Column(name="accessUrl", nullable=false)
    private String accessUrl;

    @Basic(optional=false)
    @Column(name="authzUrl", nullable=false)
    private String authzUrl;

    @Basic(optional=false)
    @Column(name="requestToken", nullable=false)
    private String requestToken;

    @Basic(optional=false)
    @Column(name="accessToken", nullable=false)
    private String accessToken;

    @Basic(optional=false)
    @Column(name="tokenSecret", nullable=false)
    private String tokenSecret;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="widget_instance_id", referencedColumnName="id")
    @SuppressWarnings("unused")
    private WidgetInstanceImpl widgetInstance;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.util.IInverseRelationship#updateInverseRelationship(java.lang.Object)
     */
    public void updateInverseRelationship(WidgetInstanceImpl owningObject)
    {
        widgetInstance = owningObject;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getAccessToken()
     */
    public String getAccessToken()
    {
        return accessToken;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setAccessToken(java.lang.String)
     */
    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getAccessUrl()
     */
    public String getAccessUrl()
    {
        return accessUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setAccessUrl(java.lang.String)
     */
    public void setAccessUrl(String accessUrl)
    {
        this.accessUrl = accessUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getAuthzUrl()
     */
    public String getAuthzUrl()
    {
        return authzUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setAuthzUrl(java.lang.String)
     */
    public void setAuthzUrl(String authzUrl)
    {
        this.authzUrl = authzUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getRequestToken()
     */
    public String getRequestToken()
    {
        return requestToken;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setRequestToken(java.lang.String)
     */
    public void setRequestToken(String requestToken)
    {
        this.requestToken = requestToken;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getRequestUrl()
     */
    public String getRequestUrl()
    {
        return requestUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setRequestUrl(java.lang.String)
     */
    public void setRequestUrl(String requestUrl)
    {
        this.requestUrl = requestUrl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#getTokenSecret()
     */
    public String getTokenSecret()
    {
        return tokenSecret;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IToken#setTokenSecret(java.lang.String)
     */
    public void setTokenSecret(String tokenSecret)
    {
        this.tokenSecret = tokenSecret;
    }
}
