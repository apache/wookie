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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.openjpa.persistence.ElementDependent;
import org.apache.openjpa.persistence.ExternalValues;
import org.apache.openjpa.persistence.Type;

import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IToken;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.jpa.InverseRelationshipCollection;

/**
 * WidgetInstanceImpl - JPA IWidgetInstance implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Entity(name="WidgetInstance")
@Table(name="WidgetInstance")
@NamedQueries({@NamedQuery(name="WIDGET_INSTANCE", query="SELECT wi FROM WidgetInstance wi JOIN wi.widget w JOIN w.widgetTypes wt WHERE wi.apiKey = :apiKey AND wi.userId = :userId AND wi.sharedDataKey = :sharedDataKey AND wt.widgetContext = :widgetContext"),
               @NamedQuery(name="WIDGET_INSTANCE_GUID", query="SELECT wi FROM WidgetInstance wi JOIN wi.widget w WHERE wi.apiKey = :apiKey AND wi.userId = :userId AND wi.sharedDataKey = :sharedDataKey AND w.guid = :guid"),
               @NamedQuery(name="WIDGET_INSTANCE_ID", query="SELECT wi FROM WidgetInstance wi WHERE wi.idKey = :idKey")})
public class WidgetInstanceImpl implements IWidgetInstance
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
    @Column(name="apikey", nullable=false)
    private String apiKey;

    @Basic(optional=false)
    @Column(name="userId", nullable=false)
    private String userId;

    @Basic
    @Column(name="sharedDataKey")
    private String sharedDataKey;

    @Basic
    @Column(name="nonce")
    private String nonce;

    @Basic(optional=false)
    @Column(name="idKey", nullable=false)
    private String idKey;

    @Basic(optional=false)
    @Column(name="opensocialToken", nullable=false)
    private String opensocialToken;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="widget_id", referencedColumnName="id", nullable=false)
    private WidgetImpl widget;

    @Basic
    @Column(name="updated")
    @ExternalValues({"true=t","false=f","null="})
    @Type(String.class)
    private Boolean updated;
    
    @Basic
    @Column(name="shown")
    @ExternalValues({"true=t","false=f","null="})
    @Type(String.class)
    private Boolean shown;
    
    @Basic
    @Column(name="hidden")
    @ExternalValues({"true=t","false=f","null="})
    @Type(String.class)
    private Boolean hidden;
    
    @Basic
    @Column(name="locked")
    @ExternalValues({"true=t","false=f","null="})
    @Type(String.class)
    private Boolean locked;
    
    @Basic
    @Column(name="lang")
    private String lang;
    
    @OneToMany(mappedBy="widgetInstance", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<PreferenceImpl> preferences;

    @OneToMany(mappedBy="widgetInstance", fetch=FetchType.LAZY, cascade=CascadeType.ALL)
    @ElementDependent
    private Collection<TokenImpl> tokens;

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getApiKey()
     */
    public String getApiKey()
    {
        return apiKey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setApiKey(java.lang.String)
     */
    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IBean#getId()
     */
    public Object getId()
    {
        return id;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getIdKey()
     */
    public String getIdKey()
    {
        return idKey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setIdKey(java.lang.String)
     */
    public void setIdKey(String idKey)
    {
        this.idKey = idKey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getLang()
     */
    public String getLang()
    {
        return lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setLang(java.lang.String)
     */
    public void setLang(String lang)
    {
        this.lang = lang;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getNonce()
     */
    public String getNonce()
    {
        return nonce;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setNonce(java.lang.String)
     */
    public void setNonce(String nonce)
    {
        this.nonce = nonce;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getOpensocialToken()
     */
    public String getOpensocialToken()
    {
        return opensocialToken;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setOpensocialToken(java.lang.String)
     */
    public void setOpensocialToken(String opensocialToken)
    {
        this.opensocialToken = opensocialToken;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getPreferences()
     */
    public Collection<IPreference> getPreferences()
    {
        if (preferences == null)
        {
            preferences = new ArrayList<PreferenceImpl>();
        }
        return new InverseRelationshipCollection<WidgetInstanceImpl,PreferenceImpl,IPreference>(this, preferences);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setPreferences(java.util.Collection)
     */
    public void setPreferences(Collection<IPreference> preferences)
    {
        getPreferences().clear();
        if (preferences != null)
        {
            for (IPreference preference : preferences)
            {
                getPreferences().add((PreferenceImpl)preference);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getSharedDataKey()
     */
    public String getSharedDataKey()
    {
        return sharedDataKey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setSharedDataKey(java.lang.String)
     */
    public void setSharedDataKey(String sharedDataKey)
    {
        this.sharedDataKey = sharedDataKey;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getTokens()
     */
    public Collection<IToken> getTokens()
    {
        if (tokens == null)
        {
            tokens = new ArrayList<TokenImpl>();
        }
        return new InverseRelationshipCollection<WidgetInstanceImpl,TokenImpl,IToken>(this, tokens);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setTokens(java.util.Collection)
     */
    public void setTokens(Collection<IToken> tokens)
    {
        getTokens().clear();
        if (tokens != null)
        {
            for (IToken token : tokens)
            {
                getTokens().add((TokenImpl)token);
            }
        }
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getUserId()
     */
    public String getUserId()
    {
        return userId;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setUserId(java.lang.String)
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getWidget()
     */
    public IWidget getWidget()
    {
        return widget;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setWidget(org.apache.wookie.beans.IWidget)
     */
    public void setWidget(IWidget widget)
    {
        this.widget = (WidgetImpl)widget;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#isHidden()
     */
    public boolean isHidden()
    {
        return ((hidden != null) ? hidden.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setHidden(boolean)
     */
    public void setHidden(boolean hidden)
    {
        this.hidden = (hidden ? Boolean.TRUE : Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#isLocked()
     */
    public boolean isLocked()
    {
        return ((locked != null) ? locked.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setLocked(boolean)
     */
    public void setLocked(boolean locked)
    {
        this.locked = (locked ? Boolean.TRUE : Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#isShown()
     */
    public boolean isShown()
    {
        return ((shown != null) ? shown.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setShown(boolean)
     */
    public void setShown(boolean shown)
    {
        this.shown = (shown ? Boolean.TRUE : Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#isUpdated()
     */
    public boolean isUpdated()
    {
        return ((updated != null) ? updated.booleanValue() : false);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setUpdated(boolean)
     */
    public void setUpdated(boolean updated)
    {
        this.updated = (updated ? Boolean.TRUE : Boolean.FALSE);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getPreference(java.lang.String)
     */
    public IPreference getPreference(String key)
    {
        return Utilities.getPreference(this, key);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getSharedData()
     */
    public ISharedData [] getSharedData()
    {
        return Utilities.getSharedData(this);
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getSharedData(java.lang.String)
     */
    public ISharedData getSharedData(String name)
    {
        return Utilities.getSharedData(this, name);
    }
}
