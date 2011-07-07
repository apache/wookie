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
 * limitations under the License.
 */

package org.apache.wookie.ajaxmodel.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.ajaxmodel.IWidgetAPI;
import org.apache.wookie.ajaxmodel.IWookieExtensionAPI;
import org.apache.wookie.beans.IDescription;
import org.apache.wookie.beans.IName;
import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IWidget;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.controller.PropertiesController;
import org.apache.wookie.controller.WidgetInstancesController;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.SharedDataHelper;
import org.apache.wookie.queues.QueueManager;
import org.apache.wookie.server.ContextListener;
import org.apache.wookie.server.LocaleHandler;
import org.apache.wookie.util.WidgetFormattingUtils;
import org.apache.wookie.w3c.util.LocalizationUtils;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * Implementation of the widget API.  This class models the the javascript implementation of
 * the w3c widget API.  Using DWR - a javascript/HTML client which has included the correct js files...
 *
 *   <script type='text/javascript' src='/wookie/dwr/interface/WidgetImpl.js'></script>
 *   <script type='text/javascript' src='/wookie/dwr/engine.js'></script>
 *   <script type='text/javascript' src='/wookie/shared/js/wookie-wrapper.js'></script>
 *
 *   ...can then access this classes methods via a call for example like...
 *
 *   Widget.preferenceForKey("Username", callbackFunctionName);
 *
 *   							and
 *
 *   Widget.setSharedDataForKey("defaultChatPresence",stringWithUserRemoved);
 *
 * @author Paul Sharples
 * @version $Id: WidgetAPIImpl.java,v 1.3 2009-09-14 21:15:07 scottwilson Exp $
 *
 */
public class WidgetAPIImpl implements IWidgetAPI, IWookieExtensionAPI {

	static Logger _logger = Logger.getLogger(WidgetAPIImpl.class.getName());

	public WidgetAPIImpl(){}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#preferences(java.lang.String)
	 */
	public List<IPreference> preferences(String id_key) {
		ArrayList<IPreference> prefs = new ArrayList<IPreference>();
		if(id_key == null) return prefs;
		// check if instance is valid
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
		if(widgetInstance==null) return prefs;
		for(IPreference preference : widgetInstance.getPreferences()){
			prefs.add(new PreferenceDelegate(preference));
		}
		return prefs;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#metadata(java.lang.String)
	 */
	public Map<String, String> metadata(String id_key) {
		Map<String, String> map = new HashMap<String, String>();
		if(id_key == null) return map;
	
		// check if instance is valid
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
		if(widgetInstance==null) return map;
		
		// Get i18n-enabled metadata for the Widget's locale and encode it using unicode control characters.
		
		String locales[] = {widgetInstance.getLang()};
		IWidget widget = widgetInstance.getWidget();
			
		String author = "";
        String email = "";
        String href = "";
		if (widget.getAuthor() != null){
            if (widget.getAuthor().getAuthor() != null) author = WidgetFormattingUtils.getEncoded(widget.getAuthor().getDir(), widget.getAuthor().getAuthor());
	        if (widget.getAuthor().getEmail() != null) email = widget.getAuthor().getEmail();
	        if (widget.getAuthor().getHref() != null) href = widget.getAuthor().getHref();
		}

		String name = "";
		IName iname = (IName)LocalizationUtils.getLocalizedElement(widget.getNames().toArray(new IName[widget.getNames().size()]), locales, widget.getDefaultLocale());
		if (iname != null && iname.getName() != null) name = WidgetFormattingUtils.getEncoded(iname.getDir(), iname.getName());
		String shortName = "";
		if (iname != null && iname.getShortName() != null) shortName = WidgetFormattingUtils.getEncoded(iname.getDir(), iname.getShortName());
		
		String description = "";
		IDescription idescription = (IDescription)LocalizationUtils.getLocalizedElement(widget.getDescriptions().toArray(new IDescription[widget.getDescriptions().size()]), locales, widget.getDefaultLocale());
		if (idescription != null && idescription.getContent() != null) description = WidgetFormattingUtils.getEncoded(idescription.getDir(), idescription.getContent());
		
		String version = "";
		if (widget.getVersion() != null) version = WidgetFormattingUtils.getEncoded(widget.getDir(), widget.getVersion());
		
		String width = "0";
		if (widget.getWidth() != null) width = String.valueOf(widget.getWidth());
		
		String height = "0";
		if (widget.getHeight() != null) height = String.valueOf(widget.getHeight());
		
		// Add in metadata

		map.put("id", String.valueOf(widget.getGuid()));	//$NON-NLS-1$
		map.put("author", author);	//$NON-NLS-1$
		map.put("authorEmail", email);//$NON-NLS-1$
		map.put("authorHref", href);//$NON-NLS-1$
		map.put("name", name);//$NON-NLS-1$
		map.put("description", description);//$NON-NLS-1$	
		map.put("shortName", shortName); //$NON-NLS-1$
		map.put("version",version);//$NON-NLS-1$
		map.put("width", width);//$NON-NLS-1$
		map.put("height", height);//$NON-NLS-1$
		
		return map;
	}

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
		return preference.getDvalue();
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
		ISharedData data = SharedDataHelper.findSharedData(widgetInstance, key);
		if (data == null) return localizedMessages.getString("WidgetAPIImpl.1");
		return data.getDvalue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#setPreferenceForKey(java.lang.String, java.lang.String, java.lang.String)
	 *    
	 * DEPRECATED: This was replaced by the W3C Storage API. We do require a method to respond to preference storage events on
	 * the client so this method will be retained unless/until an alternative is implemented, but widget authors are strongly discouraged
	 * from invoking this method in client code.
	 */
	@Deprecated
	@SuppressWarnings("static-access")
	public String setPreferenceForKey(String id_key, String key, String value) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);		
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();		
		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
		if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		if(ContextListener.usePreferenceInstanceQueues){
			QueueManager.getInstance().queueSetPreferenceRequest(id_key, key, value);	
		}
		else{
			PropertiesController.updatePreference(widgetInstance, key, value);
		}
		return "okay"; //$NON-NLS-1$
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
			QueueManager.getInstance().queueSetSharedDataRequest(id_key, widgetInstance.getSharedDataKey(), key, value, false);
		}
		else{
			PropertiesController.updateSharedDataEntry(widgetInstance, key, value, false);
		}
		Notifier.notifySiblings(widgetInstance);
		return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#appendSharedDataForKey(java.lang.String, java.lang.String, java.lang.String)
	 * 
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
			QueueManager.getInstance().queueSetSharedDataRequest(id_key, widgetInstance.getSharedDataKey(), key, value, true);
		}
		else{
			PropertiesController.updateSharedDataEntry(widgetInstance, key, value, true);
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
		String sharedDataKey = widgetInstance.getSharedDataKey();
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
		String sharedDataKey = widgetInstance.getSharedDataKey();
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

}