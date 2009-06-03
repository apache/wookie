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

import org.apache.log4j.Logger;
import org.tencompetence.widgetservice.Messages;
import org.tencompetence.widgetservice.beans.Participant;
import org.tencompetence.widgetservice.beans.Preference;
import org.tencompetence.widgetservice.beans.SharedData;
import org.tencompetence.widgetservice.beans.WidgetInstance;
import org.tencompetence.widgetservice.manager.IWidgetAPIManager;

/**
 * API manager - manages DB calls for widget API
 * @author Paul Sharples
 * @version $Id
 *
 */
public class WidgetAPIManager implements IWidgetAPIManager {
	
	public static Logger _logger = Logger.getLogger(WidgetAPIManager.class.getName());
	
	/**
	 * keep this here so that all sunclasses can access it
	 */
	protected Messages localizedMessages;
	
	boolean showProcess = false;
	
	public WidgetAPIManager(Messages localizedMessages2) {		
		this.localizedMessages = localizedMessages2;
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#checkUserKey(java.lang.String)
	 */
	public WidgetInstance checkUserKey(String key){
		if (key == null) return null;
		WidgetInstance[] instance = WidgetInstance.findByValue("idKey", key);
		if (instance == null||instance.length!=1) return null;
		return (WidgetInstance) instance[0];		
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#getSharedDataForInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public synchronized SharedData[] getSharedDataForInstance(WidgetInstance instance){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());
		map.put("widgetGuid", instance.getWidget().getGuid());
		return SharedData.findByValues(map);
	}
		
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#updateSharedDataEntry(org.tencompetence.widgetservice.beans.WidgetInstance, java.lang.String, java.lang.String, boolean)
	 */
	public synchronized void updateSharedDataEntry(WidgetInstance widgetInstance, String name, String value, boolean append){
			boolean found=false;
			for (SharedData sharedData : getSharedDataForInstance(widgetInstance)){
				if(sharedData.getDkey().equals(name)){
					// if the value is null we need to remove the tuple
					if(value==null || value.equalsIgnoreCase("null")){   
						sharedData.delete();
					}
					else{    
						if(append){
							sharedData.setDvalue(sharedData.getDvalue() + value);
						}
						else{
							sharedData.setDvalue(value);
						}
						sharedData.save();
					}
					found=true;
				}       	
			}
			if(!found){     
				if(value!=null){
					String sharedDataKey = widgetInstance.getSharedDataKey();		
					SharedData sharedData= new SharedData();
					sharedData.setWidgetGuid(widgetInstance.getWidget().getGuid());
					sharedData.setSharedDataKey(sharedDataKey);
					sharedData.setDkey(name);
					sharedData.setDvalue(value);
					sharedData.save();
				}
			}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#lockWidgetInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public synchronized void lockWidgetInstance(WidgetInstance instance){
		//doLock(instance, true);
		updateSharedDataEntry(instance, "isLocked", "true", false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#unlockWidgetInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public synchronized void unlockWidgetInstance(WidgetInstance instance){
		//doLock(instance, false);
		updateSharedDataEntry(instance, "isLocked", "false", false);
	}
	
	public synchronized boolean isInstanceLocked(WidgetInstance widgetInstance){
		if(getSharedDataValue(widgetInstance, "isLocked") == null){
			//addNewSharedDataEntry(widgetInstance, "isLocked", "false");
			return false;
		}
		else{
			return Boolean.valueOf(getSharedDataValue(widgetInstance, "isLocked"));
		}	 			
	}
	
	public String getSharedDataValue(WidgetInstance widgetInstance, String key){
		String sharedDataValue = null;
		for(SharedData sharedData : getSharedDataForInstance(widgetInstance)){
			if(sharedData.getDkey().equals(key)){
				sharedDataValue = sharedData.getDvalue();
				break;
			}
		}	
		return sharedDataValue;
	}
	
	/*
	protected synchronized void doLock(WidgetInstance instance, boolean flag){
		//TODO - update all widget instances to lock/unlock based on key
		String sharedDataKey = instance.getRunId() + "-" + instance.getEnvId() + "-" + instance.getServiceId();
		
		
		IDBManager dbManager = null;
		dbManager = DBManagerFactory.getDBManager();
		instance.setLocked(flag);
		try {
			dbManager.saveObject(instance);
		} 
		catch (Exception e) {
			dbManager.rollbackTransaction();
			_logger.error(e.getMessage());
		}
	}
	
	// this gets all other widgetinstances that use the same widget in same run, env, serv -etc
	// these will differ by username, so we are getting all the users instances in a given service
	public WidgetInstance[] getSiblingInstancesFromInstance(WidgetInstance widgetInstance){			
		IDBManager dbManager = null;
		try {
			dbManager = DBManagerFactory.getDBManager();
			final Criteria crit = dbManager.createCriteria(WidgetInstance.class);						
			crit.add( Restrictions.eq( "runId", widgetInstance.getRunId() ) );	
			crit.add( Restrictions.eq( "envId", widgetInstance.getEnvId() ) );
			crit.add( Restrictions.eq( "serviceId", widgetInstance.getServiceId() ) );	
			final List<WidgetInstance> sqlReturnList =  dbManager.getObjects(WidgetInstance.class, crit);
			WidgetInstance[] instances = sqlReturnList.toArray(new WidgetInstance[sqlReturnList.size()]);		
			return instances;
		} 
		catch (Exception ex) {
			dbManager.rollbackTransaction();
			_logger.error(ex.getMessage());
			return null;
		}		 
	}
	*/

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#getPreferenceForInstance(org.tencompetence.widgetservice.beans.WidgetInstance)
	 */
	public Preference[] getPreferenceForInstance(WidgetInstance id){		
		return Preference.findByValue("widgetInstance", id);
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#getPreferenceForInstance(org.tencompetence.widgetservice.beans.WidgetInstance, java.lang.String)
	 */
	public Preference getPreferenceForInstance(WidgetInstance instance, String key){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("widgetInstance", instance);
		map.put("dkey", key);
		Preference[] preferences = Preference.findByValues(map);
		if (preferences == null||preferences.length != 1) return null;
		return preferences[0];
	}
	
	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#updatePreference(org.tencompetence.widgetservice.beans.WidgetInstance, java.lang.String, java.lang.String)
	 */
	public void updatePreference(WidgetInstance widgetInstance, String name, String value){
        boolean found=false;
        for (Preference preference :getPreferenceForInstance(widgetInstance)){
        	if(preference.getDkey().equals(name)){
        		// if the value is null we need to remove the tuple
        		if(value==null || value.equalsIgnoreCase("null")){  
        			preference.delete();     			
        		}
        		else{    
        			preference.setDvalue(value);
        			preference.save();
        		}
        		found=true;
        	}
        }
        if(!found){        	
    		Preference pref = new Preference();
    		pref.setWidgetInstance(widgetInstance);
    		pref.setDkey(name);
    		pref.setDvalue(value);	
    		pref.save();	
        }       
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#getParticipants()
	 */
	public Participant[] getParticipants(WidgetInstance instance) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());	//$NON-NLS-1$
		map.put("widgetGuid", instance.getWidget().getGuid());	//$NON-NLS-1$
		return Participant.findByValues(map);
	}

	/* (non-Javadoc)
	 * @see org.tencompetence.widgetservice.manager.IWidgetAPIManager#getViewer()
	 */
	public Participant getViewer(WidgetInstance instance) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sharedDataKey", instance.getSharedDataKey());	//$NON-NLS-1$
		map.put("widgetGuid", instance.getWidget().getGuid());	//$NON-NLS-1$
		map.put("participant_id", instance.getUserId());	//$NON-NLS-1$
		Participant[] participants = Participant.findByValues(map);
		if (participants == null || participants.length != 1) return null;
		return participants[0];
	}

}
