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
package org.apache.wookie.services;

import java.util.Collection;
import java.util.ServiceLoader;

import org.apache.wookie.beans.IPreference;
import org.apache.wookie.services.impl.DefaultPreferencesService;

/**
 * The PreferencesService is the SPI for plugins that implement a preferences service; this service
 * must be able to store and retrieve preferences for a particular instance of a widget. This can
 * be via a database, a file system, cache etc., or by an API call to another service - for example
 * you may want to store preferences in a central store within the portal that hosts the widgets.
 * 
 * To register an implementation, edit the name of the default class in:
 * 
 * src/main/resources/META-INF/services/org.apache.wookie.services.PreferencesService 
 * 
 * ... with the name of your service implementation. It must be on the classpath for the Wookie webapp.
 *
 */
public interface PreferencesService {
	
	public abstract String getPreference(String apiKey, String widgetId, String contextId, String viewerId, String name);

	public abstract void setPreference(String apiKey, String widgetId, String contextId, String viewerId, String name, String value);
	
	public abstract void setPreference(String apiKey, String widgetId, String contextId, String viewerId, String name, String value, boolean readOnly);
	
	public abstract void setPreference(String apiKey, String widgetId, String contextId, String viewerId, IPreference preference);

	public abstract Collection<IPreference> getPreferences(String apiKey, String widgetId, String contextId, String viewerId);
	
	public abstract void setPreferences(String apiKey, String widgetId, String contextId, String viewerId, Collection<IPreference> preferences);
	
	public abstract void removePreferences(String apiKey, String widgetId, String contextId, String viewerId);
	
	public static class Factory {
		
		private static PreferencesService provider;
		
	    public static PreferencesService getInstance() {
	    	//
	    	// Use the service loader to load an implementation if one is available
	    	//
	    	if (provider == null){
	    		ServiceLoader<PreferencesService> ldr = ServiceLoader.load(PreferencesService.class);
	    		for (PreferencesService service : ldr) {
	    			// We are only expecting one
	    			provider = service;
	    		}
	    	}
	    	//
	    	// If no service provider is found, use the default
	    	//
	    	if (provider == null){
	    		provider = new DefaultPreferencesService();
	    	}
	    	
	    	return provider;
	    }
	}
}
