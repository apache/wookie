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
import org.apache.wookie.auth.AuthToken;
import org.apache.wookie.auth.AuthTokenUtils;
import org.apache.wookie.auth.InvalidAuthTokenException;
import org.apache.wookie.beans.IParticipant;
import org.apache.wookie.beans.ISharedData;
import org.apache.wookie.beans.SharedContext;
import org.apache.wookie.feature.IFeature;
import org.apache.wookie.helpers.Notifier;
import org.apache.wookie.helpers.ParticipantHelper;
import org.apache.wookie.helpers.WidgetRuntimeHelper;
import org.apache.wookie.server.LocaleHandler;
import org.apache.wookie.services.SharedContextService;
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
		// check if token is valid
		try {
			AuthToken authToken = AuthTokenUtils.decryptAuthToken(id_key);
			for (ISharedData data: SharedContextService.Factory.getInstance().getSharedData(authToken.getApiKey(), authToken.getWidgetId(), authToken.getContextId())){
				state.put(data.getDkey(), data.getDvalue());
			}
			
		} catch (InvalidAuthTokenException e) {
			state.put("message", localizedMessages.getString("WidgetAPIImpl.0"));	 //$NON-NLS-1$
			return state;
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
		
		try {
			AuthToken authToken = AuthTokenUtils.decryptAuthToken(id_key);
			
			// TODO
			// if(widgetInstance.isLocked()) return localizedMessages.getString("WidgetAPIImpl.2"); //$NON-NLS-1$
			for (String key: map.keySet())
				  new SharedContext(authToken).updateSharedData(key, map.get(key), false);
				Notifier.notifySiblings(authToken);
				return "okay"; //$NON-NLS-1$
		} catch (InvalidAuthTokenException e) {
			return localizedMessages.getString("WidgetAPIImpl.0"); //$NON-NLS-1$
		}

	}
	
	private SharedContext getSharedContext(String id_key){		
		try {
			AuthToken authToken = AuthTokenUtils.decryptAuthToken(id_key);
			return new SharedContext(authToken);
		} catch (InvalidAuthTokenException e) {
			return null;
		}
	}

}
