/*
 * Copyright (c) 2008, Consortium Board TENCompetence
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the TENCompetence nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY CONSORTIUM BOARD TENCOMPETENCE ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL CONSORTIUM BOARD TENCOMPETENCE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.tencompetence.widgetservice.ajaxmodel.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.manager.IWidgetAPIManager;
import org.tencompetence.widgetservice.manager.impl.WidgetAPIManager;
import org.tencompetence.widgetservice.server.LocaleHandler;

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
 * @version $Id: WidgetAPIImpl.java,v 1.21 2009-06-06 20:20:03 scottwilson Exp $
 *
 */
public class WidgetAPIImpl implements IWidgetAPI {

	static Logger _logger = Logger.getLogger(WidgetAPIImpl.class.getName());

	public WidgetAPIImpl(){}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#preferences(java.lang.String)
	 */
	public Map<String, String> preferences(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		HashMap<String, String> prefs = new HashMap<String,String>();
		if(id_key == null){
			prefs.put("message", localizedMessages.getString("WidgetAPIImpl.0"));	 //$NON-NLS-1$
			return prefs;
		}
		IWidgetAPIManager manager = WidgetAPIManager.getManager(session, localizedMessages);
		// check if instance is valid
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance==null){
			prefs.put("message", localizedMessages.getString("WidgetAPIImpl.0")); //$NON-NLS-1$
			return prefs;
		}
		for(Preference preference : manager.getPreferenceForInstance(widgetInstance)){
			prefs.put(preference.getDkey(), preference.getDvalue());
		}
		return prefs;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#preferenceForKey(java.lang.String, java.lang.String)
	 */
	public String preferenceForKey(String id_key, String key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		if(id_key == null) return localizedMessages.getString("WidgetAPIImpl.0");
		if(key == null)return localizedMessages.getString("WidgetAPIImpl.1");
		IWidgetAPIManager manager = WidgetAPIManager.getManager(session, localizedMessages);
		// check if instance is valid
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		Preference preference = manager.getPreferenceForInstance(widgetInstance, key);
		if (preference == null) return localizedMessages.getString("WidgetAPIImpl.1");
		return preference.getDvalue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#sharedDataForKey(java.lang.String, java.lang.String)
	 */
	public String sharedDataForKey(String id_key, String key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		if(id_key==null) return localizedMessages.getString("WidgetAPIImpl.0");
		if(key==null) return localizedMessages.getString("WidgetAPIImpl.1");
		IWidgetAPIManager manager = WidgetAPIManager.getManager(session, localizedMessages);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		return manager.getSharedDataValue(widgetInstance, key);
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#setPreferenceForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setPreferenceForKey(String id_key, String key, String value) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetAPIManager manager = WidgetAPIManager.getManager(session, localizedMessages);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if (widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		manager.updatePreference(widgetInstance, key, value);
		return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#setSharedDataForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setSharedDataForKey(String id_key, String key, String value) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetAPIManager manager = WidgetAPIManager.getManager(session, localizedMessages);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		if(manager.isInstanceLocked(widgetInstance)) return localizedMessages.getString("WidgetAPIImpl.2");
		//
		manager.updateSharedDataEntry(widgetInstance, key, value, false);
		notifyWidgets(widgetInstance);
		return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#appendSharedDataForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String appendSharedDataForKey(String id_key, String key, String value) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetAPIManager manager = WidgetAPIManager.getManager(session, localizedMessages);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		if(manager.isInstanceLocked(widgetInstance)) return localizedMessages.getString("WidgetAPIImpl.2");
		//
		manager.updateSharedDataEntry(widgetInstance, key, value, true);
		notifyWidgets(widgetInstance);
		return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#lock(java.lang.String)
	 */
	public String lock(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetAPIManager manager = WidgetAPIManager.getManager(session, localizedMessages);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		String sharedDataKey = widgetInstance.getSharedDataKey();
		manager.lockWidgetInstance(widgetInstance);
		callback(widgetInstance, "Widget.onLocked(\""+sharedDataKey+"\");");//$NON-NLS-1$
        return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#unlock(java.lang.String)
	 */
	public String unlock(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetAPIManager manager = WidgetAPIManager.getManager(session, localizedMessages);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance==null) return localizedMessages.getString("WidgetAPIImpl.0");
		//
		String sharedDataKey = widgetInstance.getSharedDataKey();
		manager.unlockWidgetInstance(widgetInstance);
		callback(widgetInstance, "Widget.onUnlocked(\""+sharedDataKey+"\");");//$NON-NLS-1$
        return "okay"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#hide(java.lang.String)
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
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#show(java.lang.String)
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
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#contextPropertyForKey(java.lang.String, java.lang.String)
	 */
	public String contextPropertyForKey(String id_key, String key) {
		return "Not available"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#setContextPropertyForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setContextPropertyForKey(String id_key, String key, String value) {
		return "Not available"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#setUserPropertyForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setUserPropertyForKey(String id_key, String key, String value) {
		return "Not available"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#userPropertyForKey(java.lang.String, java.lang.String)
	 */
	public String userPropertyForKey(String id_key, String key) {
		return "Not available"; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#openURL(java.lang.String)
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