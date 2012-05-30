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
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.beans.util.IPersistenceManager;
import org.apache.wookie.beans.util.PersistenceManagerFactory;
import org.apache.wookie.feature.IFeature;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.ParticipantHelper;
import org.apache.wookie.helpers.WidgetRuntimeHelper;
import org.apache.wookie.server.LocaleHandler;
import org.directwebremoting.WebContextFactory;

/**
 * Implementation of the Wave API
 */
public class WaveAPIImpl implements IFeature, IWaveAPI{

	/* (non-Javadoc)
   * @see org.apache.wookie.feature.IFeature#flattenOnExport()
   */
  public boolean flattenOnExport() {
    return false;
  }

  /* (non-Javadoc)
   * @see org.apache.wookie.feature.IFeature#getFolder()
   */
  public String getFolder() {
    return null;
  }
  
  public WaveAPIImpl() {
	}
	
	public String getName() {
		return "http://wave.google.com";
	}

	public String[] scripts() {
		return new String[]{WidgetRuntimeHelper.getWebContextPath() + "/dwr/interface/WaveImpl.js", 
		    WidgetRuntimeHelper.getWebContextPath() + "/shared/js/wave.js"};
	}

	public String[] stylesheets() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#getHost(java.lang.String)
	 */
	public String getHost(String id_key) {
		SharedContext sharedContext = this.getSharedContext(id_key);
		if (sharedContext != null){
			IParticipant host = sharedContext.getHost();
			if (host != null) return ParticipantHelper.createJSONParticipantDocument(host);
		}
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
		for(ISharedData data : new SharedContext(widgetInstance).getSharedData()){
			state.put(data.getDkey(), data.getDvalue());
		}
		return state;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#getParticipants(java.lang.String)
	 */
	public String getParticipants(String id_key) {
		SharedContext sharedContext = this.getSharedContext(id_key);
		if (sharedContext != null){
			return ParticipantHelper.createJSONParticipantsDocument(sharedContext.getParticipants());	
		} else {
			Messages localizedMessages = LocaleHandler.localizeMessages(WebContextFactory.get().getHttpServletRequest());
			return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$
		}
	}
	
	
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#getHosts(java.lang.String)
	 */
	public String getHosts(String id_key) {
		SharedContext sharedContext = this.getSharedContext(id_key);
		if (sharedContext != null){
			return ParticipantHelper.createJSONParticipantsDocument(sharedContext.getHosts());	
		} else {
			Messages localizedMessages = LocaleHandler.localizeMessages(WebContextFactory.get().getHttpServletRequest());
			return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$
		}
	}

	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#getViewer(java.lang.String)
	 */
	public String getViewer(String id_key) {
		SharedContext sharedContext = this.getSharedContext(id_key);
		if (sharedContext != null){
			return ParticipantHelper.createJSONParticipantDocument(sharedContext.getViewer());	
		} else {
			Messages localizedMessages = LocaleHandler.localizeMessages(WebContextFactory.get().getHttpServletRequest());
			return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$
		}
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
		  new SharedContext(widgetInstance).updateSharedData(key, map.get(key), false);
		Notifier.notifySiblings(widgetInstance);
		return "okay"; //$NON-NLS-1$
	}
	
	private SharedContext getSharedContext(String id_key){
        IPersistenceManager persistenceManager = PersistenceManagerFactory.getPersistenceManager();
        IWidgetInstance widgetInstance = persistenceManager.findWidgetInstanceByIdKey(id_key);
		if (widgetInstance != null){
			return new SharedContext(widgetInstance);
		}
		return null;
	}

}
