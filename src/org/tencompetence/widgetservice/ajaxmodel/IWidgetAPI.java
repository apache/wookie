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
package org.tencompetence.widgetservice.ajaxmodel;

import java.util.Map;


/**
 * Definition of the widget API.  
 * @author Paul Sharples
 * @version $Id: IWidgetAPI.java,v 1.7 2009-03-14 20:30:15 scottwilson Exp $
 *
 */
public interface IWidgetAPI {	
	
	/**
	 * Appends a string to the string contained in the shared data value in the DB, obtained
	 * from the given "key" 
	 * @param id_key - the unique instance id key for a widget instance
	 * @param key - key for the value to change
 	 * @param key - the value to change to
	 * @return - a string value marking status or an error message
	 */
	public String appendSharedDataForKey(String id_key, String key, String value);	
	
	/**
	 * ? not implemented yet
	 * @param id_key
	 * @param key
	 * @return
	 */
	public String contextPropertyForKey(String id_key, String key);
	
	/**
	 * Call to hide a widget instance based on the instance_key
	 * @param id_key
	 * @return
	 */
	public String hide(String id_key);
	
	/**
	 * Call to lock a widget instance
	 * @param id_key
	 * @return
	 */
	public String lock(String id_key);
	
	/**
	 * Open a url in a window
	 * @param url
	 * @return
	 */
	@Deprecated
	public String openURL(String url);
	
	/**
	 * Returns the set of preferences as a map of [key][value] objects
	 * @param id_key
	 * @return
	 */
	public Map<String, String> preferences(String id_key);
	
	/**
	 * Returns a string preference value from the DB, obtained
	 * from the given "key" 
	 * @param id_key - the unique instance id key for a widget instance
	 * @param key - key for the value to retrieve
	 * @return - a string value found in the DB or an error message
	 */
	public String preferenceForKey(String id_key, String key);
	
	/**
	 * ? not implemented yet
	 * @param id_key
	 * @param key
	 * @param value
	 * @return
	 */
	public String setContextPropertyForKey(String id_key, String key, String value);
	
	/**
	 * Sets a string preference value in the DB, obtained
	 * from the given "key" 
	 * @param id_key - the unique instance id key for a widget instance
	 * @param key - key for the value to change
 	 * @param key - the value to change to
	 * @return - a string value marking status or an error message
	 */
	public String setPreferenceForKey(String id_key, String key, String value);
	
	/**
	 * Sets a string shared data value in the DB, obtained
	 * from the given "key" 
	 * @param id_key - the unique instance id key for a widget instance
	 * @param key - key for the value to change
 	 * @param key - the value to change to
	 * @return - a string value marking status or an error message
	 */
	public String setSharedDataForKey(String id_key, String key, String value);
	
	/**
	 * ? not implemented yet
	 * @param id_key
	 * @param key
	 * @param value
	 * @return
	 */
	public String setUserPropertyForKey(String id_key, String key, String value);
	
	/**
	 * Returns a string value from the DB, obtained
	 * from the given "key". This is a shared data value
	 * between widgets using the same data 
	 * @param id_key - the unique instance id key for a widget instance
	 * @param key - key for the value to retrieve
	 * @return - a string value found in the DB or an error message
	 */
	public String sharedDataForKey(String id_key, String key);	
	
	/**
	 * show a widget instance based on the id_key
	 * @param id_key
	 * @return
	 */
	public String show(String id_key);
	
	/**
	 * Unlock a widget instance based on the id_key
	 * @param id_key
	 * @return
	 */
	public String unlock(String id_key);
	
	/**
	 * ? not implemented yet
	 * @param id_key
	 * @param key
	 * @param value
	 * @return
	 */
	public String userPropertyForKey(String id_key, String key);
}
