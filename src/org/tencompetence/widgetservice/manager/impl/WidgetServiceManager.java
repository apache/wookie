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
package org.tencompetence.widgetservice.manager.impl;

import java.util.HashMap; 
import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.beans.Participant;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.PreferenceDefault;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.Widget;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.exceptions.WidgetTypeNotSupportedException;
import org.tencompetence.widgetservice.manager.IWidgetServiceManager;
import org.tencompetence.widgetservice.server.LocaleHandler;
import org.tencompetence.widgetservice.util.opensocial.OpenSocialUtils;

/**
 * A class to manage widget instances
 * @author Paul Sharples
 * @version $Id
 *
 */
public class WidgetServiceManager extends WidgetAPIManager implements IWidgetServiceManager {

	static Logger _logger = Logger.getLogger(WidgetServiceManager.class.getName());

	public WidgetServiceManager(Messages localizedMessages) {
		super(localizedMessages);	
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getDefaultWidgetByType(java.lang.String)
	 */
	public Widget getDefaultWidgetByType(String typeToSearch) throws WidgetTypeNotSupportedException {
		Widget widget = Widget.findDefaultByType(typeToSearch);
		if(widget==null)
			throw new WidgetTypeNotSupportedException("(" + typeToSearch + ") " + localizedMessages.getString("WidgetServiceManager.0")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return widget;		 
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getWidgetById(java.lang.String)
	 */
	public Widget getWidgetById(String id)
	throws WidgetTypeNotSupportedException {
		Widget[] widget = Widget.findByValue("guid", id);
		if (widget == null || widget.length !=1) throw new WidgetTypeNotSupportedException("(" + id + ") "+ localizedMessages.getString("WidgetServiceManager.1")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return widget[0];
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getWidgetInstance(java.lang.String)
	 */
	public WidgetInstance getWidgetInstance(String key) {
		if (key == null) return null;
		WidgetInstance[] instances = WidgetInstance.findByValue("idKey", key);
		if (instances!=null && instances.length == 1) return instances[0];
		return null;
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#deletePreferenceInstancesForWidgetInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public void deletePreferenceInstancesForWidgetInstance(WidgetInstance instance){				
		Preference[] preferences = Preference.findByValue("widgetInstance", instance);	
		Preference.delete(preferences);			
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#deleteSharedDataInstancesForWidgetInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public void deleteSharedDataInstancesForWidgetInstance(WidgetInstance instance){				
		SharedData[] sharedData = SharedData.findByValue("sharedDataKey", instance.getSharedDataKey());
		SharedData.delete(sharedData);		
	}


	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#widgetInstanceExists(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean widgetInstanceExists(String api_key, String userId, String sharedDataKey, String serviceContext){
		return WidgetInstance.widgetInstanceExists(api_key, userId, sharedDataKey, serviceContext);
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getwidgetInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public WidgetInstance getWidgetInstance(String api_key, String userId, String sharedDataKey, String serviceContext){
		return WidgetInstance.getWidgetInstance(api_key, userId, sharedDataKey, serviceContext);
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#getWidgetInstanceById(java.lang.String, java.lang.String, java.lang.String)
	 */
	public WidgetInstance getWidgetInstanceById(String api_key, String userId, String sharedDataKey, String widgetId) {
		Widget[] widget = Widget.findByValue("guid",widgetId);
		if (widget == null || widget.length !=1) return null;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("sharedDataKey", sharedDataKey);
		map.put("widget", widget[0]);
		WidgetInstance[] instance  = WidgetInstance.findByValues(map);
		if(instance == null || instance.length != 1) return null;
		return instance[0];
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#addNewWidgetInstance(java.lang.String, java.lang.String, java.lang.String, java.lang.String, org.tencompetence.widgetservice.beans.Widget, java.lang.String, java.lang.String)
	 */
	public WidgetInstance addNewWidgetInstance(String api_key, String userId, String sharedDataKey, Widget widget, String nonce, String idKey, Configuration properties) {		
		WidgetInstance widgetInstance = new WidgetInstance();
		widgetInstance.setUserId(userId);
		widgetInstance.setSharedDataKey(sharedDataKey);
		widgetInstance.setIdKey(idKey);
		widgetInstance.setNonce(nonce);
		widgetInstance.setApiKey(api_key);
		// set the defaults widget for this type			
		widgetInstance.setWidget(widget);						
		widgetInstance.setHidden(false);
		widgetInstance.setShown(true);
		widgetInstance.setUpdated(false);
		widgetInstance.setLocked(false);

		// Setup opensocial token if needed
		widgetInstance.setOpensocialToken(""); //$NON-NLS-1$
		if (properties.getBoolean("opensocial.enable")){ //$NON-NLS-1$
			try {
				if (properties.getString("opensocial.token").equals("secure")){ //$NON-NLS-1$ //$NON-NLS-2$
					widgetInstance.setOpensocialToken(OpenSocialUtils.createEncryptedToken(widgetInstance,properties.getString("opensocial.key"), localizedMessages)); //$NON-NLS-1$
				} 
				else {
					widgetInstance.setOpensocialToken(OpenSocialUtils.createPlainToken(widgetInstance, localizedMessages));					
				}
			} catch (Exception e) {
				_logger.error(e.getMessage());
			}
		}

		// Save
		widgetInstance.save();

		// add in basic widget data as preferences
		//TODO setPreference(widgetInstance, "viewMode", String.valueOf(widget)); //$NON-NLS-1$
		setPreference(widgetInstance, "locale", LocaleHandler.getInstance().getDefaultLocale().getLanguage()); //$NON-NLS-1$
		setPreference(widgetInstance, "identifier", String.valueOf(widget.getGuid()));	//$NON-NLS-1$
		setPreference(widgetInstance, "authorInfo", String.valueOf(widget.getWidgetAuthor()));	//$NON-NLS-1$
		//TODO setPreference(widgetInstance, "authorEmail", String.valueOf(widget.getWidth()));//$NON-NLS-1$
		//TODO setPreference(widgetInstance, "authorHref", String.valueOf(widget.getHeight()));			//$NON-NLS-1$
		setPreference(widgetInstance, "name", String.valueOf(widget.getWidgetTitle()));//$NON-NLS-1$
		setPreference(widgetInstance, "description", String.valueOf(widget.getWidgetDescription()));//$NON-NLS-1$	
		setPreference(widgetInstance, "version", widget.getVersion());//$NON-NLS-1$
		setPreference(widgetInstance, "width", String.valueOf(widget.getWidth()));//$NON-NLS-1$
		setPreference(widgetInstance, "height", String.valueOf(widget.getHeight()));//$NON-NLS-1$

		// add in the sharedDataKey as a preference so that a widget can know
		// what sharedData event to listen to later
		setPreference(widgetInstance, "sharedDataKey", sharedDataKey);//$NON-NLS-1$

		// add in widget defaults
		PreferenceDefault[] prefs = PreferenceDefault.findByValue("widget", widget);	
		if (prefs == null) return null;
		for (PreferenceDefault pref: prefs){
			setPreference(widgetInstance, pref.getPreference(), pref.getValue());
		}	
		return widgetInstance;
	}

	/**
	 * Initialize a preference for the instance
	 * @param instance
	 * @param key
	 * @param value
	 */
	private void setPreference(WidgetInstance widgetInstance, String key, String value){
		Preference pref = new Preference();
		pref.setWidgetInstance(widgetInstance);
		pref.setDkey(key);				
		pref.setDvalue(value);
		pref.save();	
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#addParticipantToWidgetInstance(org.tencompetence.widgetservice.beans.WidgetInstance, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean addParticipantToWidgetInstance(WidgetInstance instance,
			String participantId, String participantDisplayName,
			String participantThumbnailUrl) {

		// Does participant already exist?
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());
		map.put("widgetGuid", instance.getWidget().getGuid());
		map.put("participant_id", participantId);
		if (Participant.findByValues(map).length != 0) return false;		

		// Add participant
		Participant participant = new Participant();
		participant.setParticipant_id(participantId);
		participant.setParticipant_display_name(participantDisplayName);
		participant.setParticipant_thumbnail_url(participantThumbnailUrl);
		participant.setSharedDataKey(instance.getSharedDataKey());
		participant.setWidgetGuid(instance.getWidget().getGuid());
		participant.save();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetServiceManager#removeParticipantFromWidgetInstance(org.tencompetence.widgetservice.beans.WidgetInstance, java.lang.String)
	 */
	public boolean removeParticipantFromWidgetInstance(WidgetInstance instance,
			String participantId) {
		Participant[] participants;
		// Does participant exist?
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());
		map.put("widgetGuid", instance.getWidget().getGuid());
		map.put("participant_id", participantId);
		participants = Participant.findByValues(map);
		if (participants.length != 1) return false;	
		// Remove participant
		Participant.delete(participants[0]);
		return true;
	}

}

