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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.wookie.Messages;
import org.apache.wookie.beans.Participant;
import org.apache.wookie.beans.SharedData;
import org.apache.wookie.beans.WidgetInstance;
import org.apache.wookie.controller.PropertiesController;
import org.apache.wookie.helpers.ParticipantHelper;
import org.apache.wookie.server.LocaleHandler;
import org.directwebremoting.ScriptBuffer;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;

/**
 * @author scott
 *
 */
public class WaveAPIImpl implements IWaveAPI{

	/**
	 * 
	 */
	public WaveAPIImpl() {
	}
	
	

	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.IFeature#getJavaScriptImpl()
	 */
	public String getJavaScriptImpl() {
		return "/wookie/dwr/interface/WaveImpl.js";
	}



	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.IFeature#getJavaScriptWrapper()
	 */
	public String getJavaScriptWrapper() {
		return "/wookie/shared/js/wave.js";
	}



	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#getHost(java.lang.String)
	 */
	public String getHost(String idKey) {
		// TODO Auto-generated method stub
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
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if (widgetInstance == null){
			state.put("message", localizedMessages.getString("WidgetAPIImpl.0"));	 //$NON-NLS-1$
			return state;			
		}
		//
		for(SharedData data : SharedData.findSharedDataForInstance(widgetInstance)){
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
		if(id_key == null) return localizedMessages.getString("WidgetAPIImpl.0");
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance==null) return localizedMessages.getString("WidgetAPIImpl.0");
		Participant[] participants = Participant.getParticipants(widgetInstance);
		return ParticipantHelper.createJSONParticipantsDocument(participants);
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#getViewer(java.lang.String)
	 */
	public String getViewer(String id_key) {
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		if(id_key == null) return localizedMessages.getString("WidgetAPIImpl.0");
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		Participant participant = Participant.getViewer(widgetInstance);
		if (participant != null) return ParticipantHelper.createJSONParticipantDocument(participant); //$NON-NLS-1$
		return null; // no viewer i.e. widget is anonymous
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wookie.feature.wave.IWaveAPI#submitDelta(java.lang.String, java.util.Map)
	 */
	public String submitDelta(String id_key, Map<String,String>map){
		HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
		Messages localizedMessages = LocaleHandler.localizeMessages(request);
		WidgetInstance widgetInstance = WidgetInstance.findByIdKey(id_key);
		if(widgetInstance == null) return localizedMessages.getString("WidgetAPIImpl.0");
		if(widgetInstance.isLocked()) return localizedMessages.getString("WidgetAPIImpl.2");
		//
		for (String key: map.keySet())
		 	PropertiesController.updateSharedDataEntry(widgetInstance, key, map.get(key), false);
		notifyWidgets(widgetInstance);
		return "okay"; //$NON-NLS-1$
	}
	
	/**
	 * Send notifications to other widgets of shared data updates
	 */
	private void notifyWidgets(WidgetInstance widgetInstance){
		String sharedDataKey = widgetInstance.getSharedDataKey();
		String script = "Widget.onSharedUpdate(\""+sharedDataKey+"\");"; //$NON-NLS-1$ //$NON-NLS-2$
		callback(widgetInstance, script);
	}
	
	/**
	 * Sends a callback script
	 * @param widgetInstance
	 * @param call
	 */
	private void callback(WidgetInstance widgetInstance, String call){
		WebContext wctx = WebContextFactory.get();
        String currentPage = wctx.getCurrentPage();
        ScriptBuffer script = new ScriptBuffer();
        script.appendScript(call);
        // Loop over all the users on the current page
        Collection<?> pages = wctx.getScriptSessionsByPage(currentPage);
        for (Iterator<?> it = pages.iterator(); it.hasNext();){
            ScriptSession otherSession = (ScriptSession) it.next();
            otherSession.addScript(script);
        }	
	}

}
