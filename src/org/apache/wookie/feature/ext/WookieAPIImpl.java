/*
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 */

package org.apache.wookie.feature.ext;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.controller.WidgetInstancesController;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.SharedDataHelper;
import org.apache.wookie.queues.QueueManager;
import org.apache.wookie.server.ContextListener;
import org.apache.wookie.server.LocaleHandler;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Wookie Extensions API Implementation
 */
public class WookieAPIImpl implements IWookieExtensionAPI {

  static Logger _logger = Logger.getLogger(WookieAPIImpl.class.getName());
  
  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#preferenceForKey(java.lang.String, java.lang.String)
   * 
   * DEPRECATED: This was replaced by the W3C Storage API
   */
  @Deprecated
  public String preferenceForKey(String id_key, String key) {
    HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
    Messages localizedMessages = LocaleHandler.localizeMessages(request);
    if(id_key == null) return localizedMessages.getString("WidgetAPIImpl.0");
    if(key == null)return localizedMessages.getString("WidgetAPIImpl.1");
    // check if instance is valid
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
    if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
    //
    IPreference preference = widgetInstance.getPreference(key);
    if (preference == null) return localizedMessages.getString("WidgetAPIImpl.1");
    return preference.getValue();
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#sharedDataForKey(java.lang.String, java.lang.String)
   * 
   * DEPRECATED: This was replaced by the Wave Gadget API
   */
  @Deprecated
  public String sharedDataForKey(String id_key, String key) {
    HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
    Messages localizedMessages = LocaleHandler.localizeMessages(request);
    if(id_key==null) return localizedMessages.getString("WidgetAPIImpl.0");
    if(key==null) return localizedMessages.getString("WidgetAPIImpl.1");
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
    if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
    ISharedData data = new SharedContext(widgetInstance).getSharedData(key);
    if (data == null) return localizedMessages.getString("WidgetAPIImpl.1");
    return data.getDvalue();
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#setSharedDataForKey(java.lang.String, java.lang.String, java.lang.String)
   * 
   * DEPRECATED: This was replaced by the Wave Gadget API
   */
  @Deprecated
  @SuppressWarnings("static-access")
  public String setSharedDataForKey(String id_key, String key, String value) {
    HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
    Messages localizedMessages = LocaleHandler.localizeMessages(request);
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
    IWidgetInstance widgetInstance;//
    widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
    if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
    if(widgetInstance.isLocked()) return localizedMessages.getString("WidgetAPIImpl.2");
    if(ContextListener.useSharedDataInstanceQueues){//  
      QueueManager.getInstance().queueSetSharedDataRequest(id_key, SharedDataHelper.getInternalSharedDataKey(widgetInstance), key, value, false);
    }
    else{
      new SharedContext(widgetInstance).updateSharedData(key, value, false);
    }
    Notifier.notifySiblings(widgetInstance);
    return "okay"; //$NON-NLS-1$
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#lock(java.lang.String)
   */
  public String lock(String id_key) {
    HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
    Messages localizedMessages = LocaleHandler.localizeMessages(request);
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
    if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
    //
    String sharedDataKey = SharedDataHelper.getInternalSharedDataKey(widgetInstance);
    WidgetInstancesController.lockWidgetInstance(widgetInstance);
    Notifier.callSiblings(widgetInstance,"Widget.onLocked(\""+sharedDataKey+"\");");//$NON-NLS-1$
        return "okay"; //$NON-NLS-1$
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#unlock(java.lang.String)
   */
  public String unlock(String id_key) {
    HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
    Messages localizedMessages = LocaleHandler.localizeMessages(request);
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
    if(widgetInstance==null) return localizedMessages.getString("WidgetAPIImpl.0");
    //
    String sharedDataKey = SharedDataHelper.getInternalSharedDataKey(widgetInstance);
    WidgetInstancesController.unlockWidgetInstance(widgetInstance);
    Notifier.callSiblings(widgetInstance,"Widget.onUnlocked(\""+sharedDataKey+"\");");//$NON-NLS-1$
        return "okay"; //$NON-NLS-1$
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#hide(java.lang.String)
   */
  public String hide(String id_key){
    HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
    Messages localizedMessages = LocaleHandler.localizeMessages(request);
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
    if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
    //
    Notifier.callSiblings(widgetInstance,"window.onHide()");//$NON-NLS-1$
      return "okay"; //$NON-NLS-1$
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#show(java.lang.String)
   */
  public String show(String id_key){
    HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
    Messages localizedMessages = LocaleHandler.localizeMessages(request);
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
    if(widgetInstance==null) return localizedMessages.getString("WidgetAPIImpl.0");
    Notifier.callSiblings(widgetInstance,"window.onShow()"); //$NON-NLS-1$
      return "okay"; //$NON-NLS-1$
  }

  /*
   * (non-Javadoc)
   * @see org.apache.wookie.ajaxmodel.IWidgetAPI#openURL(java.lang.String)
   */
  // DEPRICATED - implemented in local js object instead
  // NOTE - might not need this - we can call window.open in a browser -
  // The only reason to send the call to this servlet is if we somehow wish to
  // update other users.
  @Deprecated
  public String openURL(String url) {
    _logger.debug("openurl called with        "+ url ); //$NON-NLS-1$
    WebContext wctx = WebContextFactory.get();
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript("window.open(") //$NON-NLS-1$
        .appendData(url)
        .appendScript(");");        //$NON-NLS-1$
        wctx.getScriptSession().addScript(script);
        return ""; //$NON-NLS-1$
  }
  
  /*
   * Note: this method may be deprecated in a future release 
   */
  @SuppressWarnings("static-access")
  public String appendSharedDataForKey(String id_key, String key, String value) {
    HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
    Messages localizedMessages = LocaleHandler.localizeMessages(request);               
    IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();   
    IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
    if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
    if(widgetInstance.isLocked()) return localizedMessages.getString("WidgetAPIImpl.2");
    if(ContextListener.useSharedDataInstanceQueues){//
      QueueManager.getInstance().queueSetSharedDataRequest(id_key, SharedDataHelper.getInternalSharedDataKey(widgetInstance), key, value, true);
    }
    else{
      new SharedContext(widgetInstance).updateSharedData(key, value, true);
    }
    Notifier.notifySiblings(widgetInstance);
    return "okay"; //$NON-NLS-1$
  }

}
