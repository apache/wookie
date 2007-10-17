/*
 * Copyright (c) 2007, Consortium Board TENCompetence
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
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.tencompetence.widgetservice.ajaxmodel.IWidgetAPI;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.manager.WidgetAPIManager;

/**
 * Implementation of the widget API.  This class models the the javascript implementation of
 * the w3c widget API.  Using DWR - a javascript/HTML client which has included the correct js file...
 * 
 *   <script type='text/javascript' src='/wookie/dwr/interface/widget.js'> </script>  
 *   
 *   ...can then access this classes methods via a call for example like..
 *   
 *   widget.preferenceForKey("unique-key-obtained-from-widget-server", "mycolourpreferences")
 * 
 * @author Paul Sharples
 * @version $Id
 *
 */
public class WidgetAPIImpl implements IWidgetAPI {
	
	// string to return when no credential key is supplied by js call
	public static final String UNAUTHORISED_MESSAGE = "Unauthorised";
	
	// string to return when no preference key is supplied by js call
	public static final String NOKEY_MESSAGE = "No matching key found";
	
	static Logger _logger = Logger.getLogger(WidgetAPIImpl.class.getName());
	
	private WidgetAPIManager manager = new WidgetAPIManager();			
	private WidgetInstance widgetInstance = null;
	
	public WidgetAPIImpl(){}

	/**
	 * Returns a string preference value from the DB, obtained
	 * from the given "key" 
	 * @param id_key - the unique instance id key for a widget instance
	 * @param key - key for the value to retrieve
	 * @return - a string value found in the DB or an error message
	 */
	public String preferenceForKey(String id_key, String key) {	
		if(id_key==null) return UNAUTHORISED_MESSAGE;
		if(key==null)return NOKEY_MESSAGE;		
		String preferenceText = null;		
		try {
			// check if instance is valid
			widgetInstance = manager.checkUserKey(id_key);
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

	/**
	 * Returns a string value from the DB, obtained
	 * from the given "key". This is a shared data value
	 * between widgets using the same data 
	 * @param id_key - the unique instance id key for a widget instance
	 * @param key - key for the value to retrieve
	 * @return - a string value found in the DB or an error message
	 */
	public String sharedDataForKey(String id_key, String key) {
		if(id_key==null) return UNAUTHORISED_MESSAGE;
		if(key==null) return NOKEY_MESSAGE;
		String sharedDataValue = null;				
		try {
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				boolean found = false;
								
				for(SharedData sharedData : manager.getSharedDataForInstance(widgetInstance)){
					if(sharedData.getDkey().equals(key)){
						sharedDataValue = sharedData.getDvalue();
						found = true;
						break;
					}
				}				
				if(!found){
					_logger.debug("No key found in getshareddata call:" + key);
					sharedDataValue = null;
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
	
	/**
	 * Sets a string preference value in the DB, obtained
	 * from the given "key" 
	 * @param id_key - the unique instance id key for a widget instance
	 * @param key - key for the value to change
 	 * @param key - the value to change to
	 * @return - a string value marking status or an error message
	 */
	public String setPreferenceForKey(String id_key, String key, String value) {		
		try {
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				manager.updatePreference(widgetInstance, key, value);
				_logger.debug("setpreferenceForKey set " + key + "  to  " + value);
				
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

	/**
	 * Sets a string shared data value in the DB, obtained
	 * from the given "key" 
	 * @param id_key - the unique instance id key for a widget instance
	 * @param key - key for the value to change
 	 * @param key - the value to change to
	 * @return - a string value marking status or an error message
	 */
	@SuppressWarnings("unchecked")
	public String setSharedDataForKey(String id_key, String key, String value) {		
		try {
			_logger.error("setSharedDataForKey: "+ id_key+ "\tkey:" + key + "\tvalue:" + value);
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				manager.updateSharedDataEntry(widgetInstance, key, value, false);
			
				WebContext wctx = WebContextFactory.get();
		        String currentPage = wctx.getCurrentPage();

		        ScriptBuffer script = new ScriptBuffer();
		        script.appendScript("widget.onSharedUpdate(")
		            //  .appendData(sharedDataForKey(id_key, key))		        
		              .appendScript(");");

		        // Loop over all the users on the current page
		        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
		        for (Iterator<?> it = pages.iterator(); it.hasNext();)
		        {
		            ScriptSession otherSession = (ScriptSession) it.next();
		            otherSession.addScript(script);
		        }
				
			}
			else{
				return UNAUTHORISED_MESSAGE;
			}
		} 
		catch (Exception ex) {
			_logger.error("setSharedDataForKey failed", ex);
		}
		return "";
	}

	/**
	 * Appends a string to the string contained in the shared data value in the DB, obtained
	 * from the given "key" 
	 * @param id_key - the unique instance id key for a widget instance
	 * @param key - key for the value to change
 	 * @param key - the value to change to
	 * @return - a string value marking status or an error message
	 */
	public String appendSharedDataForKey(String id_key, String key, String value) {
		try {
			_logger.debug("appendSharedDataForKey: "+ id_key+ "\tkey:" + key + "\tvalue:" + value);
			widgetInstance = manager.checkUserKey(id_key);
			if(widgetInstance!=null){
				manager.updateSharedDataEntry(widgetInstance, key, value, true);

				WebContext wctx = WebContextFactory.get();
		        String currentPage = wctx.getCurrentPage();

		        ScriptBuffer script = new ScriptBuffer();
		        script.appendScript("widget.onSharedUpdate(")
		             //  .appendData(sharedDataForKey(id_key, key))	
		              .appendScript(");");

		        // Loop over all the users on the current page
		        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
		        for (Iterator<?> it = pages.iterator(); it.hasNext();)
		        {
		            ScriptSession otherSession = (ScriptSession) it.next();
		            otherSession.addScript(script);
		        }
			
			}
			else{
				return UNAUTHORISED_MESSAGE;
			}
		} 
		catch (Exception ex) {
			_logger.error("appendSharedDataForKey failed", ex);
		}
		return "";
	}

	// TODO this is what the admin or moderator does
	// need to identify who the admin person is from the UOL
	// and somehow get it from the widget..
	public String lock(String id_key) {
		return "";
	}
	
	public String unlock(String id_key) {
		return "";
	}

	public String contextPropertyForKey(String id_key, String key) {
		return null;
	}

	public String setContextPropertyForKey(String id_key, String key, String value) {
		return null;
	}

	public String setUserPropertyForKey(String id_key, String key, String value) {
		return null;
	}

	public String userPropertyForKey(String id_key, String key) {
		return null;
	}

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
}