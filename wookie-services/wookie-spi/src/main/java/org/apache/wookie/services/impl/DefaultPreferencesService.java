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
package org.apache.wookie.services.impl;

import java.util.Collection;
import java.util.HashMap;

import org.apache.wookie.services.PreferencesService;
import org.apache.wookie.beans.IPreference;

/**
 * A very basic preferences model backed only by a hashmap in memory. All data
 * is lost between server launches, so only use for testing.
 */
public class DefaultPreferencesService implements PreferencesService {

	private HashMap <String, HashMap<String,IPreference>> preferences;
	
	public DefaultPreferencesService(){
		this.preferences = new HashMap <String, HashMap<String,IPreference>>();
	}
	
	@Override
	public String getPreference(String apiKey, String widgetId, String contextId, String viewerId, String name) {
		
		//
		// Get the token (key) to store the pref against
		//
		String token = getToken(apiKey, widgetId, contextId, viewerId);
		
		HashMap<String, IPreference> widgetpreferences = preferences.get(token);
		if (widgetpreferences == null){
			widgetpreferences = new HashMap<String, IPreference>();
			this.preferences.put(token, widgetpreferences);
		}
		IPreference pref = widgetpreferences.get(name);
		if (pref != null){
			return widgetpreferences.get(name).getValue();
		} else {
			return null;
		}
	}

	@Override
	public void setPreference(String apiKey, String widgetId, String contextId, String viewerId, IPreference preference) {
		if (preference != null){
			
			//
			// Get the token (key) to store the pref against
			//
			String token = getToken(apiKey, widgetId, contextId, viewerId);
			
			HashMap<String, IPreference> widgetpreferences = preferences.get(token);
			if (widgetpreferences == null){
				widgetpreferences = new HashMap<String, IPreference>();
				this.preferences.put(token, widgetpreferences);
			}
			widgetpreferences.put(preference.getName(), preference);
		}
	}

	@Override
	public Collection<IPreference> getPreferences(String apiKey, String widgetId, String contextId, String viewerId) {
		
		//
		// Get the token (key) to store the pref against
		//
		String token = getToken(apiKey, widgetId, contextId, viewerId);
		
		HashMap<String, IPreference> widgetpreferences = preferences.get(token);
		if (widgetpreferences == null){
			widgetpreferences = new HashMap<String, IPreference>();
			this.preferences.put(token, widgetpreferences);
		}
		return widgetpreferences.values();
	}

	@Override
	public void removePreferences(String apiKey, String widgetId, String contextId, String viewerId) {
		
		//
		// Get the token (key) to store the pref against
		//
		String token = getToken(apiKey, widgetId, contextId, viewerId);
		
		HashMap<String, IPreference> widgetpreferences = new HashMap<String, IPreference>();
		preferences.put(token, widgetpreferences);
	}

	@Override
	public void setPreferences(String apiKey, String widgetId, String contextId, String viewerId, Collection<IPreference> preferences) {
		
		//
		// Get the token (key) to store the pref against
		//
		String token = getToken(apiKey, widgetId, contextId, viewerId);
		
		//
		// Setting to null is the same as clearing the preferences
		//
		if (preferences == null){
			this.removePreferences(apiKey, widgetId, contextId, viewerId);
		} else {
			HashMap<String, IPreference> widgetpreferences = new HashMap<String, IPreference>();
			for (IPreference preference: preferences){
				widgetpreferences.put(preference.getName(), preference);
			}
			this.preferences.put(token, widgetpreferences);
		} 
	}

	@Override
	public void setPreference(String apiKey, String widgetId, String contextId, String viewerId, String name, String value) {
		setPreference(apiKey, widgetId, contextId, viewerId, name, value, false);
	}

	@Override
	public void setPreference(String apiKey, String widgetId, String contextId, String viewerId, String name, String value, boolean readOnly) {
		IPreference preference = new DefaultPreferenceImpl(name, value, readOnly);
		setPreference(apiKey, widgetId, contextId, viewerId, preference);
	}
	
	private String getToken(String apiKey, String widgetId, String contextId, String viewerId){
		return apiKey + "-" + widgetId + "-" + contextId + "-" + viewerId;
	}

}
