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

package org.apache.wookie.beans.jcr.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.jackrabbit.ocm.manager.beanconverter.impl.ReferenceBeanConverterImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Bean;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.jcr.IPathBean;
import org.apache.wookie.beans.jcr.IUuidBean;
import org.apache.wookie.beans.jcr.IdCollection;
import org.apache.wookie.beans.jcr.JCRPersistenceManager;

/**
 * WidgetInstanceImpl - JCR OCM IWidgetInstance implementation.
 * 
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
@Node(jcrType="wookie:widgetInstance", jcrMixinTypes="mix:referenceable", discriminator=false)
public class WidgetInstanceImpl implements IWidgetInstance, IPathBean, IUuidBean
{
    @Field(path=true)
    private String path;
    
    @Field(uuid=true)
    private String uuid;
    
    @Field(jcrName="wookie:apiKey")
    private String apiKey;

    @Field(jcrName="wookie:userId")
    private String userId;

    @Field(jcrName="wookie:sharedDataKey")
    private String sharedDataKey;

    @Field(jcrName="wookie:nonce")
    private String nonce;

    @Field(jcrName="wookie:idKey")
    private String idKey;

    @Field(jcrName="wookie:opensocialToken")
    private String opensocialToken;
    
    @Field(jcrName="wookie:updated")
    private Boolean updated;
    
    @Field(jcrName="wookie:shown")
    private Boolean shown;
    
    @Field(jcrName="wookie:hidden")
    private Boolean hidden;
    
    @Field(jcrName="wookie:locked")
    private Boolean locked;
    
    @Field(jcrName="wookie:lang")
    private String lang;
    
    @Bean(jcrName="wookie:widget", converter=ReferenceBeanConverterImpl.class)
    private WidgetImpl widgetImpl;

    @org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection(jcrName="wookie:preferences", elementClassName=PreferenceImpl.class)    
    private Collection<PreferenceImpl> preferenceImpls;

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
        return path;
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
     * @see org.apache.wookie.beans.jcr.IPathBean#getNodePath()
     */
    public String getNodePath()
    {
        return path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IPathBean#setNodePath(java.lang.String)
     */
    public String setNodePath(String nodeRootPath)
    {
        if (path == null)
        {
            path = nodeRootPath+"/"+JCRPersistenceManager.escapeJCRName(userId)+"/"+JCRPersistenceManager.escapeJCRName(apiKey)+"/"+JCRPersistenceManager.escapeJCRName(sharedDataKey)+"/"+JCRPersistenceManager.escapeJCRName(widgetImpl.getGuid());
        }
        return path;
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

    /**
     * Get raw persistent JCR absolute node path.
     * 
     * @return absolute path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * Set raw persistent JCR absolute node path.
     * 
     * @param path absolute path
     */
    public void setPath(String path)
    {
        this.path = path;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getPreferences()
     */
    public Collection<IPreference> getPreferences()
    {
        if (preferenceImpls == null)
        {
            preferenceImpls = new ArrayList<PreferenceImpl>();
        }
        return new IdCollection<PreferenceImpl,IPreference>(preferenceImpls);
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

    /**
     * Get preference implementations collection.
     * 
     * @return preference implementations collection
     */
    public Collection<PreferenceImpl> getPreferenceImpls()
    {
        return preferenceImpls;
    }

    /**
     * Set preference implementations collection.
     * 
     * @param preferenceImpls preference implementations collection
     */
    public void setPreferenceImpls(Collection<PreferenceImpl> preferenceImpls)
    {
        this.preferenceImpls = preferenceImpls;
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
        return widgetImpl;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#setWidget(org.apache.wookie.beans.IWidget)
     */
    public void setWidget(IWidget widget)
    {
        this.widgetImpl = (WidgetImpl)widget;
    }
    
    /**
     * Get widget implementation.
     * 
     * @return widget implementation
     */
    public WidgetImpl getWidgetImpl()
    {
        return widgetImpl;
    }

    /**
     * Set widget implementation.
     * 
     * @return widgetImpl widget implementation
     */
    public void setWidgetImpl(WidgetImpl widgetImpl)
    {
        this.widgetImpl = widgetImpl;
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
     * @see org.apache.wookie.beans.jcr.IUuidBean#getUuid()
     */
    public String getUuid()
    {
        return uuid;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.jcr.IUuidBean#setUuid(java.lang.String)
     */
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    /* (non-Javadoc)
     * @see org.apache.wookie.beans.IWidgetInstance#getPreference(java.lang.String)
     */
    public IPreference getPreference(String key)
    {
        return Utilities.getPreference(this, key);
    }

}
