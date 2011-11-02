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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.wookie.beans.IOAuthToken;
import org.apache.wookie.beans.IWidgetInstance;


@Entity(name="OAuthToken")
@Table(name="OAuthToken")
@NamedQueries({@NamedQuery(name="ACCESS_TOKEN", query="SELECT o FROM OAuthToken o WHERE o.widgetInstance = :widgetInstance")})
public class OAuthTokenImpl implements IOAuthToken {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", nullable=false)
    private int id;

    @Version
    @Column(name="jpa_version")
    @SuppressWarnings("unused")
    private int jpaVersion;
    
    @Basic(optional=false)
    @Column(name="authzUrl", nullable=true)
    private String authzUrl;

    @Basic(optional=false)
    @Column(name="accessToken", nullable=false)
    private String accessToken;
    
    @Basic(optional=false)
    @Column(name="expires", nullable=false)
    private long expires;
    
    @Basic(optional=false)
    @Column(name="clientId", nullable=false)
    private String clientId;
	
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="widget_instance_id", referencedColumnName="id")
    private WidgetInstanceImpl widgetInstance;
    
	public String getAuthzUrl() {
		return authzUrl;
	}

	public void setAuthzUrl(String authzUrl) {
		this.authzUrl = authzUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}
	
	public boolean isExpires() {
		return System.currentTimeMillis() > expires;
	}

	public Object getId() {
		return id;
	}

	public IWidgetInstance getWidgetInstance() {
		return widgetInstance;
	}

	public void setWidgetInstance(IWidgetInstance widgetInstance) {
		this.widgetInstance = (WidgetInstanceImpl) widgetInstance;
	}
/*
	public void updateInverseRelationship(WidgetInstanceImpl owningObject) {
		this.widgetInstance = owningObject;
	}
*/
}
