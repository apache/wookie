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

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.manager.IWidgetAPIManager;
import org.tencompetence.widgetservice.manager.impl.WidgetAPIManager;

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
 * @version $Id: WidgetAPIImpl.java,v 1.13 2009-03-14 20:30:15 scottwilson Exp $
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
	public static final String UNAUTHORISED_MESSAGE = "Unauthorised";	
	// string to return when no preference key is supplied by js call
	public static final String NOKEY_MESSAGE = "No matching key found";
	public static final String LOCKED_MESSAGE = "Widget Instance is locked by another user";
	
	static Logger _logger = Logger.getLogger(WidgetAPIImpl.class.getName());
		
	public WidgetAPIImpl(){}

	
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#preferences(java.lang.String)
	 */
	public Map<String, String> preferences(String id_key) {
		HashMap<String, String> prefs = new HashMap<String,String>();

		if(id_key == null){
			prefs.put("message",UNAUTHORISED_MESSAGE);
			return prefs;
		}
		
		try {
			IWidgetAPIManager manager = null;
			HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
			manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
			if(manager == null){
				manager = new WidgetAPIManager();
				session.setAttribute(WidgetAPIManager.class.getName(), manager);
			} 		
			// check if instance is valid
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);			
			if(widgetInstance!=null){
				for(Preference preference : manager.getPreferenceForInstance(widgetInstance)){
					prefs.put(preference.getDkey(), preference.getDvalue());
				}
			}
			else{
				prefs.put("message",UNAUTHORISED_MESSAGE);
				return prefs;
			}	
		} 
		catch (Exception ex) {			
			_logger.error("Error getting preferences", ex);
		}		
		return prefs;	
	}



	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#preferenceForKey(java.lang.String, java.lang.String)
	 */
	public String preferenceForKey(String id_key, String key) {
		if(id_key == null) return UNAUTHORISED_MESSAGE;
		if(key == null)return NOKEY_MESSAGE;		
		String preferenceText = null;		
		try {
			IWidgetAPIManager manager = null;
			HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
			manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
			if(manager == null){
				manager = new WidgetAPIManager();
				session.setAttribute(WidgetAPIManager.class.getName(), manager);
			} 		
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
					_logger.debug("No key found in getpreference call:" + key);
					preferenceText = "null";
				}
			}
			else{
				preferenceText = UNAUTHORISED_MESSAGE;
			}	
		} 
		catch (Exception ex) {			
			_logger.error("Error getting preferences", ex);
		}		
		return preferenceText;	
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#sharedDataForKey(java.lang.String, java.lang.String)
	 */
	public String sharedDataForKey(String id_key, String key) {
		//_logger.error("sharedDataForKey: " + id_key + " | "+ key);
		if(id_key==null) return UNAUTHORISED_MESSAGE;
		if(key==null) return NOKEY_MESSAGE;
		String sharedDataValue = null;				
		try {
			IWidgetAPIManager manager = null;
			HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
			manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
			if(manager == null){
				manager = new WidgetAPIManager();
				session.setAttribute(WidgetAPIManager.class.getName(), manager);
			} 
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){			
				sharedDataValue = manager.getSharedDataValue(widgetInstance, key);
				if(sharedDataValue == null){
					_logger.debug("No key found in getshareddata call:" + key);		
				}
			}
			else{
				sharedDataValue = UNAUTHORISED_MESSAGE;
			}
		} 
		catch (Exception ex) {
			_logger.error("Error getting shared data", ex);
		}				
		return sharedDataValue;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#setPreferenceForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setPreferenceForKey(String id_key, String key, String value) {		
		try {
			//_logger.error("setPreferenceForKey: " + id_key + " | "+ key + " | "+ value);
			IWidgetAPIManager manager = null;
			HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
			manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
			if(manager == null){
				manager = new WidgetAPIManager();
				session.setAttribute(WidgetAPIManager.class.getName(), manager);
			} 
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				manager.updatePreference(widgetInstance, key, value);
				//_logger.debug("setpreferenceForKey set " + key + "  to  " + value);
			}
			else{
				return UNAUTHORISED_MESSAGE;
			}
		} 
		catch (Exception ex) {
			_logger.error("setpreferenceForKey failed", ex);			
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#setSharedDataForKey(java.lang.String, java.lang.String, java.lang.String)
	 */	
	public String setSharedDataForKey(String id_key, String key, String value) {		
		try {
			//_logger.error("setSharedDataForKey: " + id_key + " | "+ key + " | "+ value);
			IWidgetAPIManager manager = null;
			HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
			manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
			if(manager == null){
				manager = new WidgetAPIManager();
				session.setAttribute(WidgetAPIManager.class.getName(), manager);
			} 
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				if(!manager.isInstanceLocked(widgetInstance)){
					String sharedDataKey = widgetInstance.getSharedDataKey();
					manager.updateSharedDataEntry(widgetInstance, key, value, false);					
					WebContext wctx = WebContextFactory.get();
			        String currentPage = wctx.getCurrentPage();			        
			        ScriptBuffer script = new ScriptBuffer();
			        script.appendScript("Widget.onSharedUpdate(\"").appendScript(sharedDataKey).appendScript("\");");
			        // Loop over all the users on the current page
			        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
			        for (Iterator<?> it = pages.iterator(); it.hasNext();){
			            ScriptSession otherSession = (ScriptSession) it.next();
			            otherSession.addScript(script);
			        }
				}
				else {
					return LOCKED_MESSAGE;
				}
				
			}
			else{
				return UNAUTHORISED_MESSAGE;
			}
		} 
		catch (Exception ex) {
			_logger.error("setSharedDataForKey failed", ex);
		}
		return "okay";
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#appendSharedDataForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String appendSharedDataForKey(String id_key, String key, String value) {
		try {	
			//_logger.error("appendSharedDataForKey: " + id_key + " | "+ key + " | "+ value);
			IWidgetAPIManager manager = null;
			HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
			manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
			if(manager == null){
				manager = new WidgetAPIManager();
				session.setAttribute(WidgetAPIManager.class.getName(), manager);
			} 
			WidgetInstance widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				if(!manager.isInstanceLocked(widgetInstance)){
					String sharedDataKey = widgetInstance.getSharedDataKey();					
					manager.updateSharedDataEntry(widgetInstance, key, value, true);	
					WebContext wctx = WebContextFactory.get();
			        String currentPage = wctx.getCurrentPage();	
			        ScriptBuffer script = new ScriptBuffer();
			        script.appendScript("Widget.onSharedUpdate(\"").appendScript(sharedDataKey).appendScript("\");");	
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
					return LOCKED_MESSAGE;
				}			
			}
			else{
				return UNAUTHORISED_MESSAGE;
			}
		} 
		catch (Exception ex) {			
			_logger.error("appendSharedDataForKey failed", ex);
		}
		return "okay";
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#lock(java.lang.String)
	 */
	public String lock(String id_key) {
		IWidgetAPIManager manager = null;
		HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
		manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
		if(manager == null){
			manager = new WidgetAPIManager();
			session.setAttribute(WidgetAPIManager.class.getName(), manager);
		} 
		WidgetInstance widgetInstance = manager.checkUserKey(id_key);
		if(widgetInstance!=null){
			String sharedDataKey = widgetInstance.getSharedDataKey();
			//_logger.error("lock called by " + widgetInstance.getUserId());			
			manager.lockWidgetInstance(widgetInstance);	
			WebContext wctx = WebContextFactory.get();
	        String currentPage = wctx.getCurrentPage();
	        ScriptBuffer script = new ScriptBuffer();
	        script.appendScript("Widget.onLocked(\"").appendScript(sharedDataKey).appendScript("\");");
	        // Loop over all the users on the current page
	        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
	        for (Iterator<?> it = pages.iterator(); it.hasNext();){
	            ScriptSession otherSession = (ScriptSession) it.next();
	            otherSession.addScript(script);
	        }
	        return "";		
		}
		else{
			return UNAUTHORISED_MESSAGE;
		}		
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#unlock(java.lang.String)
	 */
	public String unlock(String id_key) {
		IWidgetAPIManager manager = null;
		HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
		manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
		if(manager == null){
			manager = new WidgetAPIManager();
			session.setAttribute(WidgetAPIManager.class.getName(), manager);
		} 
		WidgetInstance widgetInstance = manager.checkUserKey(id_key);
		if(widgetInstance!=null){
			String sharedDataKey = widgetInstance.getSharedDataKey();
			//_logger.error("unlock called by " + widgetInstance.getUserId());			
			manager.unlockWidgetInstance(widgetInstance);	
			WebContext wctx = WebContextFactory.get();
	        String currentPage = wctx.getCurrentPage();
	        ScriptBuffer script = new ScriptBuffer();
	        script.appendScript("Widget.onUnlocked(\"").appendScript(sharedDataKey).appendScript("\");");
	        // Loop over all the users on the current page
	        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);	        
	        for (Iterator<?> it = pages.iterator(); it.hasNext();){	        	
	            ScriptSession otherSession = (ScriptSession) it.next();
	            otherSession.addScript(script);	            	            
	        }
	        return "";		
		}
		else{
			return UNAUTHORISED_MESSAGE;
		}	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#hide(java.lang.String)
	 */
	public String hide(String id_key){
		IWidgetAPIManager manager = null;
		HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
		manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
		if(manager == null){
			manager = new WidgetAPIManager();
			session.setAttribute(WidgetAPIManager.class.getName(), manager);
		} 
		WidgetInstance widgetInstance = manager.checkUserKey(id_key);
		if(widgetInstance!=null){
			WebContext wctx = WebContextFactory.get();
	        ScriptBuffer script = new ScriptBuffer();      
	        script.appendScript("window.onHide(")	        	
	        .appendScript(");");       
	        wctx.getScriptSession().addScript(script);
	        return "";
		}
		else{
			return UNAUTHORISED_MESSAGE;
		}	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#show(java.lang.String)
	 */
	public String show(String id_key){
		IWidgetAPIManager manager = null;
		HttpSession session = WebContextFactory.get().getHttpServletRequest().getSession();
		manager = (IWidgetAPIManager)session.getAttribute(WidgetAPIManager.class.getName());
		if(manager == null){
			manager = new WidgetAPIManager();
			session.setAttribute(WidgetAPIManager.class.getName(), manager);
		} 
		WidgetInstance widgetInstance = manager.checkUserKey(id_key);
		if(widgetInstance!=null){
			WebContext wctx = WebContextFactory.get();
	        ScriptBuffer script = new ScriptBuffer();      
	        script.appendScript("window.onShow(")	        	
	        .appendScript(");");       
	        wctx.getScriptSession().addScript(script);
	        return "";
		}
		else{
			return UNAUTHORISED_MESSAGE;
		}	
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#contextPropertyForKey(java.lang.String, java.lang.String)
	 */
	public String contextPropertyForKey(String id_key, String key) {
		return "Not available";
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#setContextPropertyForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setContextPropertyForKey(String id_key, String key, String value) {
		return "Not available";
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#setUserPropertyForKey(java.lang.String, java.lang.String, java.lang.String)
	 */
	public String setUserPropertyForKey(String id_key, String key, String value) {
		return "Not available";
	}

	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI#userPropertyForKey(java.lang.String, java.lang.String)
	 */
	public String userPropertyForKey(String id_key, String key) {
		return "Not available";
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
		_logger.debug("openurl called with        "+ url );
		WebContext wctx = WebContextFactory.get();
        ScriptBuffer script = new ScriptBuffer();      
        script.appendScript("window.open(")
        .appendData(url)	
        .appendScript(");");       
        wctx.getScriptSession().addScript(script);
        return "";
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
}