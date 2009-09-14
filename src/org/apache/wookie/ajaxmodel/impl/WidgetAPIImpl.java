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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.wookie.Messages;
import org.apache.wookie.ajaxmodel.IWidgetAPI;
import org.apache.wookie.beans.Preference;
import org.apache.wookie.beans.SharedData;
import org.apache.wookie.beans.Widget;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.controller.PropertiesController;
import org.apache.wookie.controller.WidgetInstancesController;
import org.apache.wookie.server.LocaleHandler;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
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
public class WidgetAPIImpl implements IWidgetAPI {

	static Logger _logger = Logger.getLogger(WidgetAPIImpl.class.getName());

	public WidgetAPIImpl(){}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#preferences2(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Preference> preferences(String id_key) {
		ArrayList<Preference> prefs = new ArrayList<Preference>();
		if(id_key == null) return prefs;
		// check if instance is valid
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance==null) return prefs;
		for(Preference preference : Preference.findPreferencesForInstance(widgetInstance)){
			prefs.add(preference);
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
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance==null) return map;
		// Add in metadata
		Widget widget = widgetInstance.getWidget();
		map.put("id", String.valueOf(widget.getGuid()));	//$NON-NLS-1$
		map.put("author", String.valueOf(widget.getWidgetAuthor()));	//$NON-NLS-1$
		//TODO map.put("authorEmail", String.valueOf(widget.getWidth()));//$NON-NLS-1$
		//TODO map.put("authorHref", String.valueOf(widget.getHeight()));//$NON-NLS-1$
		map.put("name", String.valueOf(widget.getWidgetTitle()));//$NON-NLS-1$
		map.put("description", String.valueOf(widget.getWidgetDescription()));//$NON-NLS-1$	
		map.put("version", widget.getVersion());//$NON-NLS-1$
		map.put("width", String.valueOf(widget.getWidth()));//$NON-NLS-1$
		map.put("height", String.valueOf(widget.getHeight()));//$NON-NLS-1$
		return map;
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#preferenceForKey(java.lang.String, java.lang.String)
	 */
	public String preferenceForKey(String id_key, String key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		if(id_key == null) return localizedMessages.getString("WidgetAPIImpl.0");
		if(key == null)return localizedMessages.getString("WidgetAPIImpl.1");
		// check if instance is valid
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		Preference preference = Preference.findPreferenceForInstance(widgetInstance, key);
		if (preference == null) return localizedMessages.getString("WidgetAPIImpl.1");
		return preference.getDvalue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#sharedDataForKey(java.lang.String, java.lang.String)
	 */
	public String sharedDataForKey(String id_key, String key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		if(id_key==null) return localizedMessages.getString("WidgetAPIImpl.0");
		if(key==null) return localizedMessages.getString("WidgetAPIImpl.1");
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		return SharedData.findSharedDataForInstance(widgetInstance, key).getDvalue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#setPreferenceForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setPreferenceForKey(String id_key, String key, String value) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		PropertiesController.updatePreference(widgetInstance, key, value);
		return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#setSharedDataForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setSharedDataForKey(String id_key, String key, String value) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		if(widgetInstance.isLocked()) return localizedMessages.getString("WidgetAPIImpl.2");
		//
		PropertiesController.updateSharedDataEntry(widgetInstance, key, value, false);
		notifyWidgets(widgetInstance);
		return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#appendSharedDataForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String appendSharedDataForKey(String id_key, String key, String value) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		if(widgetInstance.isLocked()) return localizedMessages.getString("WidgetAPIImpl.2");
		//
		PropertiesController.updateSharedDataEntry(widgetInstance, key, value, true);
		notifyWidgets(widgetInstance);
		return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#lock(java.lang.String)
	 */
	public String lock(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		String sharedDataKey = widgetInstance.getSharedDataKey();
		WidgetInstancesController.lockWidgetInstance(widgetInstance);
		callback(widgetInstance, "Widget.onLocked(\""+sharedDataKey+"\");");//$NON-NLS-1$
        return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#unlock(java.lang.String)
	 */
	public String unlock(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance==null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		String sharedDataKey = widgetInstance.getSharedDataKey();
		WidgetInstancesController.unlockWidgetInstance(widgetInstance);
		callback(widgetInstance, "Widget.onUnlocked(\""+sharedDataKey+"\");");//$NON-NLS-1$
        return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#hide(java.lang.String)
	 */
	public String hide(String id_key){
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		callback(widgetInstance,"window.onHide()");//$NON-NLS-1$
	    return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#show(java.lang.String)
	 */
	public String show(String id_key){
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance==null) return localizedMessages.getString("WidgetAPIImpl.0");
		callback(widgetInstance,"window.onShow()"); //$NON-NLS-1$
	    return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#contextPropertyForKey(java.lang.String, java.lang.String)
	 */
	public String contextPropertyForKey(String id_key, String key) {
		return "Not available"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#setContextPropertyForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setContextPropertyForKey(String id_key, String key, String value) {
		return "Not available"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#setUserPropertyForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setUserPropertyForKey(String id_key, String key, String value) {
		return "Not available"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#userPropertyForKey(java.lang.String, java.lang.String)
	 */
	public String userPropertyForKey(String id_key, String key) {
		return "Not available"; //$NON-NLS-1$
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
	
	/**
	 * Send notifications to other widgets of shared data updates
	 * TODO consider reusing the Notifier
	 */
	private void notifyWidgets(WidgetInstance widgetInstance){
		String sharedDataKey = widgetInstance.getSharedDataKey();
		String script = "Widget.onSharedUpdate(\""+sharedDataKey+"\");"; //$NON-NLS-1$ //$NON-NLS-2$
		callback(widgetInstance, script);
	}
	
	/**
	 * Sends a callback script
	 * @param widgetInstance
	 * @param call
	 */
	private void callback(WidgetInstance widgetInstance, String call){
		WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(call);
        // Loop over all the users on the current page
        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
        for (Iterator<?> it = pages.iterator(); it.hasNext();){
            ScriptSession otherSession = (ScriptSession) it.next();
            otherSession.addScript(script);
        }	
	}

}