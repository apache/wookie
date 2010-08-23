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

package org.apache.wookie.feature.wave;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.wookie.Messages;
import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.IWidgetInstance;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.controller.PropertiesController;
import org.apache.wookie.feature.IFeature;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.ParticipantHelper;
import org.apache.wookie.server.LocaleHandler;
import org.directwebremoting.WebContextFactory;

/**
 * Implementation of the Wave API
 */
public class WaveAPIImpl implements IFeature, IWaveAPI{

	public WaveAPIImpl() {
	}
	
	public String getName() {
		return "http://wave.google.com";
	}

	public String[] scripts() {
		return new String[]{"/wookie/dwr/interface/WaveImpl.js", "/wookie/shared/js/wave.js"};
	}

	public String[] stylesheets() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#getHost(java.lang.String)
	 */
	public String getHost(String idKey) {
		// TODO FIXME see WOOKIE-66
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.ajaxmodel.IWidgetAPI#state(java.lang.String)
	 */
	public Map<String, String> state(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		HashMap<String, String> state = new HashMap<String,String>();
		if(id_key==null){
			state.put("message", localizedMessages.getString("WidgetAPIImpl.0"));	 //$NON-NLS-1$
			return state;
		}
		// check if instance is valid
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
		if (widgetInstance == null){
			state.put("message", localizedMessages.getString("WidgetAPIImpl.0"));	 //$NON-NLS-1$
			return state;			
		}
		//
		for(ISharedData data : widgetInstance.getSharedData()){
			state.put(data.getDkey(), data.getDvalue());
		}
		return state;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#getParticipants(java.lang.String)
	 */
	public String getParticipants(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		if(id_key == null) return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$
		IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
		IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
		if(widgetInstance==null) return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$
		IParticipant[] participants = persistenceManager.findParticipants(widgetInstance);
		return ParticipantHelper.createJSONParticipantsDocument(participants);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#getViewer(java.lang.String)
	 */
	public String getViewer(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		if(id_key == null) return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
		if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$
        IParticipant participant = persistenceManager.findParticipantViewer(widgetInstance);
		if (participant != null) return ParticipantHelper.createJSONParticipantDocument(participant); //$NON-NLS-1$
		return null; // no viewer i.e. widget is anonymous
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#submitDelta(java.lang.String, java.util.Map)
	 */
	public String submitDelta(String id_key, Map<String,String>map){
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
		if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$
		if(widgetInstance.isLocked()) return localizedMessages.getString("WidgetAPIImpl.2"); //$NON-NLS-1$
		//
		for (String key: map.keySet())
		 	PropertiesController.updateSharedDataEntry(widgetInstance, key, map.get(key), false);
		Notifier.notifySiblings(widgetInstance);
		return "okay"; //$NON-NLS-1$
	}

}
