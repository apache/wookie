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

package org.apache.wookie.beans;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.w3c.ILocalizedElement;

/**
 * IWidgetInstance - a simple bean to model an actual widgets instance attributes.
 * 
 * @author Paul Sharples
 * @author <a href="mailto:rwatler@apache.org">Randy Watler</a>
 * @version $Id$
 */
public interface IWidgetInstance extends IBean, ILocalizedElement
{
    /**
     * Get owning widget instance.
     * 
     * @return widget instance
     */
    IWidget getWidget();

    /**
     * Set owning widget instance.
     * 
     * @param widget widget instance
     */
    void setWidget(IWidget widget);

    /**
     * Set widget instance language.
     * 
     * @param lang language
     */
    void setLang(String lang);
    
    /**
     * Get widget instance shared data key.
     * 
     * @return shared data key
     */
    String getSharedDataKey();
    
    /**
     * Set widget instance shared data key.
     * 
     * @param sharedDataKey shared data key
     */
    void setSharedDataKey(String sharedDataKey);
    
    /**
     * Get widget instance hidden flag.
     * 
     * @return hidden flag
     */
    boolean isHidden();
    
    /**
     * Set widget instance hidden flag.
     * 
     * @param hidden hidden flag
     */
    void setHidden(boolean hidden);
    
    /**
     * Get widget instance id key.
     * 
     * @return id key
     */
    String getIdKey();
    
    /**
     * Set widget instance id key.
     * 
     * @param idKey id key
     */
    void setIdKey(String idKey);
    
    /**
     * Get widget instance API key.
     * 
     * @return API key
     */
    String getApiKey();
    
    /**
     * Set widget instance API key.
     * 
     * @param apiKey API key
     */
    void setApiKey(String apiKey);
    
    /**
     * Get widget instance OpenSocial token.
     * 
     * @return OpenSocial token
     */
    String getOpensocialToken();
    
    /**
     * Set widget instance OpenSocial token.
     * 
     * @param opensocialToken OpenSocial token
     */
    void setOpensocialToken(String opensocialToken);
    
    /**
     * Get widget instance nonce string.
     * 
     * @return nonce string
     */
    String getNonce();
    
    /**
     * Set widget instance nonce string.
     * 
     * @param nonce nonce string.
     */
    void setNonce(String nonce);
    
    /**
     * Get widget instance shown flag.
     * 
     * @return shown flag
     */
    boolean isShown();
    
    /**
     * Set widget instance shown flag.
     * 
     * @param shown shown flag
     */
    void setShown(boolean shown);
    
    /**
     * Get widget instance updated flag.
     * 
     * @return updated flag
     */
    boolean isUpdated();
    
    /**
     * Set widget instance updated flag.
     * 
     * @param updated updated flag
     */
    void setUpdated(boolean updated);
    
    /**
     * Get widget instance user id.
     * 
     * @return user id
     */
    String getUserId();
    
    /**
     * Set widget instance user id.
     * 
     * @param userId user id
     */
    void setUserId(String userId);
    
    /**
     * Get widget instance locked flag.
     * 
     * @return locked flag
     */
    boolean isLocked();
    
    /**
     * Set widget instance locked flag.
     * 
     * @param locked locked flag
     */
    void setLocked(boolean locked);
    
    /**
     * Get collection of widget instance preferences.
     * 
     * @return preferences collection
     */
    Collection<IPreference> getPreferences();

    /**
     * Set collection of widget instance preferences.
     * 
     * @param preferences preferences collection
     */
    void setPreferences(Collection<IPreference> preferences);

    /**
     * Get preference with specified key for widget instance.
     * 
     * @param key shared data key
     * @return shared data
     */
    IPreference getPreference(String key);

    /**
     * Shared implementation utilities.
     */
    public static class Utilities
    {

      /**
       * Get preference with specified key for widget instance.
       * 
       * @param key shared data key
       * @return shared data
       */
      public static IPreference getPreference(IWidgetInstance widgetInstance, String key)
      {
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("widgetInstance", widgetInstance);//$NON-NLS-1$
        map.put("dkey", key);//$NON-NLS-1$
        IPreference[] preference = persistenceManager.findByValues(IPreference.class, map);
        if (preference.length == 1) return preference[0];
        return null;
      }
    }
}
