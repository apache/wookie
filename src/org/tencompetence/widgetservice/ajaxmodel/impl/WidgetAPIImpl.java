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
import org.tencompetence.widgetservice.beans.Participant;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
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
 * @version $Id: WidgetAPIImpl.java,v 1.17 2009-06-03 15:46:31 scottwilson Exp $
 *
 */
public class WidgetAPIImpl implements IWidgetAPI {

	/*
	TODO - i18n these strings - use locale to get browser language & set that by default
	 - but allow user to change it in the widget using a dropdown menu

    Locale locale = request.getLocale();
    ResourceBundle bundle = ResourceBundle.getBundle("i18n.widgetBundle", locale);
    String welcome = bundle.getString("Welcome");

	 */

	// string to return when no credential key is supplied by js call
	//public static final String UNAUTHORISED_MESSAGE = Messages.getString("WidgetAPIImpl.0");	 //$NON-NLS-1$
	// string to return when no preference key is supplied by js call
	//public static final String NOKEY_MESSAGE = Messages.getString("WidgetAPIImpl.1"); //$NON-NLS-1$
	//public static final String LOCKED_MESSAGE = Messages.getString("WidgetAPIImpl.2"); //$NON-NLS-1$

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

		try {
			IWidgetAPIManager manager = getManager(session, localizedMessages);
			// check if instance is valid
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				for(Preference preference : manager.getPreferenceForInstance(widgetInstance)){
					prefs.put(preference.getDkey(), preference.getDvalue());
				}
			}
			else{
				prefs.put("message", localizedMessages.getString("WidgetAPIImpl.0")); //$NON-NLS-1$
				return prefs;
			}
		}
		catch (Exception ex) {
			_logger.error("Error getting preferences", ex); //$NON-NLS-1$
		}
		return prefs;
	}



	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#state(java.lang.String)
	 */
	public Map<String, String> state(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		HashMap<String, String> state = new HashMap<String,String>();
		if(id_key==null){
			state.put("message", localizedMessages.getString("WidgetAPIImpl.0"));	 //$NON-NLS-1$
			return state;
		}
		try {
			IWidgetAPIManager manager = getManager(session, localizedMessages);
			// check if instance is valid
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				for(SharedData data : manager.getSharedDataForInstance(widgetInstance)){
					state.put(data.getDkey(), data.getDvalue());
				}
			}
			else{
				state.put("message", localizedMessages.getString("WidgetAPIImpl.0")); //$NON-NLS-1$
				return state;
			}
		}
		catch (Exception ex) {
			_logger.error("Error getting preferences", ex); //$NON-NLS-1$
		}
		return state;
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
		String preferenceText = null;
		try {
			IWidgetAPIManager manager = getManager(session, localizedMessages);
			// check if instance is valid
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				boolean found = false;
				// find the correct preference...
				for(Preference preference : manager.getPreferenceForInstance(widgetInstance)){
					if(preference.getDkey().equals(key)){
						preferenceText = preference.getDvalue();
						found = true;
						break;
					}
				}
				if(!found){
					_logger.debug("No key found in getpreference call:" + key); //$NON-NLS-1$
					preferenceText = "null"; //$NON-NLS-1$
				}
			}
			else{
				preferenceText = localizedMessages.getString("WidgetAPIImpl.0");
			}
		}
		catch (Exception ex) {
			_logger.error("Error getting preferences", ex); //$NON-NLS-1$
		}
		return preferenceText;
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
		String sharedDataValue = null;
		try {
			IWidgetAPIManager manager = getManager(session, localizedMessages);
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				sharedDataValue = manager.getSharedDataValue(widgetInstance, key);
				if(sharedDataValue == null){
					_logger.debug("No key found in getshareddata call:" + key);		 //$NON-NLS-1$
				}
			}
			else{
				sharedDataValue = localizedMessages.getString("WidgetAPIImpl.0");
			}
		}
		catch (Exception ex) {
			_logger.error("Error getting shared data", ex); //$NON-NLS-1$
		}
		return sharedDataValue;
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#setPreferenceForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setPreferenceForKey(String id_key, String key, String value) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		try {
			//_logger.error("setPreferenceForKey: " + id_key + " | "+ key + " | "+ value);
			IWidgetAPIManager manager = getManager(session, localizedMessages);
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				manager.updatePreference(widgetInstance, key, value);
				//_logger.debug("setpreferenceForKey set " + key + "  to  " + value);
			}
			else{
				return localizedMessages.getString("WidgetAPIImpl.0");
			}
		}
		catch (Exception ex) {
			_logger.error("setpreferenceForKey failed", ex);			 //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#setSharedDataForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setSharedDataForKey(String id_key, String key, String value) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		try {
			//_logger.error("setSharedDataForKey: " + id_key + " | "+ key + " | "+ value);
			IWidgetAPIManager manager = getManager(session, localizedMessages);
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				if(!manager.isInstanceLocked(widgetInstance)){
					String sharedDataKey = widgetInstance.getSharedDataKey();
					manager.updateSharedDataEntry(widgetInstance, key, value, false);
					WebContext wctx = WebContextFactory.get();
			        String currentPage = wctx.getCurrentPage();
			        System.out.println(currentPage);
			        ScriptBuffer script = new ScriptBuffer();
			        script.appendScript("Widget.onSharedUpdate(\"").appendScript(sharedDataKey).appendScript("\");"); //$NON-NLS-1$ //$NON-NLS-2$
			        // Loop over all the users on the current page
			        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
			        for (Iterator<?> it = pages.iterator(); it.hasNext();){
			            ScriptSession otherSession = (ScriptSession) it.next();
			            otherSession.addScript(script);
			        }
				}
				else {
					// is locked
					return localizedMessages.getString("WidgetAPIImpl.2");
				}

			}
			else{
				// not authorized
				return localizedMessages.getString("WidgetAPIImpl.0");
			}
		}
		catch (Exception ex) {
			_logger.error("setSharedDataForKey failed", ex); //$NON-NLS-1$
		}
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
		try {
			//_logger.error("appendSharedDataForKey: " + id_key + " | "+ key + " | "+ value);
			IWidgetAPIManager manager = getManager(session, localizedMessages);
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				if(!manager.isInstanceLocked(widgetInstance)){
					String sharedDataKey = widgetInstance.getSharedDataKey();
					manager.updateSharedDataEntry(widgetInstance, key, value, true);
					WebContext wctx = WebContextFactory.get();
			        String currentPage = wctx.getCurrentPage();
			        ScriptBuffer script = new ScriptBuffer();
			        script.appendScript("Widget.onSharedUpdate(\"").appendScript(sharedDataKey).appendScript("\");");	 //$NON-NLS-1$ //$NON-NLS-2$
			        // Loop over all the users on the current page
			        //TODO - on high load the "otherSession.addScript(script)" call can throw illegalmodificationexception
			        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
			        //Collection<?> pages = Collections.synchronizedCollection(wctx.getScriptSessionsByPage(currentPage));
			        // synchronized(pages){
				    for (Iterator<?> it = pages.iterator(); it.hasNext();){
				    	ScriptSession otherSession = (ScriptSession) it.next();
				        // synchronized(otherSession){
				    	otherSession.addScript(script);
				        // }
				    }
			        //}
				}
				else {
					// is locked
					return localizedMessages.getString("WidgetAPIImpl.2");
				}
			}
			else{
				// not authorized
				return localizedMessages.getString("WidgetAPIImpl.0");
			}
		}
		catch (Exception ex) {
			_logger.error("appendSharedDataForKey failed", ex); //$NON-NLS-1$
		}
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
		IWidgetAPIManager manager = getManager(session, localizedMessages);
		WidgetInstance widgetInstance = manager.checkUserKey(id_key);
		if(widgetInstance!=null){
			String sharedDataKey = widgetInstance.getSharedDataKey();
			//_logger.error("lock called by " + widgetInstance.getUserId());
			manager.lockWidgetInstance(widgetInstance);
			WebContext wctx = WebContextFactory.get();
	        String currentPage = wctx.getCurrentPage();
	        ScriptBuffer script = new ScriptBuffer();
	        script.appendScript("Widget.onLocked(\"").appendScript(sharedDataKey).appendScript("\");"); //$NON-NLS-1$ //$NON-NLS-2$
	        // Loop over all the users on the current page
	        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
	        for (Iterator<?> it = pages.iterator(); it.hasNext();){
	            ScriptSession otherSession = (ScriptSession) it.next();
	            otherSession.addScript(script);
	        }
	        return "";		 //$NON-NLS-1$
		}
		else{
			return localizedMessages.getString("WidgetAPIImpl.0");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#unlock(java.lang.String)
	 */
	public String unlock(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetAPIManager manager = getManager(session, localizedMessages);
		WidgetInstance widgetInstance = manager.checkUserKey(id_key);
		if(widgetInstance!=null){
			String sharedDataKey = widgetInstance.getSharedDataKey();
			//_logger.error("unlock called by " + widgetInstance.getUserId());
			manager.unlockWidgetInstance(widgetInstance);
			WebContext wctx = WebContextFactory.get();
	        String currentPage = wctx.getCurrentPage();
	        ScriptBuffer script = new ScriptBuffer();
	        script.appendScript("Widget.onUnlocked(\"").appendScript(sharedDataKey).appendScript("\");"); //$NON-NLS-1$ //$NON-NLS-2$
	        // Loop over all the users on the current page
	        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
	        for (Iterator<?> it = pages.iterator(); it.hasNext();){
	            ScriptSession otherSession = (ScriptSession) it.next();
	            otherSession.addScript(script);
	        }
	        return "";		 //$NON-NLS-1$
		}
		else{
			return localizedMessages.getString("WidgetAPIImpl.0");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#hide(java.lang.String)
	 */
	public String hide(String id_key){
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetAPIManager manager = getManager(session, localizedMessages);
		WidgetInstance widgetInstance = manager.checkUserKey(id_key);
		if(widgetInstance!=null){
			WebContext wctx = WebContextFactory.get();
	        ScriptBuffer script = new ScriptBuffer();
	        script.appendScript("window.onHide(")	        	 //$NON-NLS-1$
	        .appendScript(");");        //$NON-NLS-1$
	        wctx.getScriptSession().addScript(script);
	        return ""; //$NON-NLS-1$
		}
		else{
			return localizedMessages.getString("WidgetAPIImpl.0");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#show(java.lang.String)
	 */
	public String show(String id_key){
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		IWidgetAPIManager manager = getManager(session, localizedMessages);
		WidgetInstance widgetInstance = manager.checkUserKey(id_key);
		if(widgetInstance!=null){
			WebContext wctx = WebContextFactory.get();
	        ScriptBuffer script = new ScriptBuffer();
	        script.appendScript("window.onShow(")	        	 //$NON-NLS-1$
	        .appendScript(");");        //$NON-NLS-1$
	        wctx.getScriptSession().addScript(script);
	        return ""; //$NON-NLS-1$
		}
		else{
			return localizedMessages.getString("WidgetAPIImpl.0");
		}
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



	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#getParticipants(java.lang.String)
	 */
	public String getParticipants(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		if(id_key == null) return localizedMessages.getString("WidgetAPIImpl.0");
		try {
			IWidgetAPIManager manager = getManager(session, localizedMessages);
			// check if instance is valid
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				// find the correct participants...
				Participant[] participants = manager.getParticipants(widgetInstance);
				String json = "{\"Participants\":[";
				String delimit = "";
				for (Participant participant: participants){
					json+=delimit+toJson(participant);
					delimit = ",";
				}
				json+="]}";
				return json;
			}
			else{
				return "ERROR";
			}
		}
		catch (Exception ex) {
			_logger.error("Error getting preferences", ex); //$NON-NLS-1$
		}
		return "ERROR";
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#getViewer(java.lang.String)
	 */
	public String getViewer(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		HttpSession session = request.getSession(true);
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		if(id_key == null) return localizedMessages.getString("WidgetAPIImpl.0");
		try {
			IWidgetAPIManager manager = getManager(session, localizedMessages);
			// check if instance is valid
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				// find the correct participants...
				Participant participant = manager.getViewer(widgetInstance);
				if (participant != null) return "{\"Participant\":"+toJson(participant)+"}";
			}
			else{
				return null;
			}
		}
		catch (Exception ex) {
			_logger.error("Error getting preferences", ex); //$NON-NLS-1$
		}
		return null;
	}
	
	private String toJson(Participant participant){
		String json = "{"+
		"\"participant_id\":\""+participant.getParticipant_id()+
		"\", \"participant_display_name\":\""+participant.getParticipant_display_name()+
		"\", \"participant_thumbnail_url\":\""+participant.getParticipant_thumbnail_url()+"\"}";
		return json;
	}

	/*
	public String system(String id_key, String arg1) {
		_logger.debug("system called with " + arg1 );
		return "Not available";
	}

	public String system(String id_key, String arg1, String arg2) {
		_logger.debug("system called with " + arg1 + "   " + arg2);
		return "Not available";
	}

	public String system(String id_key, String arg1, String arg2, String arg3) {
		_logger.debug("system called with " + arg1 + "   " + arg2+ "   " + arg3);
		return "Not available";
	}
	 */
	
	private IWidgetAPIManager getManager(HttpSession session, Messages localizedMessages){
		//
		IWidgetAPIManager manager = null;
		manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
		if(manager == null){
			manager = new WidgetAPIManager(localizedMessages);
			session.setAttribute(WidgetAPIManager.class.getName(), manager);
		}
		return manager;
	}
}