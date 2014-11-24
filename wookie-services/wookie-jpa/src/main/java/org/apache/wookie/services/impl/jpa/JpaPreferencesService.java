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
 *  limitations under the License.
 */
package org.apache.wookie.services.impl.jpa;

import java.util.Collection;

import org.apache.wookie.beans.IPreference;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.services.PreferencesService;

public class JpaPreferencesService implements PreferencesService {
	
	private int getId(String apiKey, String widgetId,
			String contextId, String viewerId, String name){
		return (apiKey+"-"+widgetId+"-"+contextId+"-"+viewerId+"::"+name).hashCode();
	}

	@Override
	public String getPreference(String apiKey, String widgetId,
			String contextId, String viewerId, String name) {
	      IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	      IPreference preference = persistenceManager.findById(IPreference.class, getId(apiKey, widgetId ,contextId ,viewerId, name));
	      if (preference == null) return null;
	      return preference.getValue();
	}

	@Override
	public void setPreference(String apiKey, String widgetId, String contextId,
			String viewerId, String name, String value) {
		// TODO Auto-generated method stub
		setPreference(apiKey, widgetId, contextId, viewerId, name, value, false);
	}

	@Override
	public void setPreference(String apiKey, String widgetId, String contextId,
			String viewerId, String name, String value, boolean readOnly) {
		// TODO Auto-generated method stub
	      IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
	      IPreference preference = persistenceManager.newInstance(IPreference.class);
	      preference.setName(name);
	      preference.setReadOnly(readOnly);
	      preference.setValue(value);
		
	}

	@Override
	public void setPreference(String apiKey, String widgetId, String contextId,
			String viewerId, IPreference preference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Collection<IPreference> getPreferences(String apiKey,
			String widgetId, String contextId, String viewerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPreferences(String apiKey, String widgetId,
			String contextId, String viewerId,
			Collection<IPreference> preferences) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePreferences(String apiKey, String widgetId,
			String contextId, String viewerId) {
		// TODO Auto-generated method stub
		
	}




}
