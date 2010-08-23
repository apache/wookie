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

package org.apache.wookie.ajaxmodel;

import java.util.Map;
import java.util.List;

import org.apache.wookie.beans.IPreference;


/**
 * Definition of the widget API.  
 * @author Paul Sharples
 * @version $Id: IWidgetAPI.java,v 1.3 2009-09-14 21:15:08 scottwilson Exp $
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
	public List<IPreference> preferences(String id_key);
	
	/**
	 * Returns the widget metadata
	 * @param id_key
	 * @return
	 */
	public Map<String, String> metadata(String id_key);
	
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
